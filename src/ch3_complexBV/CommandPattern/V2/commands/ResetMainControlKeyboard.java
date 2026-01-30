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
        // Reset 操作通常不需要 undo，因為無法恢復之前的快捷鍵設置
        // 但為了符合介面，我們提供一個空的實作
    }
}
