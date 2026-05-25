package ch4_structural.Gym6b_employeeDb;

import java.util.ArrayList;
import java.util.List;

public class VirtualEmployeeProxy implements Employee {

    private final int id;
    private final String name;
    private final int age;
    private final List<Integer> subordinateIds;
    private final Database database;
    private List<Employee> subordinatesCache;

    public VirtualEmployeeProxy(int id, String name, int age,
                                List<Integer> subordinateIds, Database database) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.subordinateIds = subordinateIds;
        this.database = database;
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
        if (subordinatesCache == null) {
            subordinatesCache = new ArrayList<>();
            for (int subId : subordinateIds) {
                database.getEmployeeById(subId).ifPresent(subordinatesCache::add);
            }
        }
        return subordinatesCache;
    }
}
