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
// 1. first, check if there is a valid path to the half way
// cannot figure out the path if we cannot even have a valid path to the half way.

half_way_G <- BFS(G, h) // half way path value

if half_way_G is None: // if we don't have one, return bool: false
    return False

level_rev <- hash table
parent_rev <- hash table
level_rev[half_way_G[0].length - 1] = 0, parent_rev[half_way_G[1].length - 1]=0, Q.enqueue(half_way_G[1][half_way_G[1].length-1])

while !Q.isEmpty():
    u_prime <- Q.dequeue();
    for v_prime in G[u_prime] from G[u_prime].length - 1 to 0:
        if v_prime not in level_rev or v_prime not in half_way_G[0]:
            level_rev[v_prime] = half_way_G[0][u_prime]+1;
            parent_rev[v_prime] = u_prime;
            Q.enqueue(v_prime);
level_enum <- enumerate(half_way_G[0], level_rev)
parent_enum <- enumerate(half_way_G[1], parent_rev)

return level_enum, parent_enum
```