
import mappedfile.FastMemoryFile;
import org.junit.*;
import org.junit.rules.TestRule;
import org.junit.runners.MethodSorters;
import org.junit.Assert;

/**
 * Created by Administrator on 6/25/2016.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FastMemoryFileTest
{
    static FastMemoryFile memfile;
    static byte[] testBytes;
    static byte[] readBytes;


    @Test
    public void a0_open () throws Exception
    {
        memfile = new FastMemoryFile ("c:\\memfilexxx.txt");
        Assert.assertNotNull(memfile);
    }

    @Test
    public void a1_write () throws Exception
    {
        memfile.write(0, testBytes);
    }

    @Test
    public void a2_close () throws Exception
    {
        memfile.close();
    }

    @Test
    public void a3_reopen () throws Exception
    {
        memfile = null;
        memfile = FastMemoryFile.load ("c:\\memfilexxx.txt");
        Assert.assertNotNull(memfile);
    }

    @Test
    public void a4_read () throws Exception
    {
        readBytes = memfile.read(0, testBytes.length);
        Assert.assertArrayEquals(testBytes, readBytes);
    }

    @Test
    public void x0_fullTest () throws Exception
    {
    }

    //////////////////////////////////////////////////////////////

    @BeforeClass
    static public void begin () throws Exception
    {
        testBytes = new byte[1000000];
        readBytes = new byte[testBytes.length];
        for (int s=0; s<testBytes.length; s++)
        {
            testBytes[s] = (byte)s;
        }
    }

    @AfterClass
    static public void end () throws Exception
    {
        //memfile.close();
    }
}