package ch4_structural.Gym6_prescriberSystem.code.prescriber;

import ch4_structural.Gym6_prescriberSystem.code.core.Patient;
import ch4_structural.Gym6_prescriberSystem.code.core.PatientDatabase;
import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.DiseaseHandler;

import java.util.Optional;

public class Prescriber {
    private final DiseaseHandler chainHead;
    private final PatientDatabase patientDB;
    private final Queue queue;
    private final Sleeper sleeper;

    public Prescriber(DiseaseHandler chainHead, PatientDatabase patientDB, Queue queue, Sleeper sleeper) {
        if (chainHead == null) {
            throw new IllegalArgumentException("chainHead must not be null.");
        }
        if (patientDB == null) {
            throw new IllegalArgumentException("patientDB must not be null.");
        }
        if (queue == null) {
            throw new IllegalArgumentException("queue must not be null.");
        }
        if (sleeper == null) {
            throw new IllegalArgumentException("sleeper must not be null.");
        }
        this.chainHead = chainHead;
        this.patientDB = patientDB;
        this.queue = queue;
        this.sleeper = sleeper;
    }

    public void submit(PrescriptionDemand demand) {
        queue.enqueue(demand);
    }

    public void processNext() {
        if (queue.isEmpty()) {
            return;
        }
        var demand = queue.dequeue();
        sleeper.sleep();
        Optional<Patient> patient = patientDB.findById(demand.getPatientId());
        Optional<Prescription> result = patient
                .flatMap(p -> chainHead.handle(p, demand.getSymptoms()));
        demand.getOnComplete().accept(result);
    }

    public void processAll() {
        while (!queue.isEmpty()) {
            processNext();
        }
    }
}
