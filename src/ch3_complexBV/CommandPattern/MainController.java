package ch3_complexBV.CommandPattern;

import java.util.Stack;
import ch3_complexBV.CommandPattern.commands.Command;

public class MainController {
    private final Command[] commands = new Command[6]; // Tank, Telecom
    private final Stack<Command> s1 = new Stack<>();
    private final Stack<Command> s2 = new Stack<>();

    public MainController() {

    }

    public void setCommand(int button, Command command) {
        commands[button] = command;
    }

    public void press(int button) {
        if(button >= 97 && button <= 122) { // a - z = 97 - 122
            Command command = commands[button];
            command.execute();
            s1.push(command);
            s2.clear();
        }
        else {
            throw new IllegalArgumentException("Button " + button + " unsupported.");
        }
    }

    public void redo() {
        if(!s1.isEmpty()) {
            Command preCommand = s1.pop();
            preCommand.undo();
            s2.push(preCommand);
        }
    }

    public void undo() {
        if(!s2.isEmpty()) {
            Command nextCommand = s2.pop();
            nextCommand.execute();
            s1.push(nextCommand);
        }
    }
}
