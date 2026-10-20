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
    distances = {}
    finalized = {}
    parents = {}
    in_queue = {}
    Q = MinPriorityQueue()
    distances[s] = 0.0
    parents[s] = None
    Q.insert(s, distances[s])
    in_queue[s] = True
    while not Q.is_empty():
        u, d = Q.extract_min()
        if u not in finalized:
            finalized[u] = True
            for v in G.adj[u]:
                new_length = distances[u] + G.adj[u][v]
                if v not in distances or new_length < distances[v]:
                    distances[v] = new_length
                    parents[v] = u
                    if v in in_queue:
                        Q.decrease_key(v, distances[v])
                    else:
                        Q.insert(v, distances[v])
                        in_queue[v] = True
    return distances, parents
