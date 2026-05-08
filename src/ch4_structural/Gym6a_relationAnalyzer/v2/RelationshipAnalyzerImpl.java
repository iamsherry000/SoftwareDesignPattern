package ch4_structural.Gym6a_relationAnalyzer.v2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RelationshipAnalyzerImpl implements RelationshipAnalyzer {

    private final Map<String, Set<String>> directFriends = new LinkedHashMap<>();

    @Override
    public RelationshipGraph parse(String script) {
        directFriends.clear();
        ThirdPartyGraph graph = new ThirdPartyGraph();
        for (String rawLine : script.split("\\R")) {
            String line = rawLine.trim();
            if (line.isEmpty()) continue;
            int colon = line.indexOf(':');
            if (colon < 0) continue;
            String owner = line.substring(0, colon).trim();
            if (owner.isEmpty()) continue;
            graph.addPerson(owner);
            directFriends.computeIfAbsent(owner, k -> new LinkedHashSet<>());
            String rest = line.substring(colon + 1).trim();
            if (rest.isEmpty()) continue;
            for (String friend : rest.split("\\s+")) {
                if (friend.isEmpty()) continue;
                graph.link(owner, friend);
                directFriends.computeIfAbsent(owner, k -> new LinkedHashSet<>()).add(friend);
                directFriends.computeIfAbsent(friend, k -> new LinkedHashSet<>()).add(owner);
            }
        }
        return new RelationshipGraphAdapter(graph);
    }

    @Override
    public String[] getMutualFriends(String name1, String name2) {
        Set<String> f1 = directFriends.getOrDefault(name1, Collections.emptySet());
        Set<String> f2 = directFriends.getOrDefault(name2, Collections.emptySet());
        List<String> result = new ArrayList<>();
        for (String candidate : f1) {
            if (f2.contains(candidate)) result.add(candidate);
        }
        return result.toArray(new String[0]);
    }
}
