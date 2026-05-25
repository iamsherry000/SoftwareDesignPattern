package ch4_structural.Gym6_prescriberSystem.code.diagnosis;

import ch4_structural.Gym6_prescriberSystem.code.core.Patient;
import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;

import java.util.List;
import java.util.Optional;

public abstract class DiseaseHandler {
    private DiseaseHandler next;

    public DiseaseHandler setNext(DiseaseHandler next) {
        this.next = next;
        return next;
    }

    public final Optional<Prescription> handle(Patient patient, List<Symptom> symptoms) {
        if (matches(patient, symptoms)) {
            return Optional.of(getPrescription());
        }
        if (next != null) {
            return next.handle(patient, symptoms);
        }
        return Optional.empty();
    }

    protected abstract boolean matches(Patient patient, List<Symptom> symptoms);

    protected abstract Prescription getPrescription();
}
