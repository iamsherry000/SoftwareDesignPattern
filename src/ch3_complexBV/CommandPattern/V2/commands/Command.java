package ch3_complexBV.CommandPattern.V2.commands;

public interface Command {
    void execute();
    void undo(); // OOAD need to redesign - 2026-01-20
    String getName();
}
