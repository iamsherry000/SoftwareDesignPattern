package ch4_structural.Gym6c_log_framework.usage;

import ch4_structural.Gym6c_log_framework.core.Level;
import ch4_structural.Gym6c_log_framework.core.LogFramework;
import ch4_structural.Gym6c_log_framework.core.Logger;
import ch4_structural.Gym6c_log_framework.exporter.CompositeExporter;
import ch4_structural.Gym6c_log_framework.exporter.ConsoleExporter;
import ch4_structural.Gym6c_log_framework.exporter.FileExporter;
import ch4_structural.Gym6c_log_framework.layout.StandardLayout;

public class Main {

    public static void main(String[] args) {
        LogFramework framework = new LogFramework();

        // 根日誌器：完整定義 levelThreshold / exporter / layout
        Logger root = new Logger(
                "Root",
                null,
                Level.DEBUG,
                new ConsoleExporter(),
                new StandardLayout());

        // app.game：覆寫 levelThreshold + exporter（layout 繼承 root）
        Logger gameLogger = new Logger(
                "app.game",
                root,
                Level.INFO,
                new CompositeExporter(
                        new ConsoleExporter(),
                        new CompositeExporter(
                                new FileExporter("game.log"),
                                new FileExporter("game.backup.log"))),
                null);

        // app.game.ai：覆寫 levelThreshold（exporter / layout 繼承 app.game）
        Logger aiLogger = new Logger(
                "app.game.ai",
                gameLogger,
                Level.TRACE,
                null,
                null);

        framework.declareLoggers(root, gameLogger, aiLogger);

        Game game = new Game(framework);
        game.start();
    }
}
