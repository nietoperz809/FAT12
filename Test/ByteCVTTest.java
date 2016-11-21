import misc.ByteCVT;
import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Administrator on 10/27/2016.
 */
public class ByteCVTTest
{
    @Test
    public void testRead ()
    {
        byte[] b1 = {0, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 1, 0, 0, 0};
        long[] arr1;
        long[] res1 = {65536*256, 65536, 256, 1};
        long[] res2 = {1, 256, 65536, 65536*256};

        arr1 = ByteCVT.readLE(b1, 4);
        Assert.assertArrayEquals(arr1,res1);
        arr1 = ByteCVT.readBE(b1, 4);
        Assert.assertArrayEquals(arr1,res2);
    }

}
