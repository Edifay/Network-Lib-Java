package apifornetwork;

import apifornetwork.data.packets.SendPacket;
import apifornetwork.tcp.client.ClientTCP;
import apifornetwork.tcp.server.ServerTCP;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    public static final short packetSize = 8192;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            Thread.currentThread().setName("test Server !");
            try {
                testServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            Thread.currentThread().setName("Test Client !");
            try {
                testClient();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void testServer() throws IOException {
        ServerTCP server = new ServerTCP(4999, packetSize);

        AtomicInteger receive = new AtomicInteger();
        server.addEventOnNewClient((socket) -> {
            System.out.println("Nouveau client connecté : " + " port : " + " client n°" + receive);
            receive.getAndIncrement();
            try {
                socket.startListen();
                socket.send(new SendPacket((short) 44, new byte[1], (short) 10));
                try {
                    socket.waitForPacket(20);
                    System.out.println("Receive 20");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("After sended !");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        server.startListenClient();
    }

    public static void testClient() throws IOException, InterruptedException {
        ClientTCP client = new ClientTCP("localhost", 4999);
        client.startListen();
        System.out.println("Start listening from client");
        /*client.addPacketEvent((packet) -> {
            System.out.println("Client receive : " + packet.getPacketNumber());
        });*/
        System.out.println(client.waitForPacket(44).getSize());
        client.send(new SendPacket((short) 20, new byte[1], (short) 10));
        System.out.println("After !");
    }


}