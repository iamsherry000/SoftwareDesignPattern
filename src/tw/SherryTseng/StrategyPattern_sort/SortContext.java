package src.tw.SherryTseng.StrategyPattern_sort;

public class SortContext {
    private SortingStrategy strategy;

    public void setStrategy(SortingStrategy strategy){
        this.strategy = strategy;
    }

    public void performSort(int[] array) {
        strategy.sort(array);
    }
}
