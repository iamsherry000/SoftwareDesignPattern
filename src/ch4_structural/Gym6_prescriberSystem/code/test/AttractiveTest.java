package ch4_structural.Gym6_prescriberSystem.code.test;

import ch4_structural.Gym6_prescriberSystem.code.core.Gender;
import ch4_structural.Gym6_prescriberSystem.code.core.Patient;
import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.Attractive;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class AttractiveTest {
    @Test
    public void testMatch18FemaleSneeze() {
        Patient patient = new Patient("B123456789", "Bella", Gender.FEMALE, 18, 160, 50);
        Attractive handler = new Attractive();
        Optional<Prescription> result = handler.handle(patient, List.of(Symptom.SNEEZE));

        assertTrue(result.isPresent());
        assertEquals("青春抑制劑", result.get().getName());
        assertEquals("有人想你了（Attractive）", result.get().getPotentialDisease());
    }

    @Test
    public void testNotMatchAge17() {
        // 17 歲 → 不中
        Patient patient = new Patient("B123456789", "Bella", Gender.FEMALE, 17, 160, 50);
        Attractive handler = new Attractive();
        assertTrue(handler.handle(patient, List.of(Symptom.SNEEZE)).isEmpty());
    }

    @Test
    public void testNotMatchMale() {
        // 18 歲男 → 不中
        Patient patient = new Patient("B123456789", "Ben", Gender.MALE, 18, 160, 50);
        Attractive handler = new Attractive();
        assertTrue(handler.handle(patient, List.of(Symptom.SNEEZE)).isEmpty());
    }
}
