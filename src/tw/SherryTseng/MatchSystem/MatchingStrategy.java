package src.tw.SherryTseng.MatchSystem;

import java.util.List;

public interface MatchingStrategy {
    List<Individual> match(Individual individual, List<Individual> candidates);
}
