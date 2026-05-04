package ch3_complexBV.Gym5_RPG.src.player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ch3_complexBV.Gym5_RPG.src.Role;
import ch3_complexBV.Gym5_RPG.src.skill.Skill;

public abstract class Player {

    public Skill chooseAction(List<Skill> skills) {
        printActionMenu(skills);
        int idx = pickIndex(skills.size());
        return skills.get(idx);
    }

    public List<Role> chooseTargets(int targetAmount, List<Role> candidates) {
        if (targetAmount == 0) return List.of();
        if (targetAmount < 0 || candidates.size() <= targetAmount) {
            return new ArrayList<>(candidates);
        }
        return pickTargets(targetAmount, candidates);
    }

    private void printActionMenu(List<Skill> skills) {
        String menu = IntStream.range(0, skills.size())
            .mapToObj(i -> "(" + i + ") " + skills.get(i).getName())
            .collect(Collectors.joining(" "));
        System.out.println("選擇行動：" + menu);
    }

    protected void printTargetMenu(int targetAmount, List<Role> candidates) {
        String menu = IntStream.range(0, candidates.size())
            .mapToObj(i -> "(" + i + ") " + candidates.get(i).display())
            .collect(Collectors.joining(" "));
        System.out.println("選擇 " + targetAmount + " 位目標: " + menu);
    }

    protected abstract int pickIndex(int count);
    protected abstract List<Role> pickTargets(int targetAmount, List<Role> candidates);
}
