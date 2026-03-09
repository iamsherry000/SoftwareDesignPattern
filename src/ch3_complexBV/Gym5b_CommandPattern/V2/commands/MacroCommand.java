package ch3_complexBV.CommandPattern.V2.commands;

import java.util.List;
import java.util.ArrayList;

public class MacroCommand implements Command {
    private List<Command> commands;

    public MacroCommand(List<Command> commands) {
        this.commands = new ArrayList<>(commands);
    }

    @Override
    public void execute() {
        for (Command command : commands) {
            command.execute();
        }
    }
    
    @Override
    public void undo() {
        for (int i = commands.size() - 1; i >= 0; i--) {
            commands.get(i).undo();
        }
    }
    
    @Override
    public String getName() {
        List<String> names = new ArrayList<>();
        for (Command command : commands) {
            names.add(command.getName());
        }
        return String.join(" & ", names);
    }

}
