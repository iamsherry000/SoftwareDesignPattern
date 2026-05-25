package ch4_structural.Gym6c_log_framework.core;

import ch4_structural.Gym6c_log_framework.exporter.Exporter;
import ch4_structural.Gym6c_log_framework.layout.Layout;

public class Logger {

    private final String name;
    private final Logger parent;
    private final Level levelThreshold;
    private final Exporter exporter;
    private final Layout layout;

    public Logger(String name,
                  Logger parent,
                  Level levelThreshold,
                  Exporter exporter,
                  Layout layout) {
        this.name = name;
        this.parent = parent;
        this.levelThreshold = levelThreshold;
        this.exporter = exporter;
        this.layout = layout;
    }

    public String getName() {
        return name;
    }

    public Logger getParent() {
        return parent;
    }

    // Null-fallback lookup — 自己沒設就向 parent 查（OO 基本，不掛 GoF 名）
    public Level resolveLevelThreshold() {
        if (levelThreshold != null) return levelThreshold;
        if (parent != null) return parent.resolveLevelThreshold();
        throw new IllegalStateException("Root logger must define levelThreshold");
    }

    public Exporter resolveExporter() {
        if (exporter != null) return exporter;
        if (parent != null) return parent.resolveExporter();
        throw new IllegalStateException("Root logger must define exporter");
    }

    public Layout resolveLayout() {
        if (layout != null) return layout;
        if (parent != null) return parent.resolveLayout();
        throw new IllegalStateException("Root logger must define layout");
    }

    public void log(Level level, String content) {
        if (!level.isAtLeast(resolveLevelThreshold())) {
            return;
        }
        Message message = new Message(level, name, content);
        String formatted = resolveLayout().format(message);
        resolveExporter().export(formatted);
    }

    public void trace(String content) { log(Level.TRACE, content); }
    public void info(String content)  { log(Level.INFO, content); }
    public void debug(String content) { log(Level.DEBUG, content); }
    public void warn(String content)  { log(Level.WARN, content); }
    public void error(String content) { log(Level.ERROR, content); }
}
