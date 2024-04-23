package tw.waterballsa.c2m1h1;

import java.util.List;

public interface MatchingStrategy {
	List<Individual> match(Individual individual, List<Individual> candidates);
}
