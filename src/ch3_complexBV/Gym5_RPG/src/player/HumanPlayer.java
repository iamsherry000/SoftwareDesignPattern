package ch3_complexBV.Gym5_RPG.src.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import ch3_complexBV.Gym5_RPG.src.Role;

public class HumanPlayer extends Player {
    private final Scanner scanner;

    public HumanPlayer(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    protected int pickIndex(int count) {
        return Integer.parseInt(scanner.nextLine().trim());
    }

    @Override
    protected List<Role> pickTargets(int targetAmount, List<Role> candidates) {
        printTargetMenu(targetAmount, candidates);
        String line = scanner.nextLine().trim();
        int[] indices = Arrays.stream(line.split(","))
            .map(String::trim)
            .mapToInt(Integer::parseInt)
            .toArray();
        List<Role> picked = new ArrayList<>();
        for (int i : indices) picked.add(candidates.get(i));
        return picked;
    }
}
