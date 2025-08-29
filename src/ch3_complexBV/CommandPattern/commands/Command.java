package ch3_complexBV.CommandPattern.commands;

public interface Command {
    void execute();
    void undo();
}
