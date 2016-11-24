package fat;

import bytearray.DynamicByteArray;
import mappedfile.FastMemoryFile;

import java.util.ArrayList;

/**
 * Implements handling of FAT part of the disk
 */
final class Fat12
{
    private final byte[] _fat;
    private final FastMemoryFile _fmf;

    /**
     * Last entry on 1.44 mb disk
     */
    public static final int MAXENTRY_1440KB = 2880;
    public static final int CLUSTERSIZE = 512;
    public static final int SECTORSIZE = 512;
    public static final int DATAOFFSET = 31;

    public Fat12 (FastMemoryFile fmf) throws Exception
    {
        _fmf = fmf;
        _fat = DiskRW.readFAT1 (fmf);
    }

    public byte[] getArray()
    {
        return _fat;
    }

    public void writeBack() throws Exception
    {
        DiskRW.writeFAT1(_fmf, _fat);
    }


    public ArrayList<Integer> getFreeEntryList (int needed)
    {
        ArrayList<Integer> list = new ArrayList<>();
        for (int s=2; s<=MAXENTRY_1440KB; s++)
        {
            if (Fat12Entry.getFatEntryValue(_fat, s) == 0)
                list.add(s);
            if (list.size() == needed)
                return list;
        }
        throw new RuntimeException("Insufficient Disk Space");
    }

    /**
     * Read file data of a file that is on the disk
     * @param de DirEntry alread in Directory
     * @return DynArray filled witd file data
     * @throws Exception
     */
    public DynamicByteArray getFile (DirectoryEntry de) throws Exception
    {
        DynamicByteArray out = new DynamicByteArray();

        int blocks = (int)de.getFileSize() / Fat12.CLUSTERSIZE;
        int remainder = (int)de.getFileSize() % Fat12.CLUSTERSIZE;

        int cluster = de.getFirstCluster();
        byte[] bytes;
        for (int s=0; s<blocks; s++)
        {
            bytes = DiskRW.readSector(_fmf, cluster+DATAOFFSET);
            out.append(bytes);
            cluster = Fat12Entry.getFatEntryValue(_fat, cluster);
        }
        if (remainder != 0)
        {
            bytes = DiskRW.readPartialSector(_fmf, cluster+DATAOFFSET, remainder);
            out.append(bytes);
        }
        return out;
    }

    public void deleteFile (DirectoryEntry de) throws Exception
    {
        int blocks = (int)de.getFileSize() / Fat12.CLUSTERSIZE;
        int remainder = (int)de.getFileSize() % Fat12.CLUSTERSIZE;
        int total = blocks + (remainder !=0 ? 1: 0);

        int cluster = de.getFirstCluster();
        for (int s=0; s<total; s++)
        {
            int next = Fat12Entry.getFatEntryValue(_fat, cluster);
            Fat12Entry.writeFatEntryValue(_fat, cluster, 0);
            cluster = next;
        }
    }

}
