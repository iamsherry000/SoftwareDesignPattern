package abstractClass;

public enum Decision {
    ROCK("石頭"), PAPER("布"), SCISSORS("剪刀");

    private final String name;

    Decision(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
