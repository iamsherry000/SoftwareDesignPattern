package ch3_complexBV.CommandPattern.V2.commands;

import ch3_complexBV.CommandPattern.V2.receivers.Tank;

public class moveForward implements Command {
    private Tank tank;
    private String commandName = "MoveTankForward";
    
    public moveForward(Tank tank) {
        this.tank = tank;
    }

    @Override
    public String getName() {
        return commandName;
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
