package ch4_structural.Gym6c_log_framework.json;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

// 外部 JSON 配置檔的領域實體（OOA F6 對應）
public class JsonFile {

    private final String path;
    private final String content;

    public JsonFile(String path) {
        this.path = path;
        try {
            this.content = Files.readString(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + path, e);
        }
    }

    public String getPath() {
        return path;
    }

    public String getContent() {
        return content;
    }
}
