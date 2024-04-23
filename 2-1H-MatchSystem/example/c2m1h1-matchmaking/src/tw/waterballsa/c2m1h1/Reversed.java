package tw.waterballsa.c2m1h1;

import java.util.List;

import static java.util.Collections.reverse;

public class Reversed implements MatchingStrategy {
    private final MatchingStrategy matchingStrategy;

    public static Reversed reversed(MatchingStrategy matchingStrategy) {
        return new Reversed(matchingStrategy);
    }

    public Reversed(MatchingStrategy matchingStrategy) {
        this.matchingStrategy = matchingStrategy;
    }

    @Override
    public List<Individual> match(Individual individual, List<Individual> candidates) {
        List<Individual> result = matchingStrategy.match(individual, candidates);
        reverse(result);
        return result;
    }
}
