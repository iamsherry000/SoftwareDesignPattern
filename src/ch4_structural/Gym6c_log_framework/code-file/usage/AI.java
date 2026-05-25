package ch4_structural.Gym6c_log_framework.usage;

import ch4_structural.Gym6c_log_framework.core.LogFramework;
import ch4_structural.Gym6c_log_framework.core.Logger;

public class AI {

    private final Logger log;
    private final String name;

    public AI(String name, LogFramework framework) {
        this.name = name;
        this.log = framework.getLogger("app.game.ai");
    }

    public String getName() {
        return name;
    }

    public void makeDecision() {
        log.trace(name + " starts making decisions...");
        log.warn(name + " decides to give up.");
        log.error("Something goes wrong when AI gives up.");
        log.trace(name + " completes its decision.");
    }
}
