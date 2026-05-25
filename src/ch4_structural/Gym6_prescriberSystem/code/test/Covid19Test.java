package ch4_structural.Gym6_prescriberSystem.code.test;

import ch4_structural.Gym6_prescriberSystem.code.core.Gender;
import ch4_structural.Gym6_prescriberSystem.code.core.Patient;
import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.Covid19;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class Covid19Test {
    private final Patient patient =
            new Patient("A123456789", "Alice", Gender.FEMALE, 30, 165, 55);

    @Test
    public void testMatchAllThreeSymptoms() {
        Covid19 handler = new Covid19();
        Optional<Prescription> result = handler.handle(patient,
                List.of(Symptom.SNEEZE, Symptom.HEADACHE, Symptom.COUGH));

        assertTrue(result.isPresent());
        assertEquals("清冠一號", result.get().getName());
        assertEquals("新冠肺炎（COVID-19）", result.get().getPotentialDisease());
    }

    @Test
    public void testMissingOneSymptomReturnsEmpty() {
        // 缺咳嗽 → 不中，鏈尾無 next → empty
        Covid19 handler = new Covid19();
        Optional<Prescription> result = handler.handle(patient,
                List.of(Symptom.SNEEZE, Symptom.HEADACHE));

        assertTrue(result.isEmpty());
    }
}
