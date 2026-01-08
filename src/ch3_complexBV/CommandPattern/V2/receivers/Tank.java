package ch3_complexBV.CommandPattern.V2.receivers;

public class Tank {
    private int position = 0;

    public Tank() {}

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
