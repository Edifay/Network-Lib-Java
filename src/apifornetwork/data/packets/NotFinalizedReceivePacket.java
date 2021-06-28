package apifornetwork.data.packets;

import apifornetwork.data.Data;

import java.util.ArrayList;
import java.util.Arrays;

public class NotFinalizedReceivePacket extends ReceivePacket {

    private final ArrayList<Thread> waitingForFinalize;
    private short[] packetsReceived;

    public NotFinalizedReceivePacket(final byte[][] allData, final boolean isFastPacket) {
        super(allData, isFastPacket);
        this.packetNumber = getShort(this.data[0][5], this.data[0][6]);
        this.waitingForFinalize = new ArrayList<>();
        this.packetsReceived = new short[this.data.length];
        emit();
    }

    public NotFinalizedReceivePacket(final byte[] firstPacketFound, final boolean isFastPacket) {
        super(new byte[getShort(firstPacketFound[3], firstPacketFound[4])][firstPacketFound.length], getShort(firstPacketFound[5], firstPacketFound[6]), isFastPacket);
        this.packetsReceived = new short[this.data.length];
        Arrays.fill(this.packetsReceived, (short) -1);
        short actualIndex = getShort(firstPacketFound[1], firstPacketFound[2]);
        this.data[actualIndex] = firstPacketFound;
        this.ID = getShort(firstPacketFound[7], firstPacketFound[8]);
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

    public ReceivePacket waitForFinalized(long millis) throws InterruptedException {
        if (!isFinalized())
            try {
                synchronized (this) {
                    this.waitingForFinalize.add(Thread.currentThread());
                }
                synchronized (Thread.currentThread()) {
                    Thread.currentThread().wait(millis);
                    if (!isFinalized())
                        return null;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new InterruptedException(e.getMessage());
            } finally {
                synchronized (this) {
                    this.waitingForFinalize.remove(Thread.currentThread());
                }
            }
        return new ReceivePacket(this.data, this.packetNumber, this.isFastPacket);
    }

    public ReceivePacket waitForFinalized() throws InterruptedException {
        return waitForFinalized(0L);
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

    public int getNumberOfPackets() {
        return this.data.length;
    }

    public int getActualNumberOfPackets() {
        int number = 0;
        for (short s : this.packetsReceived)
            if (s == 0)
                number++;
        return number;
    }

    public short[] getActualReceived() {
        return this.packetsReceived;
    }


}
