package ch3_complexBV.CommandPattern.V2.invoker;

//*
// 1. 儲存按鍵與命令的對應關係
// 2.在按鍵被按下時，快速找到並執行對應的命令
// 3. 支援靈活的配置（動態綁定）
// */

import java.util.HashMap;
import java.util.Map;
import ch3_complexBV.CommandPattern.V2.commands.Command;

public class Keyboard {

    private Map<Character, Command> keyMap = new HashMap<>();

    public Keyboard() {
        
    }
}
