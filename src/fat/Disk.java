package fat;

import bytearray.DynamicByteArray;
import mappedfile.FastMemoryFile;
import mappedfile.MemoryFile;

import java.security.SecureRandom;
import java.util.ArrayList;

/**
 * Created by Administrator on 11/21/2016.
 */
public final class Disk
{
    private FastMemoryFile _fmf;

    private static final byte[] fatInitBytes = {(byte) 0xf0, (byte) 0xff, (byte) 0xff};
    private static final int DISKSIZE = 1474560;

    private static byte[] getFourRandomBytes ()
    {
        byte b[] = new byte[4];
        new SecureRandom().nextBytes(b);
        return b;
    }

    /**
     * Constructs a disk from file image
     * @param path path to file
     * @return a new Disk
     * @throws Exception
     */
    public static Disk load (String path) throws Exception
    {
        Disk d = new Disk();
        d._fmf.load(path);
        return d;
    }

    /**
     * Saves Disk as image to file
     * @param path output file/path
     * @throws Exception
     */
    public void saveTo (String path) throws Exception
    {
        _fmf.saveCopyAs(path);
    }

    /**
     * Constructs an empty Disk
     */
    private Disk()
    {
        _fmf = new FastMemoryFile();
        _fmf.setLength(DISKSIZE);
    }

    /**
     * Constructs a named Disk
     */
    public Disk (String name)
    {
        super();
        _fmf.setName(name);
    }

    /**
     * Set volume label of disk
     * @param label the label text
     * @throws Exception
     */
    public void setVolumeLabel (String label) throws Exception
    {
        while (label.length()<11)
            label = label+' ';
        if (label.length() > 11)
            label = label.substring (0, 11);
        _fmf.write (0x2b, label.getBytes());   // write label in boot block

        Directory directory = new Directory(_fmf); // write label in directory
        int freedir = directory.getFreeDirectoryEntryOffset();
        DirectoryEntry de = DirectoryEntry.createVolumeLabel(label);
        directory.put (de, freedir);
        directory.writeBack ();
    }

    /**
     * Put a new File on disk
     * @param filename File name
     * @param ext Extension
     * @param data File Data
     * @throws Exception
     */
    public void putFile (String filename, String ext, byte[] data) throws Exception
    {
        Fat12 fat = new Fat12(_fmf);
        Directory directory = new Directory(_fmf);
        int freedir = directory.getFreeDirectoryEntryOffset();

        int blocks = data.length / Fat12.CLUSTERSIZE;
        int remainder = data.length % Fat12.CLUSTERSIZE;
        int totalblocks = blocks + (remainder !=0 ? 1 : 0);
        ArrayList<Integer> freeList = fat.getFreeEntryList(totalblocks);
        DynamicByteArray splits[] = new DynamicByteArray(data).split(Fat12.CLUSTERSIZE);
        DirectoryEntry de = DirectoryEntry.create(filename,
                ext,
                data.length,
                freeList.get(0),
                DirectoryEntry.ARCHIVE);

        directory.put (de, freedir);

        for (int i=0; i<totalblocks; i++)
        {
            int sector = freeList.get(i);
            int nextsector;
            if (freeList.size() == i+1)
            {
                nextsector = 0x0fff;
            }
            else
            {
                nextsector = freeList.get(i + 1);
            }
            DiskRW.writeSectors(_fmf, sector+Fat12.DATAOFFSET, splits[i].getArray());
            Fat12Entry.writeFatEntryValue (fat.getArray(), sector, nextsector);
        }

        directory.writeBack ();
        fat.writeBack();
    }

    /**
     * Read a file on Disk
     * @param filename Name of file (Format: name.ext)
     * @return a DynArray containing the file data
     * @throws Exception if smth gone wrong or file doesn't exist
     */
    public DynamicByteArray getFileData (String filename) throws Exception
    {
        Fat12 fat = new Fat12(_fmf);
        Directory d = new Directory(_fmf);
        DirectoryEntry de = d.seekFile(filename);
        return fat.getFile(de);
    }

    /**
     * Get a directory listing
     * @return String containing the output
     * @throws Exception
     */
    public String dir() throws Exception
    {
        Directory d = new Directory(_fmf);
        return d.list();
    }


    /**
     * Formats the disk, destroys all data
     * @throws Exception
     */
    public void format(String label) throws Exception
    {
        _fmf.clearAll();
        _fmf.write(0, BootBlock.dos622BootSector);
        _fmf.write(0x27, getFourRandomBytes());  // serial number
        _fmf.write(0x200, fatInitBytes);        // fat 1
        _fmf.write(0x1400, fatInitBytes);       // fat 2
        _fmf.fillArea(0x4200, (byte) 0xf6, 1457664);
        setVolumeLabel(label);
    }

//    public void close() throws Exception
//    {
//        _fmf.close();
//    }
//
//    MemoryFile getBootSector() throws Exception
//    {
//        return new FastMemoryFile("bootsect", _fmf, 0, Fat12.SECTORSIZE);
//    }
}
