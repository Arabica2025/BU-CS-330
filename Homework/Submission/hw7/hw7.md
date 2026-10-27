# 1. Verifying Distances
## (a) linear-time validity-check algorithm
### pseudocode
```pseudocode
VerifyDijkstra(G: adjacency list, s: source node, dist: distance hash table):
    if dist[s] != 0: // if the source node's distance isn't 0, invalid.
        return "incorrect"

    // case 1: if pi[v] is greater than pi[u] + weight
    // we should maintain pi[v] = min_{u ∈ S}_{v ∈ adj(u)} d(u) + l(u,v) for valid Dijkstra's output. 
    // it is absolutely not true if pi[v] is greater because this problem prohibits negative weights

    for u in G:
        for v in G[u]:
            weight <- G[u][v]
            if dist[v] > dist[u] + weight:
                return "incorrect"

    // case 2: now we need to verify if the dist hash table is valid
    // First, run BFS on G so that we check every node in G, just like Dijkstra's Algorithm optimizes the shortest path by visiting every node
    opt_cand <- empty hash table
    Q <- empty Queue // this is not a Priority Queue so there is no extract_min() or peak() operation that requires additional logrithmic runtime.
    Q.enqueue(s), opt_cand.add(s) e
    while !Q.isempty():
        u <- Q.dequeue();

        for v in G[u]:
            weight <- G[u][v]
            if dist[v] = dist[u] + weight: // we only add the ones that are valid by definition of Dijkstra's
                if v not in opt_cand:
                    opt_cand.add(v)
                    Q.enqueue(v)
    
    // now we check if the possible valid candidate hash table is correct
    // 1. if the node is in the candidate hash table (or we included that node as one of the shortest pathways in BFS), then its weight should not be infinity because we recorded the constant weight
    // 2. otherwise the weight should be infinity because we do not go through that node for shortest path
    for v in G:
        if v in opt_cand:
            if dist[v] == ∞:
                return "incorrect"
        else:
            if dist[v] != ∞:
                return "incorrect"

    return "correct"
```

### runtime analysis
1. $O(1)$: constant time to check if source node is correct
2. $O(n+m)$: linear time to check the first case (pi[v] > min_{u ∈ S}_{v ∈ adj(u)} d(u) + l(u,v))
3. $O(n+m)$: linear time to create possible candidate distance that Dijkstra would have 
4. $O(n)$: linear time to compare the input distance table with possible candidate table to see if there exists any node missing or overcounted

Therefore, the total runtime of the algorithm above is $O(1) + O(n+m) + O(n+m) + O(n) = O(n+m)$.

### proof of correctness
For Dijkstra's Algorithm to be correct:
1. $\pi(v) = min_{u \in S, v \in adj(u)} d(u) + l(u,v)$
2. If $u \in S$, then $d[u]$ is the length of the shortest path from $s$ to $u$
3. Every vertex $\in G$ does not have weights of $\infty$ if it is in $dist$.


The algorithm above checks if those three claims hold for the distnace hash table $dist$.

- The first claim is checked in the first case ($\pi(v) > min_{u \in S, v \in adj(u)} d(u) + l(u,v)$) in the algorithm to restrict upper bound of $\pi(v)$ for each node in $G$ to $\pi(v) = min_{u \in S, v \in adj(u)} d(u) + l(u,v)$.
- Then, with the BFS implementation to iterate through $G$ to verify the equality.
- Lastly, to check if every node on the shortest path is accounted, we run the last for loop to check if the node is in the candidate hash table and the node's weight is not $\infty$ or vise versa. 

Therefore, the algorithm always verify the distance produced by Dijkstra's algorithm that $dist$ is the shortest path.

## (b) parent reconstruction algorithm
### pseudocode

```
reconstruct(G,s,dist):
    // the maximum length of the parent hash table is the number of vertices; space: O(n)
    parents <- initialized hash table with n null values filled // n = |V|

    // loop through the adjacency table to find parents of each node that goes through shortest path
    // dijkstra's algorithm determines the shortest path with greedy approach that selects a node of a distance that equals the sum of minimum total distance of previous node that directs to current node and the weight
    for u in G:
        for v in G[u]:
            weight <- G[u][v] // weight extraction from G

            // Three conditions to check
            // 1. make sure it is not a source node; it is root such that it cannot have a parent
            // 2. make sure we visited that node so dist[u] cannot be infinity
            // 3. distance of current node must be equal to the sum of minimum distance of previous node and its weights
            if v != s AND dist[u] != ∞ AND dist[v] == dist[u] + weight:
                parents[v] <- u // update parent if passed the conditions
    return parents
```

### Runtime analysis
1. $O(n)$: `parents` hash table initialization. Because we have to initialize it with at most length $n$, we need to iterate through $n$ times to assign `null` value to each index of `parents`
2. $O(n+m)$: we have to iterate through $G$ adjacency graph to check the conditions given above. while condition check only takes constant time $O(1)$, we need to check for every vertex to find the parent nodes through the shortest path possible.

Therefore, the total runtime of the algorithm is $O(n) + O(n+m) = O(n+m)$.

### Proof of Correctness
To reconstruct `parents` hash table, a table that contains the parent of each node that goes through the shortest path, 
1. we have to make sure if `dist` given is valid
2. we have to make sure if the parent of the source node does not exist
3. we have to make sure if all nodes in `parents` were visited  
4. we have to make sure if the distance of child node must equal to the sum of the minimum distance of parent node and the edge weight between the child and its parent nodes. 

1. Because the algorithm assumes that the output of (a) is "correct", `dist` must always be valid and contains the shortest path from the source to every node in $G$.
2,3, and 4 are checked at if condition inside the for loop that iterates through the adjacency list $G$.

Therefore, the algorithm above always reconstructs `parents` hash table that has every edge between nodes through the shortest path correctly.

## (c)
