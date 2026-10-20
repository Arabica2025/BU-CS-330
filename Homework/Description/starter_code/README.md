This starter code directory has the following files:

* `README.md`: This file
* `dijkstra.py`: The file you should modify
* `simplegraphsPA1.py`: The Python library we give you to work with graphs. Do not modify this file; you can read it to look at how it works, though!
* `MinIndexedPriorityQueue.py`: A Python priority queue. Do not modify this file; you can read it to look at how it works, though!
* `input_tests`: A folder with test inputs (described below)
* `output_tests`: A folder with outputs for the test inputs
* Files with names like `run_dijkstra_XYZ.py`. These run a bunch of tests for you (see below). You do not need to understand how they work.

You should submit only the file `dijkstra.py` (with your changes), via Gradescope.

# Visible Dijkstra tests

Run both test runners from this directory:

```sh
python3 run_dijkstra_sanity_tests.py
python3 run_dijkstra_file_tests.py
```

The four sanity checks are quick smoke tests. The file-based suite below is the complete visible set. The starter implementation intentionally fails cases 4 and 5.

## Matching local cases to Gradescope

Gradescope **1.i** runs exactly the CSV, source, and expected-output files for local **case i**. For example, Gradescope **1.5** corresponds to `input_tests/test_input_5.csv` and `input_tests/test_input_5.source.txt`; expected metrics are in `output_tests/test_output_5.json`.

| Gradescope | Case | Vertices | Edges | What it checks |
|---|---|---:|---:|---|
| 1.1 | Source only | 1 | 0 | Initialization and source parent |
| 1.2 | Weight before edge count | 3 | 3 | Weight takes priority over hops |
| 1.3 | Direct-edge tie | 3 | 3 | Fewest edges among shortest paths |
| 1.4 | Better tie discovered later | 5 | 5 | Equal-distance relaxation |
| 1.5 | Queue ordering and propagation | 10 | 11 | Hop improvement must propagate consistently |
| 1.6 | Zero-weight cycle | 4 | 6 | No parent cycles; source stays root |
| 1.7 | Ordinary distance improvement | 4 | 5 | Preserve distance relaxation and decrease-key |
| 1.8 | Disconnected graph | 6 | 3 | Unreachable component and isolated vertex |
| 1.9 | Two equally valid answers | 4 | 4 | Accept any optimal parent tree |
| 1.10 | Fractional weights | 4 | 5 | Do not round weights; zero-weight tie |
| 1.11 | Mixed regression and long chain | 8030 | 8141 | Mixed directed/fractional/zero-weight edges and path-length scaling |

## Understanding how graphs are handled

If you are confused by the code that manipulates graphs, you might find it helpful to read simplegraphsPA1.py to see what's going on "under the hood". It is a fairly straightforward implementation that uses the nested hash tables discussed in class.

## Inspecting the small cases

You can check the smaller test cases by hand by reading the CSV files that describe the inputs and the JSON files that describe the expected output. You can read both kinds of files in any text editor.

A CSV row `u,v,w` represents a directed edge of weight `w`. A row with only `u` declares a vertex. Every visible case except case 11 has at most ten vertices; cases 1–4 and 6–10 have at most six.

The JSON files contain expected distances and minimum edge counts for every vertex. `"inf"` means unreachable. `reference_parents` shows one valid answer; your tree may differ when both weight and edge count tie.

## How we check runtime

The timing rule is that, on Gradescope, your solution should run in at most 10 times the reference runtime recorded in each JSON file. If your solution is too slow on any of the test cases, you'll see that when you submit on Gradescope.

_Note:_ The test run by `run_dijkstra_file_tests.py` also reports timing failures, but its PASS/FAIL summary checks correctness only. Also, your machine may be substantially faster or slower than the one that Gradescope uses. So don't wait until the last minute to submit on Gradescope.
