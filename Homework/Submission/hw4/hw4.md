# 1. Elvish languages of Middle-Earth
- `developed`: if we can infer an alphabetical order from the manuscript
- `incomplete`: if we cannot.

## 1.1 Extracting relationships of characters from the manuscript
### Goal
- read the manuscript and extract the alphabetical relationship between the words and list the relsionhip of each character inferred.
- there is no "right" ordering at this moment; we need to figure out the right ordering from the output of this algorithm
- input: nested array of raw manuscript $M$
    - $M$: nested array; `M[i]` consists of the character array of word $i$. 
    ```JSON
        [
            [`$`, `%`, `%`, `*`],
            ...
        ]
    ```
- output: graph adjacency list $G$ as a nested hash table
### Pseudocode
```Pseudocode
ExtractCharRel(M):
    G <- empty hash table // initialize the output

/* Main Loop: loop through the entire nested array (in worst case): O(Wc)
W: the number of words in the array
c: the same constant length of each word
L: the number of distinct characters in the manuscript

However, since each word has the same constant number of $c$ characters, we can generalize Big O notation as $O(W)$.
*/
    for word in range(W-1):
        checked <- false // checker if we already have the words in the alphabetical order; if true, we skip the second if statement. we need checker because we are not guaranteed that every letter will appear in the first position.
        for char in range(c):
            if char not in G: // insert $L$ distinct character in the manuscript
                G[char] <- true
            if M[word][char] != M[word+1][char] AND checked == false:
                prereq <- M[word][char]
                following <- M[word+1][char]
                G[prereq][following] <- true
                checked <- true
    return G
```
### Runtime Analysis
1. $O(1)$: output initialization
2. $O(Wc)$: main loop. We iterate through every single character in each word in nested array. In fact, we assume that the length of each word is the same constant $c$, we can simplify the Big O notaiton as $O(W)$.
3. $O(L)$: since we have to worry about $L$ distinct characters in the manuscript, we have to execute INSERT operation $L$ times to $G$. However, since $L \le Wc$, we can ignore this in the final runtime analysis.
4. $O(1)$: simple `if` condition to check if the words are in alphabetical order.

Therefore, $O(1) + O(Wc) + O(L) + O(1) = O(W)$

### Proof of correctness
For the algorithm to be correct, there are two things to be proven:
1. There must be $L$ distinct vertices in $G$.
2. the edges between two vertices in $G$ must be directed in ascending order. In other words, the character comparison between words must be from left to right.

1. $L$ distinct vertices in $G$
    - the `if` statement to check if each character is in $G$ before inserting edges (alphabetical relationships between characters of words) ensures that the nested hash table $G$ has $L$ distinct vertices that possibly include singleton nodes that are disconnected
2. edges in ascending order
    - the main loop that iterates through $Wc$ times where $W$ is the number of words in the manuscript and $c$ is the constant number of characters of each character checks the alphabetical ascending order with `if` statements
    - suppose there two words of `M[word]` and `M[word+1]` that we compare to get the ascending order. To check the order of each character from the words in ascending order, `M[word][character] != M[word+1][character]`. The algorithm above explicitly checks this condition and insert the edge between each character from the comparing words to $G$. 

Therefore, the algorithm above correctly computes the relationship between characters of words in ascending order for every $L$ distinct character in the manuscript $M$.

## 1.2 Alphabetical Order Validation
### Pseudocode
```pseudocode
ValidAlphaOrder(G):
    /* 
    1. Goal: check if the given adjacency list of G represents alphabetical order.
    2. I/O:
        2.1 Input: adjacency list of "alphabetical order" of $L$ distinct characters from the Manuscript $M$.
        2.2 Output: 
            - If there exists alphabetical order: return the alphabetical order
            - If there not exsit alphabetical order: return "No alphabetical order"
    3. How:
        - application of Zero-indegree Algorithm that finds the order of the DAG. If cycle detected (or there exists a node with more than 1 indegree), we stop the algorithm. Otherwise, we traverse through the DAG and return the topological order of the DAG.
        - in our case, the order is alphabetical. However, if there is a cycle in the graph, then the relationship among $L$ distinct characters from tne Manuscript $M$ is not in alphabetical order.
        - If all nodes have zero indegree during the process of algorithm, then the characters are in alphabetical order.
    */
    // Zero-Indegrree algorithm that returns the topological order of the given graph $G$ with runtime of $O(m+n)$ where $m= |L|$ and $n=\text{the number of edges that defines the relationship between L distinct characters}$
    alpha_order <- ZeroIndegree(G) // if $v$ is discovered but unfinished (or cycle detected), then return "no alphabetical order"
    return alpha_order
```
### Runtime Analysis
1. $O(n+m)$: runtime of ZeroIndegree algorithm from the class that iterates through $G$ until either we detect a cycle (or not alphabetical order in this problem) or we traverse through every node in $G$. Because $G$ has $n=L$ distinct characters where $n$ denote the number of nodes and $m=$ the number of alphabetical order candidates from $M$.

Since the algorithm requires to output only either the alphabetical order if there exists a valid order that does not create a cycle or an exception that says there exists a cycle in the graph $G$, the algorithm only requires to implement ZeroIndegree algorithm and return its value. Therefore, the final runtime of the algorithm is $O(n+m)$.

### Proof of Correctness
lemma1: If $G$ has a topological order, the $G$ is a DAG.
Lemma2: ZeroIndegree(G) outputs either a topological order of $G$ or an exception with a cycle detected. 
Lemma3: If there is a cycle in graph $G$, then there exists a node $u$ remaining in $G$ after the execution of ZeroIndegree Algorithm.
Claim: If $G$ is in alphabetical order, then $G$ is in topological order.

According to ZeroIndegree algorithm, if it returns the topological order, then
1. it must terminate when it iterated through the entire input graph
2. the input graph $G$ must be empty by the time the algorithm terminates.

First, because alphabetical order does not allow a cycle as it would require a character to precede itself, if something is in alphabetical order, then that is acyclic. If an order is acyclic, then it can be represented as DAG. According to the ZeroIndegree Algorithm, it must remove every node and return a topological order that contains all $L$ distinct characters from $G$. By the definition of topological order, every edge from one node to another--$u \rightsquigarrow v$-- must place $u$ before $v$. Each edge represents the relationship of the first different character of consecutive word; therefore, every consequtive pair of distinct character is alphabetically ordered. Second, if the algorithm detects a cycle, since there exists no pairs that has a character that precede itself by the algorithm, the algorithm will simply not delete all nodes in $G$ and return "no alphabetical order." 

# 2. Trip planning




