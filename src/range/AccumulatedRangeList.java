/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package range;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Administrator
 */
public class AccumulatedRangeList 
{
    private final ArrayList<Range> _list = new ArrayList<>();
    
    public List<Range> getList()
    {
        return _list;
    }
    
    public Range add (Range r) throws Exception
    {
        Range ov = r;
        Range[] obs = (Range[])Array.newInstance (r.getClass(), _list.size());
        _list.toArray(obs);
        for (Range t : obs)
        {
            Range r1 = t.combineIfOverlap(ov);
            if (r1 != null)
            {
                ov = r1;
                _list.remove(t);
            }
        }
        _list.add(ov);
        return ov;
    }

    @Override
    public String toString()
    {
        Range[] obs = (Range[])Array.newInstance(Range.class, _list.size());
        _list.toArray(obs);
        return Arrays.toString(obs);
    }
}
