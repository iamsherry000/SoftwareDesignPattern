package ch4_structural.Gym6a_relationAnalyzer.v2;

public class RelationshipGraphAdapter implements RelationshipGraph {

    private final ThirdPartyGraph adaptee;

    public RelationshipGraphAdapter(ThirdPartyGraph adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public boolean hasConnection(String name1, String name2) {
        return adaptee.pathExists(name1, name2);
    }
}
