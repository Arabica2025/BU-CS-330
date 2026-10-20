#!/usr/bin/env python3
"""
run_dijkstra_sanity_tests.py

A lightweight local tester for starter_code/dijkstra.py. This runs a potential solution on a few small, hard-coded examples.

Make sure the following files are in the same directory
* dijkstra.py
* simplegraphsPA1.py
* MinIndexedPriorityQueue.py
"""

from math import inf
import sys
import traceback

import simplegraphsPA1 as sg

try:
    from dijkstra import dijkstra
except Exception:
    print("ERROR: Could not import dijkstra() from dijkstra.py\n")
    traceback.print_exc()
    sys.exit(1)


def reconstruct_path(parents, s, t):
    if t not in parents:
        return []
    path = []
    cur = t
    seen = set()
    while cur is not None:
        path.append(cur)
        if cur == s:
            return list(reversed(path))
        if cur in seen:
            return []
        seen.add(cur)
        cur = parents.get(cur, None)
    return []


def assert_equal(actual, expected, msg=""):
    if actual != expected:
        raise AssertionError(f"{msg}\nExpected: {expected}\nActual:   {actual}")


def assert_almost_equal(a, b, msg=""):
    if a == b:
        return
    raise AssertionError(f"{msg}\nExpected: {b}\nActual:   {a}")


def pretty_result(title, ok, details=""):
    mark = "✅" if ok else "❌"
    print(f"{mark} {title}")
    if details:
        print(details)
    if not ok:
        print()


def graph_triangle_tie():
    G = sg.Graph(directed=True)
    G.add_edge("s", "t", 2)
    G.add_edge("s", "a", 1)
    G.add_edge("a", "t", 1)
    return G, "s"


def graph_two_routes_len3():
    G = sg.Graph(directed=True)
    # Equal total cost to t:
    # s->a->b->t has cost 5 with 3 edges
    # s->z->t    has cost 5 with 2 edges (should be chosen by tie-break rule)
    G.add_edge("s", "a", 0)
    G.add_edge("a", "b", 0)
    G.add_edge("b", "t", 5)
    G.add_edge("s", "z", 4)
    G.add_edge("z", "t", 1)
    return G, "s"


def graph_zero_weight_tie():
    G = sg.Graph(directed=True)
    G.add_edge("s", "t", 0)
    G.add_edge("s", "x", 0)
    G.add_edge("x", "t", 0)
    return G, "s"


def graph_disconnected():
    G = sg.Graph(directed=True)
    G.add_edge("s", "a", 1)
    G.add_edge("a", "b", 2)
    G.add_node("u")
    return G, "s"


def test_triangle_tie():
    G, s = graph_triangle_tie()
    d, parents = dijkstra(G, s)

    assert_almost_equal(d["s"], 0, "d[s] should be 0")
    assert_almost_equal(d["a"], 1, "d[a] incorrect")
    assert_almost_equal(d["t"], 2, "d[t] incorrect")

    assert_equal(parents.get("s"), None, "parent[s] must be None")
    assert_equal(parents.get("t"), "s", "Tie-break failed: parent[t] should be 's' (fewest-edge path).")

    path = reconstruct_path(parents, s, "t")
    assert_equal(path, ["s", "t"], "Path s->t should be direct due to fewest-edge tie-break.")


def test_two_routes_len3():
    G, s = graph_two_routes_len3()
    d, parents = dijkstra(G, s)

    path = reconstruct_path(parents, s, "t")
    assert_almost_equal(d["t"], 5, "Shortest distance to t should be 5")
    assert_equal(path, ["s", "z", "t"],
                 "Tie-break failed: should choose path with fewer edges (s->z->t).")


def test_zero_weight_tie():
    G, s = graph_zero_weight_tie()
    d, parents = dijkstra(G, s)

    assert_almost_equal(d["t"], 0, "Shortest distance to t should be 0")
    path = reconstruct_path(parents, s, "t")
    assert_equal(path, ["s", "t"], "Zero-weight tie should still prefer fewest edges (direct edge).")


def test_disconnected():
    G, s = graph_disconnected()
    d, parents = dijkstra(G, s)

    assert_almost_equal(d["s"], 0, "d[s] should be 0")
    assert_almost_equal(d["a"], 1, "d[a] incorrect")
    assert_almost_equal(d["b"], 3, "d[b] incorrect")

    # Current starter dijkstra only returns reached nodes.
    assert_almost_equal(d.get("u", inf), inf, "Unreachable node should have distance inf")
    assert_equal(parents.get("u"), None, "Unreachable node should have parent None")


TESTS = [
    ("Triangle tie behavior", test_triangle_tie),
    ("Two equal-weight routes of length 3", test_two_routes_len3),
    ("Zero-weight tie behavior", test_zero_weight_tie),
    ("Disconnected node handling", test_disconnected),
]


def main():
    print("Running local Dijkstra tester for Graph-based starter code\n")
    passed = 0
    for title, fn in TESTS:
        try:
            fn()
            pretty_result(title, True)
            passed += 1
        except AssertionError as e:
            pretty_result(title, False, details=str(e))
        except Exception:
            pretty_result(title, False, details=f"Unexpected error:\n{traceback.format_exc()}")

    total = len(TESTS)
    print("\nSummary: {}/{} tests passed.".format(passed, total))
    if passed != total:
        sys.exit(1)


if __name__ == "__main__":
    main()
