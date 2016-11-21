import fat.Fat12Entry;
import org.junit.Assert;
import org.junit.Test;

public class FatTest
{
    @Test
    public void testReadFatEntry ()
    {
        byte b[] = {0x2d, (byte)0xe0, 0x02};
        int v1 = Fat12Entry.getFatEntryValue(b,0);
        int v2 = Fat12Entry.getFatEntryValue(b,1);
        Assert.assertEquals(v1, 45);
        Assert.assertEquals(v2, 46);
    }

    @Test
    public void testWiteFatEntry ()
    {
        byte x[] = {0, 0, 0};
        byte y[] = {0x23, 0x61, 0x45};
        Fat12Entry.writeFatEntryValue(x, 0, 0x123);
        Fat12Entry.writeFatEntryValue(x, 1, 0x456);
        Assert.assertArrayEquals(x, y);
//        System.out.println(Integer.toHexString(x[0]));
//        System.out.println(Integer.toHexString(x[1]));
//        System.out.println(Integer.toHexString(x[2]));
        int a = Fat12Entry.getFatEntryValue(x,0);
        int b = Fat12Entry.getFatEntryValue(x,1);
        Assert.assertEquals(a, 0x123);
        Assert.assertEquals(b, 0x456);
    }


    @Test
    public void TestRW2 ()
    {
        byte x[] = {0,0,0, 0x78, (byte)0x9a, (byte)0xbc};
        byte y[] = {0,0,0, 0,0,0};

        int a = Fat12Entry.getFatEntryValue(x, 2);
        int b = Fat12Entry.getFatEntryValue(x, 3);
//        System.out.println(Integer.toHexString(a));
//        System.out.println(Integer.toHexString(b));
//        System.out.println("------------------------");

        Fat12Entry.writeFatEntryValue(y, 2, a);
        Fat12Entry.writeFatEntryValue(y, 3, b);
        Assert.assertArrayEquals(x, y);
//        System.out.println(Integer.toHexString(y[0]));
//        System.out.println(Integer.toHexString(y[1]));
//        System.out.println(Integer.toHexString(y[2]));
    }
}



