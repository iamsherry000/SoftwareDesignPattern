package ch4_structural.Gym6_prescriberSystem.code.test;

import ch4_structural.Gym6_prescriberSystem.code.core.Gender;
import ch4_structural.Gym6_prescriberSystem.code.core.Patient;
import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.Attractive;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.Covid19;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.DiseaseHandler;
import ch4_structural.Gym6_prescriberSystem.code.diagnosis.SleepApneaSyndrome;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DiagnosisChainTest {
    // 鏈組裝順序：規則一 Covid → 規則二 Attractive → 規則三 SleepApnea
    private DiseaseHandler buildChain() {
        DiseaseHandler head = new Covid19();
        head.setNext(new Attractive()).setNext(new SleepApneaSyndrome());
        return head;
    }

    @Test
    public void testMultiMatchReturnsFirstInChainOrder() {
        // 18 歲女 + BMI>26 + 噴嚏 + 打呼 → 同時中規則二與規則三
        // 鏈序 first-match：規則二 Attractive 在規則三前 → 回青春抑制劑
        Patient patient = new Patient("B123456789", "Bella", Gender.FEMALE, 18, 150, 70); // BMI ≈ 31.1
        assertTrue(patient.getBMI() > 26);

        Optional<Prescription> result = buildChain()
                .handle(patient, List.of(Symptom.SNEEZE, Symptom.SNORE));

        assertTrue(result.isPresent());
        assertEquals("青春抑制劑", result.get().getName());
    }

    @Test
    public void testCovidWinsOverAttractive() {
        // 18 歲女 + 噴嚏+頭痛+咳嗽 → 同時中規則一與規則二（噴嚏共用）
        // 鏈序 first-match：規則一 Covid 在最前 → 回清冠一號
        Patient patient = new Patient("B123456789", "Bella", Gender.FEMALE, 18, 160, 50);
        Optional<Prescription> result = buildChain()
                .handle(patient, List.of(Symptom.SNEEZE, Symptom.HEADACHE, Symptom.COUGH));

        assertTrue(result.isPresent());
        assertEquals("清冠一號", result.get().getName());
    }

    @Test
    public void testNoMatchReturnsEmpty() {
        // 一條都沒命中 → Optional.empty()，不造假兜底處方
        Patient patient = new Patient("D123456789", "Dan", Gender.MALE, 25, 175, 65);
        Optional<Prescription> result = buildChain()
                .handle(patient, List.of(Symptom.HEADACHE));

        assertTrue(result.isEmpty());
    }
}
