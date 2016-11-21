/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tlv;

/**
 *
 * @author Administrator
 */
public class TLV8 extends TLV
{
    public TLV8()
    {
    }

    public TLV8(int t, byte[] v)
    {
        super(t, v);
    }

    @Override
    public byte[] toBytes()
    {
        byte[] b = new byte[_v.length+2];
        b[0] = (byte)_t;
        b[1] = (byte)_v.length;
        System.arraycopy (_v, 0, b, 2, _v.length);
        return b;
    }  

    @Override
    public void fromBytes (byte[] b)
    {
        _t = b[0];
        int len = b[1];
        _v = new byte[len];
        System.arraycopy (b, 2, _v, 0, len);
    }
}
