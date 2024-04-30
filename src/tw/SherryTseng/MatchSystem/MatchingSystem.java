package src.tw.SherryTseng.MatchSystem;

import java.util.List;
import java.util.stream.Collectors;

public class MatchingSystem {
    private final List<Individual> individuals;
	private MatchingStrategy matchingStrategy;

    public MatchingSystem(List<Individual> individuals, MatchingStrategy matchingStrategy){
        this.individuals = individuals;
		this.matchingStrategy = matchingStrategy;
    }

    public void match() {
        for (Individual individual : individuals) {
            List<Individual> candidates = individuals.stream()
                    .filter(i -> i != individual)
                    .collect(Collectors.toList());
            List<Individual> result = matchingStrategy.match(individual, candidates);
            individual.match(result.get(0));
        }
    }
}
