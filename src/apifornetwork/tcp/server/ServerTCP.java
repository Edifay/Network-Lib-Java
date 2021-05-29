package apifornetwork.tcp.server;

import apifornetwork.udp.server.ServerUDP;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerTCP extends ServerSocket {

    private Thread listen;

    private final ArrayList<RunnableParamSocket> eventsOnNewClient;
    private final ExecutorService exe = Executors.newCachedThreadPool();

    private final short packetSize;

    private ServerUDP udp;

    public ServerTCP(final int port, short packetSize) throws IOException {
        super(port);
        this.eventsOnNewClient = new ArrayList<>();
        this.listen = new Thread();
        this.packetSize = packetSize;
    }

    public void addEventOnNewClient(RunnableParamSocket event) {
        synchronized (this.eventsOnNewClient) {
            this.eventsOnNewClient.add(event);
        }
    }

    public void removeEventOnNewClient(RunnableParamSocket event) {
        synchronized (this.eventsOnNewClient) {
            this.eventsOnNewClient.remove(event);
        }
    }

    public void startListenClient() {
        synchronized (this.listen) {
            this.listen = new Thread(() -> {
                Thread.currentThread().setName("Server for Client Waiting");
                try {
                    while (true) {
                        SocketClient socket = new SocketClient(this.accept(), this);
                        new Thread(() -> {
                            synchronized (this.eventsOnNewClient) {
                                for (RunnableParamSocket atRun : this.eventsOnNewClient) {
                                    System.out.println("Exe " + this.eventsOnNewClient.size());
                                    exe.submit(new NewClientEvent(socket, atRun));
                                }
                            }
                        }).start();

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            this.listen.start();
        }

    }

    public void stopListenClient() {
        synchronized (this.listen) {
            this.listen.interrupt();
        }
    }

    public boolean isBindedUDP() {
        return this.udp != null;
    }

}
