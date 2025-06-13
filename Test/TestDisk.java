import fat.Disk;
import fat.Timestamp;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Administrator on 11/27/2016.
 */
public class TestDisk
{
    @Test
    public void TestRead() throws Exception
    {
        Disk d = Disk.getDosFormatted("MyDisk");

        byte[] buff = "hello".getBytes();
        d.putFile("wixx", "x", buff);

        buff = "doof".getBytes();
        d.putFile("wixx2", "x", buff);

        buff = new byte[513];
        d.putFile("wixx3", "x", buff);
        d.putFile("wixx4", "x", buff);
    }

    @Test
    public void testTime()
    {
        int ret = Timestamp.getTimeStamp(0xffff, 0, 0);
        Assert.assertEquals(31, ret);
        ret = Timestamp.getTimeStamp(0, 0xffff, 0);
        Assert.assertEquals(2016, ret);
        ret = Timestamp.getTimeStamp(0, 0, 0xffff);
        Assert.assertEquals(ret, 63488);
    }
}
