package fat;

import mappedfile.FastMemoryFile;

/**
 * Created by Administrator on 11/21/2016.
 */
public class DiskRW
{
    public static void writeSectors (FastMemoryFile fm, int sectorNumber, byte data[]) throws Exception
    {
        fm.write (sectorNumber*512, data);
    }

    public static byte[] readSector (FastMemoryFile fm, int sectorNumber) throws Exception
    {
        return fm.read(sectorNumber*512, 512);
    }

    public static byte[] readPartialSector (FastMemoryFile fm, int sectorNumber, int len) throws Exception
    {
        return fm.read(sectorNumber*512, len);
    }


    public static byte[] readSectors (FastMemoryFile fm, int sectorNumber, int num) throws Exception
    {
        return fm.read(sectorNumber*512, 512*num);
    }
}
