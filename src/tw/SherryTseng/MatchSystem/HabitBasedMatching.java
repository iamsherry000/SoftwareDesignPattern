package src.tw.SherryTseng.MatchSystem;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;

import java.util.Comparator;
import java.util.List;

public class HabitBasedMatching {
    @Override
	public List<Individual> match(Individual individual, List<Individual> candidates) {
		Comparator<Individual> comparator = comparingInt(k -> intersectionSize(k.getHabits(), individual.getHabits()));
		return candidates.stream()
				.sorted(comparator.reversed())
				.collect(toList());
	}
}
