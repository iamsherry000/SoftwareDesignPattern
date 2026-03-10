package ch3_complexBV.StatePattern;

import ch3_complexBV.StatePattern.treasures.*;
import java.util.*;

/**
 * Map – holds the 2-D grid and all map objects.
 *
 * Responsibilities:
 *   - Grid initialisation (treasures, obstacles, monsters, character).
 *   - Moving a Role (handles touch-collision with Treasure / Obstacle / other Role).
 *   - Character attack resolution (line attack or full-map Erupting attack).
 *   - Monster AI (adjacent → attack; otherwise → random move).
 *   - Teleportation.
 *   - Random spawning of new treasures / monsters each N turns.
 *   - Pretty-printing the board.
 */
public class Map {

    // ── Grid ────────────────────────────────────────────────────────────────
    private final int width;
    private final int height;
    private final MapObj[][] grid;

    // ── Actors ──────────────────────────────────────────────────────────────
    private Character character;
    private final List<Monster> monsters = new ArrayList<>();

    // ── Generation constants ─────────────────────────────────────────────────
    private static final float TREASURE_DENSITY    = 0.25f; // 25 % of cells
    private static final int   OBSTACLE_COUNT      = 4;
    private static final int   INITIAL_MONSTER_COUNT = 3;
    private static final Random RNG = new Random();

    // ── Constructors ─────────────────────────────────────────────────────────

    public Map() {
        this.width  = RNG.nextInt(5) + 8;   // 8–12
        this.height = RNG.nextInt(5) + 8;   // 8–12
        this.grid   = new MapObj[height][width];
    }

    public Map(int width, int height) {
        this.width  = width;
        this.height = height;
        this.grid   = new MapObj[height][width];
    }

    // ── Initialisation ───────────────────────────────────────────────────────

    public void init() {
        generateTreasures();
        placeObstacles();
        placeMonsters();
        placeCharacter();
    }

    private void generateTreasures() {
        for (int r = 0; r < height; r++) {
            for (int c = 0; c < width; c++) {
                if (RNG.nextFloat() < TREASURE_DENSITY) {
                    grid[r][c] = randomTreasure();
                }
            }
        }
    }

    /** Selects a Treasure type using the cumulative generation-rate distribution. */
    private Treasure randomTreasure() {
        float r = RNG.nextFloat();
        if (r < 0.10f) return new SuperStar();
        if (r < 0.35f) return new Poison();
        if (r < 0.55f) return new AccPotion();
        if (r < 0.70f) return new HealingPotion();
        if (r < 0.80f) return new DevilFruit();
        if (r < 0.90f) return new KingRock();
        return new Dokodemo();
    }

    private void placeObstacles() {
        for (int i = 0; i < OBSTACLE_COUNT; i++) {
            int[] p = randomEmptyCell();
            if (p != null) grid[p[0]][p[1]] = new Obstacle();
        }
    }

    private void placeMonsters() {
        for (int i = 0; i < INITIAL_MONSTER_COUNT; i++) {
            int[] p = randomEmptyCell();
            if (p != null) {
                Monster m = new Monster();
                m.setPosition(p[0], p[1]);
                grid[p[0]][p[1]] = m;
                monsters.add(m);
            }
        }
    }

    private void placeCharacter() {
        int[] p = randomEmptyCell();
        if (p != null) {
            character = new Character();
            character.setPosition(p[0], p[1]);
            grid[p[0]][p[1]] = character;
        }
    }

    /** Returns the coordinates of a random empty cell, or null if none found. */
    private int[] randomEmptyCell() {
        for (int attempt = 0; attempt < 200; attempt++) {
            int r = RNG.nextInt(height);
            int c = RNG.nextInt(width);
            if (grid[r][c] == null) return new int[]{r, c};
        }
        return null;
    }

    // ── Movement ─────────────────────────────────────────────────────────────

    /**
     * Moves {@code role} one step in {@code dir}.
     *
     * Touch semantics (per requirements):
     *   - Empty cell   → role moves there.
     *   - Treasure      → treasure disappears + onTouch() fires; role stays.
     *   - Obstacle      → role stays (blocked).
     *   - Other Role   → role stays (blocked).
     *   - Out-of-bounds → role stays.
     *
     * @return true if the role actually moved to a new cell.
     */
    public boolean moveRole(Role role, Direction dir) {
        int newR = role.getRow() + dir.getDRow();
        int newC = role.getCol() + dir.getDCol();

        if (newR < 0 || newR >= height || newC < 0 || newC >= width) {
            System.out.println("  → Blocked: map boundary.");
            return false;
        }

        MapObj target = grid[newR][newC];

        if (target == null) {
            // Free cell – move
            grid[role.getRow()][role.getCol()] = null;
            role.setPosition(newR, newC);
            grid[newR][newC] = role;
            return true;

        } else if (target instanceof Treasure) {
            // Treasure touch
            Treasure t = (Treasure) target;
            System.out.println("  → Touched " + t.getClass().getSimpleName() + "!");
            t.onTouch(role);
            grid[newR][newC] = null;   // treasure consumed
            System.out.println("  → State is now: " + role.getState().getName());
            return false;

        } else if (target instanceof Obstacle) {
            System.out.println("  → Blocked by obstacle.");
            return false;

        } else {
            // Another Role
            System.out.println("  → Blocked by " + (target instanceof Monster ? "monster" : "character") + ".");
            return false;
        }
    }

    // ── Attack ───────────────────────────────────────────────────────────────

