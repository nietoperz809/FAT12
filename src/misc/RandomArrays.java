/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package misc;

import java.util.ArrayList;

/**
 *
 * @author Administrator
 */
public class RandomArrays {

    private final ArrayList<Chunk> chunks = new ArrayList<>();

    public ArrayList<Chunk> getList() {
        return chunks;
    }

    public RandomArrays(int maxaddr, int maxlen, int num) {
        for (int s = 0; s < num; s++) {
            Chunk c = new Chunk();
            c.address = (int) (Math.random() * maxaddr);
            int len = 1 + (int) (Math.random() * maxlen);
            c.array = new byte[len];
            byte ch = (byte) ('0' + (Math.random() * 10));
            for (int i = 0; i < len; i++) {
                c.array[i] = ch;
            }
            chunks.add(c);
        }
    }
}
