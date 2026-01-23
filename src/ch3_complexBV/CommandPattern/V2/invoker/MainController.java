package ch3_complexBV.CommandPattern.V2.invoker;

import ch3_complexBV.CommandPattern.V2.commands.Command;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class MainController {
    
    private Map<String, Command> keyboard;

    public MainController() {
        this.keyboard = new HashMap<>();
    }

    public void setCommand(String key, Command command) {
        keyboard.put(key, command);
        // System.out.println(key + ": " + command.getName());
    }

    public void pressKey(char key) {
        Command command = keyboard.get(String.valueOf(key));
        if (command != null) {
            command.execute();
        } else {
            System.out.println("No command bound to key: " + key);
        }
    }

    public Command getCommand(char key) {
        return keyboard.get(String.valueOf(key));
    }

    public void resetKeyboard() {
        keyboard.clear();
    }

    public void printAllKeys() {
        if (keyboard.isEmpty()) {
            return;
        }
        
        keyboard.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> System.out.println(entry.getKey() + ": " + entry.getValue().getName()));
    }
}
