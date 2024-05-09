package src.tw.SherryTseng.MatchSystem;

import java.util.Comparator;
import java.util.List;
import static java.util.stream.Collectors.toList;

import java.util.Collections;

public class DistanceBasedMatching implements MatchingStrategy{
    @Override
    public List<Individual> match(Individual individual, List<Individual> candidates) {
        //Comparator<Individual> comparator= Comparator.comparing(individual.getCoord(), (c1, c2) -> (Coordinate)c1);
        Comparator<Individual> comparator = comparing(c -> individual.getCoord().distance(c.getCoord()));
        
        return candidates.stream()
				.sorted(comparator.thenComparingInt(Individual::getId))
				.collect(toList());
    }
}
