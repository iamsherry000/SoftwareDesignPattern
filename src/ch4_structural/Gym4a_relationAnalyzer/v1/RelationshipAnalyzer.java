package ch4_structural.relationAnalyzer.v1;

public interface RelationshipAnalyzer {
    void parse(String script);

    String[] getMutualFriends(String name1, String name2);
}
