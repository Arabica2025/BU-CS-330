# 1. Job Fair Speedrun
## a. 
smallest acceptable set of attendance times for the example in Fig1: 3
## b.
counterexample to find the minimum hitting set counted by always picking the time that covers the largest number of remaining employers:
$$
\begin{array}{c|cccccccccc}
\text{Time} & 0 & 1 & 2 & 3 & 4 & 5 & 6 & 7 & 8 & 9 \\
\hline
A & \bullet & - & \bullet & & & & & & & \\
B & & \bullet & - & - & - & \bullet & & & & \\
C & & \bullet & - & - & - & \bullet & & & & \\
D & & & & & \bullet & - & - & - & \bullet & \\
E & & & & & \bullet & - & - & - & \bullet & \\
F & & & & & & & & \bullet & - & \bullet
\end{array}
$$

Greedy picks $t=4$, covering B, C, D, and E (the maximum overlap of four). A and F require two additional visits, for three visits total. The optimal solution uses only $t=1$ and $t=8$, covering all six intervals.

## c.
If the input contains $k$ mutually disjoint intervals, then every acceptable set of visit time smust have size at least $k$. 
Proof by contradiction
### Claim
The minimum requirement for an *acceptable* set of time visit is that each employer's interval $[s_i, f_i]$ contains at least one $t_j$ such that $s_i \le t_j \le j_i$. 
### Proof by Contradiction
Suppose if the input contains $k$ mutually disjoint intervals, then every acceptable set of visit times has size less than $k$.

If every interval is mutually disjoint, then $f_{i-1} < s_i$ and $f_i < s_{i+1}$ such that $[s_{i-1}, f_{i-1}] \cap [s_i, f_i] \cap [s_{i+1}, f_{i+1}] = \emptyset$.

So, $t_j$ that $s_i \le t_j \le f_i$ cannot exist in neither $[s_{i-1},f_{i-1}]$ nor $[s_{i+1}, f_{i+1}]$ such that there must exist at least one visit time $t$ for each interval for mutually disjoint set.

Therefore, if there are $k$ mutually disjoint intervals, then the least number of $t$ must be $k$. This contradicts the supposition.

QED.


## d.
```pseudocode
JFS(timetable: list[list[int,int]]) -> list[int]:
    Interval <- empty Priority Queue with minheap of ascending finishing time

    // set time interval as priority queue with minheap of earliest finishing time
    for i in range(timetable.length):
        Interval.enqueue(timetable[i])

    opt_interval <- empty nested list
    min <- 0
    while !Interval.isempty():
        opt_interval[min][0] <- Interval.extract_min() // if the PQ is not empty, then there is always the leftover. initialize the interval list with the first interval
        count <- 1 // counter for optimizing the number of visit times; counting the overlaps

        while !Interval.isempty():
            if Interval.get_min()[0] > opt_interval[min][0][1]: // if s_{i+count} ≤ f_{i}, then overlap. Otherwise, disjoint.
                break
            opt_interval[min][count] <- Interval.extract_min() // we remove the previous compared interval; the final length of opt_interval will be the minimum(optimized) number of visit times
            count <- count + 1 
        min <- min + 1
    
    acceptable <- empty list[int]
    for i in range(opt_interval.length):
        acceptable[i] <- intersection(opt_interval[i])
    return acceptable

intersection(opt_interval_i):
    intersect <- opt_interval_i[0][0] // initialize with first element
    for i = 1; i < opt_interval_i.length; i++ {
        intersect <- max(intersect, opt_interval_i[i][0])
    }
    // intersection among the overlaps: maximum starting time among the intervals. if that equals the finish time of the first interval, be that as the intersection time.
    return intersect
```
## e.
1. $O(logn)$: Priority Queue enqueue takes $O(logn)$ average and worst case for each. upper bound is $O(nlogn)$.
2. $O(nlogn)$: we loop through all of the nodes in the Priority Queue once so that it iterates $n$ times. However, as we extract minimum from the minheap, we have to reallocate the nodes in ascending finishing time that takes $logn$ times.
3. $O(n)$: getting acceptable times from the minimum(optimal) time visit intersections takes $n$ times worst case.

$O(logn) + O(nlogn) + O(n) = O(nlogn)$.
Therefore, the total runtime of the algorithm is $O(nlogn)$.

