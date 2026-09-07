package MyLibrary.MyHeap;

public interface MyHeap<T> {
    T peek(); // look at root
    int size(); // size of the heap tree

    boolean isEmpty(); // check if root == null
    boolean isFull(); // always false

    void insert$new(T value); // assume 
}
