package apifornetwork;

public class Server {

    private final int portTCP;
    private final int portUDP;

    public Server(final int portTCP, final int portUDP){
        this.portTCP = portTCP;
        this.portUDP = portUDP;
    }


    public int getPortTCP(){
        return this.portTCP;
    }

    public int getPortUDP(){
        return this.portUDP;
    }

}