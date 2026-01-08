package ch3_complexBV.CommandPattern.V2.commands;

import ch3_complexBV.CommandPattern.V2.receivers.Tank;

public class moveForward implements Command {
    private Tank tank;
    
    public moveForward(Tank tank) {
        this.tank = tank;
    }

    @Override
    public void execute() {
        tank.setPosition(tank.getPosition() + 1);
    }
}
