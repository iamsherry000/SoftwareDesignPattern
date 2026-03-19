package ch3_complexBV.StatePattern.v1;

public abstract class MapObj {
    private final char symbol;

    public MapObj(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }
}
