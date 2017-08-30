/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mappedfile;

import java.nio.ByteBuffer;
import range.Range;

/**
 *
 * @author Administrator
 */
class FMap
{
    public ByteBuffer bytebuff = null;
    public long offset; /* changed */
    public char rw;
    public Range range;
    
    public FMap (long b, long c, char e) throws Exception
    {
        range = new Range (b, b+c, this);
        rw = e;
    }
    
    public FMap (ByteBuffer a, long b, long c, char e) throws Exception
    {
        range = new Range (b, b+c, this);
        bytebuff = a;
        rw = e;
    }
}
