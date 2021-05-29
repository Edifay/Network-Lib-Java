package apifornetwork.tcp;

import apifornetwork.data.packets.ReceivePacket;

import java.util.EventListener;

public interface RunnableParamPacket extends EventListener {
    void run(ReceivePacket socket);
}
