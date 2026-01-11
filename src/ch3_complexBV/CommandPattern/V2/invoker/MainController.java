package ch3_complexBV.CommandPattern.V2.invoker;

import java.util.HashMap;
import java.util.Map;
import ch3_complexBV.CommandPattern.V2.commands.Command;

public class MainController {
    
    private Map<Integer, Command> keyboard;

    public MainController() {
        this.keyboard = new HashMap<>();
    }

    public void setCommand(char key, int commandChoice) {
        keyboard.put(key, );
    }
}
