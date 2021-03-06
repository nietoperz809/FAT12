package fat;

/**

 Sector(s)	  Address	           Function
 -------------------------------------------------------------------
 0	          0x0000-0x01ff	       Boot Sector
 1-9	      0x0200-0x13ff	       File Allocation Table (primary)
 10-18	      0x1400-0x25ff	       File Allocation Table (secondary)
 19-32	      0x2600-0x41ff	       Root Directory
 33-2879	  0x4200-0x167fff	   File storage space

 */
class FloppyMaster
{

//    private static final byte[] fatInitBytes = {(byte) 0xf0, (byte) 0xff, (byte) 0xff};
//
//    private static final int byteLength = 1474560;
//
//    public static void makeEmptyDisk (String imagePath) throws Exception
//    {
//        FastMemoryFile fm = new FastMemoryFile(imagePath);
//        fm.setLength(byteLength);
//        fm.close();
//    }

//    private static MemoryFile makeFormattedDisk (String imagePath, byte[] bootSector) throws Exception
//    {
//        FastMemoryFile fm = new FastMemoryFile(imagePath);
//        fm.write(0, bootSector);
//        fm.write(0x27, getFourRandomBytes());  // serial number
//        fm.write(0x200, fatInitBytes);        // fat 1
//        fm.write(0x1400, fatInitBytes);       // fat 2
//        fm.fillArea(0x4200, (byte) 0xf6, 1457664);
//        fm.setLength(byteLength);
//        fm.close();
//        return fm;
//    }

//    public static MemoryFile makeDos622Disk (String imagePath) throws Exception
//    {
//        return makeFormattedDisk(imagePath, BootBlock.dos622BootSector);
//    }
//
//    public static MemoryFile makeDrDosDisk (String imagePath) throws Exception
//    {
//        return makeFormattedDisk(imagePath, BootBlock.drDosBootSector);
//    }
//
//    public static MemoryFile makeWinNt351Disk (String imagePath) throws Exception
//    {
//        return makeFormattedDisk(imagePath, BootBlock.winNtBootSector);
//    }


    //////////////////////////////////////////////////////////////////

    public static void main (String[] args)
    {
        try
        {
            Disk d = Disk.load("c:\\manipulated.img");

            d.format("halloweltdubistcool");

//            byte[] buff;
//            buff = new byte[1024];
//            d.putFile("wixx", "x", buff);
////            String list = d.dir();
////            System.out.println(list);
////
////            d.deleteFile("wixx.x");
////            list = d.dir();
////            System.out.println(list);
//
//            buff = "hello world".getBytes();
//            d.putFile("peter", "txt", buff);
//            d.putFile("peter2", "txt", buff);
//            d.putFile("peter3", "txt", buff);
//
//            buff = new byte[2049];
//            d.putFile("peter4", "txt", buff);
//
//            buff = new byte[1];
//            d.putFile("peter5", "txt", buff);
//
//            d.createSubDir("sexy", "doof");
//
//            //d.createSubDir("sub1", "lala");
//
////            list = d.dir();
////            System.out.println(list);
//

            d.saveTo("c:\\manipulated.img");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
        //BootBlock.printBootSector(d.getBootSector());

//
//        DynamicByteArray data = d.getFileData("ntldr");
//        System.out.println(data.getCurrentSize());

////        MemoryFile mf = FloppyMaster.makeDos622Disk("c:\\myWinnt.img");
////        printBootSector(mf);
////        mf = FloppyMaster.makeWinNt351Disk("c:\\myWinnt.img");
////        printBootSector(mf);
////        mf = FloppyMaster.makeDrDosDisk("c:\\myWinnt.img");
////        printBootSector(mf);
//
//        FastMemoryFile mf = FastMemoryFile.load("c:\\ntfat.img");
//        Directory dir = new Directory(mf);
//
//        DirectoryEntry de = dir.seekFile("ntldr");
//        if (de != null)
//        {
//            System.out.println("found");
//            Fat12 fat = new Fat12(mf);
//            fat.traverseFile(de);
//        }
    }
}
