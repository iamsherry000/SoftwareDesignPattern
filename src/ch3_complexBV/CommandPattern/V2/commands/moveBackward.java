package ch3_complexBV.CommandPattern.V2.commands;

import ch3_complexBV.CommandPattern.V2.receivers.Tank;

public class moveBackward implements Command {
    private Tank tank;
    private String commandName = "MoveTankBackward";

    public moveBackward(Tank tank) {
        this.tank = tank;
    }

    @Override
    public String getName() {
        return commandName;
    }

    @Override
    public void execute() {
        tank.setPosition(tank.getPosition() - 1);
        System.out.println("Tank moves backward, current position: " + tank.getPosition());
    }

    @Override
    public void undo() {
        tank.setPosition(tank.getPosition() + 1);
        System.out.println("Tank moves forward, current position: " + tank.getPosition());
    }
}
