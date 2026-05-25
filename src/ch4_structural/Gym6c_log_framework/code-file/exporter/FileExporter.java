package ch4_structural.Gym6c_log_framework.exporter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class FileExporter implements Exporter {

    private final String fileName;

    public FileExporter(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public void export(String formatted) {
        try {
            Files.writeString(
                    Path.of(fileName),
                    formatted + System.lineSeparator(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Failed to append to " + fileName, e);
        }
    }
}
