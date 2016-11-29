package fat;

import misc.ByteCVT;

import java.time.LocalDateTime;

/**
 * Created by Administrator on 11/20/2016.
 */
public class DirectoryEntry
{
//    public boolean deleted;
//    public boolean nullEntry;
//    private String fileName;
//    private String extension;
//    private int attributes;
//    private int creationTime;
//    private int creationDate;
//    private int lastAccessData;
//    private int lastWriteTime;
//    private int lastWriteDate;
//    public int firstLogicalCluster;
//    public long fileSize;

    /**
     * Number of bytes of single dir entry
     */
    public static final int DIRENTRYSIZE = 32;
    /**
     * Attribute bits
     */
    public static final int READONLY = 0x01;
    public static final int HIDDEN = 0x02;
    public static final int SYSTEM = 0x04;
    public static final int VOLUMELABEL = 0x08;
    public static final int SUBDIRECTORY = 0x10;
    public static final int ARCHIVE = 0x20;
    public static final byte DELETED = (byte) 0xe5;
    public int positionInDirectory; // Internal use
    private byte[] RawData = null;
    /**
     * Standard Constructor made private
     */
    private DirectoryEntry ()
    {
        RawData = new byte[DIRENTRYSIZE];
    }
    /**
     * Constructor from bigger array that contains a Directory Entry
     *
     * @param array  Byte array
     * @param offset Offset where the directory entry can be found
     */
    public DirectoryEntry (byte[] array, int offset)
    {
        RawData = new byte[DIRENTRYSIZE];
        System.arraycopy(array, offset, RawData, 0, DIRENTRYSIZE);
    }

    /**
     * Create a volume label dir entry
     *
     * @param lab the label text
     * @return a dir entry with that label
     */
    public static DirectoryEntry createVolumeLabel (String lab)
    {
        while (lab.length() < 11)
        {
            lab = lab + ' ';
        }
        return create(lab.substring(0, 8), lab.substring(8, 11), 0, 0, (byte) VOLUMELABEL);
    }

    /**
     * Build a Dir entry from input
     *
     * @param name         File name
     * @param ext          File extension
     * @param fileSize     File size
     * @param firstCluster First cluster on disk
     * @return Newly created Dir Entry
     */
    public static DirectoryEntry create (String name, String ext,
                                         long fileSize,
                                         int firstCluster,
                                         int attributes)
    {
        int ts = getCurrentTimeStamp();
        int ds = getCurrentDateStmp();
        DirectoryEntry d = new DirectoryEntry();
        d.setFileName(name);
        d.setExtension(ext);
        d.setAttributes((byte) attributes);
        ByteCVT.toLE16(ts, d.RawData, 14);
        ByteCVT.toLE16(ds, d.RawData, 16);
        ByteCVT.toLE16(0, d.RawData, 18);
        ByteCVT.toLE16(ts, d.RawData, 22);
        ByteCVT.toLE16(ds, d.RawData, 24);
        d.setFirstCluster(firstCluster);
        d.setFileSize(fileSize);
        return d;
    }

    public static DirectoryEntry createSubdirEntry (String name, String ext, int cluster)
    {
        return create(name, ext, 0, cluster, (byte) SUBDIRECTORY);
    }

    /**
     * Make FAT time stamp
     * @param sec seconds 0...59
     * @param min minutes 0...59
     * @param hour hours 0...23
     * @return Packed time stamp
     */
    public static int getTimeStamp (int sec, int min, int hour)
    {
        int a = (sec/2) & 31;
        int b = (min & 63) << 5;
        int c = (hour & 31) << 11;
        return (a | b | c) & 0xffff;
    }

    public static int getCurrentTimeStamp()
    {
        LocalDateTime now = LocalDateTime.now();
        int hour = now.getHour();
        int minute = now.getMinute();
        int second = now.getSecond();
        return getTimeStamp(second, minute, hour);
    }

    /**
     * Makes FAT date stamp
     * @param day day 1...31
     * @param month month 1...12
     * @param year year 1980...2107
     * @return
     */
    public static int getDateStamp (int day, int month, int year)
    {
        int a = (day) & 31;
        int b = (month & 15) << 5;
        int c = ((year-1980) & 127) << 9;
        return (a | b | c) & 0xffff;
    }

    public static int getCurrentDateStmp()
    {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonthValue();
        int day = now.getDayOfMonth();
        return getDateStamp(day, month, year);
    }

    /**
     * Get Dir entry as array
     *
     * @return the dir entry as bytes
     */
    public byte[] asArray ()
    {
        return RawData;
    }

    public void setDeleted ()
    {
        RawData[0] = DELETED;
    }

    public boolean isNull ()
    {
        return (RawData[0] == 0);
    }

    /**
     * Shows this dir entry in human readable form
     *
     * @return
     */
    @Override
    public String toString ()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName());
        sb.append(", ").append(getFileSize()).append(" Bytes, ");
        byte attributes = getAttributes();
        if ((attributes & 1) == 1)
        {
            sb.append("Rea");
        }
        if ((attributes & 2) == 2)
        {
            sb.append("Hid");
        }
        if ((attributes & 4) == 4)
        {
            sb.append("Sys");
        }
        if ((attributes & 8) == 8)
        {
            sb.append("Lab");
        }
        if ((attributes & 16) == 16)
        {
            sb.append("Dir");
        }
        if ((attributes & 32) == 32)
        {
            sb.append("Arc");
        }
        if (isDeleted())
        {
            sb.append("Del");
        }
        sb.append(", ").append("1stSector: ").append(getFirstCluster());
        return sb.toString();
    }

    /**
     * Get Name.Ext
     *
     * @return String containing name+ext with a dot between them
     */
    public String getFullName ()
    {
        String ext = getExtension();
        String name = getFileName();
        if (ext.isEmpty())
        {
            return name;
        }
        return name + "." + ext;
    }

    public long getFileSize ()
    {
        long fs = ByteCVT.fromLE32(RawData, 28);
        return fs;
    }

    public void setFileSize (long val)
    {
        ByteCVT.toLE32(val, RawData, 28);
    }

    public byte getAttributes ()
    {
        return RawData[11];
    }

    public boolean isDeleted ()
    {
        return (RawData[0] == DELETED);
    }

    public int getFirstCluster ()
    {
        return ByteCVT.fromLE16(RawData, 26);
    }

    public String getExtension ()
    {
        return new String(RawData, 8, 3).toUpperCase().trim();
    }

    public String getFileName ()
    {
        if (isDeleted())
        {
            return "?" + new String(RawData, 1, 7).toUpperCase().trim();
        }
        else
        {
            return new String(RawData, 0, 8).toUpperCase().trim();
        }
    }

    public void setFileName (String name)
    {
        for (int s = 0; s < 8; s++)
        {
            if (s < name.length())
            {
                RawData[s] = (byte) Character.toUpperCase(name.charAt(s));
            }
            else
            {
                RawData[s] = (byte) ' ';
            }
        }
    }

    public void setExtension (String ext)
    {
        for (int s = 0; s < 3; s++)
        {
            if (s < ext.length())
            {
                RawData[s + 8] = (byte) Character.toUpperCase(ext.charAt(s));
            }
            else
            {
                RawData[s + 8] = (byte) ' ';
            }
        }
    }

    public void setFirstCluster (int val)
    {
        ByteCVT.toLE16(val, RawData, 26);
    }

    public void setAttributes (byte v)
    {
        RawData[11] = v;
    }

}
