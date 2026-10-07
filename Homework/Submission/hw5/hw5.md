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
JFS(Interval: PriorityQueue[list[int,int]]) -> list[int]:
    opt_interval <- empty nested list
    min <- 0
    while !Interval.isempty():
        opt_interval[min][0] <- Interval.extract_min() // if the PQ is not empty, then there is always the leftover. initialize the interval list with the first interval
        count <- 1 // counter for optimizing the number of visit times; counting the overlaps

        while !Interval.isempty():
            if Interval.get_min()[0] > opt_interval[min][0][1]: // if s_{i+count} ≤ f_{i}, then overlap 
                break
            opt_interval[min][count] <- Interval.extract_min() // we remove the previous compared interval; the final length of opt_interval will be the minimum(optimized) number of visit times
            count <- count + 1 
        min <- min + 1
    
    acceptable <- empty list[int]
    for i in opt_interval:
        acceptable[i] <- intersection(opt_interval[i])
    return acceptable

intersection(opt_interval_i):
    intersect <- opt_interval_i[0][0] // initialize with first element
    for i = 1; i < opt_interval_i.length - 1; i++ {
        intersect <- max(intersect, opt_interval_i[i][0])
    }
    // intersection among the overlaps: maximum starting time among the intervals. if that equals the finish time of the first interval, be that as the intersection time.
    return intersect
```
## e.
## f.

# 2. Earliest Interval
## Pseudocode
## Runtime Analysis
## Proof of Correctness
