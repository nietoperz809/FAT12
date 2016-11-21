/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

import mappedfile.MemoryFile;
import misc.ByteCVT;
import misc.RandomArrays;
import org.junit.Test;

/**
 *
 * @author Administrator
 */
public class Tester
{
    public static void test1  (MemoryFile mem) throws Exception
    {
        byte[] b = "123".getBytes();
        mem.write(0, b);
        mem.write (100, b);
        byte[] b2 = mem.read(0, 3);
        System.out.println(java.util.Arrays.toString(b2));
        mem.close();
    }
    
    public static void testDynBA()
    {
//        DynamicByteArray ba = new DynamicByteArray();
//        ba.put(1000, "1234".getBytes());
//        ba.put(0, "5678".getBytes());
//        ba.put(500, "abcd".getBytes());
//        byte[] res1 = ba.get(1000, 4);
//        byte[] res2 = ba.get(0, 4);
//        byte[] res3 = ba.get(500, 4);
//        byte[] res4 = ba.get(2000, 4);
//        System.out.println(Arrays.toString(res1));
//        System.out.println(Arrays.toString(res2));
//        System.out.println(Arrays.toString(res3));
//        System.out.println(Arrays.toString(res4));
    }    
    
    public static void testRandom (MemoryFile mem, ArrayList<RandomArrays.Chunk> ch) throws Exception
    {
        System.out.print(mem.getClass().toString() + " --> ");
        Instant start = Instant.now();
        for (RandomArrays.Chunk c : ch)
        {
            mem.write(c.address, c.array);
        }
        Instant end = Instant.now();        
        System.out.println(Duration.between(start, end).toMillis());
    }
    
    public static void main(String[] args) throws Exception
    {
//        SlowMemoryFile1 mem1 = new SlowMemoryFile1 ("c:\\memfile1.txt");        
//        test(mem1);
//        SlowMemoryFile2 mem2 = new SlowMemoryFile2 ("c:\\memfile2.txt");        
//        test(mem2);
//        FastMemoryFile mem3 = new FastMemoryFile ("c:\\memfile3.txt");        
//        test(mem3);
//        System.out.println("------------------");
//        testDynBA();
//        System.out.println("------------------");
        
//        RandomArrays ra = new RandomArrays (30000, 30000, 30000);
//        ArrayList<RandomArrays.Chunk> ch = ra.getList();
//        MemoryFile mem13 = new FastMemoryFile ("c:\\memfile13.txt");
//        testRandom (mem13, ch);
//        MemoryFile mem11 = new SlowMemoryFile1 ("c:\\memfile11.txt");
//        testRandom (mem11, ch);
//        MemoryFile mem12 = new SlowMemoryFile2 ("c:\\memfile12.txt");
//        testRandom (mem12, ch);
//        System.out.println("------------------");
        
//        MemoryFile f1 = new FastMemoryFile ("c:\\inserttest.txt");
//        f1.write (0, "1234567890".getBytes());
//        //f1.write (30, "abcdefgh".getBytes());
//        //f1.insertBytes(1, "---------------".getBytes());
//        f1.deleteBytes(4, 3);
//        f1.close();

//        TLV8 t = new TLV8(1, "test".getBytes());
//        System.out.println(t);
//        TLV16 t2 = new TLV16(1, "test".getBytes());
//        System.out.println(t2);
//    
//        TLV16 t3 = new TLV16();
//        byte[] b = {1,1,3,4,5,6,7,8,9,0};
//        t3.fromBytes(b);
//        System.out.println(t3);
        
//        byte[] b = {1,2,3,4};
//        int i1 = ByteCVT.fromBE32(b, 0);
//        System.out.println(i1);
//        i1 = ByteCVT.fromLE32(b, 0);
//        System.out.println(i1);
//
//        byte[] b1 = {0,0,0,0};
//        ByteCVT.toBE32(i1, b1, 0);
//        System.out.println(Arrays.toString(b1));
//
//        BigInteger bi1 = ByteCVT.readBE(b1, 0, 4);
//        System.out.println(bi1);
//        BigInteger bi2 = ByteCVT.readLE(b1, 0, 4);
//        System.out.println(bi2);
//
//        byte[] b2 = {0,0,0,0};
//        BigInteger bix = BigInteger.valueOf(67305985);
//        ByteCVT.writeBE(bix, b2, 0, 4);
//        System.out.println(Arrays.toString(b2));
//        ByteCVT.writeLE(bix, b2, 0, 4);
//        System.out.println(Arrays.toString(b2));

    }
}
