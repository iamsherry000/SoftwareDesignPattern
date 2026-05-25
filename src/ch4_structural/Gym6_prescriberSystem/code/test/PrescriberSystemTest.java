package ch4_structural.Gym6_prescriberSystem.code.test;

import ch4_structural.Gym6_prescriberSystem.code.core.Case;
import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.Attractive;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.Covid19;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.SleepApneaSyndrome;
import ch4_structural.Gym6_prescriberSystem.code.prescriber.Sleeper;
import ch4_structural.Gym6_prescriberSystem.code.PrescriberSystem;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PrescriberSystemTest {
    private final Sleeper noWait = () -> {};

    private static final String DISEASES = "COVID-19,Attractive,SleepApneaSyndrome";

    private PrescriberSystem systemWith(String patientsJson) {
        PrescriberSystem system = new PrescriberSystem(noWait);
        system.registerRule("COVID-19", Covid19::new);
        system.registerRule("Attractive", Attractive::new);
        system.registerRule("SleepApneaSyndrome", SleepApneaSyndrome::new);
        system.provideData(patientsJson, DISEASES);
        return system;
    }

    @Test
    public void testRequestDiagnosisThenSaveCase() {
        PrescriberSystem system = systemWith(
                "[{\"id\":\"A123456789\",\"name\":\"Alice\",\"gender\":\"female\",\"age\":30,\"height\":165,\"weight\":55}]");

        var symptoms = List.of(Symptom.SNEEZE, Symptom.HEADACHE, Symptom.COUGH);
        var captured = new ArrayList<Optional<Prescription>>();

        system.requestDiagnosis("A123456789", symptoms, captured::add);
        system.processAll();

        assertEquals(1, captured.size());
        assertTrue(captured.get(0).isPresent());

        Optional<Case> saved = system.saveCase("A123456789", symptoms, captured.get(0).get(), LocalDateTime.now());
        assertTrue(saved.isPresent());
        assertEquals("清冠一號", saved.get().getPrescription().getName());
    }

    @Test
    public void testNoMatchDoesNotBuildCase() {
        PrescriberSystem system = systemWith(
                "[{\"id\":\"D123456789\",\"name\":\"Dan\",\"gender\":\"male\",\"age\":25,\"height\":175,\"weight\":65}]");

        var symptoms = List.of(Symptom.HEADACHE);
        var captured = new ArrayList<Optional<Prescription>>();

        system.requestDiagnosis("D123456789", symptoms, captured::add);
        system.processAll();

        assertTrue(captured.get(0).isEmpty());

        Optional<Case> saved = system.saveCase("D123456789", symptoms, captured.get(0).orElse(null), LocalDateTime.now());
        assertTrue(saved.isEmpty());
    }
}
