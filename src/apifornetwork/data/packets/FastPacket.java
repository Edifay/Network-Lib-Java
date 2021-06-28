package apifornetwork.data.packets;

import java.io.IOException;

public class FastPacket extends SendPacket {
    public FastPacket(short packetNumber, byte[] data, short packetSize) {
        super(packetNumber, data, packetSize);
        this.isFastPacket = true;
    }

    public FastPacket(final short packetNumber, final byte[] data) throws IOException {
        super(packetNumber, data);
        this.isFastPacket = true;
    }
}
