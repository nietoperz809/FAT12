package fat;

import mappedfile.FastMemoryFile;

/**
 * R/W functions
 */
class DiskRW
{
    public static byte[] readFAT1 (FastMemoryFile fmf) throws Exception
    {
        return readSectors(fmf, 1, 9);
    }

    public static byte[] readFAT2 (FastMemoryFile fmf) throws Exception
    {
        return readSectors(fmf, 10, 9);
    }

    public static void writeFAT1 (FastMemoryFile fmf, byte[] data) throws Exception
    {
        writeSectors(fmf, 1, data);
    }

    public static void writeFAT2 (FastMemoryFile fmf, byte[] data) throws Exception
    {
        writeSectors(fmf, 10, data);
    }

    public static byte[] readDirectory (FastMemoryFile fmf) throws Exception
    {
        return readSectors(fmf, 19, 14);
    }

    public static void writeDirectory (FastMemoryFile fmf, byte[] data) throws Exception
    {
        writeSectors(fmf, 19, data);
    }

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
