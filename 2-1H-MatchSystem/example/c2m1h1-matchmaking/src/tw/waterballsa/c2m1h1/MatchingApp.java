package tw.waterballsa.c2m1h1;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MatchingApp {
	private final List<Individual> individuals;
	private MatchingStrategy matchingStrategy;

	public MatchingApp(List<Individual> individuals, MatchingStrategy matchingStrategy) {
		this.individuals = individuals;
		this.matchingStrategy = matchingStrategy;
	}

	public void match() {
		for (Individual individual : individuals) {
			List<Individual> candidates = individuals.stream()
					.filter(i -> i != individual).collect(toList());
			List<Individual> result = matchingStrategy.match(individual, candidates);
			individual.match(result.get(0));
		}
	}

	public void setMatchingStrategy(MatchingStrategy matchingStrategy) {
		this.matchingStrategy = matchingStrategy;
	}
}
