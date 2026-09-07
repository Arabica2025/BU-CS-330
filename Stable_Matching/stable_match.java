package Stable_Matching;
/*
Stable Matching - Gale-Shapley Algorithm in Java
Matching between Hospital and Student

runtime: O(n^2)
both hospitals and students have IDs: 1,2,...,n


0. Initialization & Variable Setup
INPUT:
h_prefs: hospital preference to student; 2D int array
    h_prefs[h,i]: preference of hospital h for student i; ith favorite student on h's preference
s_prefs: student preference to hospital; 2D int array
    s_prefs[s,j]: preference of student s for hospital j; jth favorite hospital on s's preference

OUTPUT:
job; where job[r]=h if student r matched to hospital h (array of size n)

Tracking engagements:
a queue of `free_hospitals` with operation: empty(), enqueue(), dequeue()

two arrays `job` and `resident` that store who is "engaged" to whom
    - if hospital h is engaged to r, job[r] = h, resident[h]=r
    - entries for unmatched students/hospitals: -1

Hospital proposing: two arrays `hospital_prefs` and `count` (length n)
    - hospital_prefs[h]: list of students preferred by hospital h
    - count[h]: number of proposals made by hospital h
*/
import MyLibrary.MyQueue.*;
import java.util.Arrays;

class stable_match{
    // Fields
    public int n = 5; // number of hospitals and students fixed to 5 for simulation
    private Integer[] job = new Integer[n]; // job array length n
    private Integer[] resident = new Integer[n]; // resident array length n
    private Integer[] count = new Integer[n]; // hospital proposal count of length n

    private void initialize(){
        for (int i=0; i<n;i++){
            this.job[i] = -1;
            this.resident[i] = -1;
            this.count[i] = 0;
        }
    }

    public Integer[] stable_matching_hos_propose(Integer[][] h_prefs, Integer[][] s_prefs){
        initialize();
        // Initialize free hospitals queue
        MyQueue<Integer> free_hospitals = new MyQueueArray<Integer>(n);
        free_hospitals.foritm(
            (hos) -> {
                free_hospitals.enque$exn(s_prefs[0][hos]);
            }
        );

        // Main loop: loop until all hospitals are matched
        while (!free_hospitals.isEmpty()){
            Integer h = free_hospitals.deque$exn(); // pick a free hospital
            Integer r = h_prefs[h][count[h]]; // get the next student to propose to
            count[h]++; // increment the proposal count for hospital h

            // 1st case: if student r is free
            if (job[r].equals(-1)){
                job[r] = h; // student r is assigned to hospital h
                resident[h]=r; // hospital h hired student r as resident
            } else if (s_prefs[r][h] < s_prefs[resident[h]][h]){ // 2nd case: if student r is not free AND prefers hospital h over the current hospital h' (I don't have to reiterate job[r]==-1 since else if already checks this off)
                Integer h_prime = job[r]; // current hospital that student r is matched to
                free_hospitals.enque$exn(h_prime); // put h'(current hospital) back into the free hospitals queue
                job[r] = h; // student r is reassigned to new hospital h
                resident[h] = r; // new hospital h hired student r as resident
            } else {
                free_hospitals.enque$exn(h); // put hospital h back into the free hospitals queue
            }
        };
        return job; // return the final matching of students to hospitals
    }

    public static void main(String[] args){
        // preference list
        Integer[][] h_prefs = {
            {0, 1, 2, 3, 4},   // Hospital 0
            {1, 0, 2, 4, 3},   // Hospital 1
            {2, 3, 1, 0, 4},   // Hospital 2
            {3, 2, 4, 1, 0},   // Hospital 3
            {4, 3, 2, 1, 0}    // Hospital 4
        };

        Integer[][] s_prefs = {
            {1, 0, 2, 3, 4},   // Student 0
            {0, 1, 3, 2, 4},   // Student 1
            {2, 3, 1, 0, 4},   // Student 2
            {3, 2, 4, 1, 0},   // Student 3
            {4, 3, 2, 0, 1}    // Student 4
        };
        /*
        expected output: [0,1,2,3,4] */
        stable_match match = new stable_match();
        
        System.out.println("Stable Matching: " + Arrays.toString(match.stable_matching_hos_propose(h_prefs, s_prefs)));
    }
}