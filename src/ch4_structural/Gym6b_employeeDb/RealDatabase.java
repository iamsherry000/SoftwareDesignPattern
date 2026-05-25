package ch4_structural.Gym6b_employeeDb;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RealDatabase implements Database {

    private final String filePath;

    public RealDatabase(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public Optional<Employee> getEmployeeById(int id) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            br.readLine();
            for (int i = 1; i < id; i++) {
                if (br.readLine() == null) return Optional.empty();
            }
            String line = br.readLine();
            if (line == null) return Optional.empty();
            return Optional.ofNullable(parseEmployee(line));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private Employee parseEmployee(String line) {
        String trimmed = line.trim();
        if (trimmed.isEmpty()) return null;
        String[] parts = trimmed.split("\\s+", 4);
        if (parts.length < 3) return null;
        int id = Integer.parseInt(parts[0]);
        String name = parts[1];
        int age = Integer.parseInt(parts[2]);
        List<Integer> subIds = (parts.length == 4 && !parts[3].trim().isEmpty())
                ? parseSubIds(parts[3].trim())
                : new ArrayList<>();
        return new VirtualEmployeeProxy(id, name, age, subIds, this);
    }

    private List<Integer> parseSubIds(String s) {
        List<Integer> result = new ArrayList<>();
        for (String token : s.split(",")) {
            String t = token.trim();
            if (!t.isEmpty()) result.add(Integer.parseInt(t));
        }
        return result;
    }
}
