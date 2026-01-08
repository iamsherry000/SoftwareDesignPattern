package ch3_complexBV.CommandPattern.V2.commands;

import ch3_complexBV.CommandPattern.V2.receivers.Telecom;

public class Connect implements Command {
    private Telecom telecom;

    public Connect(Telecom telecom) {
        this.telecom = telecom;
    }

    @Override
    public void execute() {
        telecom.setConnected(true);
    }
}
