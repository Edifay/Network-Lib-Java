package apifornetwork.data.packets;

import apifornetwork.data.Data;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public abstract class Packet {

    protected byte[][] data;

    protected final int headSize = 6;

    protected short packetNumber;

    public abstract Data getData();

    public byte[][] getBytes() {
        return this.data;
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
        out.writeObject(obj);
        return outArray.toByteArray();
    }

}
