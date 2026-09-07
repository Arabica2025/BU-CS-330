package MyLibrary.Sorting;

import java.util.function.ToIntBiFunction;

import MyLibrary.FnList.*;
interface SrtFnListInterface<T> {

    /* 2. Sorting with FnList as argument */

    FnList<T> selectionSort(FnList<T> xs);
    FnList<T> selectionSort(FnList<T> xs, ToIntBiFunction<T,T> cmp);
    FnList<T> insertionSort(FnList<T> xs);
    FnList<T> bubbleSort(FnList<T> xs);
    FnList<T> bubbleSort(FnList<T> xs, ToIntBiFunction<T,T> cmp);
    FnList<T> quickSort(FnList<T> xs);
    FnList<T> mergeSort(FnList<T> xs);
}
