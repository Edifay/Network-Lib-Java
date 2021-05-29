package apifornetwork.tcp;


import apifornetwork.data.packets.NotFinalizedReceivePacket;
import apifornetwork.data.packets.ReceivePacket;
import apifornetwork.tcp.server.ServerTCPEvent;

public final class NewPacketEvent extends ServerTCPEvent {

    protected ReceivePacket packet;
    protected RunnableParamPacket event;

    public NewPacketEvent(ReceivePacket packet, RunnableParamPacket event) {
        this.packet = packet;
        this.event = event;
    }

    /*
     * a runnable with a parameter
     */
    @Override
    public void run() {
        this.event.run(this.packet);
    }

}