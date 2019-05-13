package bluetooth.client.pbap.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.obex.HeaderSet;

public final class ObexAppParameters {
    private final HashMap<Byte, byte[]> mParams = new HashMap();

    public ObexAppParameters(byte[] raw) {
        if (raw != null) {
            int i = 0;
            while (i < raw.length && raw.length - i >= 2) {
                int i2 = i + 1;
                byte tag = raw[i];
                i = i2 + 1;
                byte len = raw[i2];
                if ((raw.length - i) - len >= 0) {
                    byte[] val = new byte[len];
                    System.arraycopy(raw, i, val, 0, len);
                    add(tag, val);
                    i += len;
                } else {
                    return;
                }
            }
        }
    }

    public static ObexAppParameters fromHeaderSet(HeaderSet headerset) {
        try {
            return new ObexAppParameters((byte[]) headerset.getHeader(76));
        } catch (IOException e) {
            return null;
        }
    }

    public byte[] getHeader() {
        int length = 0;
        for (Entry<Byte, byte[]> entry : this.mParams.entrySet()) {
            length += ((byte[]) entry.getValue()).length + 2;
        }
        byte[] ret = new byte[length];
        int idx = 0;
        for (Entry<Byte, byte[]> entry2 : this.mParams.entrySet()) {
            length = ((byte[]) entry2.getValue()).length;
            int i = idx + 1;
            ret[idx] = ((Byte) entry2.getKey()).byteValue();
            idx = i + 1;
            ret[i] = (byte) length;
            System.arraycopy(entry2.getValue(), 0, ret, idx, length);
            idx += length;
        }
        return ret;
    }

    public void addToHeaderSet(HeaderSet headerset) {
        if (this.mParams.size() > 0) {
            headerset.setHeader(76, getHeader());
        }
    }

    public boolean exists(byte tag) {
        return this.mParams.containsKey(Byte.valueOf(tag));
    }

    public void add(byte tag, byte val) {
        this.mParams.put(Byte.valueOf(tag), ByteBuffer.allocate(1).put(val).array());
    }

    public void add(byte tag, short val) {
        this.mParams.put(Byte.valueOf(tag), ByteBuffer.allocate(2).putShort(val).array());
    }

    public void add(byte tag, int val) {
        this.mParams.put(Byte.valueOf(tag), ByteBuffer.allocate(4).putInt(val).array());
    }

    public void add(byte tag, long val) {
        this.mParams.put(Byte.valueOf(tag), ByteBuffer.allocate(8).putLong(val).array());
    }

    public void add(byte tag, String val) {
        this.mParams.put(Byte.valueOf(tag), val.getBytes());
    }

    public void add(byte tag, byte[] bval) {
        this.mParams.put(Byte.valueOf(tag), bval);
    }

    public byte getByte(byte tag) {
        byte[] bval = (byte[]) this.mParams.get(Byte.valueOf(tag));
        if (bval == null || bval.length < 1) {
            return (byte) 0;
        }
        return ByteBuffer.wrap(bval).get();
    }

    public short getShort(byte tag) {
        byte[] bval = (byte[]) this.mParams.get(Byte.valueOf(tag));
        if (bval == null || bval.length < 2) {
            return (short) 0;
        }
        return ByteBuffer.wrap(bval).getShort();
    }

    public int getInt(byte tag) {
        byte[] bval = (byte[]) this.mParams.get(Byte.valueOf(tag));
        if (bval == null || bval.length < 4) {
            return 0;
        }
        return ByteBuffer.wrap(bval).getInt();
    }

    public String getString(byte tag) {
        byte[] bval = (byte[]) this.mParams.get(Byte.valueOf(tag));
        if (bval == null) {
            return null;
        }
        return new String(bval);
    }

    public byte[] getByteArray(byte tag) {
        return (byte[]) this.mParams.get(Byte.valueOf(tag));
    }

    public String toString() {
        return this.mParams.toString();
    }
}
