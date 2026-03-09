package ch3_complexBV.CommandPattern.V2.commands;

import ch3_complexBV.CommandPattern.V2.receivers.Tank;

public class MoveBackward implements Command {
    private static final String COMMAND_NAME = "MoveTankBackward";
    private Tank tank;

    public MoveBackward(Tank tank) {
        this.tank = tank;
    }

    @Override
    public String getName() {
        return COMMAND_NAME;
    }

    @Override
    public void execute() {
        tank.setPosition(tank.getPosition() - 1);
        System.out.println("The tank has moved backward.");
    }

    @Override
    public void undo() {
        tank.setPosition(tank.getPosition() + 1);
        System.out.println("The tank has moved forward.");
    }
}
