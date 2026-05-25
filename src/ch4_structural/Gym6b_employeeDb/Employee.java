package ch4_structural.Gym6b_employeeDb;

import java.util.List;

public interface Employee {
    int getId();

    String getName();

    int getAge();

    List<Employee> getSubordinates();
}
