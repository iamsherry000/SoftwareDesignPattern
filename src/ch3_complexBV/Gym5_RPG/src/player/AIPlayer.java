package ch3_complexBV.Gym5_RPG.src.player;

import java.util.ArrayList;
import java.util.List;

import ch3_complexBV.Gym5_RPG.src.Role;

public class AIPlayer extends Player {
    private int seed = 0;

    @Override
    protected int pickIndex(int count) {
        int idx = seed % count;
        seed++;
        return idx;
    }

    @Override
    protected List<Role> pickTargets(int targetAmount, List<Role> candidates) {
        // 依需求範例：seed=2, candidates=(A,B,C), pick 2 → (C, A), 最後 seed=3
        // 意即「同一次多目標選擇」共用同一個 seed，結束後才 +1
        List<Role> remaining = new ArrayList<>(candidates);
        List<Role> picked = new ArrayList<>();
        for (int i = 0; i < targetAmount; i++) {
            int idx = seed % remaining.size();
            picked.add(remaining.remove(idx));
        }
        seed++;
        return picked;
    }
}
