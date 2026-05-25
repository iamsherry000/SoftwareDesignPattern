package ch4_structural.Gym6_prescriberSystem.code.export;

import ch4_structural.Gym6_prescriberSystem.code.core.Medicine;
import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;

import java.util.StringJoiner;

public class SaveStrategy_CSV implements SaveStrategy {

    private static final String HEADER =
            "patientId,caseTime,symptoms,prescriptionName,potentialDisease,medicines,usage";

    @Override
    public String export(DiagnosisRecord record) {
        Prescription prescription = record.getPrescription();
        StringJoiner row = new StringJoiner(",");
        row.add(csvField(record.getPatientId()));
        row.add(csvField(record.getCaseTime().toString()));
        row.add(csvField(joinSymptoms(record)));
        row.add(csvField(prescription.getName()));
        row.add(csvField(prescription.getPotentialDisease()));
        row.add(csvField(joinMedicines(prescription)));
        row.add(csvField(prescription.getUsage()));
        return HEADER + "\n" + row.toString();
    }

    private String joinSymptoms(DiagnosisRecord record) {
        StringJoiner joiner = new StringJoiner(";");
        for (Symptom symptom : record.getSymptoms()) {
            joiner.add(symptom.name());
        }
        return joiner.toString();
    }

    private String joinMedicines(Prescription prescription) {
        StringJoiner joiner = new StringJoiner(";");
        for (Medicine medicine : prescription.getMedicines()) {
            joiner.add(medicine.getName());
        }
        return joiner.toString();
    }

    private String csvField(String raw) {
        return "\"" + raw.replace("\"", "\"\"") + "\"";
    }
}
