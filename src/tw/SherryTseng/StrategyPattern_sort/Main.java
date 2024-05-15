package src.tw.SherryTseng.StrategyPattern_sort;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int [] array = {1,3,5,7,9,2,4,6,8,10};

        System.out.println("原始數組：");
        printArray(array);

        SortContext context = new SortContext();

        int [] bubbleSortArray = Arrays.copyOf(array, array.length);
        context.setStrategy(new BubbleSort());
        context.performSort(bubbleSortArray);
        System.out.println("BubbleSort：");
        printArray(bubbleSortArray);

        context.setStrategy(new QuickSort());
        context.performSort(array);
        System.out.println("QuickSort：");
        printArray(array);
    }

    private static void printArray(int[] array) {
        for (int num : array) {
            System.out.print(num + " ");
        }
        System.out.println();
    }
}
