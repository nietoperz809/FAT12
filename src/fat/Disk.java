package fat;

import mappedfile.FastMemoryFile;

import java.security.SecureRandom;

/**
 * Created by Administrator on 11/21/2016.
 */
public final class Disk
{
    private FastMemoryFile _fmf;

    private static final byte[] fatInitBytes = {(byte) 0xf0, (byte) 0xff, (byte) 0xff};
    private static final int byteLength = 1474560;

    private static byte[] getFourRandomBytes ()
    {
        byte b[] = new byte[4];
        new SecureRandom().nextBytes(b);
        return b;
    }

    public static Disk load (String path)
    {
        Disk d = new Disk();
        d._fmf = FastMemoryFile.load(path);
        return d;
    }

    private Disk()
    {

    }

    public Disk (String name)
    {
        _fmf = new FastMemoryFile(name);
        _fmf.setLength(byteLength);
    }

    public String dir() throws Exception
    {
        Directory d = new Directory(_fmf);
        return d.list();
    }

    public void format() throws Exception
    {
        _fmf.write(0, BootBlock.dos622BootSector);
        _fmf.write(0x27, getFourRandomBytes());  // serial number
        _fmf.write(0x200, fatInitBytes);        // fat 1
        _fmf.write(0x1400, fatInitBytes);       // fat 2
        _fmf.fillArea(0x4200, (byte) 0xf6, 1457664);
    }

    public void close() throws Exception
    {
        _fmf.close();
    }
}
