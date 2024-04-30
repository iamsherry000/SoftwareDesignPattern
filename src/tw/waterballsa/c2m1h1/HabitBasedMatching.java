package src.tw.waterballsa.c2m1h1;

import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.comparing;
import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static src.tw.waterballsa.c2m1h1.Utils.intersectionSize;

public class HabitBasedMatching implements MatchingStrategy {

	@Override
	public List<Individual> match(Individual individual, List<Individual> candidates) {
		Comparator<Individual> comparator = comparingInt(k -> intersectionSize(k.getHabits(), individual.getHabits()));
		return candidates.stream()
				.sorted(comparator.reversed())
				.collect(toList());
	}
}
