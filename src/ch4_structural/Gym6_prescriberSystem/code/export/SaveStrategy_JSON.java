package ch4_structural.Gym6_prescriberSystem.code.export;

import ch4_structural.Gym6_prescriberSystem.code.core.Medicine;
import ch4_structural.Gym6_prescriberSystem.code.core.Prescription;
import ch4_structural.Gym6_prescriberSystem.code.core.Symptom;

import java.util.StringJoiner;

public class SaveStrategy_JSON implements SaveStrategy {

    @Override
    public String export(DiagnosisRecord record) {
        Prescription prescription = record.getPrescription();
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"patientId\": ").append(jsonString(record.getPatientId())).append(",\n");
        sb.append("  \"caseTime\": ").append(jsonString(record.getCaseTime().toString())).append(",\n");
        sb.append("  \"symptoms\": ").append(symptomsArray(record)).append(",\n");
        sb.append("  \"prescription\": {\n");
        sb.append("    \"name\": ").append(jsonString(prescription.getName())).append(",\n");
        sb.append("    \"potentialDisease\": ").append(jsonString(prescription.getPotentialDisease())).append(",\n");
        sb.append("    \"medicines\": ").append(medicinesArray(prescription)).append(",\n");
        sb.append("    \"usage\": ").append(jsonString(prescription.getUsage())).append("\n");
        sb.append("  }\n");
        sb.append("}");
        return sb.toString();
    }

    private String symptomsArray(DiagnosisRecord record) {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (Symptom symptom : record.getSymptoms()) {
            joiner.add(jsonString(symptom.name()));
        }
        return joiner.toString();
    }

    private String medicinesArray(Prescription prescription) {
        StringJoiner joiner = new StringJoiner(", ", "[", "]");
        for (Medicine medicine : prescription.getMedicines()) {
            joiner.add(jsonString(medicine.getName()));
        }
        return joiner.toString();
    }

    private String jsonString(String raw) {
        StringBuilder sb = new StringBuilder("\"");
        for (int i = 0; i < raw.length(); i++) {
            char c = raw.charAt(i);
            switch (c) {
                case '"':  sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\n': sb.append("\\n");  break;
                case '\r': sb.append("\\r");  break;
                case '\t': sb.append("\\t");  break;
                default:   sb.append(c);
            }
        }
        sb.append("\"");
        return sb.toString();
    }
}
