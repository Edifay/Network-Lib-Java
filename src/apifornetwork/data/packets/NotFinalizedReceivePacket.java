package apifornetwork.data.packets;

import apifornetwork.data.Data;

import java.util.ArrayList;
import java.util.Arrays;

public class NotFinalizedReceivePacket extends ReceivePacket {

    private final ArrayList<Thread> waitingForFinalize;
    private short[] packetsReceived;

    public NotFinalizedReceivePacket(final byte[][] allData) {
        super(allData);
        this.packetNumber = getShort(this.data[0][5], this.data[0][6]);
        System.out.println("Instant read : "+this.packetNumber);
        this.waitingForFinalize = new ArrayList<>();
        this.packetsReceived = new short[this.data.length];
        System.out.println(Arrays.toString(this.packetsReceived));
        emit();
    }

    public NotFinalizedReceivePacket(final byte[] firstPacketFound) {
        super(new byte[getShort(firstPacketFound[3], firstPacketFound[4])][firstPacketFound.length]);
        this.packetsReceived = new short[this.data.length];
        Arrays.fill(this.packetsReceived, (short) -1);
        short actualIndex = getShort(firstPacketFound[1], firstPacketFound[2]);
        this.data[actualIndex] = firstPacketFound;
        this.packetsReceived[actualIndex] = 0;
        this.waitingForFinalize = new ArrayList<>();
        verifyAndEmit();
    }

    public void addData(final byte[] packetData) {
        short actualIndex = getShort(packetData[1], packetData[2]);
        this.data[actualIndex] = packetData;
        this.packetsReceived[actualIndex] = 0;
        verifyAndEmit();
    }


    @Override
    public Data getData() {
        return null;
    }

    final public boolean isFinalized() {
        for (short s : this.packetsReceived)
            if (s != 0)
                return false;
        return true;
    }

    public ReceivePacket waitForFinalized() throws InterruptedException {
        if (!isFinalized())
            try {
                synchronized (this) {
                    this.waitingForFinalize.add(Thread.currentThread());
                }
                synchronized (Thread.currentThread()) {
                    Thread.currentThread().wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new InterruptedException(e.getMessage());
            } finally {
                synchronized (this) {
                    this.waitingForFinalize.remove(Thread.currentThread());
                }
            }
        return new ReceivePacket(this.data, this.packetNumber);
    }

    private void verifyAndEmit() {
        if (isFinalized())
            emit();
    }

    private synchronized void emit() {
        for (Thread t : this.waitingForFinalize)
            synchronized (t) {
                t.notify();
            }
    }

}
