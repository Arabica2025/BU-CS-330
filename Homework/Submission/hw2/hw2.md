# 1. Colorful Path
## Pseudocode
```pseudocode
/* G: input adjacency list as a nested hash table */
/* P: output list of tuples of four nodes that are "colorful" */
/* For this algorithm, 1-based indexing was used.*/
/* Task: Design an algorithm that takes as input a graph $G$ and returns all node quadruples that form a "colorful" path (colorful: sequence of red -> blue -> green)*/

ColorfulPath(G):
    P <- empty list[tuple(length of 4)] // initialize the output list P of tuples of four nodes.

    for u in G:
        for v in G[u]: // first 2 nested for loops: n+m operations

            colorful <- empty tuple of length of 4. // create an empty tuple to record the possible permutation
            if (G[u][v] == 'r' AND G[v][u]=='r'): // checking if the edge between $u$ and $v$ is red (undirected; we have to check both)
                colorful[1] <- G[u][v] // put $u$th node into the tuple: initial node $u$
                colorful[2] <- G[v][u] // put $v$th node into the tuple: $u$ connected with $v$

                /* third nested for loop: D operations*/
                for r in G[v]: // we have to iterate through $v$th dictionary or edges of node $v$ to look for 'blue' edge
                    if (G[v][r]=='b' AND G[r][v] == 'b'): // checking if the edge between $v$ and $r$ is blue (undirected; we have to check both)
                        colorful[3] <- G[v][r] // if it's red->blue, put $r$th node into the tuple: $v$ connected with $r$

                        /*fourth nested for loop: D operations*/
                        for b in G[r]: // we have to iterate through $r$th dictionary or edges of node $r$ to look for 'green' edge
                            if (G[r][b] == 'g' AND G[b][r]=='g'): // checking if the edge between $r$ and $b$ is green (undirected; we have to check both)
                                colorful[4] <- G[r][b] // if it's red->blue->green, puth $b$th node into the tuple: $r$ connected with $b$
                                P.append(colorful) // now that we have a "colorful" tuple, append the tuple to list $P$; this operation is repeated until the whole loops terminate
    return P // after appending all possible tuples, return the list of tuples.
```
## Runtime Analysis
1. $O(1)$: initialize the empty list of tuples of four nodes ($P$) to return 
2. $O(n+m)$: first two nested for loops that iterates through the end of the input nested hash table $G$. Since the length of each Node is different, the upper bound of these two nested loops would be $n+m$. <br>
    2-1. $O(1)$: initialize the empty tuple of length of 4 for "colorful" tuple of 4 to filter only the "colorful" ones. Put first 2 nodes $u$ and $v$ into `colorful` tuple. At this point, we can find red.<br>
    2-2. $O(D)$: loop through the edges of Node $v$ to look for proceeding color. Put third node $r$ into `colorful` tuple. At this point, we can find red->blue.<br>
        2-2-1. $O(D)$: loop through the edges of Node $r$ to look for proceeding color. Put fourth node $b$ into `colorful` tuple. At this point, we can find red->blue->green.<br>
        2-2-2. $O(1)$: Since we found 1 "colorful" path, append the tuple to the list

To sum all the steps, at maximum, there can be $n + m(D \cdot D)$ operations for this algorithm. Therefore, the running time of `ColorfulPath(G)` is $O(n+mD^2)$.

# 2. Part 1: Asymptotics

## (a) $f(n)=n^{0.9} \text{ and } g(n)=\sqrt{2n}$<br>
Answer: $f(n)=O(g(n))$
If we rewrite $g(n)$, 
$$
\begin{array}{ll}
\sqrt{2n}\\
=(2n)^{0.5}\\
=2^{0.5} \times n^{0.5}\\\\
\text{Let } c=2^{0.5}\text{ and } n_0 = 0 \text{ and } n_1 = 1\\

\text{Case 1: }n_0=1\\
f(n) = 1^{0.9}=1 < g(n) = 2^{0.5} \times 1^{0.5} = \sqrt{2}\\
\text{Case 2: }n+1 = 2\\
f(n) = 2^{0.9} < g(n) = 2^{0.5} \times 2^{0.5} = 2
\end{array}
$$

