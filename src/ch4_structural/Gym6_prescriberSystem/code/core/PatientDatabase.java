package ch4_structural.Gym6_prescriberSystem.code.core;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class PatientDatabase {
    private final Map<String, Patient> patients = new LinkedHashMap<>();

    public PatientDatabase(List<Patient> patients) {
        if (patients == null) {
            throw new IllegalArgumentException("patients must not be null.");
        }
        for (var patient : patients) {
            this.patients.put(patient.getId(), patient);
        }
    }

    public Optional<Patient> findById(String id) {
        return Optional.ofNullable(patients.get(id));
    }
}
