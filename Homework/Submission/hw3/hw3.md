# 1. The Quickest Round Trip
## Goal
Find a route that will get me out and back home, $h$, as soon as possible without doubling back. (no overlapping path to go out and come back)

## Factos
1. map: undirected graph ($G$)
2. nodes: intersections, edges: roads
3. $G$: adjacency list with nested hash map.
4. each street is of equal length and takes same amount of time to walk. (unweighted)
5. home node: $h$ (where I depart and must return)
6. A route is valid if it never uses the same edge twice.

## I/O
### Input
the adjacency of an undirected graph $G$ and home vertex $h$
### Output
A boolean indicating whether a valid route from $h$ exists.
- if True then return:
    - the length of the shortest path
    - a list of nodes on this path 
        - return only one if there are multiple solutions
## Runtime
Should be $O(m+n)$ where:<br>
1. $n = |V|$<br>
2. $m = |E|$

## Pseudocode
```
QuickestRound(G,h):

// $n=|V|$, $m=|E|$

    level <- hash table
    parent <- hash table
    level[h] = 0, parent[h] = 0
    Q <- Queue
    branch <- hash table // branch table to record different paths
    Q.enqueue(h)

    // regular BFS 
    while !Q.isempty():
        u <- Q.dequeue()
        for v in G[u]:
            if v not in level:
                level[v] <- level[u]+1
                parent[v] <- u

                // checking if it's source node or not
                if u == h:
                    branch[v] <- v
                else:
                // if not source, then put in differnt branch or children of the same parent node
                    branch[v] <- branch[u]
                Q.enqueue(v)

    shortest_round_dist <- 0
    round_edge <- pair of nodes 

    // iteration through each level
    for u in level:
        // skip the source
        if u == h:
            continue
        for v in G[u]:
            // skip the source; we start from the children of the source
            if v == h:
                continue

            if branch[u] != branch[v]:
                length <- level[u] + 1 + level[v]

                if round_edge == None OR length < shortest_round_dist:
                    shortest_round_dist <- length
                    round_edge <- (u,v)

    if round_edge == None:
        return False
            
// sorry i was quickly editing this but ran out of time.

// first, check if there is a valid path to the half way
// cannot figure out the path if we cannot even have a valid path to the half way.

half_way_G <- BFS(G, h) // half way path value

if half_way_G is None: // if we don't have one, return bool: false
    return False, None, None

// initialize the level and parent hash table that goes backwards to the home vertex, $h$. 
level_rev <- hash table
parent_rev <- hash table

// assign the original BFS path to the variables for readability
level <- half_way_G[0]
parent <- half_way_G[1]
// reverse level and parent should start from the last index.
// we don't know if the level of original BFS and reverse BFS are the same
// so for simplicity, we always start from the last for easier comparison.
level_rev[level.length - 1] = 0, parent_rev[parent.length - 1]=0, Q.enqueue(parent[parent.length-1])

// Reverse BFS loops 
while !Q.isEmpty():
    u_prime <- Q.dequeue();
    // reverse for loops
    for v_prime in G[u_prime] from G[u_prime].length - 1 to 0:
        // check if
        // 1. there is a connecting nodes
        // 2. the nodes are already visited (or exist in level hash table for the record)
        if v_prime not in level_rev or v_prime not in level:
            // go through that node if the condition is met
            level_rev[v_prime] = level[u_prime]+1;
            parent_rev[v_prime] = u_prime;
            Q.enqueue(v_prime);

if level_rev or parent_rev is None:
    return False, None, None

// enumerate the original BFS and reverse BFS tree(tree that does not visit the nodes or vertices that are not visited in the original BFS.)
level_enum <- enumerate(level, level_rev)
parent_enum <- enumerate(parent, parent_rev)

// return 1. bool (True)
// 2. the length of the shortest path (level_enum)
// 3. a list of nodes on this path (parent_enum)
return True, level_enum, parent_enum
```

