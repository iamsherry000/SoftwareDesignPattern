package ch4_structural.Gym6_prescriberSystem.code.core;

import java.util.Arrays;
import java.util.List;

public enum Symptom {
    SNEEZE,
    HEADACHE,
    COUGH,
    SNORE;

    public static List<Symptom> parse(String commaSeparated) {
        return Arrays.stream(commaSeparated.split(","))
                .map(String::trim)
                .filter(token -> !token.isEmpty())
                .map(token -> valueOf(token.toUpperCase()))
                .toList();
    }
}
