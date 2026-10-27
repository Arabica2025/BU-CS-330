# 1. Maximum Clearance Routes
## (a) Cut and Cycle Property of *maximum* spanning tree.

$$
\text{Maximum Spanning tree G: a tree that connects all vertices in G }
\text{while maximizing the sum of the edge weights in the tree.}
$$

1. Cut Property: Let $i$ and $j$ be the nodes in the two sides of a cut. 
Let $e$ be the maximum weight across the cut. Then $e$ is in the Maximum Spanning Tree $G^{*}$

2. Cycle Property: Let $C$ be a cycle and let $f$ be the minimum weight edge in $C$. 
Then, $f$ is *not* in the Maximum Spanning Tree.

## (b) MaxST Proof
Let $a$ be another route between $(u,v) \in G$ that has a greater route weight than the original edge between $(u,v)$, $o \in G$ and that it creates a cycle.
Then, according to the cycle property of MaxST, we have to choose $a$ route to be MaxST. Here, using $o$ route contradicts the cycle property.
Since $o$ is in the MaxST, $o$ must have the maximum route weight; otherwise there would be contradiction in the cycle property. 
Therefore, any graph $G$ with unique edge clearance and disntinct route weights, the path given by using edges from the maximum spanning tree of $G$ gives the maximum clearance route of $G$.

## (c) MaxST Polynomial runtime Algorithm
### Pseudocode
```Pseudocode
MaxST(G):
    return ReverseKruskal(G) /*G: undirected, weighted adj list. Kruskal's Algoritm with edges sorted by decreasing order*/
```
### Runtime Analysis
The above pseudocode runs Kruskal's Algorithm with sorted edges by decreasing order. Since the sorting edges reversely does not change the runtime,
the total runtime of the above algorithm is same as Kruskal's Algorithm's runtime: $O(mlog(m))$.


