import matplotlib.pyplot as plt
import csv
from pathlib import Path

powersoftwo = [2**x for x in range(4,12)]

def plot():
    w = 5*10**-9
    # cubes = [w*n**3 for n in powersoftwo]
    square = [w*n**2 for n in powersoftwo]
    original_run_times = []
    # with open("run_times.csv", 'r') as f:
    #     reader = csv.reader(f)
    #     for row in reader: 
    #         original_run_times.append(float(row[0]))
    asym_run_times = []
    with Path(__file__).with_name("asym_run_times.csv").open() as f:
        reader = csv.reader(f)
        for row in reader:
            asym_run_times.append(float(row[0]))
    
    plt.plot(powersoftwo, asym_run_times, "r.-", label=r'$t = wF(n)$ (Asymptotically faster Coin Choice Algorithm)')
    # plt.plot(powersoftwo, original_run_times, "r.-")
    plt.plot(powersoftwo, square, "b--", label=r'Baseline: $t = wn^2$, $w = 5 \times 10^{-9}$')
    plt.yscale('log')
    plt.xscale('log', base=2)
    plt.xticks(powersoftwo, [str(n) for n in powersoftwo])
    plt.xlabel("n")
    plt.ylabel("time (s)")
    plt.legend()
    plt.show()
    
plot()
