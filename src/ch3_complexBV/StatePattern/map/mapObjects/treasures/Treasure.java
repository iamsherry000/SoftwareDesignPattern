package ch3_complexBV.StatePattern.map.mapObjects.treasures;
import ch3_complexBV.StatePattern.map.mapObjects.MapObj;

public abstract class Treasure extends MapObj {
    private static final char TREASURE_SYMBOL = 'x'; // 自己用 x 表示寶物
    public static float TREASURE_GENERATION_RATE; // 寶物生成機率 給各自的 Treasure 設定

    protected Treasure() {
        super(TREASURE_SYMBOL);
    }
}
