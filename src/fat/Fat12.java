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

//    public void traverseFile (DirectoryEntry de)
//    {
//        int clusterNum = de.firstLogicalCluster;
//        for (;;)
//        {
//            System.out.println(clusterNum);
//            if (clusterNum <= 0x0fff && clusterNum >= 0x0ff8)
//                break;
//            if (clusterNum == 0)
//                throw new RuntimeException("Unused Cluster");
//            if (clusterNum <= 0x0ff6 && clusterNum >= 0x0ff0)
//                throw new RuntimeException("Reserved Cluster");
//            if (clusterNum == 0x0ff7)
//                throw new RuntimeException("Bad Cluster");
//            clusterNum = Fat12Entry.getFatEntryValue(_fat, clusterNum);
//        }
//    }

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

        int blocks = (int)de.fileSize / Fat12.CLUSTERSIZE;
        int remainder = (int)de.fileSize % Fat12.CLUSTERSIZE;

        int cluster = de.firstLogicalCluster;
        byte[] bytes;
        for (int s=0; s<blocks; s++)
        {
            bytes = DiskRW.readSector(_fmf, cluster+31);
            out.append(bytes);
            cluster = Fat12Entry.getFatEntryValue(_fat, cluster);
        }
        if (remainder != 0)
        {
            bytes = DiskRW.readPartialSector(_fmf, cluster+31, remainder);
            out.append(bytes);
        }
        return out;
    }
}
