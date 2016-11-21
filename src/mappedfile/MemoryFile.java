/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mappedfile;

/**
 *
 * @author Administrator
 */
public interface MemoryFile
{
    byte[] clone();
    void write (long address, byte[] data) throws Exception;
    byte[] read (long address, int length) throws Exception; 
    void insertBytes (long addess, byte[] data) throws Exception;
    void deleteBytes (long address, int lenght) throws Exception;
    void fillArea (long address, byte b, int length) throws Exception;
    void setLength (int len);
    void close() throws Exception;
}
