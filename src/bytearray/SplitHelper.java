package bytearray;

/**
 * Created by Administrator on 11/24/2016.
 */
public class SplitHelper
{
    public int getBlocks()
    {
        return blocks;
    }
    private int blocks;

    public int getRemainder ()
    {
        return remainder;
    }

    private int remainder;

    public int getTotalblocks ()
    {
        return totalblocks;
    }

    private int totalblocks;

    public SplitHelper (long length, int chunksize)
    {
        blocks = (int)(length / chunksize);
        remainder = (int)(length % chunksize);
        totalblocks = blocks + (remainder !=0 ? 1 : 0);
    }
}
