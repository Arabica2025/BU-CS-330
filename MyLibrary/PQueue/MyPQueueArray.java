package MyLibrary.PQueue;
// package declaration issue
import java.util.function.Consumer;
import java.util.function.ToIntBiFunction;
import java.util.function.BiConsumer;

/*
    Array-based Priority Queue that supports the operations of:
        1. enque
        2. deque

    Underlying data structure: array-based heap.

    Priority Queue: First In First Out
        - Highest Priority comes first
        - Lowest Priority comes last

    Max Heap
 */

public class MyPQueueArray<T> extends MyPQueueBase<T> {
    private int size;
    private T[] array;
    private ToIntBiFunction<T,T> cmp;


    public MyPQueueArray(int size, ToIntBiFunction<T,T> cmp){
        this.size = 0;
        this.array = (T[]) new Object[size];
        this.cmp = cmp;
    }

    public int size(){
        return size;
    }

    public boolean isFull(){
        return size == array.length;
    }
    public boolean isEmpty(){
        return size == 0;
    }


    public T top$raw(){
        return array[0];
    }
    public T top$opt(){
        return isEmpty() ? null : top$raw();
    }
    public T top$exn(){
        if(isEmpty()){
            throw new MyPQueueEmptyExn();
        }
        return top$raw();
    }


    private void exchange(int itm1idx, int itm2idx){
        T temp = array[itm1idx];
        array[itm1idx] = array[itm2idx];
        array[itm2idx] = temp;
    }

    public T deque$raw(){
        T rmd = array[0];// save the top element to be rmved
        // case: we have only one element in the heap <- checking this is inefficient I guess on average
        // if (size() <= 1){
        //     array[rootidx] = null;
        //     size -= 1;
        //     return rmd;
        // }
        size--;
        if (size > 0){
            array[0] = array[size];
            array[size] = null;
            siftDwn(0);
        } else {
            array[0] = null;
        }
        return rmd;
    }

    public T deque$opt(){
        return isEmpty() ? null : deque$raw();
    }

    public T deque$exn(){
        if (isEmpty()) throw new MyPQueueEmptyExn();
        return deque$raw();
    }


    private void siftDwn(int current){

        // until we do not have a left child
        // if we don't have a left child,
        // then we are already at the bottom level so no need to sift down any longer
        while(true){ 
            int lchild = (2 * current) + 1;
            if (lchild >= size) break; // no children -> no sifting down

            int rchild = lchild+1;
            // if the parent is less than the left child
            int larger = (rchild < size && cmp.applyAsInt(array[rchild], array[lchild])> 0)
                        ? rchild : lchild;

            if(cmp.applyAsInt(array[current], array[larger]) > 0){
                break;
            }
            exchange(current, larger);
            current = larger;
        }
    }
    

    public void enque$raw(T itm){
        // assign the itm at last idx of current size and sift up
        array[size] = itm; 
        size++; //increment size by 1 as we add an element

        int i = size() -1; // index of input
        siftUp(i);
    }

    public boolean enque$opt(T itm){
        if(isFull()) return false;
        enque$raw(itm);
        return true;
    }
    
    public void enque$exn(T itm){
        if(isFull()) throw new MyPQueueFullExn();
        enque$raw(itm);
    }

    private void siftUp(int current){
        while(current > 0){
            int parent = (current - 1) / 2;

            if (cmp.applyAsInt(array[current], array[parent]) > 0){
                exchange(current, parent);
                current = parent;
            } else {
                break;
            }
        }
    }

    /*
    Calling MyPQueueArray(Maxheap)
    1. Integer
    MyPQueueArray<Integer> pq = new MyPQueueArray<>(16, Integer::compare);

    2. String
    MyPQueueArray<String> pq = new MyPQueueArray<>(16, String::compareTo);

    3. Any Comparable
    MyPQueueArray<SomeType> pq = new MyPQueueArray<>(16, (a, b) -> a.compareTo(b));

    4. custom order (custom cmp)
    MyPQueueArray<Integer> minHeap = new MyPQueueArray<>(16, (a, b) -> Integer.compare(b, a));

    */

}