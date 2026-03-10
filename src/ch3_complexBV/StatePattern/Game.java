package ch3_complexBV.StatePattern;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Game – orchestrates the turn loop.
 *
 * Input uses raw byte reading so arrow keys (ESC sequences) work
 * when IntelliJ's "Emulate terminal in output console" is enabled.
 *
 * Controls:
 *   ↑ ↓ ← →  – move (also changes facing)
 *   F         – attack in current facing direction
 *
 * Turn structure:
 *   1. ch.onTurnStart()             (state effects: poison, heal, orderless flip)
 *   2. printHUD() + map.printMap()
 *   3. player actions × getActionsPerTurn()
 *   4. ch.onTurnEnd()               (state timer & transitions)
 *   5. teleport if flagged
 *   6. each monster: onTurnStart → monsterAct → onTurnEnd → teleport
 *   7. win / lose check
 *   8. every SPAWN_INTERVAL turns: +1 treasure, +1 monster
 */
public class Game {

    private static final int SPAWN_INTERVAL = 5;

    private final Map      map;
    private int            turn = 0;

    public Game() {
        this.map = new Map(10, 10);
    }

    // ── Entry point ──────────────────────────────────────────────────────────

    public void start() {
        System.out.println("╔══════════════════════════════════════════╗");
        System.out.println("║   2-D Adventure Game  (State Pattern)   ║");
        System.out.println("║  Controls: ↑↓←→ move   F = attack       ║");
        System.out.println("╚══════════════════════════════════════════╝");
        map.init();

        while (true) {
            turn++;
            System.out.println("\n══════════  Turn " + turn + "  ══════════");

            Character ch = map.getCharacter();

            // 1. Character turn-start state effects
            ch.onTurnStart();

            // 2. HUD + map
            printHUD(ch);
            map.printMap();

            // Death from env damage (e.g. Poisoned)
            if (!ch.isAlive()) { gameOver("Poisoned to death."); break; }

            // 3. Player actions (Accelerated = 2 per turn)
            int actions = ch.getState().getActionsPerTurn();
            for (int a = 1; a <= actions; a++) {
                if (actions > 1) System.out.println("─── Action " + a + "/" + actions + " ───");
                playerAction(ch);
                if (!ch.isAlive()) { gameOver("Killed by monster."); return; }
            }

            // Win after player's turn
            if (map.allMonstersDead()) { victory(); break; }

            // 4-5. Character turn-end + teleport
            ch.onTurnEnd();
            handleTeleport(ch);

            // 6. Monsters' turns
            System.out.println("\n[Monsters' Turn]");
            List<Monster> snapshot = new ArrayList<>(map.getMonsters());
            for (Monster m : snapshot) {
                m.onTurnStart();
                map.monsterAct(m);
                m.onTurnEnd();
                handleTeleport(m);
            }

            // 7. Win / lose after monsters
            if (!ch.isAlive()) { gameOver("Killed by monster."); break; }
            if (map.allMonstersDead()) { victory(); break; }

            // 8. Periodic spawn
            if (turn % SPAWN_INTERVAL == 0) {
                System.out.println("\n[Periodic Spawn] New treasure appeared!");
                map.spawnTreasure();
            }
        }
    }

    private void handleTeleport(Role role) {
        if (role.isNeedsTeleport()) {
            map.teleportRole(role);
            role.setNeedsTeleport(false);
        }
    }

    // ── Player action ────────────────────────────────────────────────────────

