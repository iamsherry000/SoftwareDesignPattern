package ch3_complexBV.CommandPattern.V2.commands;

import ch3_complexBV.CommandPattern.V2.receivers.Telecom;

public class Connect implements Command {
    private static final String COMMAND_NAME = "ConnectTelecom";
    private Telecom telecom;

    public Connect(Telecom telecom) {
        this.telecom = telecom;
    }

    @Override
    public String getName() {
        return COMMAND_NAME;
    }

    @Override
    public void execute() {
        telecom.setConnected(true);
        System.out.println("Telecom connected");
    }

    @Override
    public void undo() {
        telecom.setConnected(false);
        System.out.println("Telecom disconnected");
    }
}
