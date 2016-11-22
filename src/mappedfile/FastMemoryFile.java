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
    private final DynamicByteArray _arr = new DynamicByteArray();
    private String filename = null;

    /**
     * Factory Constructor
     * @param fname file name
     */
    public void load(String fname)
    {
        _arr.setArray(fromFile(fname));
    }

    public void saveCopyAs(String fname) throws Exception
    {
        toFile (fname,_arr.getArray() );
    }

    /**
     * Constructor
     * @param fname file name
     */
    public FastMemoryFile(String fname) 
    {
        setName(fname);
    }

    public FastMemoryFile()
    {
    }

    public FastMemoryFile (String name, byte[] array)
    {
        setName (name);
        _arr.setArray (array.clone());
    }

    public FastMemoryFile (String name, FastMemoryFile src, int start, int len) throws Exception
    {
        setName (name);
        byte[] bytes = src.read(start, len);
        _arr.setArray (bytes);
    }

    public FastMemoryFile (String fname, FastMemoryFile src)
    {
        setName(fname);
        _arr.setArray(src.clone());
    }

    public void setName (String name)
    {
        filename = name;
    }

    /**
     * Loads file into byte array
     * @param fname name of file
     * @return byte array filled with file content or null on error
     */
    public static byte[] fromFile (String fname)
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
    public static void toFile(String fname, byte[] in) throws Exception
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
        if (filename == null)
            throw new RuntimeException("Nameless Object");
        this.toFile(filename, _arr.getArray());
    }

    @Override
    public void insertBytes(long address, byte[] data)
    {
        _arr.insert ((int)address, data);
    }

    @Override
    public void deleteBytes(long address, int lenght)
    {
        _arr.delete ((int)address, lenght);
    }

    @Override
    public void fillArea (long address, byte b, int length)
    {
        _arr.fill((int)address, b, length);
    }

    public void clearAll ()
    {
        _arr.clear();
    }

    @Override
    public void setLength (int len)
    {
        _arr.realloc(len);
    }
}
