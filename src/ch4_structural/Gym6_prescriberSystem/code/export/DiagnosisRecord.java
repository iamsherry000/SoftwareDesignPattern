package ch4_structural.Gym6_prescriberSystem.code.export;

import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;

import java.time.LocalDateTime;
import java.util.List;

public class DiagnosisRecord {
    private final String patientId;
    private final List<Symptom> symptoms;
    private final Prescription prescription;
    private final LocalDateTime caseTime;

    public DiagnosisRecord(String patientId, List<Symptom> symptoms,
                           Prescription prescription, LocalDateTime caseTime) {
        if (patientId == null || patientId.isEmpty()) {
            throw new IllegalArgumentException("patientId must not be empty.");
        }
        if (symptoms == null || symptoms.isEmpty()) {
            throw new IllegalArgumentException("symptoms must not be empty.");
        }
        if (prescription == null) {
            throw new IllegalArgumentException("prescription must not be null.");
        }
        if (caseTime == null) {
            throw new IllegalArgumentException("caseTime must not be null.");
        }
        this.patientId = patientId;
        this.symptoms = List.copyOf(symptoms);
        this.prescription = prescription;
        this.caseTime = caseTime;
    }

    public String getPatientId() {
        return patientId;
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
