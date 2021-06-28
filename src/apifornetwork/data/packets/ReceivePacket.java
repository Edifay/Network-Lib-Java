package apifornetwork.data.packets;

import apifornetwork.data.Data;

public class ReceivePacket extends Packet {

    public ReceivePacket(final byte[][] allData, final boolean isFastPacket) {
        this.data = allData;
        this.isFastPacket = isFastPacket;
    }

    public ReceivePacket(final byte[][] allData, final short packetNumber, final boolean isFastPacket) {
        this.data = allData;
        this.packetNumber = packetNumber;
        this.isFastPacket = isFastPacket;
    }

    public short getPacketNumber() {
        return this.packetNumber;
    }

    public int getSize() {
        return this.data[0].length * this.data.length;
    }

    @Override
    public Data getData() {
        return null;
    }
}
