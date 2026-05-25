package ch4_structural.Gym6c_log_framework.json;

import ch4_structural.Gym6c_log_framework.core.LogFramework;
import ch4_structural.Gym6c_log_framework.usage.Game;

// 進階題 entry — 從 JSON 配置檔載入 Logger tree 後跑同樣的 Game 範例
public class Main_Json {

    public static void main(String[] args) {
        String configPath = args.length > 0
                ? args[0]
                : "src/ch4_structural/Gym6c_log_framework/game-config.json";

        LogFramework framework = new LogFramework();
        framework.loadFromJsonFile(new JsonFile(configPath));

        Game game = new Game(framework);
        game.start();
    }
}
