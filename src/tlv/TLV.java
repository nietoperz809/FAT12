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
abstract class TLV
{
    int _t = 0;
    byte[] _v = new byte[0];

    TLV ()
    {
    }

    TLV (int t, byte[] v)
    {
        setTLV (t, v);
    }

    private void setT (int t)
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
    
    private void setV (byte[] v)
    {
        _v = new byte[v.length];
        System.arraycopy (v, 0, _v, 0, v.length);
    }
    
    private void setTLV (int t, byte[] v)
    {
        setT(t);
        setV(v);
    }
    
    protected abstract byte[] toBytes ();
    abstract public void fromBytes (byte[] b);

    @Override
    public String toString()
    {
        return "T:"+_t+" L:"+_v.length+" --> "+Arrays.toString(toBytes());
    }
}
