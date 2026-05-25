package ch4_structural.Gym6b_employeeDb;

public class Main {

    public static void main(String[] args) {
        String filePath = args.length > 0
                ? args[0]
                : "src/ch4_structural/Gym6b_employeeDb/employees.txt";

        Database real = new RealDatabase(filePath);
        Database withPasswordProtection = new PasswordProtectionProxy(real);
        Client client = new Client(withPasswordProtection);

        try {
            client.run(2);
            System.out.println();
            client.run(4);
            System.out.println();
            client.run(99);
        } catch (IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Hint: set PASSWORD=1qaz2wsx before running.");
        }
    }
}
