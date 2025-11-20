package TemplateMethod.example.common;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;
import static java.util.stream.IntStream.range;

public class Student {
    private final String name;
    private final int experiencce;
    private final String language;
    private final String jobTitle;
    private final boolean[] availableTimeSlots;

    public Student(String name, int experiencce, String language, String jobTitle, boolean[] availableTimeSlots) {
        this.name = name;
        this.experiencce = experiencce;
        this.language = language;
        this.jobTitle = jobTitle;
        this.availableTimeSlots = availableTimeSlots;
    }

    public String getName() {
        return name;
    }

    public int getExperiencce() {
        return experiencce;
    }

    public String getLanguage() {
        return language;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public boolean[] getAvailableTimeSlots() {
        return availableTimeSlots;
    }

    @Override
    public String toString() {
        return format("%S %dy %s %s [%s]", name, experiencce, language, jobTitle, getAvailableTimeSlotsString());
    }

    private String getAvailableTimeSlotsString() {
        return range(0, availableTimeSlots.length)
                .filter(i -> availableTimeSlots[i])
                .mapToObj(i -> String.valueOf(i+9 /*start with 9 PM*/))
                .collect(joining(" "));
    }

}
