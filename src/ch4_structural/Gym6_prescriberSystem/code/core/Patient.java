package ch4_structural.Gym6_prescriberSystem.code.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Patient {
    private final String id;
    private final String name;
    private final Gender gender;
    private final int age;
    private final double height;
    private final double weight;
    private final List<Case> cases = new ArrayList<>();

    public Patient(String id, String name, Gender gender, int age, double height, double weight) {
        if (id == null || !id.matches("^[A-Z][0-9]{9}$")) {
            throw new IllegalArgumentException("id must be 1 uppercase letter followed by 9 digits: " + id);
        }
        if (name == null || !name.matches("^[A-Za-z]{1,30}$")) {
            throw new IllegalArgumentException("name must be 1~30 English letters: " + name);
        }
        if (gender == null) {
            throw new IllegalArgumentException("gender must not be null.");
        }
        if (age < 1 || age > 180) {
            throw new IllegalArgumentException("age must be 1~180: " + age);
        }
        if (height < 1 || height > 500) {
            throw new IllegalArgumentException("height must be 1~500 cm: " + height);
        }
        if (weight < 1 || weight > 500) {
            throw new IllegalArgumentException("weight must be 1~500 kg: " + weight);
        }
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public int getAge() {
        return age;
    }

    public double getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }

    public double getBMI() {
        // height 存公分，BMI 公式分母要公尺
        double heightInMeters = height / 100.0;
        return weight / Math.pow(heightInMeters, 2);
    }

    public void addCase(Case aCase) {
        if (aCase == null) {
            throw new IllegalArgumentException("case must not be null.");
        }
        cases.add(aCase);
    }

    public List<Case> getCases() {
        return Collections.unmodifiableList(cases);
    }
}
