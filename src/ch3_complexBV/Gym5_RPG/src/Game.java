package ch3_complexBV.Gym5_RPG.src;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import ch3_complexBV.Gym5_RPG.src.player.AIPlayer;
import ch3_complexBV.Gym5_RPG.src.player.HumanPlayer;
import ch3_complexBV.Gym5_RPG.src.player.Player;
import ch3_complexBV.Gym5_RPG.src.skill.BasicAttack;
import ch3_complexBV.Gym5_RPG.src.skill.Skill;
import ch3_complexBV.Gym5_RPG.src.skill.SkillFactory;

public class Game {
    private final Scanner scanner;
    private final Map<Role, Player> controllers = new HashMap<>();
    private Troop troop1;
    private Troop troop2;
    private Role hero;

    public Game(Scanner scanner) {
        this.scanner = scanner;
    }

    public void start() {
        parseTroops();
        setupControllers();
        runBattle();
        announceResult();
    }

    // ---------- input parsing ----------

    private void parseTroops() {
    troop1 = parseTroop();
        troop2 = parseTroop();
        hero = troop1.getRoles().get(0);
    }

    private Troop parseTroop() {
        String header = scanner.nextLine().trim();            // e.g. #軍隊-1-開始
        int id = Integer.parseInt(header.split("-")[1]);
        Troop troop = new Troop(id);
        String line;
        while (!(line = scanner.nextLine().trim()).startsWith("#軍隊-")) {
            troop.addRole(parseRole(line));
        }
        return troop;
    }

    private Role parseRole(String line) {
        String[] tokens = line.split("\\s+");
        Role role = new Role(tokens[0],
                             Integer.parseInt(tokens[1]),
                             Integer.parseInt(tokens[2]),
                             Integer.parseInt(tokens[3]));
        role.addSkill(new BasicAttack());
        for (int i = 4; i < tokens.length; i++) {
            role.addSkill(SkillFactory.create(tokens[i]));
        }
        return role;
    }

    // ---------- controllers ----------

    private void setupControllers() {
        for (Role r : troop1.getRoles()) {
            controllers.put(r, (r == hero) ? new HumanPlayer(scanner) : new AIPlayer());
        }
        for (Role r : troop2.getRoles()) {
            controllers.put(r, new AIPlayer());
        }
    }

    // ---------- battle loop ----------

    private void runBattle() {
        while (!isOver()) {
            playTroopRound(troop1);
            if (isOver()) break;
            playTroopRound(troop2);
        }
    }

    private void playTroopRound(Troop troop) {
        for (int i = 0; i < troop.getRoles().size(); i++) {
            if (isOver()) return;
            Role role = troop.getRoles().get(i);
            if (role.isAlive()) playTurn(role);
        }
    }

    private void playTurn(Role role) {
        printRoleHeader(role);                           // (P)
        role.getState().onTurnEffect(role);              // (E)
        if (role.isAlive()) {
            if (role.getState().canAct()) {              // (S1-S3)
                Player player = controllers.computeIfAbsent(role, r -> new AIPlayer());
                Skill skill = chooseAffordableAction(role, player);
                Troop ally = (role.getTroopId() == troop1.getId()) ? troop1 : troop2;
                Troop enemy = (ally == troop1) ? troop2 : troop1;
                List<Role> candidates = skill.targetPool(role, ally, enemy);
                List<Role> targets = player.chooseTargets(skill.getTargetAmount(), candidates);
                skill.execute(role, targets);
            }
            role.setState(role.getState().nextState()); // tick duration
        }
    }

    private Skill chooseAffordableAction(Role role, Player player) {
        while (true) {
            Skill skill = player.chooseAction(role.getSkills());
            if (role.getMp() >= skill.getMpCost()) return skill;
            System.out.println("你缺乏 MP，不能進行此行動。");
        }
    }

    private void printRoleHeader(Role r) {
        System.out.printf("輪到 %s (HP: %d, MP: %d, STR: %d, State: %s)。%n",
            r.display(), r.getHp(), r.getMp(), r.getStrength(), r.getState().getName());
    }

    // ---------- end / result ----------

    private boolean isOver() {
        return !hero.isAlive() || troop1.isAnnihilated() || troop2.isAnnihilated();
    }

    private void announceResult() {
        if (hero.isAlive() && troop2.isAnnihilated()) {
            System.out.println("你獲勝了！");
        } else {
            System.out.println("你失敗了！");
        }
    }
}
