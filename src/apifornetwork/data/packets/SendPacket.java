package apifornetwork.data.packets;

import apifornetwork.data.Data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

public class SendPacket extends Packet {

    protected final short packetSize;

    public SendPacket(final short packetNumber, final byte[] data, final short packetSize) {
        this.packetNumber = packetNumber;
        this.packetSize = packetSize;
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        this.data = new byte[(int) Math.ceil((double) in.available() / (packetSize - this.headSize))][packetSize];
        System.out.println("Data size -> " + in.available() + "bytes -> " + this.data.length + " * " + this.data[0].length + " = " + this.getSize() + "bytes. Head size -> " + this.headSize + ". Packet number -> " + this.packetNumber);
        short actualTable = 0;
        short totalTableSize = (short) this.data.length;
        while (in.available() > 0) {
            System.arraycopy(getByteFromShort(actualTable), 0, this.data[actualTable], 1, 2);
            System.arraycopy(getByteFromShort(totalTableSize), 0, this.data[actualTable], 3, 2);
            System.arraycopy(getByteFromShort(this.packetNumber), 0, this.data[actualTable], 5, 2);
            System.out.println("Read directly : " + getShort(this.data[actualTable][5], this.data[actualTable][6]));
            in.read(this.data[actualTable], this.headSize, packetSize - this.headSize);
            actualTable++;
        }
        System.out.println(Arrays.deepToString(this.data));
    }

    public SendPacket(final short packetNumber, final byte[] data) throws IOException {
        this(packetNumber, data, (short) 8192);
    }

    @Override
    public Data getData() {
        return null;
    }

    public int getSize() {
        return this.data[0].length * this.data.length;
    }

}
