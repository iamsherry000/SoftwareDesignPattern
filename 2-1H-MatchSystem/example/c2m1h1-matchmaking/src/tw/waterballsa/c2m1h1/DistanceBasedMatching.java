package tw.waterballsa.c2m1h1;

import java.util.Comparator;
import java.util.List;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.toList;

public class DistanceBasedMatching implements MatchingStrategy {
	@Override
	public List<Individual> match(Individual individual, List<Individual> candidates) {
		Comparator<Individual> comparator = comparing(c -> individual.getCoord().distance(c.getCoord()));
		return candidates.stream()
				.sorted(comparator.thenComparingInt(Individual::getId))
				.collect(toList());
	}
}
