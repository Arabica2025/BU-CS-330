############################################################
# A simple OO graphs package
# Version 2.0
############################################################

import csv
import math
import queue
import random
from MinIndexedPriorityQueue import MinPriorityQueue


class Graph:
    # Core representation:
    #   self.adj[u] is u's adjacency dictionary
    #   self.adj[u][v] is the edge weight from u to v
    def __init__(self, directed=True):
        self.directed = directed
        self.n = 0
        self.m = 0
        self.adj = {}

    ############################################################
    # Part 1: Basic graph edits and queries
    ############################################################

    def _name(self, u):
        # Store every node name as a string for consistency.
        return str(u)

    def add_node(self, u):
        u = self._name(u)
        if u not in self.adj:
            self.adj[u] = {}
            self.n += 1
        return self

    def add_edge(self, u, v, weight=1.0):
        u, v = self._name(u), self._name(v)
        self.add_node(u)
        self.add_node(v)
        if v not in self.adj[u]:
            self.m += 1
        self.adj[u][v] = float(weight)
        if not self.directed:
            if u not in self.adj[v]:
                self.m += 1
            self.adj[v][u] = float(weight)
        return self

    def add_undir_edge(self, u, v, weight=1.0):
        u, v = self._name(u), self._name(v)
        self.add_node(u)
        self.add_node(v)
        if v not in self.adj[u]:
            self.m += 1
        if u not in self.adj[v]:
            self.m += 1
        self.adj[u][v] = float(weight)
        self.adj[v][u] = float(weight)
        return self

    def del_undir_edge(self, u, v):
        u, v = self._name(u), self._name(v)
        assert u in self.adj and v in self.adj
        assert v in self.adj[u] and u in self.adj[v]
        del self.adj[u][v]
        del self.adj[v][u]
        self.m -= 2
        return self

    def degree(self, u):
        return len(self.adj[self._name(u)])

    def copy(self):
        H = Graph(directed=self.directed)
        H.n, H.m = self.n, self.m
        H.adj = {u: dict(self.adj[u]) for u in self.adj}
        return H

    def reverse(self):
        H = Graph(directed=True)
        for u in self.adj:
            H.add_node(u)
        for u in self.adj:
            for v, w in self.adj[u].items():
                H.add_edge(v, u, w)
        return H

    def equals(self, H):
        if self.n != H.n or self.m != H.m or len(self.adj) != len(H.adj):
            return False
        for u in self.adj:
            if u not in H.adj or len(self.adj[u]) != len(H.adj[u]):
                return False
            for v in self.adj[u]:
                if v not in H.adj[u] or self.adj[u][v] != H.adj[u][v]:
                    return False
        return True

    def make_undirected(self):
        for u in list(self.adj.keys()):
            for v in list(self.adj[u].keys()):
                if u not in self.adj[v]:
                    self.add_edge(v, u, self.adj[u][v])
        self.directed = False
        return self

    def check_cycle(self, node_list):
        nodes = [self._name(x) for x in node_list]
        cost = 0.0
        for i in range(len(nodes)):
            x = nodes[i]
            y = nodes[(i + 1) % len(nodes)]
            if y not in self.adj[x]:
                return False, "Reported nodes do not form a cycle."
            cost += self.adj[x][y]
        return True, cost

    def edges(self):
        return [(u, v, self.adj[u][v]) for u in self.adj for v in self.adj[u]]

    def print_edges(self):
        for u, v, w in self.edges():
            if w == 1.0:
                print(f"({u}, {v})")
            else:
                print(f"({u}, {v}, {w})")

    ############################################################
    # Part 2: File I/O (CSV format)
    ############################################################

    def write_csv(self, filename):
        with open(filename, "w", newline="") as f:
            out = csv.writer(f)
            out.writerow(["u", "v", "w"])
            for u in self.adj:
                if len(self.adj[u]) == 0:
                    out.writerow([u, "", ""])
                for v, w in self.adj[u].items():
                    out.writerow([u, v, w])
        return self

    @classmethod
    def read_csv(cls, filename, directed=True):
        G = cls(directed=directed)
        with open(filename, newline="") as f:
            for row in csv.DictReader(f):
                u = (row.get("u") or "").strip()
                v = (row.get("v") or "").strip()
                w = (row.get("w") or "").strip()
                if not u:
                    continue
                if not v:
                    G.add_node(u)
                else:
                    G.add_edge(u, v, 1.0 if w == "" else float(w))
        return G

    ############################################################
    # Part 3: Factory methods (build common graphs)
    ############################################################

    @classmethod
    def empty_graph(cls, n, directed=True):
        G = cls(directed=directed)
        for i in range(n):
            G.add_node(i)
        return G

    @classmethod
    def cycle_graph(cls, n, weight=1.0):
        G = cls.empty_graph(n, directed=False)
        for i in range(n):
            G.add_undir_edge(i, (i + 1) % n, weight)
        return G

    @classmethod
    def complete_graph(cls, n, weight=1.0):
        G = cls.empty_graph(n, directed=True)
        for i in range(n):
            for j in range(i + 1, n):
                G.add_edge(i, j, weight)
                G.add_edge(j, i, weight)
        return G

    @classmethod
    def random_er_graph(cls, n, p, seed=None):
        rng = random.Random(seed)
        G = cls.empty_graph(n, directed=False)
        for i in range(n):
            for j in range(i + 1, n):
                if rng.random() < p:
                    G.add_undir_edge(i, j)
        return G

    @classmethod
    def random_er_graph_fast(cls, n, p, seed=None):
        rng = random.Random(seed)
        G = cls.empty_graph(n, directed=False)
        if p <= 0:
            return G
        if p >= 1:
            return cls.complete_graph(n, weight=1.0).make_undirected()

        def geometric_step():
            # Sample k >= 1 with P[k=t] = (1-p)^(t-1)p
            return 1 + int(math.log(1.0 - rng.random()) / math.log(1.0 - p))

        for i in range(n):
            j = i + geometric_step()
            while j < n:
                G.add_undir_edge(i, j)
                j = j + geometric_step()
        return G

    @staticmethod
    def _sample_without_replace(n, d, exclude, rng):
        sample = [exclude]
        for _ in range(d):
            nbr = rng.randrange(n)
            while nbr in sample:
                nbr = rng.randrange(n)
            sample.append(nbr)
        return sample[1:]

    @classmethod
    def random_digraph_degree_bound(cls, n, d, seed=None):
        rng = random.Random(seed)
        G = cls.empty_graph(n, directed=True)
        for i in range(n):
            out_list = cls._sample_without_replace(n, d, i, rng)
            for nbr in out_list:
                G.add_edge(i, nbr, rng.random())
            in_list = cls._sample_without_replace(n, d, i, rng)
            for nbr in in_list:
                G.add_edge(nbr, i, rng.random())
        return G

    @classmethod
    def random_signed_digraph(cls, n, d, q, seed=None):
        G = cls.random_digraph_degree_bound(n, d, seed=seed)
        rng = random.Random(seed)
        for u in G.adj:
            for v in G.adj[u]:
                G.adj[u][v] = -1.0 if rng.random() < q else 1.0
        return G

    @classmethod
    def one_neg_cycle(cls, n):
        G = cls.empty_graph(n, directed=True)
        for i in range(n):
            G.add_edge(i, (i + 1) % n, 1.0)
        G.adj["0"]["1"] = float(-n)
        return G

    ############################################################
    # Part 4: Traversals and shortest paths
    ############################################################

    def bfs(self, s):
        s = self._name(s)
        distances = {}
        finalized = {}
        parents = {}
        layers = [[] for _ in range(self.n)]
        Q = queue.Queue()
        distances[s] = 0
        parents[s] = None
        Q.put(s)
        while not Q.empty():
            u = Q.get()
            if u not in finalized:
                finalized[u] = True
                layers[distances[u]].append(u)
                for v in self.adj[u]:
                    if v not in distances:
                        distances[v] = distances[u] + 1
                        parents[v] = u
                        Q.put(v)
        return distances, parents, layers

    def dfs(self):
        color = {}
        discovered = {}
        finished = {}
        parent = {}
        for u in self.adj:
            color[u] = "white"
            parent[u] = None
        timestamp = [0]

        def visit(u):
            color[u] = "gray"
            timestamp[0] += 1
            discovered[u] = timestamp[0]
            for v in self.adj[u]:
                if color[v] == "white":
                    parent[v] = u
                    visit(v)
            color[u] = "black"
            timestamp[0] += 1
            finished[u] = timestamp[0]

        for u in self.adj:
            if color[u] == "white":
                visit(u)
        return discovered, finished, parent

    def dfs_stack(self):
        color = {}
        discovered = {}
        finished = {}
        parent = {}
        stack = []
        for u in self.adj:
            color[u] = "white"
            parent[u] = None
            stack.append((u, "discover"))
        timestamp = 0

        while stack:
            u, task = stack.pop()
            if task == "discover":
                if color[u] == "white":
                    color[u] = "gray"
                    timestamp += 1
                    discovered[u] = timestamp
                    stack.append((u, "finish"))
                    for v in self.adj[u]:
                        if color[v] == "white":
                            parent[v] = u
                            stack.append((v, "discover"))
            else:
                color[u] = "black"
                timestamp += 1
                finished[u] = timestamp
        return discovered, finished, parent

    def dijkstra(self, s):
        s = self._name(s)
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
                for v in self.adj[u]:
                    new_length = distances[u] + self.adj[u][v]
                    if v not in distances or new_length < distances[v]:
                        distances[v] = new_length
                        parents[v] = u
                        if v in in_queue:
                            Q.decrease_key(v, distances[v])
                        else:
                            Q.insert(v, distances[v])
                            in_queue[v] = True
        return distances, parents
