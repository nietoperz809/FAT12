package fat;

import mappedfile.FastMemoryFile;

import java.util.HexFormat;


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

    public static void writeSectors (FastMemoryFile fm, int sectorNumber, byte[] data) throws Exception
    {
        fm.write ((long) sectorNumber * Globals.SECTORSIZE, data);
    }

    public static byte[] readSector (FastMemoryFile fm, int sectorNumber) throws Exception
    {
        return fm.read((long) sectorNumber * Globals.SECTORSIZE, Globals.SECTORSIZE);
    }

    public static byte[] readPartialSector (FastMemoryFile fm, int sectorNumber, int len) throws Exception
    {
        return fm.read((long) sectorNumber * Globals.SECTORSIZE, len);
    }

    public static byte[] readSectors (FastMemoryFile fm, int sectorNumber, int num) throws Exception
    {
        return fm.read((long) sectorNumber * Globals.SECTORSIZE, Globals.SECTORSIZE*num);
    }

    public static void printSectorBytes40 (FastMemoryFile fm, int sectnum) throws Exception
    {
        byte[] b = readPartialSector(fm, sectnum, 40);
        if (Disk.DEBUG)
        {
            System.out.println(HexFormat.of().formatHex(b));
        }
    }
}
