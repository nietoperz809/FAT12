package fat;

import mappedfile.FastMemoryFile;

/**
 * Repesents FAT12 Directory
 */
final class Directory
{
    /**
     * Copy of directora
     */
    private final byte[] directoryBytes;
    /**
     * Reference to FMF that holds entire disk
     */
    FastMemoryFile parentFMF;

    /**
     * Number of Dir Entries
     */
    public final static int DIRENTRYCOUNT = 224;
    /**
     * Byte size of single Dir entry
     */
    public final static int DIRENTRYSIZE = 32;

    /**
     * Constructor
     * @param fmf FMF containing disk file
     * @throws Exception
     */
    public Directory (FastMemoryFile fmf) throws Exception
    {
        parentFMF = fmf;
        directoryBytes = DiskRW.readDirectory(fmf);
    }

    /**
     * Writes dir bytes back to Disk FMF
     * @throws Exception
     */
    public void writeBack() throws Exception
    {
        DiskRW.writeDirectory (parentFMF, directoryBytes);
    }

//    public byte[] getDataBlock()
//    {
//        return directoryBytes;
//    }

    /**
     * List all files of master directory as human readable string
     * @return string containing list
     */
    public String list()
    {
        StringBuilder sb = new StringBuilder();
        for (int s=0; ; s++)
        {
            DirectoryEntry de = new DirectoryEntry(directoryBytes, s * DIRENTRYSIZE);
            if (de.nullEntry)
                break;
            sb.append(de.toString()).append('\n');
        }
        return sb.toString();
    }

    /**
     * Seeks file by file name
     * @param fname file name
     * @return new Dir entry representing the file
     */
    public DirectoryEntry seekFile (String fname)
    {
        fname = fname.toUpperCase();
        for (int s=0; ; s++)
        {
            DirectoryEntry de = new DirectoryEntry(directoryBytes, s * DIRENTRYSIZE);
            if (de.nullEntry)
                throw new RuntimeException("file not found");
            if (de.getFullName().equals(fname))
            {
                de.positionInDirectory = s;
                return de;
            }
        }
    }

    /**
     * Sets new dir entry at specified position
     * @param de The new Dir Entry
     * @param index index of position in dir table
     */
    public void put (DirectoryEntry de, int index)
    {
        int offset = DirectoryEntry.DIRENTRYSIZE * index;
        byte[] dat = de.asArray();
        System.arraycopy(dat,0, directoryBytes,offset, DirectoryEntry.DIRENTRYSIZE);
    }

    /**
     * Finds first free Dir index
     * @return Found index or none (in this case an Exception is thrown)
     */
    public int getFreeDirectoryEntryOffset ()
    {
        for (int s = 0; s< DIRENTRYCOUNT; s++)
        {
            DirectoryEntry de = new DirectoryEntry(directoryBytes, s * DIRENTRYSIZE);
            if (de.nullEntry || de.deleted)
                return s;
        }
        throw new RuntimeException("directory full");
    }
}
