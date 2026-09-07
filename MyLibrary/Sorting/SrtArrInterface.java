package MyLibrary.Sorting;

interface SrtArrInterface{
    /* 1. Sorting with arrays as argument */
    void selectionSort(int[] arr);
    void insertionSort(int[] arr);
    void bubbleSort(int[] arr);
    void quickSort(int[] arr);
    void mergeSort(int[] arr);
    // Utility function to print arrays
    // SrtInterface.printArr(int[] arr) to call the static method
    static void printArr(int[] arr){
        System.out.print("[");
        for(int i : arr) System.out.print(i + " ");
        System.out.print("]");
    }
    /*
    Time & Space Complexity
    Selection Sort: All time O(n^2) time, O(1) space
    Insertion Sort: Best: O(n) time, avg, worst: O(n^2) time, O(1) space
    Bubble Sort: All time O(n^2) time, O(1) space
    Quick Sort: Worst: O(n^2) times, avg, best: O(n log n) time, O(log n) space (average), O(n^2) space (worst)
    Merge Sort: O(n log n) time, O(n) space
    */
}
