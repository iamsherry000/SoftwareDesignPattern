package ch4_structural.Gym6c_log_framework.core;

public enum Level { // 固定五個值 according to the requirements

    TRACE,
    INFO,
    DEBUG,
    WARN,
    ERROR;

    // 比較邏輯封裝在 Level
    public boolean isAtLeast(Level threshold) { //越上方分級越小
        return this.ordinal() >= threshold.ordinal();
    }
}
