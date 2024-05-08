package src.tw.SherryTseng.MatchSystem;

import java.util.List;
import java.util.stream.Collectors;

public class MatchingSystem {
    private final List<Individual> individualsList;
	private MatchingStrategy matchingStrategy;

    public MatchingSystem(List<Individual> individualsList, MatchingStrategy matchingStrategy){
        this.individualsList = individualsList;
		this.matchingStrategy = matchingStrategy;
    }

    public void match() {
        for (Individual individual : individualsList) {
            List<Individual> candidates = individualsList.stream()
                    .filter(i -> i != individual) 
                    .collect(Collectors.toList()); //創建列表包含除了自己以外的所有其他個體
            List<Individual> result = matchingStrategy.match(individual, candidates);
            individual.match(result.get(0));
        }
    }

    public void setMatchingStrategy(MatchingStrategy matchingStrategy) {
		this.matchingStrategy = matchingStrategy;
	}
}
