package MyLibrary.Sorting;
import MyLibrary.FnList.FnList;
import MyLibrary.FnList.FnListSUtil;
import java.util.function.ToIntBiFunction;
import MyLibrary.FnTuple.FnTupl2;

public class SrtFnList<T extends Comparable<T>> implements SrtFnListInterface<T>{

    // public <T extends Comparable<T>> FnList<Integer> selectionSort(FnList<T> xs) {
    //     return FnListSUtil.selectionSort(xs, (x1,x2) -> x1.compareTo(x2));
    // }
    public FnList<T> selectionSort(FnList<T> xs) {
        return selectionSort(xs, (x1, x2) -> x1.compareTo(x2));
    }

    public FnList<T> selectionSort(FnList<T> xs, ToIntBiFunction<T,T> cmp){
        if (xs.nilq()){
            return xs;
        }
        FnTupl2<T, FnList<T>> min = extractMin$raw(xs, cmp);
        return FnListSUtil.cons(min.sub0, selectionSort(min.sub1, cmp));
    }

    private static<T> FnTupl2<T, FnList<T>> extractMin$raw(FnList<T> xs,ToIntBiFunction<T,T> cmp){
        FnTupl2<T, FnList<T>> accumulate = FnListSUtil.folditm(
            xs.tl(), 
            new FnTupl2<T, FnList<T>>(xs.hd(), FnListSUtil.<T>nil()),
            (a, x) -> (cmp.applyAsInt(x, a.sub0)< 0)
            ? new FnTupl2<T,FnList<T>>(x, FnListSUtil.cons(x,a.sub1))
            : new FnTupl2<T,FnList<T>>(a.sub0, FnListSUtil.cons(x, a.sub1))
        );
        return new FnTupl2<T,FnList<T>>(accumulate.sub0, FnListSUtil.reverse(accumulate.sub1));
    }
    //     return FnListSUtil.folditm(
    //         xs.tl(), // iteration from second element
    //         xs.hd(), // initialize the min to the first element
    //         (min, x) -> // given min value and x to iterate through
    //     (x < min) ? x : min // update min value
    // );

    // private static FnList<Integer> rmOne(FnList<Integer> xs, Integer target){
    //     if (xs.nilq()){
    //         return xs;
    //     }

    //     if (xs.hd().equals(target)){
    //         return xs.tl();
    //     }

    //     return FnListSUtil.cons(xs.hd(), rmOne(xs.tl(), target));
    // }

    @Override
    public FnList<T> insertionSort(FnList<T> xs) {
        return FnListSUtil.insertSort(xs);
    }

    @Override
    public FnList<T> bubbleSort(FnList<T> xs) {
        return bubbleSort(xs, (x1,x2) -> x1.compareTo(x2));
    }

    public FnList<T> bubbleSort(FnList<T> xs, ToIntBiFunction<T,T> cmp){
        while(true){
            FnTupl2<FnList<T>, Boolean> result = swap(xs, cmp);
            xs = result.sub0;
            if(!result.sub1){
                return xs;
            }
        }
    }

    private static <S> FnTupl2<FnList<S>, Boolean> swap(FnList<S> xs, ToIntBiFunction<S,S> cmp){
        // nothing to swap; return default
        if (xs.nilq() || xs.tl().nilq()){
            return new FnTupl2<FnList<S>, Boolean>(xs, false);
        }

        S current = xs.hd();
        S compare = xs.tl().hd();

        FnList<S> rest = xs.tl().tl();

        if (cmp.applyAsInt(current, compare) > 0){
            FnTupl2<FnList<S>, Boolean> newTail = swap(FnListSUtil.cons(current, rest), cmp);
                return new FnTupl2<FnList<S>, Boolean>(
                    FnListSUtil.cons(compare, newTail.sub0),
                true
            );
        } else {
            FnTupl2<FnList<S>, Boolean> tail = swap(FnListSUtil.cons(compare, rest), cmp);
            return new FnTupl2<FnList<S>, Boolean>(
                FnListSUtil.cons(current, tail.sub0),
                tail.sub1
            );
        }
    }

    public FnList<T> mergeSort(FnList<T> xs) {
        return FnListSUtil.mergeSort(xs);
    }

    @Override
    public FnList<T> quickSort(FnList<T> xs) {
        return FnListSUtil.quickSort(xs);
    }
}
