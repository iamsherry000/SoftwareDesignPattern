package ch4_structural.relationAnalyzer.v2;

public class RelationshipAnalyzerImpl implements RelationshipAnalyzer {

    @Override
    public RelationshipGraph parse(String script) {
        ThirdPartyGraph graph = new ThirdPartyGraph();
        for (String rawLine : script.split("\\R")) {
            String line = rawLine.trim();
            if (line.isEmpty()) continue;
            int colon = line.indexOf(':');
            if (colon < 0) continue;
            String owner = line.substring(0, colon).trim();
            if (owner.isEmpty()) continue;
            graph.addPerson(owner);
            String rest = line.substring(colon + 1).trim();
            if (rest.isEmpty()) continue;
            for (String friend : rest.split("\\s+")) {
                if (friend.isEmpty()) continue;
                graph.link(owner, friend);
            }
        }
        return new RelationshipGraphAdapter(graph);
    }
}
