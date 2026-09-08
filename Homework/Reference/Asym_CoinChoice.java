package Homework.Reference;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class Asym_CoinChoice {
    public static int coinChoice(int a, int b, int c, int n) {
        // Implementation of the asymptotically faster coin choice algorithm 
        /* Set bounds for 0 < a,b,c <= n for runtime analysis */
        assert a > 0 && b > 0 && c > 0 && n > 0;
        assert a <= n && b <= n && c <= n;

        // int max_i = Math.min(a,n);
        // int max_j = Math.min(b,n / 2);
        // int max_k = Math.min(c,n / 3);

        // the maximum number of coins we can combine
        // init Variables: O(1)
        int max_three_buck = Math.min(c, n/3);
        int max_two_buck = Math.min(b,n/2);
        int max_one_buck = Math.min(a,n);

        /* Counter added */
        // int full_perm = max_one_buck * max_two_buck * max_three_buck;
        // int[][] weedout_cand = new int[max_three_buck+1][max_two_buck+1];
        int count = 0;
        
        /* Logic:
        1. iterate through the maximum numbers of three-buck and two-buck coin (O(n^2))
        2. eliminate the possible permutations that would go out of bounds from the counter
            2-1. if the given number of 3-buck coins worth more than n
            2-2. if the given number of 2-buck coins worth more than n
            2-3. if the given number of 3-AND-2-buck coins(permutation) worth more than n
            2-4. From the permutation without 2-1,2,3., if the left-over money is greater than the maximum number of 1-buck coin.
            2-5. Otherwise, put it in the counter.
        3. Return the number of permutations (O(1)) 
        */
        for (int first = max_three_buck; first >= 0; first--){
            // System.out.println("first 3s weedout: " + first);
            for (int second = max_two_buck; second >= 0; second--){
                // System.out.println("second 2s weedout: " + second);
                if(3*first > n||2 * second > n || (3*first)+(2*second) > n || (n-(2*second+3*first)) > max_one_buck){
                    continue;
                    // full_perm--;
                } else {
                    // System.out.println("possible permutation: "+ "(" + (n-(2*second+3*first))+", " + second + ", "+ first +")");
                    count++;
                    // weedout_cand[first][second] = (3*first) + (2*second);
                }
            }
        }

        // for (int i = 0; i < max_three_buck; i++){
        //     for (int j = 0; j < max_two_buck; j++){
        //         if (weedout_cand[i][j] == 0){
        //             full_perm--;
        //         }
        //     }
        // }
        // if (max_one_buck <= n){
        //     full_perm = full_perm -1;
        // }
        return count;

    }
    // private boolean smaller_than_n(int comp, int n){
    //     return (comp < n) ? true : false;
    // }

    private static double[] runtime(int[] powersoftwo){
        double[] measure_time = new double[8];
        int counter = 0;
        
        for (int n : powersoftwo){
            long start = System.nanoTime();
            coinChoice(n, n, n, n);
            double seconds = (System.nanoTime() - start) / 1000000000.0;
            System.out.printf("Execution time: %.6f seconds%n", seconds);
            measure_time[counter++] = seconds;
        }
        return measure_time;
    }

    private static void csvwrite(double[] runtime){
        String csvFile = "Homework/Reference/asym_run_times.csv";

        try(BufferedWriter w = new BufferedWriter(new FileWriter(csvFile))){
            for (int i =0; i < 8; i++){
                double time = runtime[i];
                String formattedTime = String.format("%.10f", time);
                w.write(formattedTime);
                w.newLine();
            }
            System.out.println("runtime data saved to " + csvFile + " succesfully.");
        } catch (IOException e){
            System.out.println("Error writing to CSV File: " + e.getMessage());
        }
    }
    public static void main(String[] args){
        /* Runtime Analysis:
        1. Variable initialization: O(1)
        2. Main nested loop: O(n^2); because it iterates the number of 2-buck coin for each number of 3-buck coin
        3. return result: O(1)
        O(1) + O(n^2) + O(1) = O(n^2)
        Hence, coinChoice(int a, int b, int c, int n) algorithm has a time complexity of O(n^2).
         */
        int[] powersoftwo = new int[8];

        for (int x = 4; x < 12; x++){
            powersoftwo[x-4] = 1 << x; // bitwise operator; shift 1s to the left from 1 to make 2^x
        }

        double[] RT = runtime(powersoftwo);

        csvwrite(RT);

    }
}
