package ch4_structural.Gym6_prescriberSystem.code.core;

import java.util.List;

public class Prescription {
    private final String name;
    private final String potentialDisease;
    private final List<Medicine> medicines;
    private final String usage;

    public Prescription(String name, String potentialDisease, List<Medicine> medicines, String usage) {
        if (name == null || name.length() < 4 || name.length() > 30) {
            throw new IllegalArgumentException("Prescription name must be 4~30 characters: " + name);
        }
        if (potentialDisease == null || potentialDisease.length() < 3 || potentialDisease.length() > 100) {
            throw new IllegalArgumentException("potentialDisease must be 3~100 characters: " + potentialDisease);
        }
        if (medicines == null || medicines.isEmpty()) {
            throw new IllegalArgumentException("Prescription must contain at least one medicine.");
        }
        if (usage == null || usage.length() > 1000) {
            throw new IllegalArgumentException("usage must be 0~1000 characters.");
        }
        this.name = name;
        this.potentialDisease = potentialDisease;
        this.medicines = List.copyOf(medicines);
        this.usage = usage;
    }

    public String getName() {
        return name;
    }

    public String getPotentialDisease() {
        return potentialDisease;
    }

    public List<Medicine> getMedicines() {
        return medicines;
    }

    public String getUsage() {
        return usage;
    }
}
