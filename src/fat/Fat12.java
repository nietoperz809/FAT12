package fat;

import mappedfile.FastMemoryFile;

/**
 * Implements handling of FAT part of the disk
 */
public final class Fat12
{
    private final byte[] _fat;

    public Fat12 (FastMemoryFile fmf) throws Exception
    {
        this._fat = DiskRW.readSectors (fmf, 1, 9);
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
}
