
package mappedfile;

import java.io.FileNotFoundException;


public class SlowMemoryFile2 extends SlowMemoryFile1
{
    private final MapList _map;
    
    public SlowMemoryFile2 (String fname) throws FileNotFoundException
    {
        super (fname);
        _map = new MapList (_file);
    }
    
    @Override
    public void write (long address, byte[] data) throws Exception
    {
        FMap map = _map.getMapping(address, data.length, 'w');
        map.bytebuff.put (data, (int)map.offset, data.length);
    }
    
    @Override
    public byte[] read (long address, int length) throws Exception
    {
        FMap map = _map.getMapping(address, length, 'r');
        byte[] dst = new byte[length];
        map.bytebuff.get(dst, (int)map.offset, length);
        return dst;
    }
}
