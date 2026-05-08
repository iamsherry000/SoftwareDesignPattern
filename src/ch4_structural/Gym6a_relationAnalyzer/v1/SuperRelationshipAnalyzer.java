package ch4_structural.Gym6a_relationAnalyzer.v1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SuperRelationshipAnalyzer {

    private final Map<String, Set<String>> friends = new HashMap<>();

    public void init(String script) {
        friends.clear();
        for (String rawLine : script.split("\\R")) {
            String line = rawLine.trim();
            if (line.isEmpty()) continue;
            String[] parts = line.split("\\s*--\\s*");
            if (parts.length != 2) continue;
            String a = parts[0].trim();
            String b = parts[1].trim();
            if (a.isEmpty() || b.isEmpty()) continue;
            friends.computeIfAbsent(a, k -> new HashSet<>()).add(b);
            friends.computeIfAbsent(b, k -> new HashSet<>()).add(a);
        }
    }

    public boolean isMutualFriend(String targetName, String name1, String name2) {
        Set<String> targetFriends = friends.get(targetName);
        if (targetFriends == null) return false;
        return targetFriends.contains(name1) && targetFriends.contains(name2);
    }
}
