package ch3_complexBV.CommandPattern.V2.invoker;

import ch3_complexBV.CommandPattern.V2.commands.Command;
import java.util.HashMap;
import java.util.Map;

public class MainController {
    
    private Map<String, Command> keyboard;

    public MainController() {
        this.keyboard = new HashMap<>();
    }

    public void setCommand(String key, Command command) {
        keyboard.put(key, command);
        System.out.println(key + " : " + command.getName());
    }

    public void pressKey(char key) {
        Command command = keyboard.get(key);
        if (command != null) {
            command.execute(); // Todo : This is not working correctly. 
        } else {
            System.out.println("No command bound to key: " + key);
        }
    }
}
