package apifornetwork.tcp.Secure;

import apifornetwork.data.packets.ReceivePacket;
import apifornetwork.data.packets.SendPacket;
import apifornetwork.tcp.SocketMake;
import apifornetwork.udp.Auth;

import java.io.IOException;
import java.net.Socket;

public class SecureSocketClient extends SocketMake {


    protected final SecureServerTCP server;

    public SecureSocketClient(final Socket s, SecureServerTCP server) throws IOException {
        super(s);
        this.server = server;
    }
/*

    public synchronized void initUDP() throws IOException, InterruptedException {
        byte[] data = new byte[1];
        if (this.server.initFastPacket())
            data[0] = 0x1;
        this.send(new SendPacket((short) -3, data, (short) 1));
        ReceivePacket packet = this.waitForPacket(-4);
        this.identity = new Auth(getInteger(packet.getBytesData()), this.s.getInetAddress());
    }*/

    @Override
    public synchronized void send(SendPacket packet) throws IOException {
        if (packet.isFastPacket()) {
            packet.setServerSender(this);
            //this.server.sendFastPacket(packet, this.identity);
        } else
            super.send(packet);
    }

}
