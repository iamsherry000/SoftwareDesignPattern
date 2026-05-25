package ch4_structural.Gym6_prescriberSystem.code.export;

import java.util.function.Supplier;

public enum ExportFormat {
    JSON(SaveStrategy_JSON::new),
    CSV(SaveStrategy_CSV::new);

    private final Supplier<SaveStrategy> exporterFactory;

    ExportFormat(Supplier<SaveStrategy> exporterFactory) {
        this.exporterFactory = exporterFactory;
    }

    public SaveStrategy createExporter() {
        return exporterFactory.get();
    }

    public static ExportFormat fromFileName(String fileName) {
        String lower = fileName.toLowerCase();
        if (lower.endsWith(".json")) {
            return JSON;
        }
        if (lower.endsWith(".csv")) {
            return CSV;
        }
        throw new IllegalArgumentException("Unsupported export format: " + fileName);
    }
}
