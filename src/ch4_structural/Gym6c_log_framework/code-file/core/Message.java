package ch4_structural.Gym6c_log_framework.core;

import java.time.LocalDateTime;

public class Message {

    private final Level level;
    private final String loggerName;
    private final String content;
    private final LocalDateTime timestamp;

    public Message(Level level, String loggerName, String content) {
        this(level, loggerName, content, LocalDateTime.now());
    }

    public Message(Level level, String loggerName, String content, LocalDateTime timestamp) {
        this.level = level;
        this.loggerName = loggerName;
        this.content = content;
        this.timestamp = timestamp;
    }

    public Level getLevel() {
        return level;
    }

    public String getLoggerName() {
        return loggerName;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
