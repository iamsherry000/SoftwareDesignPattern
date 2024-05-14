package src.tw.SherryTseng.StrategyPattern_sort;

public class Main {
    public static void main(String[] args) {
        int [] array = {1,3,5,7,9,2,4,6,8,10};

        SortContext context = new SortContext();

        context.setStrategy(new BubbleSort());
        context.performSort(array);

        context.setStrategy(new QuickSort());
        context.performSort(array);
    }
}
