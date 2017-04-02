package fat;

import bytearray.DynamicByteArray;
import bytearray.SplitHelper;
import mappedfile.FastMemoryFile;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Administrator on 11/21/2016.
 */
public final class Disk
{
    public final static boolean DEBUG = true;

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

    public static Disk getDosFormatted(String name) throws Exception
    {
        Disk d = new Disk();
        d.format(name);
        return d;
    }

    /**
     * Set volume label of disk
     * @param label the label text
     * @throws Exception
     */
    public void setVolumeLabel (String label) throws Exception
    {
        label = label.toUpperCase();
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

    public void createSubDir (String name, String ext) throws Exception
    {
        Fat12 fat = new Fat12(_fmf);
        ArrayList<Integer> freeList = fat.getFreeEntryList(1);
        Directory directory = new Directory(_fmf);
        int freedir = directory.getFreeDirectoryEntryOffset();
        DirectoryEntry de = DirectoryEntry.createSubdirEntry(name, ext, freedir);
        fat.setFatEntryValue (freeList.get(0), Globals.LAST_SLOT); // only one sector
        directory.put (de, freedir);

        directory.writeBack ();
        fat.writeBack();
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

        SplitHelper sh = new SplitHelper(data.length, Globals.CLUSTERSIZE);
        ArrayList<Integer> freeList = fat.getFreeEntryList(sh.getTotalblocks());

        if (DEBUG)
            System.out.println("freelist: "+Arrays.toString(freeList.toArray()));

        DynamicByteArray splits[] = new DynamicByteArray(data).split(Globals.CLUSTERSIZE);

        if (DEBUG)
            System.out.println("splits: "+splits.length);

        DirectoryEntry de = DirectoryEntry.create(filename,
                ext,
                data.length,
                freeList.get(0),
                Globals.ARCHIVE);

        directory.put (de, freedir);

        for (int i=0; i<sh.getTotalblocks(); i++)
        {
            int sector = freeList.get(i);
            int nextsector;
            if (i == (sh.getTotalblocks()-1))
            {
                nextsector = Globals.LAST_SLOT;
            }
            else
            {
                nextsector = freeList.get(i + 1);
            }
            DiskRW.writeSectors(_fmf, sector+ Globals.DATAOFFSET, splits[i].getArray());

            fat.setFatEntryValue (sector, nextsector);
            if (DEBUG)
            {
                System.out.println("FAT-setentry: "+ sector+" Value: "+ nextsector);
            }
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

    public void deleteFile (String filename) throws Exception
    {
        Fat12 fat = new Fat12(_fmf);
        Directory directory = new Directory(_fmf);
        DirectoryEntry de = directory.seekFile(filename);
        de.setDeleted();
        directory.put(de, de.positionInDirectory);
        fat.deleteFile(de);

        fat.writeBack();
        directory.writeBack();
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
        _fmf.write(0, Globals.dos622BootSector);
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
