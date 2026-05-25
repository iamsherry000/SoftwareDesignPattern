package ch4_structural.Gym6_prescriberSystem.code.diagnosis;

import ch4_structural.Gym6_prescriberSystem.code.core.Medicine;
import ch4_structural.Gym6_prescriberSystem.code.core.Patient;
import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;

import java.util.List;

// 對齊 OOD.png 子類 "Covid-19"；連字號是非法 Java 識別字 → Covid19
public class Covid19 extends DiseaseHandler {
    private final Prescription prescription = new Prescription(
            "清冠一號",
            "新冠肺炎（COVID-19）",
            List.of(new Medicine("清冠一號")),
            "將相關藥材裝入茶包裡，使用 500 mL 溫、熱水沖泡悶煮 1~3 分鐘後即可飲用。"
    );

    @Override
    protected boolean matches(Patient patient, List<Symptom> symptoms) {
        return symptoms.contains(Symptom.SNEEZE)
                && symptoms.contains(Symptom.HEADACHE)
                && symptoms.contains(Symptom.COUGH);
    }

    @Override
    protected Prescription getPrescription() {
        return prescription;
    }
}
