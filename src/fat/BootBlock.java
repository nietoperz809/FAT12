package fat;

import mappedfile.MemoryFile;

/**
 * Boot Block functions
 */
final class BootBlock
{

    private static String hexString (byte in[])
    {
        StringBuilder sb = new StringBuilder();
        for (byte b : in)
        {
            sb.append(String.format("%02x ", b & 0xff));
        }
        return sb.toString();
    }

    private static String textHexString (byte in[])
    {
        return hexString(in) + " (" +
                new String(in) + ")";
    }

    private static String twoByteString (byte in[])
    {
        int n = (in[0] & 0xff) | ((in[1] & 0xff) << 8);
        return hexString(in) + " (" +
                n + ")";
    }

    private static String decodeSize (byte[] bootsector)
    {
        int bytesPerSector = (bootsector[0x0b] & 0xff) | ((bootsector[0x0c] & 0xff) << 8);
        int SectorsPerTrack = (bootsector[0x18] & 0xff) | ((bootsector[0x19] & 0xff) << 8);
        int mediaID = bootsector[0x15] & 0xff;
        int sectorsPerCluster = bootsector[0x0d] & 0xff;
        if (mediaID == 0xff)
            return "5.25\", 320 kB";
        if (mediaID == 0xfe)
        {
            if (bytesPerSector == 128)
                return "8\", 250 kB";
            if (bytesPerSector == 1024)
                return "8\", 1200 kB";
            if (bytesPerSector == Globals.CLUSTERSIZE)
                return "5.25\", 160 kB";
        }
        if (mediaID == 0xfd)
        {
            if (bytesPerSector == 128)
                return "8\", 500 kB";
            if (bytesPerSector == Globals.CLUSTERSIZE)
                return "5.25\", 360 kB";
        }
        if (mediaID == 0xfc)
        {
            return "5.25\", 180 kB";
        }
        if (mediaID == 0xf9)
        {
            if (sectorsPerCluster == 1)
                return "5.25\", 1200 kB";
            if (sectorsPerCluster == 2)
                return "3.5\", 720 kB";
        }
        if (mediaID == 0xf0)
        {
            if (SectorsPerTrack == 18)
                return "3.5\", 1440 kB";
            if (SectorsPerTrack == 36)
                return "3.5\", 2880 kB";
        }
        return ("???");
    }

    public static void printBootSector (MemoryFile mf) throws Exception
    {
        System.out.println("------------------- Boot Sector Info -- "+decodeSize(mf.read(0, Globals.SECTORSIZE))+ " --------");
        System.out.println("Bootstrap Jump        : " + hexString(mf.read(0, 3)));
        System.out.println("Manufacturer          : " + textHexString(mf.read(3, 8)));
        System.out.println("Bytes per Sector      : " + twoByteString(mf.read(0x0b, 2)));
        System.out.println("Sectors per Cluster   : " + hexString(mf.read(0x0d, 1)));
        System.out.println("Reserved Sectors      : " + twoByteString(mf.read(0x0e, 2)));
        System.out.println("Number of FATs        : " + hexString(mf.read(0x10, 1)));
        System.out.println("Number of Root Dirs   : " + twoByteString(mf.read(0x11, 2)));
        System.out.println("Total Sector Count    : " + twoByteString(mf.read(0x13, 2)));
        System.out.println("Media Descriptor      : " + hexString(mf.read(0x15, 1)));
        System.out.println("Sectors per FAT       : " + twoByteString(mf.read(0x16, 2)));
        System.out.println("Sectors per Track     : " + twoByteString(mf.read(0x18, 2)));
        System.out.println("Number of Heads       : " + twoByteString(mf.read(0x1a, 2)));
        System.out.println("Number of hidden Sect : " + hexString(mf.read(0x1c, 4)));
        System.out.println("Sectors on Disk(2)    : " + hexString(mf.read(0x20, 4)));
        System.out.println("Physical Drive Number : " + twoByteString(mf.read(0x24, 2)));
        System.out.println("Boot Signature        : " + hexString(mf.read(0x26, 1)));
        System.out.println("Volume Serial Number  : " + hexString(mf.read(0x27, 4)));
        System.out.println("Volume Label          : " + textHexString(mf.read(0x2b, 11)));
        System.out.println("File System Ident.    : " + textHexString(mf.read(0x36, 8)));
        System.out.println("Boot Sector Signature : " + hexString(mf.read(0x1fe, 2)));
        System.out.println("---------------------------------------------------------------");
    }
}
