package apifornetwork.tcp.client;

import apifornetwork.tcp.SocketMake;

import java.io.IOException;
import java.net.Socket;

public class ClientTCP extends SocketMake {

    public ClientTCP(final String ip, final int port) throws IOException {
        super(new Socket(ip, port));
    }

}
