package ch4_structural.Gym6_prescriberSystem.code.test;

import ch4_structural.Gym6_prescriberSystem.code.core.Medicine;
import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;
import ch4_structural.Gym6_prescriberSystem.code.export.DiagnosisRecord;
import ch4_structural.Gym6_prescriberSystem.code.export.SaveStrategy_JSON;
import ch4_structural.Gym6_prescriberSystem.code.io.MiniJsonParser;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SaveStrategy_JSONTest {

    private DiagnosisRecord covidRecord() {
        Prescription prescription = new Prescription(
                "清冠一號", "新冠肺炎（COVID-19）",
                List.of(new Medicine("清冠一號")),
                "將相關藥材裝入茶包裡，使用 500 mL 溫、熱水沖泡悶煮 1~3 分鐘後即可飲用。");
        return new DiagnosisRecord("A123456789",
                List.of(Symptom.SNEEZE, Symptom.HEADACHE, Symptom.COUGH),
                prescription, LocalDateTime.of(2026, 5, 21, 10, 0));
    }

    @Test
    public void testJsonOutputIsParseableAndContainsAllFields() {
        String json = new SaveStrategy_JSON().export(covidRecord());

        // round-trip：自家輸出可被自家 parser 重新解析
        Object root = MiniJsonParser.parse(json);
        assertTrue(root instanceof Map);

        Map<?, ?> object = (Map<?, ?>) root;
        assertEquals("A123456789", object.get("patientId"));
        assertEquals(List.of("SNEEZE", "HEADACHE", "COUGH"), object.get("symptoms"));
        assertNotNull(object.get("caseTime"));

        Map<?, ?> prescription = (Map<?, ?>) object.get("prescription");
        assertEquals("清冠一號", prescription.get("name"));
        assertEquals("新冠肺炎（COVID-19）", prescription.get("potentialDisease"));
        assertEquals(List.of("清冠一號"), prescription.get("medicines"));
        assertTrue(prescription.get("usage").toString().contains("茶包"));
    }

    @Test
    public void testMultipleMedicinesSerialized() {
        Prescription prescription = new Prescription(
                "青春抑制劑", "有人想你了（Attractive）",
                List.of(new Medicine("假鬢角"), new Medicine("臭味")),
                "把假鬢角黏在臉的兩側。");
        DiagnosisRecord record = new DiagnosisRecord("B123456789",
                List.of(Symptom.SNEEZE), prescription, LocalDateTime.of(2026, 5, 21, 10, 0));

        String json = new SaveStrategy_JSON().export(record);
        Map<?, ?> object = (Map<?, ?>) MiniJsonParser.parse(json);
        Map<?, ?> presc = (Map<?, ?>) object.get("prescription");
        assertEquals(List.of("假鬢角", "臭味"), presc.get("medicines"));
    }
}
