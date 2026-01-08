package ch3_complexBV.CommandPattern.V2.commands;

import ch3_complexBV.CommandPattern.V2.receivers.Telecom;

public class Disconnect implements Command {
    private Telecom telecom;

    public Disconnect(Telecom telecom) {
        this.telecom = telecom;
    }

    @Override
    public void execute() {
        telecom.setConnected(false);
    }
}
