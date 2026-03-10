package ch3_complexBV.StatePattern;

public abstract class MapObj {
    private final char symbol;

    public MapObj(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}
