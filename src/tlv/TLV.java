/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tlv;

import java.util.Arrays;

/**
 *
 * @author Administrator
 */
public abstract class TLV
{
    protected int _t = 0;
    protected byte[] _v = new byte[0];

    public TLV ()
    {
    }

    public TLV (int t, byte[] v)
    {
        setTLV (t, v);
    }

    public void setT(int t)
    {
        _t = t;
    }
    
    public int getT()
    {
        return _t;
    }

    public byte[] getV()
    {
        return Arrays.copyOf(_v, _t);
    }
    
    public void setV (byte[] v)
    {
        _v = new byte[v.length];
        System.arraycopy (v, 0, _v, 0, v.length);
    }
    
    public void setTLV (int t, byte[] v)
    {
        setT(t);
        setV(v);
    }
    
    abstract public byte[] toBytes();
    abstract public void fromBytes (byte[] b);

    @Override
    public String toString()
    {
        return "T:"+_t+" L:"+_v.length+" --> "+Arrays.toString(toBytes());
    }
}
