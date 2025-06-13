package bytearray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

/**
 *
 */
public class UndoableDynamicByteArray extends DynamicByteArray {
    private final ArrayList<MemBlock> actionList = new ArrayList<>();

    enum Action {PUT, DELETE, INSERT}

    private Enumeration<MemBlock> getMemBlocks() {
        return Collections.enumeration(actionList);
    }

    public void printMemBocks() {
        Enumeration<MemBlock> e = getMemBlocks();
        if (!e.hasMoreElements()) {
            System.out.println("no memblocks. size == " + getCurrentSize());
        } else {
            System.out.println("memblok list:");
        }
        while (e.hasMoreElements())
            System.out.println(e.nextElement());
    }

    @Override
    public void put(int address, byte[] data) {
        int cs = super.getCurrentSize();
        byte[] old = super.get(address, data.length);
        actionList.add(new MemBlock(Action.PUT, address, old, cs));
        super.put(address, data);
    }

    @Override
    public void delete(int address, int len) {
        byte[] old = super.get(address, len);
        actionList.add(new MemBlock(Action.DELETE, address, old, super.getCurrentSize()));
        super.delete(address, len);
    }

    @Override
    public void insert(int address, byte[] data) {
        actionList.add(new MemBlock(Action.INSERT, address, data, super.getCurrentSize()));
        super.insert(address, data);
    }

    private void undo(MemBlock mb) {
        switch (mb._act) {
            case PUT:
                super.put(mb.address, mb.data);
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

    public void undo() {
        if (actionList.isEmpty())
            return;
        MemBlock mb = actionList.removeLast();
        undo(mb);
    }
}
