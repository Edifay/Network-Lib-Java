package apifornetwork.data.packets;

import apifornetwork.data.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class Packet {

    protected boolean isFastPacket;

    protected byte[][] data;

    protected static final short headSize = 9;

    protected short packetNumber;

    public abstract Data getData();

    protected short ID;

    public byte[][] getBytes() {
        return this.data;
    }

    public byte[] getBytesData() {
        byte[] data = new byte[this.data.length * (this.data[0].length - headSize)];
        for (int i = 0; i < this.data.length; i++)
            System.arraycopy(this.data[i], headSize, data, i * (this.data[i].length - headSize), this.data[i].length - headSize);
        return data;
    }

    public static short getShort(byte one, byte two) {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.put(one);
        bb.put(two);
        return bb.getShort(0);
    }

    public static byte[] getByteFromShort(short s) {
        ByteBuffer bb = ByteBuffer.allocate(2);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort(s);
        return bb.array();
    }

    public static byte[] getByteForObject(Object obj) throws IOException {
        ByteArrayOutputStream outArray = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(outArray);
        out.writeUnshared(obj);
        return outArray.toByteArray();
    }

    public boolean isFastPacket() {
        return this.isFastPacket;
    }

    public short getID() {
        return this.ID;
    }

}
