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

    public static Disk load (String path) throws Exception
    {
        Disk d = new Disk();
        d._fmf.load(path);
//        d._dir = new Directory(d._fmf);
//        d._fat = new Fat12(d._fmf);
        return d;
    }

    private Disk()
    {
        _fmf = new FastMemoryFile();
        _fmf.setLength(DISKSIZE);
    }

    public Disk (String name)
    {
        super();
        _fmf.setName(name);
    }

    // TODO:
    public void putFile (String filename, String ext, byte[] data) throws Exception
    {
        Fat12 fat = new Fat12(_fmf);
        Directory d = new Directory(_fmf);
        int freedir = d.getFreeDirectoryEntryOffset();

        int blocks = data.length / Fat12.CLUSTERSIZE;
        int remainder = data.length % Fat12.CLUSTERSIZE;
        int total = blocks + (remainder !=0 ? 1 : 0);
        ArrayList<Integer> freeList = fat.getFreeEntryList(total);
        DynamicByteArray ba[] = new DynamicByteArray(data).split(Fat12.CLUSTERSIZE);
        DirectoryEntry de = DirectoryEntry.create(filename, ext, data.length, freeList.get(0));

    }

    public DynamicByteArray getFileData (String filename) throws Exception
    {
        Fat12 fat = new Fat12(_fmf);
        Directory d = new Directory(_fmf);
        DirectoryEntry de = d.seekFile(filename);
        if (de == null)
            return null;
        return fat.getFile(de);
    }

    public String dir() throws Exception
    {
        Directory d = new Directory(_fmf);
        return d.list();
    }

    public void format() throws Exception
    {
        _fmf.clearAll();
        _fmf.write(0, BootBlock.dos622BootSector);
        _fmf.write(0x27, getFourRandomBytes());  // serial number
        _fmf.write(0x200, fatInitBytes);        // fat 1
        _fmf.write(0x1400, fatInitBytes);       // fat 2
        _fmf.fillArea(0x4200, (byte) 0xf6, 1457664);
    }

    public void close() throws Exception
    {
        _fmf.close();
    }

    MemoryFile getBootSector() throws Exception
    {
        return new FastMemoryFile("bootsect", _fmf, 0, Fat12.SECTORSIZE);
    }
}
