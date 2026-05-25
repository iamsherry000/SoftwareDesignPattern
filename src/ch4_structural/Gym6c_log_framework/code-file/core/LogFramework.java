package ch4_structural.Gym6c_log_framework.core;

import ch4_structural.Gym6c_log_framework.exporter.CompositeExporter;
import ch4_structural.Gym6c_log_framework.exporter.ConsoleExporter;
import ch4_structural.Gym6c_log_framework.exporter.Exporter;
import ch4_structural.Gym6c_log_framework.exporter.FileExporter;
import ch4_structural.Gym6c_log_framework.json.JsonFile;
import ch4_structural.Gym6c_log_framework.json.MiniJsonParser;
import ch4_structural.Gym6c_log_framework.layout.Layout;
import ch4_structural.Gym6c_log_framework.layout.StandardLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LogFramework {

    private static final Set<String> RESERVED_KEYS =
            Set.of("levelThreshold", "exporter", "layout");

    private final Map<String, Logger> loggers = new HashMap<>();

    public void declareLoggers(Logger... loggers) {
        for (Logger logger : loggers) {
            this.loggers.put(logger.getName(), logger);
        }
    }

    public Logger getLogger(String name) {
        Logger logger = loggers.get(name);
        if (logger == null) {
            throw new IllegalArgumentException("No logger registered with name: " + name);
        }
        return logger;
    }

    // ── 進階題：從 JSON 配置檔載入 Logger tree ──────────────────────
    // F6 對應：配置外部化（in-code vs JSON 檔，產出等價 Logger tree）

    @SuppressWarnings("unchecked")
    public void loadFromJsonFile(JsonFile jsonFile) {
        Object parsed = MiniJsonParser.parse(jsonFile.getContent());
        if (!(parsed instanceof Map)) {
            throw new IllegalArgumentException("JSON root must be an object");
        }
        Map<String, Object> root = (Map<String, Object>) parsed;
        Object loggersNode = root.get("loggers");
        if (!(loggersNode instanceof Map)) {
            throw new IllegalArgumentException("JSON must have 'loggers' object at root");
        }
        parseLogger("Root", (Map<String, Object>) loggersNode, null);
    }

    @SuppressWarnings("unchecked")
    private void parseLogger(String name, Map<String, Object> spec, Logger parent) {
        Level levelThreshold = parseLevel(spec.get("levelThreshold"));
        Exporter exporter = parseExporter(spec.get("exporter"));
        Layout layout = parseLayout(spec.get("layout"));

        Logger logger = new Logger(name, parent, levelThreshold, exporter, layout);
        loggers.put(name, logger);

        // 任何不是 reserved 欄位的 key = 子 logger 名稱
        for (Map.Entry<String, Object> entry : spec.entrySet()) {
            if (RESERVED_KEYS.contains(entry.getKey())) continue;
            if (!(entry.getValue() instanceof Map)) {
                throw new IllegalArgumentException(
                        "Child logger spec must be an object: " + entry.getKey());
            }
            parseLogger(entry.getKey(), (Map<String, Object>) entry.getValue(), logger);
        }
    }

    private Level parseLevel(Object obj) {
        if (obj == null) return null;
        return Level.valueOf((String) obj);
    }

    @SuppressWarnings("unchecked")
    private Exporter parseExporter(Object obj) {
        if (obj == null) return null;
        if (!(obj instanceof Map)) {
            throw new IllegalArgumentException("Exporter spec must be an object");
        }
        Map<String, Object> spec = (Map<String, Object>) obj;
        String type = (String) spec.get("type");
        if (type == null) {
            throw new IllegalArgumentException("Exporter spec must have 'type'");
        }
        return switch (type) {
            case "console" -> new ConsoleExporter();
            case "file" -> new FileExporter((String) spec.get("fileName"));
            case "composite" -> {
                List<Object> childSpecs = (List<Object>) spec.get("children");
                if (childSpecs == null) {
                    throw new IllegalArgumentException("Composite exporter must have 'children'");
                }
                Exporter[] children = childSpecs.stream()
                        .map(this::parseExporter)
                        .toArray(Exporter[]::new);
                yield new CompositeExporter(children);
            }
            default -> throw new IllegalArgumentException("Unknown exporter type: " + type);
        };
    }

    private Layout parseLayout(Object obj) {
        if (obj == null) return null;
        String name = (String) obj;
        return switch (name) {
            case "standard" -> new StandardLayout();
            default -> throw new IllegalArgumentException("Unknown layout: " + name);
        };
    }
}
