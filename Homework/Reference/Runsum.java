package Homework.Reference;
import java.util.Arrays;
public class Runsum {
    public static int[] slowFun(int[] B, int r){
        int n = B.length; // n <- length(B)
        int[] A = new int[n-r]; // A length bound: 0 ≤ i ≤ n-r
        for(int i = 0; i < A.length; i++){
            for (int j = i; j < B.length; j++){
                A[i] += B[j];
            }
            // for (int j = i; j < B.length; j++){
            //     A[i] += B[j];
            // }
        }
        return A;
    }

    // public static int[] FastFun(int[] B, int r){
    //     int n = B.length;
    //     int[] A = new int[n-r];
    //     for (int i =0; i < r; i++){
    //         A[0] += B[i];
    //     }
        
    // }
    public static void main(String[] args){
        // int[] sf = slowFun({1,2,3,4},2);
        int[] sf = new int[4];
        for (int i = 0; i < 4; i++){
            sf[i] += sf[i]+(i+1);
        }
        System.out.println("input: " + Arrays.toString(sf));
        int[] sf2 = Runsum.slowFun(sf, 2);
        System.out.println("SlowFun: " + Arrays.toString(sf2));

    }
}
