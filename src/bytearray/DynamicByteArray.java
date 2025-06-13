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
public class DynamicByteArray {
    /**
     * This is the array
     */
    byte[] theArray = null;

    public DynamicByteArray() {
    }

    public int hashCode() {
        return Arrays.hashCode(theArray);
    }

    /**
     * Construtor that take a byte array and first position of that array
     * @param address first address to be written to
     * @param data data to be written
     */
    public DynamicByteArray(int address, byte... data) {
        put(address, data);
    }

    /**
     * Construtor that take a byte array
     * @param dat
     */
    public DynamicByteArray(byte[] dat) {
        put(0, dat);
        //theArray = dat;
    }

    /**
     * Set a new array
     * @param arr the array
     */
    public void setArray(byte[] arr) {
        theArray = arr;
    }

    /**
     * Returns the array that is base of this object
     * @return the array itself
     */
    public byte[] getArray() {
        return theArray;
    }

    public int getCurrentSize() {
        if (theArray == null)
            return 0;
        return theArray.length;
    }

    /**
     * Resizes the array to a new length if it is too short
     * @param len new length
     */
    public void realloc(int len) {
        if (theArray == null) {
            theArray = new byte[len];
        } else if (len > theArray.length) {
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
    public void delete(int address, int len) {
        if (theArray == null)
            return; // do nuthing on nuthing
        if (len > theArray.length)
            len = theArray.length;
        int newsize = theArray.length - len;
        int begin2part = address + len;
        byte[] b1 = new byte[newsize];
        System.arraycopy(theArray, 0, b1, 0, address);
        System.arraycopy(theArray, begin2part, b1, address, newsize - address);
        theArray = b1;
    }

    /**
     * Inserts bytes into array
     * @param address offset
     * @param data data to be inserted at offset
     */
    public void insert(int address, byte[] data) {
        byte[] old = theArray;
        if (old == null || address > theArray.length) {
            realloc(address + data.length);
        } else {
            realloc(theArray.length + data.length);
            System.arraycopy(old, address, theArray, address + data.length, old.length - address);
        }
        System.arraycopy(data, 0, theArray, address, data.length);
    }

    /**
     * Writes bytes into array
     * @param address first address to be written to
     * @param data data to be written
     */
    public void put(int address, byte... data) {
        realloc(address + data.length);
        System.arraycopy(data, 0, theArray, address, data.length);
    }

//    /**
//     * Puts a single byte into ths dynBA
//     * @param address where to put
//     * @param b the byte
//     */
//    public void put (int address, byte b)
//    {
//        byte b1[] = {b};
//        put (address, b);
//    }

    public boolean getBit(int bitAddress) {
        int offset = bitAddress / 8;
        int bitnum = 1 << (bitAddress % 8);
        byte[] memByte = get(offset, 1);
        return (memByte[0] & bitnum) == bitnum;
    }

    public void putBit(boolean b, int bitAddress) {
        int offset = bitAddress / 8;
        int bitnum = 1 << (bitAddress % 8);
        byte[] memByte = get(offset, 1);
        if (b)
            memByte[0] = (byte) (memByte[0] | bitnum);
        else
            memByte[0] = (byte) (memByte[0] & ~bitnum);
        put(offset, memByte[0]);
    }

    /**
     * Read a Byte of variable length max 32 bits
     * @param bitAddress Adress of first bit in Array
     * @param length Byte length
     * @return The Byte
     */
    public int getVByte(int bitAddress, int length) {
        int res = 0;
        while (length > 0) {
            length--;
            if (getBit(bitAddress))
                res |= (1 << length);
            bitAddress++;
        }
        return res;
    }

    public void putVByte(int bitAddress, int length, int data) {
        while (length > 0) {
            length--;
            boolean bit = ((data & (1 << length)) != 0);
            putBit(bit, bitAddress);
            bitAddress++;
        }
    }

    /**
     * Inserts bytes at the end
     * @param data new data bytes
     */
    public void append(byte[] data) {
        int len = getCurrentSize();
        realloc(len + data.length);
        System.arraycopy(data, 0, theArray, len, data.length);
    }

    /**
     * Fills array with the same value
     * @param address Starting address
     * @param b Byte value
     * @param num Number of byte filled with that value
     */
    public void fill(int address, byte b, int num) {
        byte[] bytes = new byte[num];
        Arrays.fill(bytes, b);
        put(address, bytes);
    }

    /**
     * Clears the buffer but keeps size
     */
    public void clear() {
        if (getCurrentSize() > 0) {
            Arrays.fill(theArray, (byte) 0);
        }
    }

    /**
     * Reads bytes from array
     * @param address address to be read from
     * @param len number of bytes to be read
     * @return the read bytes
     */
    public byte[] get(int address, int len) {
        byte[] res = new byte[len];
        realloc(address + len);
        System.arraycopy(theArray, address, res, 0, len);
        return res;
    }

    public void reverse(int address, int len) {
        byte[] part = get(address, len);
        int i = 0;
        int j = part.length - 1;
        byte tmp;
        while (j > i) {
            tmp = part[j];
            part[j] = part[i];
            part[i] = tmp;
            j--;
            i++;
        }
        put(address, part);
    }

    @Override
    public String toString() {
        return Arrays.toString(theArray);
    }

    /**
     * Splits this dynArray into fragments of specific size
     * Last fragment can be smaller
     * @param fragment max length of fragment
     * @return array of dynArrays representing fragments
     */
    public DynamicByteArray[] split(int fragment) {
        SplitHelper sh = new SplitHelper(getCurrentSize(), fragment);
        DynamicByteArray[] res = new DynamicByteArray[sh.getTotalblocks()];
        int s;
        for (s = 0; s < sh.getBlocks(); s++) {
            res[s] = new DynamicByteArray(get(s * fragment, fragment));
        }
        if (sh.getRemainder() != 0) {
            res[s] = new DynamicByteArray(get(s * fragment, sh.getRemainder()));
        }
        return res;
    }

    /**
     * Write a string in this DynBA
     * if string lenght is smaller than len, the rest is filled up with spaces
     * @param address start address of string in BA
     * @param str the source string
     * @param len number of characters used
     */
    public void setString(int address, String str, int len) {
        byte[] b = new byte[len];
        for (int s = 0; s < b.length; s++) {
            if (str.length() > s)
                b[s] = (byte) str.charAt(s);
            else
                b[s] = ' ';
        }
        put(address, b);
    }

    /**
     * Write a string in this DynBA
     * @param address start address of string in BA
     * @param str the source string
     */
    public void setString(int address, String str) {
        put(address, str.getBytes());
    }

}
