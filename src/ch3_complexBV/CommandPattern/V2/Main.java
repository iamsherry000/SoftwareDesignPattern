package ch3_complexBV.CommandPattern.V2;

import ch3_complexBV.CommandPattern.V2.commands.Command;
import ch3_complexBV.CommandPattern.V2.commands.Connect;
import ch3_complexBV.CommandPattern.V2.commands.Disconnect;
import ch3_complexBV.CommandPattern.V2.commands.moveBackward;
import ch3_complexBV.CommandPattern.V2.commands.moveForward;
import ch3_complexBV.CommandPattern.V2.invoker.MainController;
import ch3_complexBV.CommandPattern.V2.receivers.Tank;
import ch3_complexBV.CommandPattern.V2.receivers.Telecom;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    public static void main(String[] args) {
        // Receivers
        Tank tank = new Tank();
        Telecom telecom = new Telecom();
        Stack<Command> undo = new Stack<>();
        Stack<Command> redo = new Stack<>();
        
        // Invoker
        MainController controller = new MainController();
        // better do this way. -2026-01-12 Needs to change it.
        Command[] commands = {new moveForward(tank), new moveBackward(tank), new Connect(telecom), new Disconnect(telecom)};
        boolean gameStatus = true;

        while(gameStatus) {
            // Interactions
            System.out.println("(1) setup key (2) Undo (3) Redo (letter) ur choice: ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if (input.equals("1")) {
                System.out.println("Set Macro (y/n): ");
                String yn = scanner.nextLine();
                if (!yn.equals("y") && !yn.equals("n")) {
                    System.out.println("Illigal");
                    return; 
                }
                if (yn.equals("y")) {
                    System.out.println("Input Macro key: "); 
                    String key = scanner.nextLine();
                    System.out.println("Which commands bind to key: " + key); // a
                    printMenuOptions();
                    String macroInput = scanner.nextLine(); // 0 2
                    String[] macroCommands = macroInput.trim().split("\\s+"); // 空白分割
                    //System.out.println("Macro: " + String.join(" ", macroCommands));
                    for(int i = 0 ; i < macroCommands.length; i++) {
                       int commandChoice = Integer.parseInt(macroCommands[i]);
                       controller.setCommand(key, commands[commandChoice]);
                    }
                    // System.out.println(key + ": " + String.join(" ", macroCommands));
                } else if (yn.equals("n")) {
                    System.out.println("Key: ");
                    String key = scanner.next();
                    System.out.println("Which command to key: " + key);
                    printMenuOptions();
                    int commandChoice = scanner.nextInt();
                    controller.setCommand(key, commands[commandChoice]);
                    undo.push(commands[commandChoice]);
                }
            }
            else if (input.equals("2")) {
                System.out.println("Undo");
                if (!undo.isEmpty()) {
                    Command command = undo.pop();
                    command.undo();
                    redo.push(command);
                }
            }
            else if (input.equals("3")) {
                System.out.println("Redo");
                if (!redo.isEmpty()) {
                    Command command = redo.pop();
                    command.execute();
                    undo.push(command);
                }
            }
            else if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
                controller.pressKey(input.toLowerCase().charAt(0));
            }
            else {
                System.out.println("Illigal");
            }
        }
    }

    public static void printMenuOptions() {
        System.out.println("(0) MoveTankForward");
        System.out.println("(1) MoveTankBackward");
        System.out.println("(2) ConnectTelecom");
        System.out.println("(3) DisconnectTelecom");
    }
}

