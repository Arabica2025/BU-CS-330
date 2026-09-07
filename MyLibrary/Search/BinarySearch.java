package MyLibrary.Search;

public class BinarySearch<T> {
    // changes: forgot to add constructor
    T[] array;
    T key;
        public BinarySearch(T[] array, T key) {
        this.array = array;
        this.key = key;
    }
        public static <T extends Comparable<T> > int indexOf(T[] a, T key) {
	// a recursive implementation of 'indexOf' 
        if (key == null || a == null || a.length == 0){
            return -1;
        }
        return helperIndexOf(a, key, 0, a.length-1);
    }
    private static <T extends Comparable<T> > int helperIndexOf(T[] a, T key, int lower, int upper){
        if (lower > upper) {
            return -1;
        }
        int mid = lower + (upper - lower) / 2;
        int sign = key.compareTo(a[mid]);

        if (key == a[mid]){
            return mid;
        } else if (sign > 0){
            return helperIndexOf(a, key, mid + 1, upper); // if key is on the right side of mid, it means we don't include mid in the parameter
        } else {
            return helperIndexOf(a, key, lower, mid -1); // if key is on the left side of mid, it meas we don't include mid in the parameter, just like when key is on the right side
        }
    }
}
