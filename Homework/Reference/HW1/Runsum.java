package Homework.Reference.HW1;
import java.util.Arrays;
public class Runsum {
    public static int[] slowFun(int[] B, int r){
        int n = B.length; // n <- length(B)
        int[] A = new int[n-r+1]; // A length bound: 0 ≤ i ≤ n-r
        // System.out.println("A length: " + A.length);
        for(int i = 0; i < A.length; i++){
            for (int j = i; j < i+r; j++){
                A[i] += B[j];
            }
            // for (int j = i; j < B.length; j++){
            //     A[i] += B[j];
            // }
        }
        return A;
    }

    public static int[] FastFun(int[] B, int r){
        int n = B.length;
        int[] A = new int[n-r+1];
        for (int i =0; i < r; i++){
            A[0] += B[i];
        }

        System.out.println("FastFun A[0]: "+ A[0]);
        System.out.println("FastFun array A before updates: "+ Arrays.toString(A));

        for (int i = 1; i < A.length; i++){
            System.out.println();
            System.out.println("A[i]: "+ A[i] + " A[i-1]: "+A[i-1] + " B[i-1]: " + B[i-1]+ " B[i+r-1]: " + B[i+r-1]);
            A[i] = A[i-1] - B[i-1] + B[i+r-1];
        }
        return A;
        
    }
    public static void main(String[] args){
        // int[] sf = slowFun({1,2,3,4},2);
        // int[] sf = new int[5];
        // for (int i = 0; i < 5; i++){
        //     sf[i] += sf[i]+(i+1);
        // }
        // int[] fstfun = {1,2,3,4,5};
        int[] slotfun = {3,5,1,7,13,17,20,25};
        int[] fstfunobj = FastFun(slotfun, 4);

        // System.out.println("Final: " + Arrays.toString(fstfunobj));
    

        // System.out.println("input: " + Arrays.toString(fstfun));

        // int[] slotfun2 = Runsum.slowFun(fstfunobj, 1);
        System.out.println("SlowFun: " + Arrays.toString(fstfunobj));


        // System.out.println("input: " + Arrays.toString(sf));
        // int[] sf2 = Runsum.slowFun(sf, 2);
        // System.out.println("SlowFun: " + Arrays.toString(sf2));

    }
}
