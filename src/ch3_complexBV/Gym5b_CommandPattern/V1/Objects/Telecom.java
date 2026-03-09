package ch3_complexBV.CommandPattern.Objects;

public class Telecom {
    private boolean connected = false;
    public Telecom() {

    }

    public void connect() {
        connected = true;
        System.out.println("Telecom is connect");
    }
    public void disconnect() {
        connected = false;
        System.out.println("Telecom is disconnect");
    }

    public boolean isConnected() {
        return connected;
    }
}
