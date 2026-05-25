package ch4_structural.Gym6_prescriberSystem.code.test;

import ch4_structural.Gym6_prescriberSystem.code.core.Gender;
import ch4_structural.Gym6_prescriberSystem.code.core.Patient;
import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.SleepApneaSyndrome;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class SleepApneaSyndromeTest {
    @Test
    public void testMatchHighBMISnore() {
        // 90kg / (170cm)² ≈ 31.14 > 26 + 打呼
        Patient patient = new Patient("C123456789", "Carl", Gender.MALE, 45, 170, 90);
        SleepApneaSyndrome handler = new SleepApneaSyndrome();
        Optional<Prescription> result = handler.handle(patient, List.of(Symptom.SNORE));

        assertTrue(result.isPresent());
        assertEquals("打呼抑制劑", result.get().getName());
        assertEquals("睡眠呼吸中止症（SleepApneaSyndrome）", result.get().getPotentialDisease());
    }

    @Test
    public void testBMIExactly26DoesNotMatch() {
        // BMI == 26 邊界：65kg / (1.5807m)² ≈ 26.0；嚴格 > 26 → 不中
        // 用 100kg / (196.116cm)² ≈ 26.0 較難湊整；改用明確 < 邊界判定：
        // 取 weight=26, height=100cm → BMI = 26 / 1² = 26.0，嚴格 >26 為 false
        Patient patient = new Patient("C123456789", "Carl", Gender.MALE, 45, 100, 26);
        assertEquals(26.0, patient.getBMI(), 0.0001);

        SleepApneaSyndrome handler = new SleepApneaSyndrome();
        assertTrue(handler.handle(patient, List.of(Symptom.SNORE)).isEmpty());
    }

    @Test
    public void testHighBMINoSnoreDoesNotMatch() {
        Patient patient = new Patient("C123456789", "Carl", Gender.MALE, 45, 170, 90);
        SleepApneaSyndrome handler = new SleepApneaSyndrome();
        assertTrue(handler.handle(patient, List.of(Symptom.HEADACHE)).isEmpty());
    }
}
