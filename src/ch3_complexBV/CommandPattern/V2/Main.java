package ch3_complexBV.CommandPattern.V2;

import ch3_complexBV.CommandPattern.V2.commands.Command;
import ch3_complexBV.CommandPattern.V2.commands.Connect;
import ch3_complexBV.CommandPattern.V2.commands.Disconnect;
<<<<<<< HEAD
import ch3_complexBV.CommandPattern.V2.commands.MoveBackward;
import ch3_complexBV.CommandPattern.V2.commands.MoveForward;
=======
import ch3_complexBV.CommandPattern.V2.commands.moveBackward;
import ch3_complexBV.CommandPattern.V2.commands.moveForward;
import ch3_complexBV.CommandPattern.V2.commands.ResetMainControlKeyboard;
>>>>>>> 63b74ea3c5037799bcae8093bee6fc0d9079539e
import ch3_complexBV.CommandPattern.V2.invoker.MainController;
import ch3_complexBV.CommandPattern.V2.receivers.Tank;
import ch3_complexBV.CommandPattern.V2.receivers.Telecom;
import ch3_complexBV.CommandPattern.V2.commands.MacroCommand;
import java.util.Scanner;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static Stack<Command> undo = new Stack<>();
    private static Stack<Command> redo = new Stack<>();
    
    public static void main(String[] args) {
        // Receivers
        Tank tank = new Tank();
        Telecom telecom = new Telecom();
        
        // Invoker
        MainController controller = new MainController();
<<<<<<< HEAD
        // better do this way. -2026-01-12 Needs to change it.
        Command[] commands = {new MoveForward(tank), new MoveBackward(tank), new Connect(telecom), new Disconnect(telecom)};
        boolean isRunning = true;

        while (isRunning) {
            // Interactions
            System.out.println("(1) setup key (2) Undo (3) Redo (letter) ur choice: ");
            Scanner scanner = new Scanner(System.in);
=======
        
        // Commands
        Command[] commands = {
            new moveForward(tank), 
            new moveBackward(tank), 
            new Connect(telecom), 
            new Disconnect(telecom),
            new ResetMainControlKeyboard(controller)
        };

        while(true) {
            controller.printAllKeys();
            
            System.out.print("(1) 快捷鍵設置 (2) Undo (3) Redo (字母) 按下按鍵: ");
>>>>>>> 63b74ea3c5037799bcae8093bee6fc0d9079539e
            String input = scanner.nextLine();
            
            if (input.equals("1")) {
                System.out.print("設置巨集指令 (y/n)：");
                String yn = scanner.nextLine();
                if (!yn.equals("y") && !yn.equals("n")) {
<<<<<<< HEAD
                    System.out.println("Illegal");
                    return; 
=======
                    System.out.println("Illigal");
                    continue; 
>>>>>>> 63b74ea3c5037799bcae8093bee6fc0d9079539e
                }
                if (yn.equals("y")) { // MacroCommand
                    System.out.print("Key: ");
                    String key = scanner.nextLine();
                    System.out.println("要將哪些指令設置成快捷鍵 " + key + " 的巨集（輸入多個數字，以空白隔開）: ");
                    printMenuOptions();
                    String macroInput = scanner.nextLine();
                    String[] macroCommands = macroInput.trim().split("\\s+");
                    List<Command> commandList = new ArrayList<>();
                    for(int i = 0; i < macroCommands.length; i++) {
                        int commandChoice = Integer.parseInt(macroCommands[i]);
                        commandList.add(commands[commandChoice]);
                    }
                    MacroCommand macroCommand = new MacroCommand(commandList);
                    controller.setCommand(key, macroCommand);
                } else if (yn.equals("n")) {
                    System.out.print("Key: ");
                    String key = scanner.nextLine();
                    System.out.println("要將哪一道指令設置到快捷鍵 " + key + " 上: ");
                    printMenuOptions();
                    int commandChoice = Integer.parseInt(scanner.nextLine());
                    controller.setCommand(key, commands[commandChoice]);
                }
            }
            else if (input.equals("2")) {
                if (!undo.isEmpty()) {
                    Command command = undo.pop();
                    command.undo();
                    redo.push(command);
                }
            }
            else if (input.equals("3")) {
                if (!redo.isEmpty()) {
                    Command command = redo.pop();
                    command.execute();
                    undo.push(command);
                }
            }
            else if (input.length() == 1 && Character.isLetter(input.charAt(0))) {
                Command command = controller.getCommand(input.toLowerCase().charAt(0));
                if (command != null) {
                    command.execute();
                    undo.push(command);
                    redo.clear(); // 執行新指令時清除 redo stack
                }
            }
            else {
                System.out.println("Illegal");
            }
        }
    }

    public static void printMenuOptions() {
        System.out.println("(0) MoveTankForward");
        System.out.println("(1) MoveTankBackward");
        System.out.println("(2) ConnectTelecom");
        System.out.println("(3) DisconnectTelecom");
        System.out.println("(4) ResetMainControlKeyboard");
    }
}

