package ch3_complexBV.CommandPattern.commands;

import ch3_complexBV.CommandPattern.Objects.Telecom;

public class DisconnectCommand implements Command {
    private Telecom telecom;
    public DisconnectCommand(Telecom telecom) {
        this.telecom = telecom;
    }

    @Override
    public void execute() {
        telecom.disconnect();
    }
}
