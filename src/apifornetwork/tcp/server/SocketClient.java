package apifornetwork.tcp.server;

import apifornetwork.data.packets.SendPacket;
import apifornetwork.tcp.SocketMake;

import java.io.*;
import java.net.Socket;

public class SocketClient extends SocketMake {

    protected final ServerTCP server;

    public SocketClient(final Socket s, ServerTCP server) throws IOException {
        super(s);
        this.server = server;
        System.out.println("Creating socket client !");
    }

    public void initUDP() {

    }
}