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
    byte[] theArray = null;

    public DynamicByteArray ()
    {
    }

    public DynamicByteArray (byte[] dat)
    {
        theArray = dat;
    }

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

    public void put (int address, byte b)
    {
        byte b1[] = {b};
        put (address, b1);
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

    @Override
    public String toString()
    {
        return Arrays.toString(theArray);
    }

    /**
     * Splits this dynArray into fragments of specific size
     * Last fragment can be smaller
     * @param fragment max length of fragment
     * @return array of dynArrays representing fragments
     */
    public DynamicByteArray[] split (int fragment)
    {
        int parts = getCurrentSize()/fragment;
        int remain = getCurrentSize()%fragment;
        int total = parts + (remain !=0 ? 1 : 0);
        DynamicByteArray[] res = new DynamicByteArray[total];
        int s = 0;
        for (; s<parts; s++)
        {
            res[s] = new DynamicByteArray();
            byte[] arr = this.get(s*fragment, fragment);
            res[s].put(0, arr);
        }
        if (remain != 0)
        {
            res[s] = new DynamicByteArray();
            byte[] arr = this.get(s*fragment, remain);
            res[s].put(0, arr);
        }
        return res;
    }
}
