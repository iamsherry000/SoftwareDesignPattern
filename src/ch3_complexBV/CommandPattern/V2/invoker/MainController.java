package ch3_complexBV.CommandPattern.V2.invoker;

import ch3_complexBV.CommandPattern.V2.commands.Command;
import java.util.HashMap;
import java.util.Map;

public class MainController {
    
    private Map<Integer, Command> keyboard;

    public MainController() {
        this.keyboard = new HashMap<>();
    }

    public void setCommand(String key, int commandChoice) {
        
    }
}
