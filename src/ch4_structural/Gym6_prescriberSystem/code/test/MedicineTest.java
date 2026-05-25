package ch4_structural.Gym6_prescriberSystem.code.test;

import ch4_structural.Gym6_prescriberSystem.code.core.Medicine;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MedicineTest {
    @Test
    public void testValidMedicine() {
        Medicine medicine = new Medicine("清冠一號");
        assertEquals("清冠一號", medicine.getName());
    }

    @Test
    public void testEmptyNameThrows() {
        // 空字串不合法
        assertThrows(IllegalArgumentException.class, () -> new Medicine(""));
        assertThrows(IllegalArgumentException.class, () -> new Medicine(null));
    }

    @Test
    public void testTwoCharNameAllowed() {
        // requirement 瑕疵繞過（ood-design.md §10）：規則二的藥「臭味」只有 2 字，
        // Medicine 下限放寬為「非空 ≤30」，故 2 字名合法（不 throw）。
        Medicine medicine = new Medicine("臭味");
        assertEquals("臭味", medicine.getName());
    }

    @Test
    public void testNameTooLong() {
        // 超過 30 個字元
        String tooLong = "a".repeat(31);
        assertThrows(IllegalArgumentException.class, () -> new Medicine(tooLong));
    }
}
