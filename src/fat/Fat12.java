package fat;

import bytearray.DynamicByteArray;
import mappedfile.FastMemoryFile;

/**
 * Implements handling of FAT part of the disk
 */
public final class Fat12
{
    private final byte[] _fat;
    FastMemoryFile _fmf;
    public Fat12 (FastMemoryFile fmf) throws Exception
    {
        _fmf = fmf;
        _fat = DiskRW.readSectors (fmf, 1, 9);
    }

    public void traverseFile (DirectoryEntry de)
    {
        int clusterNum = de.firstLogicalCluster;
        for (;;)
        {
            System.out.println(clusterNum);
            if (clusterNum <= 0x0fff && clusterNum >= 0x0ff8)
                break;
            if (clusterNum == 0)
                throw new RuntimeException("Unused Cluster");
            if (clusterNum <= 0x0ff6 && clusterNum >= 0x0ff0)
                throw new RuntimeException("Reserved Cluster");
            if (clusterNum == 0x0ff7)
                throw new RuntimeException("Bad Cluster");
            clusterNum = Fat12Entry.getFatEntryValue(_fat, clusterNum);
        }
    }

    public DynamicByteArray getFile (DirectoryEntry de) throws Exception
    {
        DynamicByteArray out = new DynamicByteArray();

        int blocks = (int)de.fileSize / 512;
        int remainder = (int)de.fileSize % 512;

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
