package ch4_structural.relationAnalyzer.v2;

public class Main {

    public static void main(String[] args) throws Exception {
        RelationshipAnalyzer analyzer = new RelationshipAnalyzerImpl();
        Client client = new Client(analyzer);

        String scriptPath = args.length > 0
                ? args[0]
                : "src/ch4_structural/Gym4a_relationAnalyzer/v2/script.txt";

        client.run(scriptPath, "A", "B");
        client.run(scriptPath, "A", "K");
        client.run(scriptPath, "A", "L");
        client.run(scriptPath, "F", "Z");
        client.run(scriptPath, "F", "A");
    }
}
