package ch4_structural.Gym6a_relationAnalyzer.v2;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Client {

    private final RelationshipAnalyzer analyzer;

    public Client(RelationshipAnalyzer analyzer) {
        this.analyzer = analyzer;
    }

    public void run(String scriptPath, String name1, String name2) throws IOException {
        String script = loadScript(scriptPath);
        RelationshipGraph graph = analyzer.parse(script);
        String[] mutualFriends = analyzer.getMutualFriends(name1, name2);
        System.out.println("Mutual friends of " + name1 + " and " + name2
                + ": " + Arrays.toString(mutualFriends));
        System.out.println(name1 + " <-> " + name2 + " connected? "
                + graph.hasConnection(name1, name2));
    }

    private String loadScript(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
}
