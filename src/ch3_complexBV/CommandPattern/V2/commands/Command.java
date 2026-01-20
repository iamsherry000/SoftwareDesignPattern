package ch3_complexBV.CommandPattern.V2.commands;

public interface Command {
    void execute();
    void undo(); // OOAD nned to redeisgn - 2026-01-20
    String getName();
}
