package ch3_complexBV.Gym5_RPG.src.state;

public class CheerupState implements State {
    private static final int BONUS_PER_VICTIM = 50;
    private static final int DURATION = 3;

    private int remaining = DURATION;

    @Override
    public String getName() { return "受到鼓舞"; }

    @Override
    public int bonusDamagePerVictim() { return BONUS_PER_VICTIM; }

    @Override
    public State nextState() {
        remaining--;
        return remaining > 0 ? this : new NormalState();
    }
}
