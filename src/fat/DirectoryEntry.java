package fat;

import misc.ByteCVT;

/**
 * Created by Administrator on 11/20/2016.
 */
public class DirectoryEntry
{
    public boolean deleted;
    public boolean nullEntry;
    public String fileName;
    public String extension;
    public int attributes;
    public int creationTime;
    public int creationDate;
    public int lastAccessData;
    public int lastWriteTime;
    public int lastWriteDate;
    public int firstLogicalCluster;
    public long fileSize;

    public DirectoryEntry (byte[] array, int offset)
    {
        nullEntry = (array[offset] == 0);
        if (nullEntry)
            return;
        deleted = (array[offset] == (byte)0xe5);
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
