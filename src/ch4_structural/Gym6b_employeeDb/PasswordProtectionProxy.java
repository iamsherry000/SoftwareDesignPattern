package ch4_structural.Gym6b_employeeDb;

import java.util.Optional;

public class PasswordProtectionProxy implements Database {

    private static final String EXPECTED_PASSWORD = "1qaz2wsx";

    private final Database adaptee;

    public PasswordProtectionProxy(Database adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public Optional<Employee> getEmployeeById(int id) {
        if (!validPassword()) {
            throw new IllegalStateException(
                    "Access denied: PASSWORD environment variable is missing or incorrect");
        }
        return adaptee.getEmployeeById(id);
    }

    private boolean validPassword() {
        String actual = System.getenv("PASSWORD");
        return EXPECTED_PASSWORD.equals(actual);
    }
}
