package ch4_structural.relationAnalyzer.v1;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class RelationshipAnalyzerAdapter implements RelationshipAnalyzer {

    private final SuperRelationshipAnalyzer adaptee;
    private final Set<String> allNames = new LinkedHashSet<>();

    public RelationshipAnalyzerAdapter(SuperRelationshipAnalyzer adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void parse(String script) {
        allNames.clear();
        collectNames(script);
        adaptee.init(convertScript(script));
    }

    @Override
    public String[] getMutualFriends(String name1, String name2) {
        List<String> result = new ArrayList<>();
        for (String candidate : allNames) {
            if (candidate.equals(name1) || candidate.equals(name2)) continue;
            if (adaptee.isMutualFriend(candidate, name1, name2)) {
                result.add(candidate);
            }
        }
        return result.toArray(new String[0]);
    }

    private void collectNames(String script) {
        for (String rawLine : script.split("\\R")) {
            String line = rawLine.trim();
            if (line.isEmpty()) continue;
            int colon = line.indexOf(':');
            if (colon < 0) continue;
            String owner = line.substring(0, colon).trim();
            if (owner.isEmpty()) continue;
            allNames.add(owner);
            String rest = line.substring(colon + 1).trim();
            if (rest.isEmpty()) continue;
            for (String name : rest.split("\\s+")) {
                if (!name.isEmpty()) allNames.add(name);
            }
        }
    }

    private String convertScript(String script) {
        StringBuilder out = new StringBuilder();
        for (String rawLine : script.split("\\R")) {
            String line = rawLine.trim();
            if (line.isEmpty()) continue;
            int colon = line.indexOf(':');
            if (colon < 0) continue;
            String owner = line.substring(0, colon).trim();
            if (owner.isEmpty()) continue;
            String rest = line.substring(colon + 1).trim();
            if (rest.isEmpty()) continue;
            for (String friend : rest.split("\\s+")) {
                if (friend.isEmpty()) continue;
                out.append(owner).append(" -- ").append(friend).append(System.lineSeparator());
            }
        }
        return out.toString();
    }
}
