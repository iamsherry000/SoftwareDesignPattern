package ch4_structural.Gym6_prescriberSystem.code.core;

import java.time.LocalDateTime;
import java.util.List;

public class Case {
    private final List<Symptom> symptoms;
    private final Prescription prescription;
    private final LocalDateTime caseTime;

    public Case(List<Symptom> symptoms, Prescription prescription, LocalDateTime caseTime) {
        if (symptoms == null || symptoms.isEmpty()) {
            throw new IllegalArgumentException("Case must contain at least one symptom.");
        }
        if (prescription == null) {
            throw new IllegalArgumentException("Case must have a prescription.");
        }
        if (caseTime == null) {
            throw new IllegalArgumentException("Case must have a caseTime.");
        }
        this.symptoms = List.copyOf(symptoms);
        this.prescription = prescription;
        this.caseTime = caseTime;
    }

    public List<Symptom> getSymptoms() {
        return symptoms;
    }

    public Prescription getPrescription() {
        return prescription;
    }

    public LocalDateTime getCaseTime() {
        return caseTime;
    }
}
