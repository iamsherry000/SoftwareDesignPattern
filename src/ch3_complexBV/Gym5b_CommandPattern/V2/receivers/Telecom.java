package ch3_complexBV.CommandPattern.V2.receivers;

public class Telecom {
    private boolean connected = false;

    public Telecom() {}

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public boolean isConnected() {
        return connected;
    }
}
