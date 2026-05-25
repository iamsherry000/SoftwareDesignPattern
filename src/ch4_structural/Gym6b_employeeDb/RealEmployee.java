package ch4_structural.Gym6b_employeeDb;

import java.util.List;

public class RealEmployee implements Employee {

    private final int id;
    private final String name;
    private final int age;
    private final List<Employee> subordinates;

    public RealEmployee(int id, String name, int age, List<Employee> subordinates) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.subordinates = subordinates;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public List<Employee> getSubordinates() {
        return subordinates;
    }
}
