import org.junit.Assert;
import org.junit.Test;
import range.AccumulatedRangeList;
import range.Range;

/**
 * Created by Administrator on 10/30/2016.
 */
public class RangeTest
{
    @Test
    public void test1() throws Exception
    {
        Range r1 = new Range (3,9, null);
        Range r2 = new Range (9,13, null);
        Range r3 = new Range (10,13, null);
        Range rc = new Range (3,13, null);

        Range r4 = r1.combine(r2);
        Range r5 = r1.combine(r3);

        Assert.assertFalse (r1.overlap(r3));
        Assert.assertTrue (r1.overlap(r2));
        Assert.assertEquals(r4, rc);
        Assert.assertEquals(r5, rc);
    }

    @Test
    public void testAccList() throws Exception
    {
        AccumulatedRangeList acl = new AccumulatedRangeList();
        System.out.println(acl.toString());
        acl.add(new Range(1,5));
        System.out.println(acl.toString());
        acl.add(new Range(1,6));
        System.out.println(acl.toString());
    }

}
