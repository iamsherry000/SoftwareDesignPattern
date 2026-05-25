package ch4_structural.Gym6_prescriberSystem.code.diagnosis;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public class DiagnosisRuleRegistry {
    private final Map<String, Supplier<DiseaseHandler>> factories = new LinkedHashMap<>();

    public void register(String diseaseName, Supplier<DiseaseHandler> factory) {
        if (diseaseName == null || diseaseName.isEmpty()) {
            throw new IllegalArgumentException("diseaseName must not be empty.");
        }
        if (factory == null) {
            throw new IllegalArgumentException("factory must not be null.");
        }
        factories.put(diseaseName, factory);
    }

    public boolean isRegistered(String diseaseName) {
        return factories.containsKey(diseaseName);
    }

    public Optional<DiseaseHandler> buildChain(List<String> diseaseNames) {
        if (diseaseNames == null) {
            throw new IllegalArgumentException("diseaseNames must not be null.");
        }
        DiseaseHandler head = null;
        DiseaseHandler tail = null;
        for (String name : diseaseNames) {
            DiseaseHandler handler = createHandler(name);
            if (head == null) {
                head = handler;
                tail = handler;
            } else {
                tail = tail.setNext(handler);
            }
        }
        return Optional.ofNullable(head);
    }

    private DiseaseHandler createHandler(String diseaseName) {
        Supplier<DiseaseHandler> factory = factories.get(diseaseName);
        if (factory == null) {
            throw new IllegalArgumentException("No rule registered for disease: " + diseaseName);
        }
        return factory.get();
    }
}
