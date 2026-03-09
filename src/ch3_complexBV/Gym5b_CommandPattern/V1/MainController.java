package ch3_complexBV.CommandPattern;

import java.util.Stack;
import ch3_complexBV.CommandPattern.commands.Command;

public class MainController {
    private Keyboard keyboard;
    private Stack<Command> undoStack;
    private Stack<Command> redoStack;

    public MainController(Keyboard keyboard) {
        this.keyboard = keyboard;
    }

    public void press(char key) {
        Command cmd = (Command) keyboard.getCommand(key);
        if (cmd != null) {
            executeCommand(cmd);
        } else {
            System.out.println("No command bound to key: " + key);
        }
    }

    // 執行命令（包含 Undo/Redo 處理）
    private void executeCommand(Command cmd) {
        cmd.execute();
        undoStack.push(cmd);
        redoStack.clear(); // 新動作後 redo stack 清空
    }

    private void setCommand(char Key, String cmdInput) {

    }

}