## Runtime Analysis
1. $O(m+n)$: original BFS from the lecture
2. $O(1)$: checking if there is a valid path in original BFS; if not, return False and that's the end of the algorithm
-> In this case, the entire algorithm is $O(m+n)$

3. $O(m+n)$: reverse BFS. almost similar to the original BFS from the lecture.
Difference:
    - reversed for loop that iterates through each index of G. Runtime is same as the original BFS algorithm.
    - additional condition in the if statement: checking if the checking node in the reversed BFS already exists in the level hash table from the original BFS tree. If it does, we are basically doubling back to the home node. This is what the question bans.
    - instead of incrementing by 1 from 0, decrement by 1 from the current level since we are iterating through the table reversely to circling back home.
4. $O(1)$: checking if there is a valid path in the reversed BFS that does not double back to the home vertex; if not, return False and that's the end of the algorithm.
5. $O(level + reversed level)$: enumerating the original with the reversed table.
    - level + reversed level ≤ m: this is always true because it is the shortest path that one can circle back to the home vertex without doubling the visited vertices. Because there cannot be any overlaps in counting the nodes and edges, the total distance cannot exceed the number of edges.

Therefore,
$O(m+n) + O(m+n) + O(1) + O(level + reversed level) = O(m+n)$

## Proof of Correctness
To prove if the algorithm is correct, we need to verify if:
1. the path exists. 
2. the path that circles back to the home vertex without doubling back.
3. the path from the algorithm is the shortest path

1. existence of path
We verify the existence of path by running the original BFS algorithm that visits every node and find the shortest path and distance to reach to the node at the end. Also, there is another case that the path of the reverse BFS that does not contain the overlapping nodes does not exist. Checking this with another if statement that filters the `None` value from the reversed level and parent hash table from the algorithm fully verifies the existence of path in the input tree of the algorithm. therefore, the algorithm always returns the path if there exists one in the given graph.

2. No overlapping vertecies in the graph
We verify that there is no overlapping path in the returned graph from the algorithm by comparing each node that we visit in the reversed BFS algorithm with the original level hash table to see if the node exists in the level hash table. By adding another condition in the if statement that checks the existence of the currently visiting node in the hash table prevents the overlapping path searching.

3. Shortest path from the algorithm
From the lecture, the BFS algorithm for unweighted, undirected graph always return the shortest path as it implements FIFO queue that dequeues vertices in order of distance or level. Moreover the additional reverse BFS algorithm does not change the overall runtime of the algorithm in total as it only changes the iteration sequence to backwards and another condition to check the overlapping nodes in the constant time of $O(1)$ so that these changes does not affect the essential logic of path-finding algorithm of BFS. 

Since the three conditions are met in the algorithm to prove its correctness, the algorithm correctlty outputs 1) boolean value indicating whether a valid route fro $h$ eists, 2) the length of the shortest path and 3) a list of nodes on this path. 

# 2. Perfect Train Ride
## 2-a
I would use BFS from class to figure out the number of hops I would need to take to get from $s$ to any city, not DFS. Since DFS does not always gurantee us to find the shortest path, but the valid path to the destination, BFS is more preferred to be used as it gurantees to find the shortest path or least hops that we need to get to any city. 

To get $hops$, initialize another hash table of lists $hops$ in BFS algorithm. For each iteration that visits the children node, at distance $i$, append the children node at $hops[i]$ so that we can see what cities are in each level from the source node $s$.

