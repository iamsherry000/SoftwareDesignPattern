package ch4_structural.Gym6_prescriberSystem.code.prescriber;

import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class PrescriptionDemand {
    private final String patientId;
    private final List<Symptom> symptoms;
    private final Consumer<Optional<Prescription>> onComplete;

    public PrescriptionDemand(String patientId, List<Symptom> symptoms,
                              Consumer<Optional<Prescription>> onComplete) {
        if (patientId == null || patientId.isEmpty()) {
            throw new IllegalArgumentException("patientId must not be empty.");
        }
        if (symptoms == null || symptoms.isEmpty()) {
            throw new IllegalArgumentException("symptoms must not be empty.");
        }
        if (onComplete == null) {
            throw new IllegalArgumentException("onComplete callback must not be null.");
        }
        this.patientId = patientId;
        this.symptoms = List.copyOf(symptoms);
        this.onComplete = onComplete;
    }

    public String getPatientId() {
        return patientId;
    }

    public List<Symptom> getSymptoms() {
        return symptoms;
    }

    public Consumer<Optional<Prescription>> getOnComplete() {
        return onComplete;
    }
}
