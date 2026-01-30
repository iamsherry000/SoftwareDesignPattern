package ch3_complexBV.CommandPattern.V2.commands;

import ch3_complexBV.CommandPattern.V2.receivers.Tank;

public class MoveForward implements Command {
    private static final String COMMAND_NAME = "MoveTankForward";
    private Tank tank;

    public MoveForward(Tank tank) {
        this.tank = tank;
    }

    @Override
    public String getName() {
        return COMMAND_NAME;
    }

    @Override
    public void execute() {
        tank.setPosition(tank.getPosition() + 1);
        System.out.println("The tank has moved forward.");
    }

    @Override
    public void undo() {
        tank.setPosition(tank.getPosition() - 1);
        System.out.println("The tank has moved backward.");
    }
}
