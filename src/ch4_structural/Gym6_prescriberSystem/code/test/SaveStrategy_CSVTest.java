package ch4_structural.Gym6_prescriberSystem.code.test;

import ch4_structural.Gym6_prescriberSystem.code.core.Medicine;
import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;
import ch4_structural.Gym6_prescriberSystem.code.export.SaveStrategy_CSV;
import ch4_structural.Gym6_prescriberSystem.code.export.DiagnosisRecord;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SaveStrategy_CSVTest {

    private DiagnosisRecord attractiveRecord() {
        Prescription prescription = new Prescription(
                "青春抑制劑", "有人想你了（Attractive）",
                List.of(new Medicine("假鬢角"), new Medicine("臭味")),
                "把假鬢角黏在臉的兩側。");
        return new DiagnosisRecord("B123456789",
                List.of(Symptom.SNEEZE), prescription, LocalDateTime.of(2026, 5, 21, 10, 0));
    }

    @Test
    public void testCsvHasHeaderThenDataRow() {
        String csv = new SaveStrategy_CSV().export(attractiveRecord());
        String[] lines = csv.split("\n");

        assertEquals(2, lines.length);
        assertEquals("patientId,caseTime,symptoms,prescriptionName,potentialDisease,medicines,usage",
                lines[0]);
    }

    @Test
    public void testCsvDataRowContainsPrescriptionFields() {
        String csv = new SaveStrategy_CSV().export(attractiveRecord());
        String dataRow = csv.split("\n")[1];

        assertTrue(dataRow.contains("\"B123456789\""));
        assertTrue(dataRow.contains("\"SNEEZE\""));
        assertTrue(dataRow.contains("\"青春抑制劑\""));
        assertTrue(dataRow.contains("\"有人想你了（Attractive）\""));
        // 多藥用 ';' 串在同一欄
        assertTrue(dataRow.contains("\"假鬢角;臭味\""));
    }

    @Test
    public void testCsvEscapesEmbeddedQuotes() {
        Prescription prescription = new Prescription(
                "test處方", "潛在疾病學名",
                List.of(new Medicine("藥")),
                "含有\"引號\"的使用方法");
        DiagnosisRecord record = new DiagnosisRecord("A123456789",
                List.of(Symptom.COUGH), prescription, LocalDateTime.of(2026, 5, 21, 10, 0));

        String csv = new SaveStrategy_CSV().export(record);
        // 內嵌雙引號被轉義成 ""
        assertTrue(csv.contains("\"含有\"\"引號\"\"的使用方法\""));
    }
}
