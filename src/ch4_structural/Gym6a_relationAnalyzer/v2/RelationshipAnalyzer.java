package ch4_structural.Gym6a_relationAnalyzer.v2;

public interface RelationshipAnalyzer {
    RelationshipGraph parse(String script);

    String[] getMutualFriends(String name1, String name2);
}
