package ch4_structural.relationAnalyzer.v2;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

// Stand-in for a real third-party graph library (e.g. JGraphT).
// Method names intentionally differ from RelationshipGraph#hasConnection
// so the Adapter has real translation work to do.
public class ThirdPartyGraph {

    private final Map<String, Set<String>> adjacency = new HashMap<>();

    public void addPerson(String name) {
        adjacency.computeIfAbsent(name, k -> new HashSet<>());
    }

    public void link(String a, String b) {
        addPerson(a);
        addPerson(b);
        adjacency.get(a).add(b);
        adjacency.get(b).add(a);
    }

    public boolean pathExists(String from, String to) {
        if (!adjacency.containsKey(from) || !adjacency.containsKey(to)) return false;
        if (from.equals(to)) return true;
        Set<String> visited = new HashSet<>();
        Deque<String> queue = new ArrayDeque<>();
        queue.add(from);
        visited.add(from);
        while (!queue.isEmpty()) {
            String current = queue.poll();
            for (String neighbor : adjacency.get(current)) {
                if (neighbor.equals(to)) return true;
                if (visited.add(neighbor)) queue.add(neighbor);
            }
        }
        return false;
    }
}
