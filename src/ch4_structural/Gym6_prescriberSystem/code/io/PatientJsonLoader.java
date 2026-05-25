package ch4_structural.Gym6_prescriberSystem.code.io;

import ch4_structural.Gym6_prescriberSystem.code.core.Gender;
import ch4_structural.Gym6_prescriberSystem.code.core.Patient;
import ch4_structural.Gym6_prescriberSystem.code.core.PatientDatabase;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PatientJsonLoader {

    public List<Patient> parsePatients(String json) {
        Object root = MiniJsonParser.parse(json);
        if (!(root instanceof List)) {
            throw new IllegalArgumentException("Patient JSON root must be an array.");
        }
        List<Patient> patients = new ArrayList<>();
        for (Object element : (List<?>) root) {
            if (!(element instanceof Map)) {
                throw new IllegalArgumentException("Each patient entry must be a JSON object.");
            }
            patients.add(toPatient((Map<?, ?>) element));
        }
        return patients;
    }

    public List<Patient> loadPatients(Path jsonFile) throws IOException {
        return parsePatients(Files.readString(jsonFile, StandardCharsets.UTF_8));
    }

    public PatientDatabase loadDatabase(Path jsonFile) throws IOException {
        return new PatientDatabase(loadPatients(jsonFile));
    }

    private Patient toPatient(Map<?, ?> object) {
        return new Patient(
                asString(object.get("id"), "id"),
                asString(object.get("name"), "name"),
                toGender(asString(object.get("gender"), "gender")),
                asInt(object.get("age"), "age"),
                asDouble(object.get("height"), "height"),
                asDouble(object.get("weight"), "weight"));
    }

    private Gender toGender(String value) {
        switch (value.toUpperCase()) {
            case "M":
            case "MALE":
                return Gender.MALE;
            case "F":
            case "FEMALE":
                return Gender.FEMALE;
            default:
                throw new IllegalArgumentException("gender must be M/F or male/female: " + value);
        }
    }

    private String asString(Object value, String field) {
        if (!(value instanceof String)) {
            throw new IllegalArgumentException("Field \"" + field + "\" must be a string.");
        }
        return (String) value;
    }

    private int asInt(Object value, String field) {
        if (!(value instanceof Number)) {
            throw new IllegalArgumentException("Field \"" + field + "\" must be a number.");
        }
        return ((Number) value).intValue();
    }

    private double asDouble(Object value, String field) {
        if (!(value instanceof Number)) {
            throw new IllegalArgumentException("Field \"" + field + "\" must be a number.");
        }
        return ((Number) value).doubleValue();
    }
}
