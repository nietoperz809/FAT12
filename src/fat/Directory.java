package fat;

import mappedfile.FastMemoryFile;

/**
 * Created by Administrator on 11/21/2016.
 */
public final class Directory
{
    private final byte[] _dir;

    public Directory (FastMemoryFile fmf) throws Exception
    {
        _dir = DiskRW.readSector(fmf, 19);
    }

    public byte[] getDataBlock()
    {
        return _dir;
    }

    public String list()
    {
        StringBuilder sb = new StringBuilder();
        for (int s=0; ; s++)
        {
            DirectoryEntry de = new DirectoryEntry(_dir, s * 32);
            if (de.nullEntry)
                break;
            sb.append(de.toString()).append('\n');
        }
        return sb.toString();
    }

    public DirectoryEntry seekFile (String fname)
    {
        fname = fname.toUpperCase();
        for (int s=0; ; s++)
        {
            DirectoryEntry de = new DirectoryEntry(_dir, s * 32);
            if (de.nullEntry)
                return null;
            if (de.getFullName().equals(fname))
                return de;
        }
    }
}
