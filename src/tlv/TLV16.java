/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tlv;

import misc.ByteCVT;

/**
 *
 * @author Administrator
 */
public class TLV16 extends TLV
{
    public TLV16()
    {
    }

    public TLV16(int t, byte[] v)
    {
        super(t, v);
    }
    
    @Override
    public byte[] toBytes()
    {
        byte[] b = new byte[_v.length+4];
        ByteCVT.toBE16 (_t, b, 0);
        ByteCVT.toBE16 (_v.length, b, 2);
        System.arraycopy (_v, 0, b, 4, _v.length);
        return b;
    }

    @Override
    public void fromBytes(byte[] b)
    {
        _t = ByteCVT.fromBE16(b, 0);
        int len = ByteCVT.fromBE16(b, 2);
        _v = new byte[len];
        System.arraycopy (b, 4, _v, 0, len);
    }
}
