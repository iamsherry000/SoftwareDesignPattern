package ch4_structural.Gym6_prescriberSystem.code.test;

import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;
import ch4_structural.Gym6_prescriberSystem.code.prescriber.PrescriptionDemand;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrescriptionDemandTest {
    @Test
    public void testHoldsOnlyPatientIdAndSymptoms() {
        PrescriptionDemand demand = new PrescriptionDemand(
                "A123456789", List.of(Symptom.SNEEZE, Symptom.COUGH), result -> {});

        assertEquals("A123456789", demand.getPatientId());
        assertEquals(2, demand.getSymptoms().size());
        assertNotNull(demand.getOnComplete());
    }

    @Test
    public void testEmptySymptomsThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> new PrescriptionDemand("A123456789", List.of(), result -> {}));
    }
}
