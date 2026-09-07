package MyLibrary;

public class f91_tail_recursion {
    // change: forgot to add constructor
    int n;
    public f91_tail_recursion(int n){
        this.n = n;
    }
    private static int tailF91(int n, int iteration){
        // if we don't have any iterations to loop through, we just return what's left 
        if (iteration == 0){
            return n;
        }

        // if condition met, we have 1 less thing to concern
        if (n > 100){
            return tailF91(n-10, iteration-1);
        } else{
        // if condition not met, we need 1 more execution to solve the problem
            return tailF91(n+11, iteration + 1);
        }
    }   

    /* tail recursion works like a loop? 
    then it has a finite number of executions? */
    public static int f91(int n){
        // if (n > 100){
        //     return n-10;
        // } else {
        //     return f91(tailF91(n, 1));
        // }
        return tailF91(n, 1); 
        // even if n == 91, we must execute the method at least once
        // -> set iteration to 1 as default

    }
}