    private void playerAction(Character ch) {
        Set<Direction> allowed   = ch.getState().getAllowedMoveDirections();
        boolean        canAttack = ch.getState().canAttack();

        System.out.println("Facing: " + ch.getFacing().name()
                + "  State: " + ch.getState().getName());
        System.out.print("Input [↑↓←→" + (canAttack ? " / F=Attack" : "") + "]: ");
        System.out.flush();

        String cmd = readKey();
        System.out.println(cmd);   // echo back so player sees what was pressed

        if ("ATTACK".equals(cmd)) {
            if (!canAttack) {
                System.out.println("  → Cannot attack while " + ch.getState().getName() + "!");
                return;
            }
            System.out.println("Character attacks " + ch.getFacing().name() + "!");
            if (ch.getState().isFullMapAttack()) map.attackAll(50);
            else                                  map.attackLine(ch);

        } else {
            Direction dir = Direction.parse(cmd);
            if (dir == null) {
                System.out.println("  → Unknown input – turn skipped.");
                return;
            }
            if (allowed != null && !allowed.contains(dir)) {
                System.out.println("  → Orderless: can't move " + dir.name()
                        + " this turn – allowed: " + allowed);
                return;
            }
            System.out.println("Character moves " + dir.name() + ".");
            ch.setFacing(dir);
            map.moveRole(ch, dir);
        }
    }

    // ── Raw input reader ─────────────────────────────────────────────────────

    /**
     * Reads one logical key from stdin and returns a canonical string:
     *   "UP" | "DOWN" | "LEFT" | "RIGHT" | "ATTACK" | "UNKNOWN"
     *
     * Handles both:
     *   - Arrow-key ESC sequences  (terminal emulation / real PTY)
     *   - WASD / F plain characters (IDE console line-buffered mode)
     */
    private String readKey() {
        try {
            InputStream in = System.in;
            int b1 = in.read();
            if (b1 == -1) return "UNKNOWN";

            // Arrow key: ESC [ A/B/C/D
            if (b1 == 27) {
                // Check if more bytes are immediately available
                try { Thread.sleep(20); } catch (InterruptedException ignored) {}
                if (in.available() >= 2) {
                    int b2 = in.read();
                    int b3 = in.read();
                    drainRemaining(in);
                    if (b2 == '[' || b2 == 'O') {
                        switch (b3) {
                            case 'A': return "UP";
                            case 'B': return "DOWN";
                            case 'C': return "RIGHT";
                            case 'D': return "LEFT";
                        }
                    }
                }
                drainRemaining(in);
                return "UNKNOWN";
            }

            // Regular character – drain rest of line (Enter, \r\n, etc.)
            drainRemaining(in);

            switch (java.lang.Character.toUpperCase((char) b1)) {
                case 'W': return "UP";
                case 'S': return "DOWN";
                case 'A': return "LEFT";
                case 'D': return "RIGHT";
                case 'F': return "ATTACK";
                default:  return "UNKNOWN";
            }

        } catch (IOException e) {
            return "UNKNOWN";
        }
    }

    /** Consumes any remaining bytes already buffered in stdin (e.g. \r\n after Enter). */
    private void drainRemaining(InputStream in) throws IOException {
        try { Thread.sleep(20); } catch (InterruptedException ignored) {}
        while (in.available() > 0) in.read();
    }

    // ── HUD ──────────────────────────────────────────────────────────────────

    private void printHUD(Character ch) {
        int filled = (int) Math.round(20.0 * ch.getHp() / ch.getMaxHp());
        String bar = "█".repeat(Math.max(0, filled)) + "░".repeat(Math.max(0, 20 - filled));
        System.out.println("HP  [" + bar + "] " + ch.getHp() + "/" + ch.getMaxHp()
                + "   State: "         + ch.getState().getName()
                + "   Facing: "        + ch.getFacing().name()
                + "   Monsters left: " + map.getMonsters().size());
    }

    // ── End-game ─────────────────────────────────────────────────────────────

    private void victory() {
        System.out.println("\n★★★  ALL MONSTERS DEFEATED – YOU WIN!  ★★★");
        System.out.println("Completed in " + turn + " turns.");
    }

    private void gameOver(String reason) {
        System.out.println("\n✖  GAME OVER  –  " + reason);
        System.out.println("Survived " + turn + " turns.");
    }
}
