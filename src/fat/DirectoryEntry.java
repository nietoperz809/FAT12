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

    private byte[] RawData = null;

    /**
     * Empty Constructor made private
     */
    private DirectoryEntry()
    {

    }

    /**
     * Constructor from bigger array that contains a Directory Entry
     * @param array Byte array
     * @param offset Offset where the directory entry can be found
     */
    public DirectoryEntry (byte[] array, int offset)
    {
        fillMembers(array, offset);
    }

    /**
     * Get Dir entry as array
     * @return the dir entry as bytes
     */
    public byte[] asArray()
    {
        return RawData;
    }

    /**
     * Number of bytes of single dir entry
     */
    public static final int DIRENTRYSIZE = 32;

    /**
     * Build a Dir entry from input
     * @param name File name
     * @param ext File extension
     * @param fileSize File size
     * @param firstCluster First cluster on disk
     * @return Newly created Dir Entry
     */
    public static DirectoryEntry create (String name, String ext,
                                         int fileSize,
                                         int firstCluster)
    {
        DirectoryEntry d = new DirectoryEntry();
        d.RawData = new byte[DIRENTRYSIZE];
        for (int s=0; s<8; s++)
        {
            if (s < name.length())
                d.RawData[s] = (byte) name.charAt(s);
            else
                d.RawData[s] = (byte)' ';
        }
        for (int s=0; s<3; s++)
        {
            if (s < ext.length())
                d.RawData[s+8] = (byte) ext.charAt(s);
            else
                d.RawData[s+8] = (byte)' ';
        }
        d.RawData[11] = 32;      // archive bit
        ByteCVT.toLE16(0, d.RawData, 14);
        ByteCVT.toLE16(0, d.RawData, 16);
        ByteCVT.toLE16(0, d.RawData, 18);
        ByteCVT.toLE16(0, d.RawData, 22);
        ByteCVT.toLE16(0, d.RawData, 24);
        ByteCVT.toLE16(firstCluster, d.RawData, 26);
        ByteCVT.toLE32(fileSize, d.RawData, 28);
        d.fillMembers(d.RawData, 0);
        return d;
    }

    /**
     * Fill data memebers from byte array
     * @param array byte array
     * @param offset offest where the dir entry can be found
     */
    private void fillMembers (byte[] array, int offset)
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

    /**
     * Get Name.Ext
     * @return String containing name+ext with a dot between them
     */
    public String getFullName()
    {
        if (extension.isEmpty())
            return fileName;
        return fileName+"."+extension;
    }

    /**
     * Shows this dir entry in human readable form
     * @return
     */
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
