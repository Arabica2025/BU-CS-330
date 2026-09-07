package Homework.Reference;
import MyLibrary.MyQueue.*;

public class Asym_CoinChoice {
    public static int coinChoice(int a, int b, int c, int n) {
        // Implementation of the asymptotically faster coin choice algorithm 
        assert a > 0 && b > 0 && c > 0 && n > 0;
        assert a <= n && b <= n && c <= n;

        // int max_i = Math.min(a,n);
        // int max_j = Math.min(b,n / 2);
        // int max_k = Math.min(c,n / 3);
        // the maximum number of Cs we can combine
        int max_three_buck = Math.min(c, n/3);
        int max_two_buck = Math.min(b,n/2);
        // int max_one_buck = Math.min(a,n);

        // full permutation count that even the numbers
        // that don't add up to n are included
        // we need to weed out those numbers
        // int full_perm = max_one_buck * max_two_buck * max_three_buck;
        // int[][] weedout_cand = new int[max_three_buck+1][max_two_buck+1];
        int count = 0;
        
        
        for (int first = max_three_buck; first >= 0; first--){
            System.out.println("first 3s weedout: " + first);
            for (int second = max_two_buck; second >= 0; second--){
                System.out.println("second 2s weedout: " + second);
                if(3*first > n||2 * second > n || (3*first)+(2*second) > n){
                    continue;
                    // full_perm--;
                } else {
                    System.out.println("possible permutation: "+ "(?, " + second + ", "+ first +")");
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
    public static void main(String[] args){
        // System.out.print(coinChoice(3, 2, 2, 5));
        // System.out.print(coinChoice(4, 4, 4, 4));
        System.out.print(coinChoice(8, 8, 8, 8));
    }
}
