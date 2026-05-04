package ch4_structural.relationAnalyzer.v1;

public class Main {

    public static void main(String[] args) throws Exception {
        RelationshipAnalyzer analyzer =
                new RelationshipAnalyzerAdapter(new SuperRelationshipAnalyzer());
        Client client = new Client(analyzer);

        String scriptPath = args.length > 0
                ? args[0]
                : "src/ch4_structural/Gym4a_relationAnalyzer/v1/script.txt";

        client.run(scriptPath, "A", "B");
        client.run(scriptPath, "A", "E");
        client.run(scriptPath, "C", "D");
        client.run(scriptPath, "F", "Z");
    }
}
