def A(n):
    if n == 0:
        return 0
    if n % 2 == 0:
        return A(n//2)
    else:
        return A(2*(n-1)) + 1


import numpy as np
class Grid:
    def __init__(self, cx: int, cy: int, n: int):
        self.n = n # input n value 
        randNumGen = np.random.default_rng()
        self.cx = cx if cx is not None else randNumGen.integers(1, n) # random x coordinate of covered cell
        self.cy = cy if cy is not None else randNumGen.integers(1, n) # random y coordinate of covered cell


        # Check bounds properly using working assertions
        # Program keeps moving ONLY if assertions evaluate to True
        assert 1 <= self.cx < self.n, f"cx ({self.cx}) must be between 1 and {self.n - 1}"
        assert 1 <= self.cy < self.n, f"cy ({self.cy}) must be between 1 and {self.n - 1}"

        k: int = int(np.log2(n)) # k = log_2(n)
        self.inst_floor = np.zeros((2**k, 2**k)) # initialize n * n grid of size 2**k * 2**k 

        self.TL = ((self.cx, self.cy+1), (self.cx+1,self.cy+1), (self.cx+1, self.cy))
        self.TR = ((self.cx-1, self.cy), (self.cx-1, self.cy+1), (self.cx, self.cy+1))
        self.BR = ((self.cx-1, self.cy), (self.cx-1, self.cy-1), (self.cx, self.cy-1))
        self.BL = ((self.cx-1, self.cy), (self.cx-1, self.cy-1), (self.cx, self.cy-1)) 

    def carpet(self, in_n: int, cover: tuple[int, int] = (self.cx, self.cy)) -> list[tuple[int, int, str]]:
        


