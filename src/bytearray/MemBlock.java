package bytearray;

import java.text.SimpleDateFormat;
import java.util.Date;

class MemBlock {
    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    final int address;
    final byte[] data;
    final UndoableDynamicByteArray.Action _act;
    final Date timestamp;
    final int currentSize;

    MemBlock(UndoableDynamicByteArray.Action act, int addr, byte[] dat, int size) {
        _act = act;
        address = addr;
        data = new byte[dat.length];
        System.arraycopy(dat, 0, data, 0, dat.length);
        timestamp = new Date();
        currentSize = size;
    }

    @Override
    public String toString() {
        return _act.name() + "|" +
                address + "|" +
                data.length + "|" +
                currentSize + "|" +
                sdf.format(timestamp);
    }
}
