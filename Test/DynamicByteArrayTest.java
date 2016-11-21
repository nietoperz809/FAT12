

import bytearray.DynamicByteArray;
import bytearray.UndoableDynamicByteArray;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by Administrator on 6/26/2016.
 */
public class DynamicByteArrayTest
{
    @Test
    public void test_DELETE()
    {
        byte[] a = "0123456789".getBytes();
        byte[] r1 = "012789".getBytes();
        DynamicByteArray ba = new DynamicByteArray();
        ba.put(1000, a);
        ba.delete(1003,4);
        byte[] b = ba.get(1000, r1.length);
        Assert.assertArrayEquals(b, r1);
    }

    @Test
    public void test_INSERT()
    {
        byte[] a = "0123456789".getBytes();
        byte[] r1 = "0123abcdef456789".getBytes();
        byte[] r2 = "hello".getBytes();
        DynamicByteArray ba = new DynamicByteArray();
        ba.put(1000, a);
        ba.insert(1004, "abcdef".getBytes());
        byte[] b = ba.get(1000, r1.length);
        Assert.assertArrayEquals(b, r1);
        ba.insert(0, "hello".getBytes());
        b = ba.get(0, r2.length);
        Assert.assertArrayEquals(b, r2);
    }

    @Test
    public void test_RW()
    {
        byte[] a = "1234".getBytes();
        byte[] b =  "5678".getBytes();
        byte[] c = "abcd".getBytes();
        DynamicByteArray ba = new DynamicByteArray();
        ba.put(1000, a);
        ba.put(0, b);
        ba.put(500, c);
        byte[] res1 = ba.get(1000, 4);
        byte[] res2 = ba.get(0, 4);
        byte[] res3 = ba.get(500, 4);
        Assert.assertArrayEquals(res1, a);
        Assert.assertArrayEquals(res2, b);
        Assert.assertArrayEquals(res3, c);
    }

    @Test
    public void test_Length() throws Exception
    {
        DynamicByteArray arr = new DynamicByteArray();
        byte[] b1 = new byte[]{1,2,3,4};
        arr.put(0, b1);
        Assert.assertEquals(arr.getCurrentSize(), b1.length);
        arr.put(100, b1);
        Assert.assertEquals(arr.getCurrentSize(), 100+b1.length);
        arr.put(10, b1);
        Assert.assertEquals(arr.getCurrentSize(), 100+b1.length);
    }

    @Test
    public void test_emptyGet() throws Exception
    {
        DynamicByteArray arr = new DynamicByteArray();
        byte[] b0 = new byte[]{0,0,0,0,0,0,0,0,0,0};
        byte[] b1 = arr.get(1000,10);
        Assert.assertArrayEquals(b1, b0); // should be all zero
        Assert.assertEquals(b1.length, 10);  // should be requested length
        Assert.assertEquals(arr.getCurrentSize(), 1010); // get should cause growing
    }

    @Test
    public void test_shrink() throws Exception
    {
        DynamicByteArray arr = new DynamicByteArray();
        Assert.assertEquals (arr.getCurrentSize(), 0); // ampty dba has size 0
        arr.insert(10, new byte[]{1,2,3,4,5});
        Assert.assertEquals (arr.getCurrentSize(), 15); // shoud be 15 now
        arr.delete (0,0);
        Assert.assertEquals (arr.getCurrentSize(), 15); // shoud be 15 now
        arr.delete (0,15);
        Assert.assertEquals (arr.getCurrentSize(), 0); // shoud be 0 now
        arr.insert(0, new byte[]{8,9,10,42,54});
        Assert.assertEquals (arr.getCurrentSize(), 5); // shoud be 5 now
        arr.delete (0,100);
        Assert.assertEquals (arr.getCurrentSize(), 0); // attempt to del > size, shoud be 0 now
    }

    @Test
    public void dynBAPut() throws Exception
    {
        byte[] b1 = new byte[]{1,2,3,4};
        byte[] b2 = new byte[]{5,6,7,8,9};
        byte[] bg;
        UndoableDynamicByteArray db = new UndoableDynamicByteArray();
        db.put(100, b1);
        bg = db.get(100,4);
        Assert.assertArrayEquals(b1,bg);
        db.put(102, b2);
        bg = db.get(100,7);
        Assert.assertArrayEquals(bg, new byte[]{1,2,5,6,7,8,9});
        db.undo();
        bg = db.get(100,4);
        Assert.assertArrayEquals(b1,bg);
    }

    @Test
    public void dynBADel() throws Exception
    {
        byte[] b1 = new byte[]{1,2,3,4};
        byte[] b2 = new byte[]{0,0,0,0,};
        byte bg[];
        UndoableDynamicByteArray db = new UndoableDynamicByteArray();
        bg = db.get(0,4);
        Assert.assertArrayEquals(b2,bg);
        db.put(0, b1);
        bg = db.get(0,4);
        Assert.assertArrayEquals(b1,bg);
        db.delete(0,4);
        bg = db.get(0,4);
        Assert.assertArrayEquals(b2,bg);
        db.undo();
        bg = db.get(0,4);
        Assert.assertArrayEquals(b1,bg);
    }

    @Test
    public void dynBAInsert() throws Exception
    {
        byte[] b1 = new byte[]{1,2,3,4};
        byte[] b2 = new byte[]{88,99,101,103,};
        byte[] b3 = new byte[]{1,2, 88,99,101,103, 3,4};
        byte[] bg;
        UndoableDynamicByteArray db = new UndoableDynamicByteArray();
        db.put(0, b1);
        db.insert(2, b2);
        bg = db.get(0, 8);
        Assert.assertArrayEquals(b3,bg);
        db.undo();
        bg = db.get(0, 4);
        Assert.assertArrayEquals(b1,bg);
    }

    @Test
    public void appendTest()
    {
        DynamicByteArray arr = new DynamicByteArray();
        byte[] b1 = new byte[]{1,2,3,4};
        byte[] b2 = new byte[]{1,2,3,4,1,2,3,4};
        arr.append(b1);
        byte[] res1 = arr.getArray();
        arr.append(b1);
        byte[] res2 = arr.getArray();
        System.out.println(Arrays.toString(res1));
        System.out.println(Arrays.toString(res2));
        Assert.assertArrayEquals(b1,res1);
        Assert.assertArrayEquals(b2,res2);
    }

    @Test
    public void dynPrintMemBlocks() throws Exception
    {
        byte[] b1 = new byte[]{1,2,3,4};
        byte[] b2 = new byte[]{5,5,5,6,6,6};
        UndoableDynamicByteArray db = new UndoableDynamicByteArray();
        db.put(0, b1);
        db.insert(2, b2);
        db.printMemBocks();
        db = new UndoableDynamicByteArray();
        db.printMemBocks();
        db.put (0, b1);
        db.put (100, b1);
        db.printMemBocks();
        db.undo();
        db.printMemBocks();
        db.undo();
        db.printMemBocks();
    }


    /////////////////////////////////////////////

    @BeforeClass
    public static void setUp () throws Exception
    {

    }
}