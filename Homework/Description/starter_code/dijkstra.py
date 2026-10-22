from MinIndexedPriorityQueue import MinPriorityQueue
import simplegraphsPA1 as sg

"""
The task of the coding homework is for you to edit the implementation below of Dijkstra 
so that if there are tied shortest paths you will return the one with the fewest edges. 
E.G. if we have a graph with three nodes a,b,c and three edges (a->b weight 1) (b->c weight 1) and (a -> c weight 2)
that you will always pick the path a->c. Note what this means: you should return the SAME distances that 
normal Dijkstra does. BUT you have to return a valid shortest paths tree with this new edge count constraint. 

The goal of this problem is to give you practice thinking about Dijkstra and to make sure you 
understand what it is doing and what you can do with weights. Also, we hope that thinking deeply about the 
pseudocode/code will help you get a sense of what is happening. 

Feel free to look at the PQ implementation in the other file. 
"""


def dijkstra(G: sg.Graph, s: str):
    """
    This is an implementation of the Dijkstra algorithm from class (as discussed in class)

    Graph format: G is a simplegraphsPA1.Graph instance

    Returns:
        d: a dict mapping node -> shortest distance from s
        parents: dict mapping node -> parent in shortest-path tree (s has parent None)
    """
    distances: dict[str, float] = {} # dist hsh tbl; the shortest-path distance from s to each reachable vertex v
    parents: dict[str,str] = {} # parents hsh tbl; the vertex immediately preceding v on its chosen path from s.
    steps: dict[str, int] = {} # steps hsh tbl; records the number of edge hops took to get to the node from the source
    finalized = {} # store nodes that we finished visiting (optimized)
    
    
    Q = MinPriorityQueue()
    distances[s] = 0.0 # init dist
    steps[s] = 0
    parents[s] = None # init parents; source does not have parents
    
    Q.insert(s, (distances[s], steps[s])) # init queue: key: node: str, value: pair of distance and steps (float, int)
    
    ## basically Dijkstra but one condition added: 
    ### if we have multiple paths with same cumulative weights from source to the destination, 
    ### we pick the path with the fewest steps to that path
    while not Q.is_empty():
        u, prioirty = Q.extract_min()
        if u not in finalized:
            finalized[u] = True
            # Check the adjacent nodes of each node and update new length
            for v in G.adj[u]:
                new_length = distances[u] + G.adj[u][v]
                new_steps = steps[u] + 1
                if v not in distances: # we make sure we have all nodes visited
                    distances[v] = new_length
                    steps[v] = new_steps
                    parents[v] = u
                    Q.insert(v, (new_length, new_steps))
                # additional condition: through each iteration for each node, 
                ## 1. invariant of Djikstra's algorithm for distance comparison
                ## 2. path with the fewest step hops if the weights are same
                ### if both 1 and 2 are true, we omit the nodes on the not-fewest path
                elif new_length < distances[v] or (new_length == distances[v] and new_steps < steps[v]):
                    distances[v] = new_length
                    steps[v] = new_steps
                    parents[v] = u
                    Q.decrease_key(v, (new_length, new_steps))
    return distances, parents
