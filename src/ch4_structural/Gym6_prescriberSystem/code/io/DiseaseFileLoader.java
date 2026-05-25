package ch4_structural.Gym6_prescriberSystem.code.io;

import ch4_structural.Gym6_prescriberSystem.code.diagnosis.DiseaseHandler;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.DiagnosisRuleRegistry;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DiseaseFileLoader {
    private final DiagnosisRuleRegistry registry;

    public DiseaseFileLoader(DiagnosisRuleRegistry registry) {
        if (registry == null) {
            throw new IllegalArgumentException("registry must not be null.");
        }
        this.registry = registry;
    }

    public List<String> parseDiseaseNames(String content) {
        List<String> names = new ArrayList<>();
        for (String token : content.split("[,\\r\\n]+")) {
            String trimmed = token.trim();
            if (!trimmed.isEmpty()) {
                names.add(trimmed);
            }
        }
        return names;
    }

    public Optional<DiseaseHandler> loadChain(Path diseaseFile) throws IOException {
        String content = Files.readString(diseaseFile, StandardCharsets.UTF_8);
        return registry.buildChain(parseDiseaseNames(content));
    }
}
