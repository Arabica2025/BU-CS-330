def SlowFun(B: list[int],r: int) -> list[int]:
    n = len(B)
    A = [0 for _ in range(n-r)]
    for i in range(len(A)):
        for j in range(n-r-i):
            A[i] += B[j+i]
    return A

print("SlowFun array A:",SlowFun([1,2,3,4,5],2))

def FastFun(B: list[int], r: int) -> list[int]:
    n = len(B)
    A = [0 for _ in range(n-r)]
    # B_prime = [0 for _ in range(n)]
    for i in range(len(B)-r):
        A[0] += B[i]
        # B_prime[i] = A[0]
        
    print("FastFun A[0]:",A[0])
    print("FastFun array A before updates:",A)
    # print("B':",B_prime)
    
    # for j in range(n-r-1):
    #     A[j+1] = B_prime[len(B_prime)-1] - B_prime[j]
    # return A
    
    # for i in range()

    
    # riter = 0
    # A[riter]= sum(i for i in range(B)[r-1])
    
    # return FastFun(B, riter+1)
    
    
    for i in range(n-r-1):
        A[i+1] = A[i] - B[i]
    return A
    
# print(SlowFun([1,2,3,4,5],2))
# print(SlowFun([1,2,3,4], 2))    
print("FastFun Final A:",FastFun([1,2,3,4,5],2))