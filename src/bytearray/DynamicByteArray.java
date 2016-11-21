/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bytearray;

import java.util.Arrays;

/**
 * Byte array that can grow
 * @author Administrator
 */
public class DynamicByteArray
{
    /**
     * This is the array
     */
    protected byte[] theArray = null;
    
    /**
     * Set a new array
     * @param arr the array
     */
    public void setArray (byte[] arr)
    {
        theArray = arr;
    }

    /**
     * Returns the array that is base of this object
     * @return  the array itself
     */
    public byte[] getArray()
    {
        return theArray;
    }

    public int getCurrentSize()
    {
        if (theArray == null)
            return 0;
        return theArray.length;
    }

    /**
     * Resizes the array to a new length
     * @param len new length
     */
    public void realloc(int len)
    {
        if (theArray == null)
        {
            theArray = new byte[len];
        }
        else if (len > theArray.length)
        {
            theArray = Arrays.copyOf(theArray, len);
        }
    }
    
    /**
     * Deletes part of array
     * delete (4,3) on [49, 50, 51, 52, 53, 54, 55, 56, 57, 48]
     * will result in: [49, 50, 51, 52, 56, 57, 48]
     * @param address first address of part to be deleted
     * @param len length of that part
     */
    public void delete (int address, int len)
    {
        if (theArray == null)
            return; // do nuthing on nuthing
        if (len > theArray.length)
            len = theArray.length;
        int newsize = theArray.length-len;
        int begin2part = address+len;
        byte[] b1 = new byte[newsize];
        System.arraycopy (theArray, 0, b1, 0, address);
        System.arraycopy (theArray, begin2part, b1, address, newsize-address);
        theArray = b1;
    }
    
    /**
     * Inserts bytes into array
     * @param address offset
     * @param data data to be inserted at offset
     */
    public void insert (int address, byte[] data)
    {
        byte[] old = theArray;
        if (old == null || address > theArray.length)
        {
            realloc (address + data.length);
        }
        else
        {
            realloc (theArray.length + data.length);
            System.arraycopy (old, address, theArray, address+data.length, old.length-address);
        }
        System.arraycopy (data, 0, theArray, address, data.length);
    }
    
    /**
     * Writes bytes into array
     * @param address first address to be written to
     * @param data data to be written
     */
    public void put (int address, byte[] data)
    {
        realloc (address + data.length);
        System.arraycopy (data, 0, theArray, address, data.length);
    }

    /**
     * Inserts bytes at the end
     * @param data new data bytes
     */
    public void append (byte[] data)
    {
        int len = getCurrentSize();
        realloc (len + data.length);
        System.arraycopy (data, 0, theArray, len, data.length);
    }

    /**
     * Fills array with the same value
     * @param address Starting address
     * @param b Byte value
     * @param num Number of byte filled with that value
     */
    public void fill (int address, byte b, int num)
    {
        byte[] bytes = new byte[num];
        for (int s=0; s<num; s++)
            bytes[s] = b;
        put (address, bytes);
    }

    public void clear()
    {
        if (getCurrentSize() > 0)
        {
            theArray = new byte[theArray.length];
        }
    }

    /**
     * Reads bytes from array
     * @param address address to be read from
     * @param len number of bytes to be read
     * @return the read bytes
     */
    public byte[] get (int address, int len)
    {
        byte[] res = new byte[len];
        realloc (address + len);
        System.arraycopy (theArray, address, res, 0, len);
        return res;
    }
}
