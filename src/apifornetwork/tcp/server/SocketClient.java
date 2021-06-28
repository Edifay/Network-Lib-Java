package apifornetwork.tcp.server;

import apifornetwork.data.packets.ReceivePacket;
import apifornetwork.data.packets.SendPacket;
import apifornetwork.tcp.SocketMake;
import apifornetwork.udp.Auth;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class SocketClient extends SocketMake {

    protected final ServerTCP server;

    public SocketClient(final Socket s, ServerTCP server) throws IOException {
        super(s);
        this.server = server;
    }

    public synchronized void initUDP() throws IOException, InterruptedException {
        byte[] data = new byte[1];
        if (this.server.initFastPacket())
            data[0] = 0x1;
        this.send(new SendPacket((short) -3, data, (short) 1));
        ReceivePacket packet = this.waitForPacket(-4);
        this.identity = new Auth(getInteger(packet.getBytesData()), this.s.getInetAddress());
    }

    @Override
    public synchronized void send(SendPacket packet) throws IOException {
        if (packet.isFastPacket()) {
            packet.setServerSender(this);
            this.server.sendFastPacket(packet, this.identity);
        } else
            super.send(packet);
    }

}