package apifornetwork.tcp;

import apifornetwork.data.packets.NotFinalizedReceivePacket;
import apifornetwork.data.packets.ReceivePacket;
import apifornetwork.data.packets.SendPacket;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class SocketMake {

    protected Socket s;
    protected Thread listen;
    protected ObjectOutputStream out;
    protected ObjectInputStream in;
    final protected HashMap<Integer, ArrayList<RunnableParamPacket>> events;
    protected ArrayList<RunnableParamPacket> eventsOnAll;
    protected ExecutorService exe = Executors.newCachedThreadPool();

    public SocketMake(final Socket s) throws IOException {
        this.s = s;
        this.out = new ObjectOutputStream(s.getOutputStream());
        this.in = new ObjectInputStream(s.getInputStream());
        this.events = new HashMap<>();
        this.eventsOnAll = new ArrayList<>();
        this.listen = new Thread();
    }

    public void startListen() {
        System.out.println("start Listen");
        synchronized (this.listen) {
            this.listen = new Thread(() -> {
                Thread.currentThread().setName("Client: Listening !");
                try {
                    while (true) {
                        ReceivePacket receivePacket = new NotFinalizedReceivePacket((byte[][]) this.in.readObject()).waitForFinalized();
                        System.out.println("Receive in  loop packet : " + receivePacket.getPacketNumber() + " class: " + this.getClass().getName());
                        this.emit(receivePacket);
                    }
                } catch (IOException | ClassNotFoundException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
            this.listen.start();
        }
    }

    public void emit(ReceivePacket packet) {
        new Thread(() -> {
            synchronized (this.events) {
                for (RunnableParamPacket event : this.eventsOnAll)
                    this.exe.submit(new NewPacketEvent(packet, event));
                if (this.events.containsKey((int) packet.getPacketNumber()))
                    for (RunnableParamPacket event : this.events.get((int) packet.getPacketNumber()))
                        this.exe.submit(new NewPacketEvent(packet, event));
            }
        }).start();
    }

    public void stopListen() {
        synchronized (this.listen) {
            this.listen.interrupt();
        }
    }

    public synchronized void send(SendPacket packet) throws IOException {
        this.out.writeObject(packet.getBytes());
        this.out.flush();
    }

    public void addPacketEvent(int packetNumber, RunnableParamPacket event) {
        synchronized (this.events) {
            if (this.events.containsKey(packetNumber))
                this.events.get(packetNumber).add(event);
            else {
                ArrayList<RunnableParamPacket> array = new ArrayList<>();
                array.add(event);
                this.events.put(packetNumber, array);
            }
        }
    }

    public void removePacketEvent(int packetNumber, RunnableParamPacket event) {
        synchronized (this.events) {
            if (this.events.containsKey(packetNumber))
                this.events.get(packetNumber).remove(event);
        }
    }

    public void addPacketEvent(RunnableParamPacket event) {
        synchronized (this.events) {
            this.eventsOnAll.add(event);
        }
    }

    public void removePacketEvent(RunnableParamPacket event) {
        synchronized (this.events) {
            this.eventsOnAll.remove(event);
        }
    }

    public ReceivePacket waitForPacket(int packetNumber) throws InterruptedException {
        Thread.currentThread().setName("CACA");
        final Thread atNotify = Thread.currentThread();
        AtomicReference<ReceivePacket> receivePacket = new AtomicReference<>();
        this.addPacketEvent(packetNumber, (packet) -> {
            synchronized (atNotify) {
                atNotify.notify();
                receivePacket.set(packet);
            }
        });
        synchronized (Thread.currentThread()) {
            Thread.currentThread().wait();
        }
        return receivePacket.get();
    }

}