package ch4_structural.Gym6b_employeeDb;

public class Client {

    private final Database database;

    public Client(Database database) {
        this.database = database;
    }

    public void run(int targetId) {
        database.getEmployeeById(targetId).ifPresentOrElse(
                employee -> {
                    System.out.println("Employee " + employee.getId()
                            + ": " + employee.getName()
                            + " (age " + employee.getAge() + ")");
                    System.out.println("Subordinates (lazy-loaded on demand):");
                    for (Employee sub : employee.getSubordinates()) {
                        System.out.println("  - " + sub.getId() + ": " + sub.getName()
                                + " (age " + sub.getAge() + ")");
                    }
                },
                () -> System.out.println("Employee " + targetId + " not found")
        );
    }
}
