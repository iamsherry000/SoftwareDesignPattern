package ch3_complexBV.CommandPattern;

import java.util.HashMap;
import java.util.Map;

public class Keyboard {
    private Map<Character, Command> keyMap = new HashMap<>();
    public Keyboard() {

    }

    // 綁定按鍵
    public void bind(char key, Command command) {
        keyMap.put(key, command);
    }

    // 取得按鍵對應的指令
    public Command getCommand(char key) {
        return keyMap.get(key);
    }

    // 移除所有綁定
    public void reset() {
        keyMap.clear();
    }

}
