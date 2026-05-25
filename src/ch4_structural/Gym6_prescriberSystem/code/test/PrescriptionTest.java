package ch4_structural.Gym6_prescriberSystem.code.test;

import ch4_structural.Gym6_prescriberSystem.code.core.Medicine;
import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrescriptionTest {
    @Test
    public void testValidPrescription() {
        Prescription prescription = new Prescription(
                "清冠一號",
                "新冠肺炎（COVID-19）",
                List.of(new Medicine("清冠一號")),
                "將相關藥材裝入茶包裡，使用 500 mL 溫、熱水沖泡悶煮 1~3 分鐘後即可飲用。");
        assertEquals("清冠一號", prescription.getName());
        assertEquals(1, prescription.getMedicines().size());
    }

    @Test
    public void testNameTooShort() {
        // 名字少於 4 個字元
        assertThrows(IllegalArgumentException.class, () -> new Prescription(
                "abc", "COVID-19", List.of(new Medicine("med")), "usage"));
    }

    @Test
    public void testPotentialDiseaseTooShort() {
        // 潛在疾病少於 3 個字元
        assertThrows(IllegalArgumentException.class, () -> new Prescription(
                "name", "ab", List.of(new Medicine("med")), "usage"));
    }

    @Test
    public void testEmptyMedicinesThrows() {
        // medicines 至少要 1 個藥
        assertThrows(IllegalArgumentException.class, () -> new Prescription(
                "name", "disease", List.of(), "usage"));
    }

    @Test
    public void testUsageMayBeEmpty() {
        // usage 長度 0 合法
        Prescription prescription = new Prescription(
                "name", "disease", List.of(new Medicine("med")), "");
        assertEquals("", prescription.getUsage());
    }
}
