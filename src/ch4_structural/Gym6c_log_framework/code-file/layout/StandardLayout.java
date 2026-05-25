package ch4_structural.Gym6c_log_framework.layout;

import ch4_structural.Gym6c_log_framework.core.Message;

import java.time.format.DateTimeFormatter;

public class StandardLayout implements Layout {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public String format(Message message) {
        return FORMATTER.format(message.getTimestamp())
                + " |-" + message.getLevel().name()
                + " " + message.getLoggerName()
                + " - " + message.getContent();
    }
}
