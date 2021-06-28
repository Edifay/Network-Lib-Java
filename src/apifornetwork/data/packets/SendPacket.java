package apifornetwork.data.packets;

import apifornetwork.data.Data;
import apifornetwork.tcp.SocketMake;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static apifornetwork.tcp.server.ServerTCP.packetFastSize;

public class SendPacket extends Packet {

    protected final short packetSize;

    public SendPacket(final short packetNumber, final byte[] data, final short packetLength, final boolean isFast) {
        this.isFastPacket = isFast;
        this.packetNumber = packetNumber;
        this.packetSize = isFast ? (short) (packetFastSize) : (short) (packetLength + headSize);
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        this.data = new byte[(int) Math.ceil((double) in.available() / (this.packetSize - headSize))][this.packetSize];
        short actualTable = 0;
        short totalTableSize = (short) this.data.length;
        while (in.available() > 0) {
            System.arraycopy(getByteFromShort(actualTable), 0, this.data[actualTable], 1, 2); // table number 1 -> 2
            System.arraycopy(getByteFromShort(totalTableSize), 0, this.data[actualTable], 3, 2); // total table 3 -> 4
            System.arraycopy(getByteFromShort(this.packetNumber), 0, this.data[actualTable], 5, 2); // packet number 5 -> 6
            in.read(this.data[actualTable], headSize, packetSize - headSize);
            actualTable++;
        }
    }

    public SendPacket(final short packetNumber, final byte[] data) throws IOException {
        this(packetNumber, data, (short) (packetFastSize-headSize), false);
    }

    public SendPacket(final short packetNumber, final byte[] data, final short packetLength) {
        this(packetNumber, data, packetLength, false);
    }

    @Override
    public Data getData() {
        return null;
    }

    public int getSize() {
        return this.data[0].length * this.data.length;
    }

    public void setServerSender(SocketMake socketMake) throws IOException {
        this.ID = socketMake.getNewID();
        for (byte[] datum : this.data) System.arraycopy(getByteFromShort(this.ID), 0, datum, 7, 2);
    }

    public static SendPacket emptyPacketOf(int packetNumber, boolean isFast) {
        int size = isFast ? packetFastSize : 1;
        return new SendPacket((short) packetNumber, new byte[size], (short) size, isFast);
    }

}
