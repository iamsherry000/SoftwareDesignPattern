package ch3_complexBV.StatePattern;

public abstract class Treasure extends MapObj {
    private static final char TREASURE_SYMBOL = 'x'; 
    public static float TREASURE_GENERATION_RATE; 

    protected Treasure() {
        super(TREASURE_SYMBOL);
    }

    public char getSymbol() {
        return TREASURE_SYMBOL;
    }

    // 以後新增新的 treasure 都不用修改其他的，只要實作不同 RATE
    public abstract float getGenerationRate();

    public abstract void onTouch(Role role);
}