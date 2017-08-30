/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mappedfile;

import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import range.AccumulatedRangeList;
import range.Range;

/**
 *
 * @author Administrator
 */
class MapList
{
    private final RandomAccessFile _file;
    private final AccumulatedRangeList maps = new AccumulatedRangeList();
    
    public MapList (RandomAccessFile f)
    {
        _file = f;
    }
    
    FMap getMapping (long address, int length, char rw) throws Exception
    {
        long last = address+length;
        FMap m1 = new FMap (address, last, rw);
        Range m2 = maps.add(m1.range);
        FMap m3 = (FMap)m2.host;
        if (m1 == m3) // Same map
        {
            if (m3.bytebuff == null)
                m3.bytebuff = _file.getChannel().map(FileChannel.MapMode.READ_WRITE, address, length);
        }
        else  // other map
        {
            if (m2.from == address && m2.to == last) // mapping okay
            {
                m3.offset = m1.range.StartDistance(m2);
                if (rw != m3.rw)
                {
                    m3.bytebuff.flip();
                    m3.rw = rw;
                }
            }
            else // new mapping
            {
                m3.bytebuff = _file.getChannel().map(FileChannel.MapMode.READ_WRITE, address, length);    
                m3.offset = 0;
            }
        }
        return m3;
    }
}
