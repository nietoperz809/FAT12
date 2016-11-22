package fat;

import misc.ByteCVT;

/**
 * Created by Administrator on 11/20/2016.
 */
public class DirectoryEntry
{
    public boolean deleted;
    public boolean nullEntry;
    private String fileName;
    private String extension;
    private int attributes;
    private int creationTime;
    private int creationDate;
    private int lastAccessData;
    private int lastWriteTime;
    private int lastWriteDate;
    public int firstLogicalCluster;
    public long fileSize;

    private byte[] _data = null;

    public static DirectoryEntry create (String name, String ext,
                                         int fileSize,
                                         int firstCluster)
    {
        DirectoryEntry d = new DirectoryEntry();
        d._data = new byte[32];
        for (int s=0; s<8; s++)
        {
            if (s < name.length())
                d._data[s] = (byte) name.charAt(s);
            else
                d._data[s] = (byte)' ';
        }
        for (int s=0; s<3; s++)
        {
            if (s < ext.length())
                d._data[s+8] = (byte) ext.charAt(s);
            else
                d._data[s+8] = (byte)' ';
        }
        d._data[11] = 32;      // archive bit
        ByteCVT.toLE16(0, d._data, 14);
        ByteCVT.toLE16(0, d._data, 16);
        ByteCVT.toLE16(0, d._data, 18);
        ByteCVT.toLE16(0, d._data, 22);
        ByteCVT.toLE16(0, d._data, 24);
        ByteCVT.toLE16(firstCluster, d._data, 26);
        ByteCVT.toLE32(fileSize, d._data, 28);
        d.getData (d._data, 0);
        return d;
    }

    public DirectoryEntry (byte[] array, int offset)
    {
        getData (array, offset);
    }

    private DirectoryEntry()
    {

    }

    private void getData (byte[] array, int offset)
    {
        nullEntry = (array[offset] == 0);
        if (nullEntry)
            return;
        deleted = ((array[offset] == (byte)0xe5));
        if (deleted)
            fileName = "?"+new String (array, offset+1, 7).trim();
        else
            fileName = new String (array, offset, 8).trim();
        extension = new String (array, offset+8, 3).trim();
        attributes = array[offset+11];
        creationTime = ByteCVT.fromLE16(array, offset+14);
        creationDate = ByteCVT.fromLE16(array, offset+16);
        lastAccessData = ByteCVT.fromLE16(array, offset+18);
        lastWriteTime = ByteCVT.fromLE16(array, offset+22);
        lastWriteDate = ByteCVT.fromLE16(array, offset+24);
        firstLogicalCluster = ByteCVT.fromLE16(array, offset+26);
        fileSize = ByteCVT.fromLE32(array, offset+28);
    }

    public String getFullName()
    {
        if (extension.isEmpty())
            return fileName;
        return fileName+"."+extension;
    }

    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append (", ").append (fileSize).append(" Bytes, ");
        if ((attributes & 1) == 1)
            sb.append("Rea");
        if ((attributes & 2) == 2)
            sb.append("Hid");
        if ((attributes & 4) == 4)
            sb.append("Sys");
        if ((attributes & 8) == 8)
            sb.append("Lab");
        if ((attributes & 16) == 16)
            sb.append("Dir");
        if ((attributes & 32) == 32)
            sb.append("Arc");
        if (deleted)
            sb.append("Del");
        sb.append (", ").append ("1stSector: ").append(firstLogicalCluster);
        return sb.toString();
    }
}
