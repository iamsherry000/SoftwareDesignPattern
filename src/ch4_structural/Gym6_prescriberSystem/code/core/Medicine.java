package ch4_structural.Gym6_prescriberSystem.code.core;

public class Medicine {
    private final String name;

    public Medicine(String name) {
        // 需求寫藥名 3~30 字，但規則二的藥「臭味」只有 2 字（需求自相矛盾）→ 下限放寬為非空，見 ood-design §10
        if (name == null || name.isEmpty() || name.length() > 30) {
            throw new IllegalArgumentException("Medicine name must be 1~30 characters: " + name);
        }
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
