package ch3_complexBV.CommandPattern.V2.commands;

import ch3_complexBV.CommandPattern.V2.receivers.Telecom;

public class Disconnect implements Command {
    private Telecom telecom;
    private String commandName = "DisconnectTelecom";

    public Disconnect(Telecom telecom) {
        this.telecom = telecom;
    }

    @Override
    public String getName() {
        return commandName;
    }

    @Override
    public void execute() {
        telecom.setConnected(false);
        System.out.println("The telecom has been turned off.");
    }

    @Override
    public void undo() {
        telecom.setConnected(true);
        System.out.println("The telecom has been turned on.");
    }
}
