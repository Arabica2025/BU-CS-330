import time
import matplotlib.pyplot as plt
import csv

def coinChoices(a,b,c,n):
    assert type(a) == type(b) == type(c) == type(n) == int
    assert a>0 and b>0 and c>0 and n>0
    max_i = min(a, n)
    max_j = min(b, n // 2)
    max_k = min(c, n // 3)
    count = 0
    for i in range(max_i + 1):
        for j in range(max_j+1):
            for k in range(max_k + 1):
                if i + 2 * j + 3 * k == n:
                    count += 1
                    print ((i,j,k), "is a way to get value", n)
    return count
print(coinChoices(8,8,8,8))
# powersoftwo = [2**x for x in range(4,12)]

# hw1-1(a) Runtime Analysis 
def runtime():
    run_times=[]
    for n in powersoftwo:
        tick = time.perf_counter()
        print("n = ",n)
        coinChoices(n,n,n,n)
        tock = time.perf_counter()
        print(tock-tick, "seconds")
        run_times.append(tock-tick)
    return run_times
def save_run_times(run_times, filename):
    with open(filename, 'w') as f:
        writer = csv.writer(f)
        for row in run_times:
            writer.writerow([row])
    print("Run times saved to", filename)


# runtime()
# save_run_times(runtime, "run_times.csv")

def plot():
    w = 5*10**-9
    cubes = [w*n**3 for n in powersoftwo]
    run_times = []
    with open("run_times.csv", 'r') as f:
        reader = csv.reader(f)
        for row in reader: 
            run_times.append(float(row[0]))
    plt.plot(powersoftwo, run_times, "r.-")
    plt.plot(powersoftwo, cubes, "b--")
    plt.yscale('log')
    plt.xscale('log')
    plt.xlabel("n")
    plt.ylabel("time (s)")
    plt.show()


# plot()
