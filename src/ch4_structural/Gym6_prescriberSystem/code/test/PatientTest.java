package ch4_structural.Gym6_prescriberSystem.code.test;

import ch4_structural.Gym6_prescriberSystem.code.core.Gender;
import ch4_structural.Gym6_prescriberSystem.code.core.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PatientTest {
    @Test
    public void testValidPatient() {
        Patient patient = new Patient("A123456789", "Alice", Gender.FEMALE, 30, 165, 55);
        assertEquals("A123456789", patient.getId());
        assertEquals("Alice", patient.getName());
        assertEquals(Gender.FEMALE, patient.getGender());
        assertEquals(30, patient.getAge());
    }

    @Test
    public void testInvalidIdNoUppercaseStart() {
        // id 必須大寫字母開頭 + 9 位數字
        assertThrows(IllegalArgumentException.class,
                () -> new Patient("a123456789", "Alice", Gender.FEMALE, 30, 165, 55));
    }

    @Test
    public void testInvalidIdWrongDigitCount() {
        // 只有 8 位數字
        assertThrows(IllegalArgumentException.class,
                () -> new Patient("A12345678", "Alice", Gender.FEMALE, 30, 165, 55));
    }

    @Test
    public void testInvalidAgeTooLow() {
        assertThrows(IllegalArgumentException.class,
                () -> new Patient("A123456789", "Alice", Gender.FEMALE, 0, 165, 55));
    }

    @Test
    public void testInvalidAgeTooHigh() {
        assertThrows(IllegalArgumentException.class,
                () -> new Patient("A123456789", "Alice", Gender.FEMALE, 181, 165, 55));
    }

    @Test
    public void testGetBMIWithCmToMeterConversion() {
        // 100kg / (200cm = 2m)² = 100 / 4 = 25.0；驗 cm→m 換算
        Patient patient = new Patient("A123456789", "Big", Gender.MALE, 30, 200, 100);
        assertEquals(25.0, patient.getBMI(), 0.0001);
    }

    @Test
    public void testGetBMIAboveThreshold() {
        // 90kg / (170cm)² ≈ 31.14 > 26
        Patient patient = new Patient("C123456789", "Carl", Gender.MALE, 45, 170, 90);
        assertTrue(patient.getBMI() > 26);
    }
}
