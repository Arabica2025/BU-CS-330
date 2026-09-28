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
        for char in range(c):
            checked <- false // checker if we already have the words in the alphabetical order; if true, we skip the second if statement. we need checker because we are not guaranteed that every letter will appear in the first position.
            if char not in G: // insert $L$ distinct character in the manuscript
                G[char] <- True
            if M[word][char] != M[word+1][char] AND checked == false:
                prereq <- M[word][char]
                following <- M[word+1][char]
                G[prereq][following] <- True
                break
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