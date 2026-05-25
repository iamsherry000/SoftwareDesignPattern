package ch4_structural.Gym6_prescriberSystem.code.test;

import ch4_structural.Gym6_prescriberSystem.code.core.Gender;
import ch4_structural.Gym6_prescriberSystem.code.core.Patient;
import ch4_structural.Gym6_prescriberSystem.code.core.PatientDatabase;
import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.Attractive;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.Covid19;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.DiseaseHandler;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.SleepApneaSyndrome;
import ch4_structural.Gym6_prescriberSystem.code.prescriber.Prescriber;
import ch4_structural.Gym6_prescriberSystem.code.prescriber.PrescriptionDemand;
import ch4_structural.Gym6_prescriberSystem.code.prescriber.Queue;
import ch4_structural.Gym6_prescriberSystem.code.prescriber.Sleeper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PrescriberTest {
    private DiseaseHandler buildChain() {
        DiseaseHandler head = new Covid19();
        head.setNext(new Attractive()).setNext(new SleepApneaSyndrome());
        return head;
    }

    private PatientDatabase buildDB() {
        return new PatientDatabase(List.of(
                new Patient("A123456789", "Alice", Gender.FEMALE, 30, 165, 55),
                new Patient("B123456789", "Bella", Gender.FEMALE, 18, 160, 50)));
    }

    // 注入 no-op sleeper，test 不真等 3 秒
    private final Sleeper noWait = () -> {};

    @Test
    public void testProcessNextTriggersCallbackWithCorrectPrescription() {
        var captured = new ArrayList<Optional<Prescription>>();
        Prescriber prescriber = new Prescriber(buildChain(), buildDB(), new Queue(), noWait);

        prescriber.submit(new PrescriptionDemand("A123456789",
                List.of(Symptom.SNEEZE, Symptom.HEADACHE, Symptom.COUGH), captured::add));
        prescriber.processNext();

        assertEquals(1, captured.size());
        assertTrue(captured.get(0).isPresent());
        assertEquals("清冠一號", captured.get(0).get().getName());
    }

    @Test
    public void testFifoProcessingOrder() {
        var order = new ArrayList<String>();
        Prescriber prescriber = new Prescriber(buildChain(), buildDB(), new Queue(), noWait);

        prescriber.submit(new PrescriptionDemand("A123456789",
                List.of(Symptom.SNEEZE, Symptom.HEADACHE, Symptom.COUGH),
                r -> order.add("A")));
        prescriber.submit(new PrescriptionDemand("B123456789",
                List.of(Symptom.SNEEZE), r -> order.add("B")));

        prescriber.processAll();

        assertEquals(List.of("A", "B"), order);
    }

    @Test
    public void testNoMatchCallbackReceivesEmpty() {
        var captured = new ArrayList<Optional<Prescription>>();
        Prescriber prescriber = new Prescriber(buildChain(), buildDB(), new Queue(), noWait);

        prescriber.submit(new PrescriptionDemand("A123456789",
                List.of(Symptom.HEADACHE), captured::add));
        prescriber.processNext();

        assertEquals(1, captured.size());
        assertTrue(captured.get(0).isEmpty());
    }

    @Test
    public void testInjectedSleeperIsCalled() {
        var sleepCount = new int[]{0};
        Sleeper countingSleeper = () -> sleepCount[0]++;
        Prescriber prescriber = new Prescriber(buildChain(), buildDB(), new Queue(), countingSleeper);

        prescriber.submit(new PrescriptionDemand("A123456789",
                List.of(Symptom.SNEEZE), r -> {}));
        prescriber.processNext();

        assertEquals(1, sleepCount[0]);
    }
}
