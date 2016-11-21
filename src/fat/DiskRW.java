package fat;

import mappedfile.FastMemoryFile;

/**
 * R/W functions
 */
public class DiskRW
{
    public static void writeSectors (FastMemoryFile fm, int sectorNumber, byte data[]) throws Exception
    {
        fm.write (sectorNumber*Fat12.SECTORSIZE, data);
    }

    public static byte[] readSector (FastMemoryFile fm, int sectorNumber) throws Exception
    {
        return fm.read(sectorNumber*Fat12.SECTORSIZE, Fat12.SECTORSIZE);
    }

    public static byte[] readPartialSector (FastMemoryFile fm, int sectorNumber, int len) throws Exception
    {
        return fm.read(sectorNumber*Fat12.SECTORSIZE, len);
    }

    public static byte[] readSectors (FastMemoryFile fm, int sectorNumber, int num) throws Exception
    {
        return fm.read(sectorNumber*Fat12.SECTORSIZE, Fat12.SECTORSIZE*num);
    }
}
