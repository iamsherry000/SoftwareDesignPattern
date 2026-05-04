package ch3_complexBV.Gym5_RPG.src.state;

public class PetrochemicalState implements State {
    private static final int DURATION = 3;

    private int remaining = DURATION;

    @Override
    public String getName() { return "石化"; }

    @Override
    public boolean canAct() { return false; }

    @Override
    public State nextState() {
        remaining--;
        return remaining > 0 ? this : new NormalState();
    }
}