## f.
Proof of Correctness
To prove the correctness of the algorithm:
1. There must be at least one acceptable visit time for each interval.
2. set of acceptable visit times must be in minimum size
3. the acceptable set of visit times must cover all time intervals given as input.

First, the algorithm intitializes the priority queue Interval with the first element of leftover time interval for every outer iteration. Then, we assign the optimal time visit for every group of time intervals that can have an acceptable visit time. Because each assigned optimal time visit must intersect all of the time intervals within the same group, there is at least one acceptable visit time for each interval. This leads to the conclusion that the acceptable set of visit times covers all groups of the time intervals given as input. 

Moreover, according to the lower bound of the set of acceptable visit times, the algorithm must output $k$ visit times for $k$ mutually disjoint intervals. the inner loop of while ensures that the groups get separated by disjoints such that there is a mutual disjoint between the groups. Therefore, by the lower bound proven at part (c), the minimum number of visit time must be the length of opt_interval. Iterating by the length of opt_interval to get the visit times, the algorithm ensures that there are only miniumu number of visit times in the output.

Therefore, the algorithm output is always correct.
# 2. Earliest Interval
## Pseudocode
```pseudocode
EarlyInterval(T: list[list[int, int]]) -> list[int]:
    /* Goal: for each finish time, get the earliest-starting interval that contains that finish time. (f_k \in [s_j, f_j]). output a list of n indices such that for the kth finish time, A[k] is the value of the index asked to find 
    
    T: list of intervals ([s_i,f_i])
    */

    EFT <- EFT(range(T.length)) // sort T's intervals' indices by EFT first. $\Theta(nlogn)$

    EIList <- initialize Priority Queue with minheap ordered by EST

    for priorityidx in range(T):
        EIList.insert(priorityidx, T[priorityidx][0]) // id, key pair for Priority Queue; id: priority integer, key: starting times

    /* now we have a priority queue sorted by EST(EIList) and a nested list sorted by EFT(EFT)*/

    A <- empty index list
    // this iterates O(n) worst time
    for k in range(EFT.length):
        i <- EFT[k] // we need to iterate through EFT interval to find what intervals belong to which each EST interval
        f <- T[i][1] // finish time to compare

        while T[EIList.get_min()[0]][1] < f: // while finish time of EST is less than the EFT finish time; if finish time of EST is less than the EFT finish time, then that EST interval will never be used to entail the finish time of any interval so we subtract his ass.
            EIList.extract_min() // if the condition met, we are sure that this belongs to no interval so out

        A[i] <- EIList.get_min()[0] // update priority index that satisfy the problem
    return A
```
## Runtime Analysis
1. $O(nlogn)$: sorting by Earliest Finishing Time takes $n \cdot log n$ times as per the lecture.
2. $O(nlogn)$: T is a list of intervals that each interval is a pair of start and finish time. There are $n$ intervals with $O(logn)$ for insertion in the priority by EST
3. $O(nlogn)$: main loop to get $A$. the outer `for` loop iterates $n$ times. inner while loop removes $n$ intervals at worst case only once. and each extract_min() operation would cost $O(logn)$.

$O(nlogn)+O(nlogn)+O(nlogn)=O(nlogn)$
Therefore. the total runtime of the algorithm is $O(nlogn)$.

## Proof of Correctness
To prove the correctness of the algorithm:
1. $s_j \le f_k \le f_j$
2. for every interval $cmp$ that contains $f_k$, $s_j \le s_{cmp}$.

First, we prepared three different intervals: $T$: original list of intervals, $EFT$: intervals by earliest finish time, $EIList$: intervals by earliest start time. By definitions of EST, the first interval of EST will be assigned to each finish time as long as that finish time is less than the finish time of first interval. By definitions of EFT, the finish time of the following interval will never be within the range of its preceeding interval as we assume each start and finish is distinct.

With these properties, if finish time of EST is less than the EFT finish time, then that EST interval will never be used to entail the finish time of any interval. The algorithm ensures that point with while inner loop and assigning the leftover EST interval index to $A$. By repeating this operation for the entire $n$ intervals, we can ensure both $s_j \le f_k \le f_j$ and for every interval $cmp$ that contains $f_k$, $s_j \le s_{cmp}$.

Therefore, the alogrithm always outputs correctly.
