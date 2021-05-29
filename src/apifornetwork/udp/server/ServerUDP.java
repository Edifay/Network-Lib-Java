package apifornetwork.udp.server;

import apifornetwork.tcp.server.ServerTCP;

import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerUDP extends DatagramSocket {

    protected final ServerTCP serverTCP;

    public ServerUDP(final int port, final ServerTCP serverTCP) throws SocketException {
        super(port);
        this.serverTCP = serverTCP;
    }

}
