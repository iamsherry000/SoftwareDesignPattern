package src.tw.SherryTseng.StrategyPattern_sort;

public class QuickSort implements SortingStrategy {
    @Override
    public void sort(int[] array){
        quickSort(array, 0, array.length - 1);
    }

    private void quickSort(int[] array, int low, int high) {
        if (low < high) {
            // 選擇pivot，進行分割
            int pivotIndex = partition(array, low, high);

            // 遞歸地對兩個子陣列進行排序
            quickSort(array, low, pivotIndex - 1);
            quickSort(array, pivotIndex + 1, high);
        }
    }

    private int partition(int[] array, int low, int high) {
        int pivot = array[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (array[j] < pivot) {
                i++;

                // 交換元素
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }
        }

        // 將pivot放到正確的位置上
        int temp = array[i + 1];
        array[i + 1] = array[high];
        array[high] = temp;

        return i + 1;
    }
}
