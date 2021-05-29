package apifornetwork.tcp.server;


public final class NewClientEvent extends ServerTCPEvent {

    protected SocketClient socket;
    protected RunnableParamSocket event;

    public NewClientEvent(SocketClient socket, RunnableParamSocket event) {
        this.socket = socket;
        this.event = event;
    }

    /*
     * a runnable with a parameter
     */
    @Override
    public void run() {
        this.event.run(this.socket);
    }

}