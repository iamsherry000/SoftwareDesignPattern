package ch4_structural.Gym6b_employeeDb;

import java.util.Optional;

public interface Database {
    Optional<Employee> getEmployeeById(int id);
}
