package src.tw.SherryTseng.MatchSystem;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HabitBasedMatching implements MatchingStrategy{
    private Map<Individual, Individual> matches = new HashMap<>();
	
	@Override
	public List<Individual> match(Individual individual, List<Individual> candidates) {
		//Comparator<Individual> comparator = comparingInt(k -> intersectionSize(k.getHabits(), individual.getHabits()));
		Comparator<Individual> comparator = comparingInt((Individual k) -> Collections.disjoint(k.getHabits(), individual.getHabits()) ? 1 : 0).reversed();

		return candidates.stream()
				.sorted(comparator.reversed())
				.collect(toList());
	}

	// 新增一個方法，用於取得指定個體的匹配對象
    public Individual getMatchedIndividual(Individual individual) {
        return matches.get(individual);
    }
}

