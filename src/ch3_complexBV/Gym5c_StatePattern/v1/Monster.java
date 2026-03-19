package ch3_complexBV.StatePattern.v1;

/**
 * Monster – an enemy NPC.
 *
 * HP is 1 (one hit kills).
 * AI behaviour (move or attack) is resolved by Map.monsterAct() so that
 * Monster itself stays decoupled from Map internals.
 */
public class Monster extends Role {

    private static final char  SYMBOL        = 'M';
    private static final int   MAX_HP        = 1;
    private static final int   ATTACK_DAMAGE = 50;

    public Monster() {
        super(SYMBOL, MAX_HP);
    }

    public int getAttackDamage() { return ATTACK_DAMAGE; }
}
