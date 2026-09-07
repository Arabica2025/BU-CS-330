package MyLibrary.Sorting;
import java.util.function.ToIntBiFunction;

public class SrtArr implements SrtArrInterface{
    // change: forgot to add constructor
    int[] arr;
    public SrtArr(int[] arr){
        this.arr = arr;
    }

    /* Swaping Utility for int[] O(1)*/
    private static void swap(int[] arr, int a, int b){
        int temp = arr[a];
        arr[a] = arr[b];
        arr[b] = temp;
    }

    /* indexMin - find index of the minimum element in the
    subarray from arr[start] to the end of the array
    
    -> SelectionSort uses this
    */
    private static int indexMin(int[] arr, int start){
        int indexMin = start;
        for (int i = start + 1; i < arr.length; i++){
            indexMin = arr[i] < arr[indexMin] ? i : indexMin;
        }
        return indexMin;
    }


    @Override
    public void selectionSort(int[] arr) {
        // Selection Sort int[] version
        for (int i = 0; i < arr.length - 1; i++){
            int j = indexMin(arr, i);
            swap(arr, i, j);
        }
    }

    @Override
    public void insertionSort(int[] arr) {
        // Insertion Sort int[] version
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i-1]) {
                // Save a copy of the element to be inserted.
                int toInsert = arr[i];
                
                // Shift right to make room for element.
                int j = i;
                do {
                    arr[j] = arr[j - 1];
                    j = j - 1;
                } while (j > 0 && toInsert < arr[j-1]);
                
                // Put the element in place.
                arr[j] = toInsert;
            }
        }
    }

    @Override
    public void bubbleSort(int[] arr) {
        // Bubble Sort int[] version
        for (int i = arr.length - 1; i > 0; i--) {
            for (int j = 0; j < i; j++) {
                if (arr[j] > arr[j+1]) {
                    swap(arr, j, j+1);
                }
            }
        }
    }

    /* partition - helper method for qSort */
    private static int partition(int[] arr, int first, int last) {
        int pivot = arr[(first + last)/2];
        int i = first - 1;  // index going left to right
        int j = last + 1;   // index going right to left
        
        do {
            // moving from left to right, find an element >= the pivot
            do {
                i++;
            } while (arr[i] < pivot);
            
            // moving from right to left, find an element <= the pivot
            do {
                j--;
            } while (arr[j] > pivot); 
            
            // If the indices still haven't met or crossed,
            // swap the elements so that they end up in the correct subarray.
            // Otherwise, the partition is complete and we return j.
            if (i < j) {
                swap(arr, i, j);
            }

        } while ( i < j );

	return(j);
    }
    /* qSort - recursive method that does the work for quickSort */
    private static void qSort(int[] arr, int first, int last) {
        int split = partition(arr, first, last);
        
        if (first < split) {
            qSort(arr, first, split);      // left subarray
        }
        if (last > split + 1) {
            qSort(arr, split + 1, last);   // right subarray
        }
    }


    /* merge - helper method for mergesort */
    private static void merge(int[] arr, int[] temp, 
      int leftStart, int leftEnd, int rightStart, int rightEnd)
    {
        int i = leftStart;    // index into left subarray
        int j = rightStart;   // index into right subarray
        int k = leftStart;    // index into temp
        
        while (i <= leftEnd && j <= rightEnd) {
            if (arr[i] < arr[j]) {
                temp[k] = arr[i];
                i++; k++;
            } else {
                temp[k] = arr[j];
                j++; k++;
            }
        }
        
        while (i <= leftEnd) {
            temp[k] = arr[i];
            i++; k++;
        }
        while (j <= rightEnd) {
            temp[k] = arr[j];
            j++; k++;
        }
        
        for (i = leftStart; i <= rightEnd; i++) {
            arr[i] = temp[i];
        }
    }

    /** mSort - recursive method for mergesort */
    private static void mSort(int[] arr, int[] temp, int start, int end) {
        if (start >= end) {
            return;
        }
        
        int middle = (start + end)/2;
        mSort(arr, temp, start, middle);
        mSort(arr, temp, middle + 1, end);
        merge(arr, temp, start, middle, middle + 1, end);
    }
    @Override
    public void quickSort(int[] arr) {
        qSort(arr, 0, arr.length - 1);
    }


    @Override
    public void mergeSort(int[] arr) {
        int[] temp = new int[arr.length];
        mSort(arr, temp, 0, arr.length - 1);
    }
}