## 2-b.
### Pseudocode
```
bestPath(hops, A, s, x):
    /* bestPath: calculate the best path from s to x in the graph A according to the minimum number of train rides from s to x (hops)
    
    Arguments:
        hops: hash table of lists that contain cities or nodes at each level from the source node.
        A: nested hash table of all possible train routes between two cities (s,x)
        s: source node (start city)
        x: destination node (destination city)

    priority: 
        1. shortest path from source to destination (s,x)
        2. smallest cumulative weights from source to destination (s,x)
    */

    w_filtered <- empty hash table /* initialize hash table of total average number of passengers on the train for each path provided. initialize as lists to compare the weights of paths with same distance filled with 0s
    */
    w_filtered[s] <- 0 // starting point always has weight 0

    p_filtered <- empty hash table /* hash table of paths that contains parents directing towards only the shortest path.*/

    level <- empty hash table // tracking distance as we do in BFS

    // getting level table from BFS
    for i in hops:
        for n in hops[i]:
            level[n] <- i
    
    // validity check
    if x not in level:
        return exception("No Valid Path")

    shortest_dist <- level[x] // shortest path is at x's level


    // main loop
    // 1. looping from the source to the destination in shortest distance only
    for i in range(shortest_dist):
        // 2. looping for nodes in each level
        for u in hops[i]:
            // 3. looping for childrens of the parent from the second loop
            for v in A[u]:
                // check if the current node has children
                if level[v] == i+1:  
                    possible_path_weight <- w_filtered[u] + A[u][v] // keep adding the weights on top of the previous one
                    // if it is the first time adding weights OR the current weight is greater than the input weight
                    if v not in w_filtered OR possible_path_weight < w_filtered[v]:
                        // update total weight and path with shortest AND least weight
                        w_filtered[v] <- possible_path_weight
                        p_filtered[v] <- u
    
    // after the loop, we have reversed path so we have to reverse it back
    path <- empty list of length of shortest_dist + 1
    // initialize first index and last index as the input $s$ and $x$
    path[0] <- s
    backwards <- x

    // loop backwards. in this way, we can skip if condition for additional check for updating backwards.
    for i = shortest_dist to 1:
        path[i] <- backwards
        backwards <- p_filtered[backwards]

    return path
```
### Runtime Analysis
Let $n=|V|$ and $m=|E|$.
1. $O(n)$: first for loop for getting level hash table
2. $O(1)$: initialization, validity check and shortest path assignment
3. $O(n+n+m)$: the main loop. for each iteration of the first for loop, we check the nodes in each level and at most the shortest path is $n-1$. 
4. $O(n)$: returning path backwards. we need a for loop with at most iteration of $n$ times.
Therefore,
the total running time of the algorithm is $O(n) + O(1) + O(n+n+m) + O(n) = O(n+m)$.

### Proof of correctness
To prove the algorithm's correctness, we need to prove:
1. the algorithm always returns either exception that there is no valid path from $s$ to $x$ or shortest path from $s$ to $x$.
2. If the algorithm returns the shortest path, then it must have the least average number of passengers possible.

1. Always shortest path from $s$ to $x$ OR warning call for no valid path:
    - According to the algorithm, we are given the shortest path we can take to $x$ to any cities and eventually to the destination. 
    - Other than the shortest path bound in the main loop, it follows BFS algorithm from the lecture that the child nodes are always one level later than its parent connected by an edge such that $x$ always reaches $s$ in exactly level[x] edges.
    - Therefore, the algorithm always returns shortest valid path from $s$ to $x$ or otherwise return exception to warn that there is no valid paths.

2. always return shortest path with fewest passengers
    - proof by induction
        - base case: `w_filtered[s] = 0` for the least passengers
        - inductive hypothesis: there is always the least passengers for all nodes at level $i$ among all other shortest paths.
        - inductive step:
            - considering the fact that the algorithm always has the parent node one level earlier than its children nodes, every shortest path to node at $i+1$ ends with an edge at from $i$. According to the algorithm, it considers every possible shortest paths to the destination $x$ while keeping the total minimum average passenger numbers by comparing the average cumulative passengers of current node in level $i$ to those of other node in level $i$ and do so for the rest of the nodes in proceeding levels until the destination node.
        - therefore, the algorithm always returns shortest path with fewest passengers along the path.
