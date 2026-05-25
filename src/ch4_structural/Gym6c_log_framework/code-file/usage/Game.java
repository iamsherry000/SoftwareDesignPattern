package ch4_structural.Gym6c_log_framework.usage;

import ch4_structural.Gym6c_log_framework.core.LogFramework;
import ch4_structural.Gym6c_log_framework.core.Logger;

public class Game {

    private final Logger log;
    private final AI[] players;

    public Game(LogFramework framework) {
        this.log = framework.getLogger("app.game");
        this.players = new AI[] {
                new AI("AI 1", framework),
                new AI("AI 2", framework),
                new AI("AI 3", framework),
                new AI("AI 4", framework)
        };
    }

    public void start() {
        log.info("The game begins.");

        for (AI ai : players) {
            log.trace("The player *" + ai.getName() + "* begins his turn.");
            ai.makeDecision();
            log.trace("The player *" + ai.getName() + "* finishes his turn.");
        }

        log.debug("Game ends.");
    }
}
