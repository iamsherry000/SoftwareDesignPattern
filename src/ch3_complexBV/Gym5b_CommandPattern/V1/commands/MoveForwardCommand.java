package ch3_complexBV.CommandPattern.commands;

import ch3_complexBV.CommandPattern.Objects.Tank;

public class MoveForwardCommand implements Command {
    private Tank tank;
    public MoveForwardCommand() {
        this.tank = tank;
    }

    @Override
    public void execute() {
        tank.moveForward();
    }
}
