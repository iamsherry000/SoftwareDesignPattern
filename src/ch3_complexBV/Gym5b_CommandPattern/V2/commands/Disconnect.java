package ch3_complexBV.CommandPattern.V2.commands;

import ch3_complexBV.CommandPattern.V2.receivers.Telecom;

public class Disconnect implements Command {
    private static final String COMMAND_NAME = "DisconnectTelecom";
    private Telecom telecom;

    public Disconnect(Telecom telecom) {
        this.telecom = telecom;
    }

    @Override
    public String getName() {
        return COMMAND_NAME;
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
