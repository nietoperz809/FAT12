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

    public int positionInDirectory; // Internal use

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
        RawData = new byte[DIRENTRYSIZE];
        System.arraycopy (array,offset, RawData,0,DIRENTRYSIZE);
        fillMembers();
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

    public static final int READONLY = 0x01;
    public static final int HIDDEN = 0x02;
    public static final int SYSTEM = 0x04;
    public static final int VOLUMELABEL = 0x08;
    public static final int SUBDIRECTORY = 0x10;
    public static final int ARCHIVE = 0x20;
    public static final byte DELETED = (byte)0xe5;


    /**
     * Create a volume label dir entry
     * @param lab the label text
     * @return a dir entry with that label
     */
    public static DirectoryEntry createVolumeLabel (String lab)
    {
        while (lab.length() < 11)
            lab = lab+' ';
        return create (lab.substring(0,8), lab.substring(8,11), 0,0, VOLUMELABEL);
    }

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
                                         int firstCluster,
                                         int attributes)
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
        d.RawData[11] = (byte)attributes;
        ByteCVT.toLE16(0, d.RawData, 14);
        ByteCVT.toLE16(0, d.RawData, 16);
        ByteCVT.toLE16(0, d.RawData, 18);
        ByteCVT.toLE16(0, d.RawData, 22);
        ByteCVT.toLE16(0, d.RawData, 24);
        ByteCVT.toLE16(firstCluster, d.RawData, 26);
        ByteCVT.toLE32(fileSize, d.RawData, 28);
        d.fillMembers();
        return d;
    }

    /**
     * Fill data memebers from byte array
     */
    private void fillMembers ()
    {
        nullEntry = (RawData[0] == 0);
        if (nullEntry)
            return;
        deleted = ((RawData[0] == DELETED));
        if (deleted)
            fileName = "?"+new String (RawData, 1, 7).trim();
        else
            fileName = new String (RawData, 0, 8).trim();
        extension = new String (RawData, 8, 3).trim();
        attributes = RawData[11];
        creationTime = ByteCVT.fromLE16(RawData, 14);
        creationDate = ByteCVT.fromLE16(RawData, 16);
        lastAccessData = ByteCVT.fromLE16(RawData, 18);
        lastWriteTime = ByteCVT.fromLE16(RawData, 22);
        lastWriteDate = ByteCVT.fromLE16(RawData, 24);
        firstLogicalCluster = ByteCVT.fromLE16(RawData, 26);
        fileSize = ByteCVT.fromLE32(RawData, 28);
    }

    public void markAsDeleted ()
    {
        deleted = true;
        RawData[0] = DELETED;
    }

    /**
     * Get Name.Ext
     * @return String containing name+ext with a dot between them
     */
    public String getFullName()
    {
        if (extension.isEmpty())
            return fileName.toUpperCase();
        return (fileName+"."+extension).toUpperCase();
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
