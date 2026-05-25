package ch4_structural.Gym6_prescriberSystem.code.diagnosis;

import ch4_structural.Gym6_prescriberSystem.code.core.Gender;
import ch4_structural.Gym6_prescriberSystem.code.core.Medicine;
import ch4_structural.Gym6_prescriberSystem.code.core.Patient;
import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;

import java.util.List;

public class Attractive extends DiseaseHandler {
    private final Prescription prescription = new Prescription(
            "青春抑制劑",
            "有人想你了（Attractive）",
            List.of(new Medicine("假鬢角"), new Medicine("臭味")),
            "把假鬢角黏在臉的兩側，讓自己異性緣差一點，自然就不會有人想妳了。"
    );

    @Override
    protected boolean matches(Patient patient, List<Symptom> symptoms) {
        return patient.getGender() == Gender.FEMALE
                && patient.getAge() == 18
                && symptoms.contains(Symptom.SNEEZE);
    }

    @Override
    protected Prescription getPrescription() {
        return prescription;
    }
}
