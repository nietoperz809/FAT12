/*
 * MemoryFile using dynamic byte array
 */
package mappedfile;

import bytearray.DynamicByteArray;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Byte array mapped file access
 * @author Administrator
 */
public class FastMemoryFile implements MemoryFile
{
    protected final DynamicByteArray _arr = new DynamicByteArray();
    private final String filename;

    /**
     * Factory Constructor
     * @param fname file name
     */
    public static FastMemoryFile load(String fname)
    {
        FastMemoryFile fm = new FastMemoryFile(fname);
        fm._arr.setArray(fromFile(fname));
        return fm;
    }

    /**
     * Constructor
     * @param fname file name
     */
    public FastMemoryFile(String fname) 
    {
        filename = fname;
    }

    public FastMemoryFile (String fname, FastMemoryFile src)
    {
        filename = fname;
        _arr.setArray(src.clone());
    }
    
    /**
     * Loads file into byte array
     * @param fname name of file
     * @return byte array filled with file content or null on error
     */
    protected static byte[] fromFile(String fname)
    {
        try
        {
            Path path = Paths.get(fname);
            return Files.readAllBytes(path);
        }
        catch (IOException ex)
        {
            return null;
        }
    }

    /**
     * Stores byte array as file
     * @param fname the file/path name
     * @param in the byte array to be stored
     * @throws Exception if something goes wrong
     */
    private void toFile(String fname, byte[] in) throws Exception
    {
        if (in == null)
            return;
        Path path = Paths.get(fname);
        Files.write(path, in);
    }

    @Override
    public byte[] clone ()
    {
        return _arr.getArray().clone();
    }

    public byte[] getBytes ()
    {
        return _arr.getArray();
    }

    @Override
    public void write(long address, byte[] data) throws Exception
    {
        _arr.put((int) address, data);
    }

    @Override
    public byte[] read(long address, int length) throws Exception
    {
        return _arr.get((int) address, length);
    }

    @Override
    public void close() throws Exception
    {
        this.toFile(filename, _arr.getArray());
    }

    @Override
    public void insertBytes(long address, byte[] data) throws Exception
    {
        _arr.insert ((int)address, data);
    }

    @Override
    public void deleteBytes(long address, int lenght) throws Exception
    {
        _arr.delete ((int)address, lenght);
    }

    @Override
    public void fillArea (long address, byte b, int length) throws Exception
    {
        _arr.fill((int)address, b, length);
    }

    @Override
    public void setLength (int len)
    {
        _arr.realloc(len);
    }
}
