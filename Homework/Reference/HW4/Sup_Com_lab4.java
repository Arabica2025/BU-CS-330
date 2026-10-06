package Homework.Reference.HW4;
// import MyLibrary.MyStack.*;
import MyLibrary.Sorting.SrtArr;
import java.util.Random;


public class Sup_Com_lab4 extends RuntimeException{
    /*
     * This implementation does not find a minimum valid committee: it counts
     * increases in start times without using finish times to track which
     * students overlap a selected committee member.
     * Counterexample: A = [1, 3], B = [2, 8], C = [7, 9].
     * The code returns 2, but the committee {B} covers all three students,
     * so the minimum size is 1.
     * Also, isValid() only checks interval bounds and a nonempty input;
     * it does not verify that the selected committee covers every student.
     * A correct greedy approach picks the earliest-finishing uncovered
     * student, selects the latest-finishing interval overlapping that student
     * (considering all students), and marks its overlapping students covered.
     * Repeat until every student is covered.
     * here, stack does not optimize for the minimum committee size
     * optimized data structure: sorted array
     */
    public static int min_valid_com(Pair[] shiftInterval){
        // 1. sorted by earliest finishing shift
        SrtArr srtByEST = new SrtArr(new int[shiftInterval.length]);
        srtByEST.mergeSort(shiftInterval,
            (a,b) -> Integer.compare(a.f_i(), b.f_i())
        );

        // MyStackArray<Integer> valid_com = new MyStackArray<Integer>(shiftInterval.length);
        // valid_com.push$exn(shiftInterval[0].s_i());
        // for (int i = 1; i < shiftInterval.length - 1; i++){
        //     int start = shiftInterval[i].s_i();
        //     int start_comp = shiftInterval[i+1].s_i();
        //     if (start < start_comp){
        //         valid_com.push$exn(start_comp);
        //     }
        // }
        Pair[] C = new Pair[shiftInterval.length];
        int count = 1; // at least one committee member is needed
        while (count <= shiftInterval.length){
            int max_finish = count;
            for (int j = count+1; j < shiftInterval.length; j++){
                if (shiftInterval[j].s_i() <= shiftInterval[max_finish].f_i() && shiftInterval[j].f_i() > shiftInterval[count-1].f_i()){
                    max_finish = j;
                }
            }
            C[count-1] = shiftInterval[max_finish];
            count++;
            while (count <= shiftInterval.length && shiftInterval[max_finish].s_i() <= shiftInterval[count-1].f_i()){
                max_finish++;
            }
        }

        return C.length;
    }

    // validity check: Proof of correctness
    private static boolean isValid(Pair[] shiftInterval){
        return noOverlap(shiftInterval) && outOfBound(shiftInterval);
    }
    private static boolean noOverlap(Pair[] shiftInterval){
        for (Pair pair: shiftInterval){
            if (pair.s_i() >= pair.f_i()) return false;
        }
        return true;
    }

    private static boolean outOfBound(Pair[] shiftInterval){
        return shiftInterval.length > 0;
    }

    record Pair(int s_i, int f_i) {}

    public static void main(String[] args) {
        Random rand1 = new Random();
        Random rand2 = new Random();

        int intervalLength = rand1.nextInt(50) - 1 ;
        Pair[] shiftInterval = new Pair[intervalLength];

        for (int i = 0; i < shiftInterval.length; i++){
            shiftInterval[i] = new Pair(rand1.nextInt(50), rand2.nextInt(50));
        }
        // Pair[] shiftInterval = {
        //     new Pair(1, 3),
        //     new Pair(2, 5),
        //     new Pair(4, 6),
        //     new Pair(7, 8)
        // };
        int minCom = min_valid_com(shiftInterval);
        System.out.println("Minimum valid computers needed: " + minCom);
    }
    
}
