package ch3_complexBV.CommandPattern.Objects;

public class Tank {
    private int position = 0;
    public Tank() {

    }

    public void moveForward() {
        position ++;
        System.out.println("Tank moves forward, current position: " + position);
    }
    public void moveBackward() {
        position --;
        System.out.println("Tank moves backward, current position: " + position);
    }

    public int getPosition() {
        return position;
    }
}
