package ch3_complexBV.StatePattern;

public enum Direction {
    UP('↑', -1, 0),
    RIGHT('→', 0, 1),
    DOWN('↓', 1, 0),
    LEFT('←', 0, -1);

    private final char symbol;
    private final int dRow;
    private final int dCol;

    Direction(char symbol, int dRow, int dCol) {
        this.symbol = symbol;
        this.dRow = dRow;
        this.dCol = dCol;
    }

    public char getSymbol() { return symbol; }
    public int getDRow()    { return dRow; }
    public int getDCol()    { return dCol; }

    /** Parse WASD or full name (case-insensitive). Returns null if unrecognised. */
    public static Direction parse(String input) {
        if (input == null) return null;
        switch (input.trim().toUpperCase()) {
            case "W": case "UP":    return UP;
            case "D": case "RIGHT": return RIGHT;
            case "S": case "DOWN":  return DOWN;
            case "A": case "LEFT":  return LEFT;
            default:                return null;
        }
    }
}