So, there exist constants $c = 2^{0.5}>0$ and $n_0=1$ such that
$∀n≥n_0,f(n)≤c\times g(n)$.

## (b) $f(n)=2^n+n^2 \text{ and } g(n)=n^6$<br>
Answer: $g(n)=O(f(n))$

According to the growth-rate rule of thumb,
$$\begin{array}{ll}
n^c <<a^n \text{ for any constant }c>0,a>1\\
\text{Let }c=6,a=2\\
\\
n^c = n^6 = g(n) << a^n = 2^n\\
\text{For the remainder of f(n), }n^2 \text{ can be ignored.}\\
lim_{n \rightarrow \infty} \frac{f(n)}{g(n)} = \frac{2^n+n^2}{n^6} > 0 \text{ such that } f(n) = \Omega(g(n)).\\
\end{array}
$$

Therefore, $f(n) = \Omega(g(n))$ such that $g(n) = O(f(n))$.

## (c)$f(n)=log_2(n), g(n)=log_2(n^2+n)$<br>
Answer: $f(n) = O(g(n))$
$$\begin{array}{rl}
g(n) = log_2(n^2+n)\\
=log_2(n(n+1))\\
= \text{(Use log product rule) }log_2(n)+log_2(n+1)\\
\text{So, }f(n)≤c\cdot g(n) \text{ for any constant }c > 0, n_0
\end{array}
$$
Therefore, $f(n) = O(g(n))$.

## (d) $f(n) = n^2log_2(n), g(n) = n^2 \cdot \sqrt{n}$
Answer: $g(n) = O(f(n))$
$$\begin{array}{rl}
lim_{n \rightarrow \infty}\frac{f(n)}{g(n)} = \frac{n^2log_2(n)}{n^2 \cdot \sqrt{n}}\\
= \frac{log_2(n)}{\sqrt{n}} = \frac{log_2(n)}{n^{0.5}}\\
\\
\text{According to growth-rate rule of thumb: }\\
log(n) <<n^c = log_2(n)<<n^0.5\\
\text{So, }\frac{log_2(n)}{n^{0.5}} > 0\\
\text{So, }f(n) = \Omega(g(n)) 
\end{array}
$$
Therefore, $g(n) = O(f(n))$.

# 3. Part 2: Runtime Analysis
## e. Basic Merging
- $O(1)$ to initialize the empty list $C$.
- `while` loop until either list $A$ or $B$ is empty to remove the smaller of the first elements of $A$ and $B$ and append it to $C$. The worst case would be to iterate through the end of BOTH $A$ and $B$ lists ($O(ab)$)
- there are three cases of append task:
    1. $a>b$
        - In this case, the append job would take $O(a-b)$ times.
    2. $b>a$
        - In this case, the append job would take $O(b-a)$ times.
    3. $a=b$
        - In this case, we do not have append task.
Therefore, $O(1) + O(ab) +$ and either $O(a-b)$ or $O(b-a) = O(ab)$.
## f. Merging $k$ Sorted Lists
$O(n)$ for the first initialization of $R$ by copying $L_1$.
For the worst case, the upper bound of the `for` loop operation would be $(k-1)n^2$ as the $\text{Merge}(R,L_i)$ Operation is $O(n^2)$ and we repeat for $k-1$ times. We subtract the number of `Merge` operation by 1 as we already did once for the initialization.
Therefore, it is $O(n) + O((k-1)n^2) = O(k \cdot n^2)$.
## g. Counting Colors
$O(1)$ for initializing the color counters.
The graph $G$ is a nested hash table that its length is the number of nodes $n$ and the length of "dictionary" at each index is the number of edges $m$.

The upper bound of  `for` loops of `ColorCounting(G)` is the following ($n+m$).
    - the outer loop (`for u in G do`): loop through the entire nodes in the graph so the outer iteration operates $n$ times.
    - the inner loop (`for v in G[u] do`): loop through edges connected to each node. The number of connected edges to nodes can be different. However, in the end, we have to loop for the number of edges, $m$, between the nodes in the graph.

Therefore, the running time of `ColorCounting(G)` is $O(n+m)$.