    /**
     * Line attack in the character's current facing direction.
     * Kills every Monster in the line; stops at the first Obstacle.
     * Cannot kill the Character itself.
     */
    public void attackLine(Character ch) {
        Direction dir = ch.getFacing();
        int r = ch.getRow() + dir.getDRow();
        int c = ch.getCol() + dir.getDCol();
        boolean hit = false;

        while (r >= 0 && r < height && c >= 0 && c < width) {
            MapObj obj = grid[r][c];
            if (obj instanceof Obstacle) {
                System.out.println("  → Attack blocked by obstacle at (" + r + "," + c + ").");
                break;
            }
            if (obj instanceof Monster) {
                Monster m = (Monster) obj;
                m.receiveAttack(Integer.MAX_VALUE / 2);   // guaranteed kill (HP=1)
                grid[r][c] = null;
                monsters.remove(m);
                System.out.println("  → Monster slain at (" + r + "," + c + ")!");
                hit = true;
            }
            r += dir.getDRow();
            c += dir.getDCol();
        }
        if (!hit) System.out.println("  → Attack hit nothing.");
    }

    /**
     * Full-map attack (Erupting state): every Monster on the board takes
     * {@code damage} points.  Dead monsters are removed.
     */
    public void attackAll(int damage) {
        System.out.println("  *** ERUPTING: Full-map attack for " + damage + " dmg! ***");
        List<Monster> dead = new ArrayList<>();
        for (Monster m : monsters) {
            m.receiveAttack(damage);
            System.out.println("  → Monster at (" + m.getRow() + "," + m.getCol() + ") takes " + damage + " dmg!");
            if (!m.isAlive()) {
                grid[m.getRow()][m.getCol()] = null;
                dead.add(m);
                System.out.println("    Slain!");
            }
        }
        monsters.removeAll(dead);
    }

    // ── Monster AI ───────────────────────────────────────────────────────────

    /**
     * Executes one monster's turn:
     *   - If the character is adjacent (Manhattan distance == 1) → attack for 50 dmg.
     *   - Otherwise → move one step in a random direction.
     */
    public void monsterAct(Monster m) {
        if (!m.isAlive() || character == null || !character.isAlive()) return;

        if (isAdjacent(m, character)) {
            System.out.println("  Monster at (" + m.getRow() + "," + m.getCol() + ") attacks Character!");
            character.receiveAttack(m.getAttackDamage());
            System.out.println("  Character HP: " + character.getHp() + "/" + character.getMaxHp()
                    + "  State: " + character.getState().getName());
        } else {
            Direction[] dirs = Direction.values();
            Direction dir = dirs[RNG.nextInt(dirs.length)];
            boolean moved = moveRole(m, dir);
            System.out.println("  Monster (" + m.getRow() + "," + m.getCol() + ") tries to move "
                    + dir.name() + (moved ? " – moved." : " – blocked."));
        }
    }

    public boolean isAdjacent(Role a, Role b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol()) == 1;
    }

    // ── Teleport ─────────────────────────────────────────────────────────────

    /** Moves {@code role} to a random empty cell on the map. */
    public void teleportRole(Role role) {
        int[] p = randomEmptyCell();
        if (p == null) {
            System.out.println("  → No empty cell found for teleport!");
            return;
        }
        grid[role.getRow()][role.getCol()] = null;
        role.setPosition(p[0], p[1]);
        grid[p[0]][p[1]] = role;
        System.out.println("  → " + (role instanceof Character ? "Character" : "Monster")
                + " teleported to (" + p[0] + "," + p[1] + ")!");
    }

    // ── Spawning ─────────────────────────────────────────────────────────────

    public void spawnTreasure() {
        int[] p = randomEmptyCell();
        if (p != null) grid[p[0]][p[1]] = randomTreasure();
    }

    public void spawnMonster() {
        int[] p = randomEmptyCell();
        if (p != null) {
            Monster m = new Monster();
            m.setPosition(p[0], p[1]);
            grid[p[0]][p[1]] = m;
            monsters.add(m);
            System.out.println("  [Spawn] A new monster appeared at (" + p[0] + "," + p[1] + ")!");
        }
    }

    // ── Display ──────────────────────────────────────────────────────────────

    public void printMap() {
        // Top border
        System.out.print("┌");
        for (int c = 0; c < width; c++) System.out.print("───");
        System.out.println("┐");

        for (int r = 0; r < height; r++) {
            System.out.print("│");
            for (int c = 0; c < width; c++) System.out.print(cellStr(r, c));
            System.out.println("│");
        }

        // Bottom border
        System.out.print("└");
        for (int c = 0; c < width; c++) System.out.print("───");
        System.out.println("┘");

        // Legend
        System.out.println("Legend: [↑→↓←]=Character  [M]=Monster  [x]=Treasure  [□]=Obstacle  [·]=Empty");
    }

    private String cellStr(int r, int c) {
        MapObj obj = grid[r][c];
        if (obj == null)               return " · ";
        if (obj instanceof Character)  return "[" + obj.getSymbol() + "]";
        if (obj instanceof Monster)    return "[M]";
        if (obj instanceof Obstacle)   return "[□]";
        if (obj instanceof Treasure)   return "[x]";
        return " ? ";
    }

    // ── Accessors ────────────────────────────────────────────────────────────

    public Character    getCharacter()    { return character; }
    public List<Monster> getMonsters()   { return Collections.unmodifiableList(monsters); }
    public boolean      allMonstersDead(){ return monsters.isEmpty(); }
    public int          getWidth()       { return width; }
    public int          getHeight()      { return height; }
}
