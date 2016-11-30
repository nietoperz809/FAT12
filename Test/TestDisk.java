import fat.DirectoryEntry;
import fat.Disk;
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

        byte[] buff = new String ("hello").getBytes();
        d.putFile("wixx", "x", buff);

        buff = new String ("doof").getBytes();
        d.putFile("wixx2", "x", buff);

        buff = new byte[513];
        d.putFile("wixx3", "x", buff);
        d.putFile("wixx4", "x", buff);
    }

    @Test
    public void testTime()
    {
        int ret = DirectoryEntry.getTimeStamp(0xffffffff, 0, 0);
        Assert.assertEquals(ret, 31);
        ret = DirectoryEntry.getTimeStamp(0, 0xffffffff, 0);
        Assert.assertEquals(ret, 2016);
        ret = DirectoryEntry.getTimeStamp(0, 0, 0xffffffff);
        Assert.assertEquals(ret, 63488);
    }
}
