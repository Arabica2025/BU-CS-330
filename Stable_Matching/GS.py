import numpy as np

class GS:
    def __init__(self, s: list[list[int]], h: list[list[int]]): # s: student, h: hospital; they are both containing the ranking of preference to each other
        self.s = s
        self.h = h

    def stable_matching(self):
        # Implement the Gale-Shapley algorithm for stable matching
        # initialize each hos and student to be free
        assert len(self.s) == len(self.h), "The number of students and hospitals must be equal."
        s_isMatched = np.zeros(len(self.s)) # instead of bool, use binary; 0: false, 1: true
        h_isMatched = np.zeros(len(self.h))
        
        proposal_num = np.zeros(len(h_isMatched))
        while ()
