package ch3_complexBV.CommandPattern.V2.commands;

import ch3_complexBV.CommandPattern.V2.invoker.MainController;

public class ResetMainControlKeyboard implements Command {
    private MainController controller;
    private String commandName = "ResetMainControlKeyboard";

    public ResetMainControlKeyboard(MainController controller) {
        this.controller = controller;
    }

    @Override
    public String getName() {
        return commandName;
    }

    @Override
    public void execute() {
        controller.resetKeyboard();
    }

    @Override
    public void undo() {
        // 無法恢復之前的快捷鍵設置, 空的實作
    }
}
