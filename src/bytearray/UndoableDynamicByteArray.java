package bytearray;

import bytearray.DynamicByteArray;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */
public class UndoableDynamicByteArray extends DynamicByteArray
{
    private final ArrayList<MemBlock> actionList = new ArrayList<>();

    private enum Action {PUT,DELETE,INSERT}

    private static class MemBlock
    {
        final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        final int address;
        final byte[] data;
        final Action _act;
        final Date timestamp;
        final int currentSize;

        MemBlock (Action act, int addr, byte[] dat, int size)
        {
            _act = act;
            address = addr;
            data = new byte[dat.length];
            System.arraycopy(dat, 0, data, 0, dat.length);
            timestamp = new Date();
            currentSize = size;
        }

        @Override
        public String toString()
        {
            return  _act.name() + "|" +
                    String.valueOf(address) + "|" +
                    data.length + "|" +
                    currentSize + "|" +
                    sdf.format(timestamp);
        }
    }

    public Enumeration<MemBlock> getMemBlocks()
    {
        return Collections.enumeration(actionList);
    }

    public void printMemBocks()
    {
        Enumeration<MemBlock> e = getMemBlocks();
        if (!e.hasMoreElements())
        {
            System.out.println("no memblocks. size == " + getCurrentSize());
        }
        else
        {
            System.out.println("memblok list:");
        }
        while(e.hasMoreElements())
            System.out.println(e.nextElement());
    }

    @Override
    public void put (int address, byte[] data)
    {
        int cs = super.getCurrentSize();
        byte[] old = super.get(address, data.length);
        actionList.add(new MemBlock (Action.PUT, address, old, cs));
        super.put(address, data);
    }

    @Override
    public void delete (int address, int len)
    {
        byte[] old = super.get(address, len);
        actionList.add(new MemBlock(Action.DELETE, address, old, super.getCurrentSize()));
        super.delete(address, len);
    }

    @Override
    public void insert (int address, byte[] data)
    {
        actionList.add(new MemBlock(Action.INSERT, address, data, super.getCurrentSize()));
        super.insert(address, data);
    }

    private void undo (MemBlock mb)
    {
        switch (mb._act)
        {
            case PUT:
                super.put (mb.address, mb.data);
                break;

            case DELETE:
                super.insert(mb.address, mb.data);
                break;

            case INSERT:
                super.delete(mb.address, mb.data.length);
                break;
        }
        theArray = Arrays.copyOf(theArray, mb.currentSize); // shrink array to old size
    }

    public void undo()
    {
        if (actionList.isEmpty())
            return;
        MemBlock mb = actionList.remove(actionList.size()-1);
        undo (mb);
    }
}
