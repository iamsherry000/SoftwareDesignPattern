package ch3_complexBV.CommandPattern.V2;

import ch3_complexBV.CommandPattern.V2.invoker.MainController;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Invoker
        MainController controller = new MainController();
        
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
                System.out.println("Input Macro: ");
                // todo: setMacroCommand
            } else if (yn.equals("n")) {
                System.out.println("Key: ");
                String command = scanner.nextLine();
                // controller.setCommand(command);
            }
        }
        else if (input.equals("2")) {
            System.out.println("Undo");
        }
        else if (input.equals("3")) {
            System.out.println("Redo");
        }
        else {
            System.out.println("Illigal");
        }
    }
}
