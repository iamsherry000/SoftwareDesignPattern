package ch3_complexBV.StatePattern.v1;

import ch3_complexBV.StatePattern.v1.states.NormalState;

/**
 * Role – abstract base for all moving actors (Character and Monster).
 *
 * Extends MapObj so roles live in the same grid as Obstacles and Treasures.
 *
 * State Pattern Context:
 *   Role holds a reference to a RoleState object and delegates all
 *   state-sensitive operations to it.  Concrete subclasses only need to
 *   override afterReceiveDamage() for any role-specific post-hit logic
 *   (e.g. Character gaining Invincible).
 */
public abstract class Role extends MapObj {

    protected int hp;
    protected final int maxHp;
    protected RoleState state;

    private boolean needsTeleport = false;
    private int row;
    private int col;

    public Role(char symbol, int maxHp) {
        super(symbol);
        this.maxHp = maxHp;
        this.hp    = maxHp;
        this.state = new NormalState();
    }

    // ── State Pattern delegation ────────────────────────────────────────────

    public void setState(RoleState state) { this.state = state; }
    public RoleState getState()           { return state; }

    /** Called by Game at the start of this role's turn. */
    public void onTurnStart() { state.onTurnStart(this); }

    /** Called by Game at the end of this role's turn. */
    public void onTurnEnd()   { state.onTurnEnd(this); }

    /**
     * Entry-point for external attacks (monster hits character, character's
     * attack line kills monsters).
     * The current state filters rawDamage → actual damage.
     * Subclasses may override afterReceiveDamage() to add role-specific logic.
     */
    public void receiveAttack(int rawDamage) {
        boolean prevent = state.preventInvincibleAfterHit();
        int actual = state.onReceiveDamage(rawDamage, this);
        if (actual > 0) {
            takeDamage(actual);
            afterReceiveDamage(prevent);
        }
    }

    /**
     * Hook called after damage is taken.
     * Default: no-op.  Character overrides to conditionally gain Invincible.
     *
     * @param preventInvincible true if the state that was active at the moment
     *                          of the hit already handled the "no Invincible" rule.
     */
    protected void afterReceiveDamage(boolean preventInvincible) {}

    // ── HP helpers ──────────────────────────────────────────────────────────

    /** Direct HP deduction – skips state filtering (used by Poisoned, etc.). */
    public void takeDamage(int amount) { hp = Math.max(0, hp - amount); }

    /** Direct HP recovery – skips state filtering (used by Healing). */
    public void heal(int amount)       { hp = Math.min(maxHp, hp + amount); }

    public boolean isFullHp()  { return hp >= maxHp; }
    public boolean isAlive()   { return hp > 0; }

    // ── Accessors ───────────────────────────────────────────────────────────

    public int getHp()     { return hp; }
    public int getMaxHp()  { return maxHp; }
    public int getRow()    { return row; }
    public int getCol()    { return col; }

    public void setPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean isNeedsTeleport()          { return needsTeleport; }
    public void    setNeedsTeleport(boolean b) { needsTeleport = b; }
}
