package ch4_structural.Gym6_prescriberSystem.code.diagnosis;

import ch4_structural.Gym6_prescriberSystem.code.core.Medicine;
import ch4_structural.Gym6_prescriberSystem.code.core.Patient;
import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;

import java.util.List;

public class SleepApneaSyndrome extends DiseaseHandler {
    private final Prescription prescription = new Prescription(
            "打呼抑制劑",
            "睡眠呼吸中止症（SleepApneaSyndrome）",
            List.of(new Medicine("一捲膠帶")),
            "睡覺時，撕下兩塊膠帶，將兩塊膠帶交錯黏在關閉的嘴巴上，就不會打呼了。"
    );

    @Override
    protected boolean matches(Patient patient, List<Symptom> symptoms) {
        return patient.getBMI() > 26
                && symptoms.contains(Symptom.SNORE);
    }

    @Override
    protected Prescription getPrescription() {
        return prescription;
    }
}
