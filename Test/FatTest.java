import fat.Fat12Entry;
import org.junit.Assert;
import org.junit.Test;

public class FatTest
{
    @Test
    public void testReadFatEntry ()
    {
        byte b[] = {0x2d, (byte)0xe0, 0x02};
        Fat12Entry fe = new Fat12Entry(b);
        int v1 = fe.getFatEntryValue(0);
        int v2 = fe.getFatEntryValue(1);
        Assert.assertEquals(v1, 45);
        Assert.assertEquals(v2, 46);
    }

    @Test
    public void testWriteFatEntry ()
    {
        byte x[] = {0, 0, 0};
        Fat12Entry fx = new Fat12Entry(x);
        byte y[] = {0x23, 0x61, 0x45};

        fx.setFatEntryValue(0, 0x123);
        fx.setFatEntryValue(1, 0x456);
        Assert.assertArrayEquals(x, y);
//        System.out.println(Integer.toHexString(x[0]));
//        System.out.println(Integer.toHexString(x[1]));
//        System.out.println(Integer.toHexString(x[2]));
        int a = fx.getFatEntryValue(0);
        int b = fx.getFatEntryValue(1);
        Assert.assertEquals(a, 0x123);
        Assert.assertEquals(b, 0x456);
    }


    @Test
    public void TestRW2 ()
    {
        byte x[] = {0,0,0, 0x78, (byte)0x9a, (byte)0xbc};
        Fat12Entry fx = new Fat12Entry(x);
        byte y[] = {0,0,0, 0,0,0};
        Fat12Entry fy = new Fat12Entry(y);

        int a = fx.getFatEntryValue(2);
        int b = fx.getFatEntryValue(3);
//        System.out.println(Integer.toHexString(a));
//        System.out.println(Integer.toHexString(b));
//        System.out.println("------------------------");

        fy.setFatEntryValue(2, a);
        fy.setFatEntryValue(3, b);
        Assert.assertArrayEquals(x, y);
//        System.out.println(Integer.toHexString(y[0]));
//        System.out.println(Integer.toHexString(y[1]));
//        System.out.println(Integer.toHexString(y[2]));
    }
}



