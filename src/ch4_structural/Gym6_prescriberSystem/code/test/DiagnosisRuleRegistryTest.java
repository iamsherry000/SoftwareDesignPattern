package ch4_structural.Gym6_prescriberSystem.code.test;

import ch4_structural.Gym6_prescriberSystem.code.core.Gender;
import ch4_structural.Gym6_prescriberSystem.code.core.Patient;
import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.Attractive;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.Covid19;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.DiseaseHandler;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.DiagnosisRuleRegistry;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.SleepApneaSyndrome;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DiagnosisRuleRegistryTest {

    private DiagnosisRuleRegistry registryWithThreeRules() {
        DiagnosisRuleRegistry registry = new DiagnosisRuleRegistry();
        registry.register("COVID-19", Covid19::new);
        registry.register("Attractive", Attractive::new);
        registry.register("SleepApneaSyndrome", SleepApneaSyndrome::new);
        return registry;
    }

    @Test
    public void testRegisterMarksDiseaseAsKnown() {
        DiagnosisRuleRegistry registry = registryWithThreeRules();
        assertTrue(registry.isRegistered("COVID-19"));
        assertFalse(registry.isRegistered("Unknown"));
    }

    @Test
    public void testBuildChainPreservesListOrder() {
        // 鏈序 = 清單序：Covid → Attractive。一名 18 歲女、三症狀齊全 → 命中鏈首 Covid。
        DiseaseHandler head = registryWithThreeRules()
                .buildChain(List.of("COVID-19", "Attractive"))
                .orElseThrow();

        Patient bella = new Patient("B123456789", "Bella", Gender.FEMALE, 18, 160, 50);
        Optional<Prescription> result = head.handle(bella,
                List.of(Symptom.SNEEZE, Symptom.HEADACHE, Symptom.COUGH));

        assertTrue(result.isPresent());
        assertEquals("清冠一號", result.get().getName());
    }

    @Test
    public void testBuildChainHonoursReorderedList() {
        // 反序清單：Attractive 在 Covid 前 → 18 歲女 + 噴嚏命中 Attractive 先。
        DiseaseHandler head = registryWithThreeRules()
                .buildChain(List.of("Attractive", "COVID-19", "SleepApneaSyndrome"))
                .orElseThrow();

        Patient bella = new Patient("B123456789", "Bella", Gender.FEMALE, 18, 160, 50);
        Optional<Prescription> result = head.handle(bella,
                List.of(Symptom.SNEEZE, Symptom.HEADACHE, Symptom.COUGH));

        assertTrue(result.isPresent());
        assertEquals("青春抑制劑", result.get().getName());
    }

    @Test
    public void testBuildChainWiresAllDiseases() {
        // 三條全組進鏈：打呼 + BMI>26 → 走到鏈尾 SleepApnea 才命中（驗證整條鏈長度足夠）。
        DiseaseHandler head = registryWithThreeRules()
                .buildChain(List.of("COVID-19", "Attractive", "SleepApneaSyndrome"))
                .orElseThrow();

        Patient carl = new Patient("C123456789", "Carl", Gender.MALE, 45, 170, 90); // BMI ≈ 31.1
        Optional<Prescription> result = head.handle(carl, List.of(Symptom.SNORE));

        assertTrue(result.isPresent());
        assertEquals("打呼抑制劑", result.get().getName());
    }

    @Test
    public void testBuildChainEmptyListReturnsEmpty() {
        assertTrue(registryWithThreeRules().buildChain(List.of()).isEmpty());
    }

    @Test
    public void testBuildChainUnknownDiseaseThrows() {
        assertThrows(IllegalArgumentException.class,
                () -> registryWithThreeRules().buildChain(List.of("Ebola")));
    }
}
