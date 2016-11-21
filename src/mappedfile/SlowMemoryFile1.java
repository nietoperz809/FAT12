/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mappedfile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * @author Administrator
 */
public class SlowMemoryFile1 implements MemoryFile
{
    protected RandomAccessFile _file = null;

    public SlowMemoryFile1 (String fname) throws FileNotFoundException
    {
        _file = new RandomAccessFile (new File(fname), "rw");
    }
    
    private ByteBuffer map (long address, int length) throws IOException
    {
        return _file.getChannel().map(FileChannel.MapMode.READ_WRITE, address, length);
    }

    @Override
    public byte[] clone ()
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void write (long address, byte[] data) throws Exception
    {
        ByteBuffer m = map (address, data.length);
        m.put (data);
    }
    
    @Override
    public byte[] read (long address, int length) throws Exception
    {
        ByteBuffer m = map (address, length);
        byte[] dst = new byte[length];
        m.get(dst);
        return dst;
    }
    
    @Override
    public void close() throws Exception
    {
        _file.close();
        _file = null;
    }

    @Override
    public void insertBytes(long addess, byte[] data) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void deleteBytes(long address, int lenght) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void fillArea (long address, byte b, int length) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setLength (int len)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

