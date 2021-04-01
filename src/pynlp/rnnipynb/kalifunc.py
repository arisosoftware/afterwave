# coding: utf-8
import math
import random
import matplotlib.pyplot as plt
import uuid
from collections import Counter
from pandas import DataFrame as df
import numpy as np

# kelly 公式，b代表赔率，p代表获胜概率
def kelly(b, p):
    return (p * (b + 2) - 1) / b

def playAndSave(rank,b, p, n, pre_money):
    # b 赔率，p 获胜概率 n 下注次数
    # 赔率计算方式：赚的钱(包含本金) / 亏损的钱
    # pre_money 本金
    # result 投注比例
    # r 每次投入本金比例    
    bidRate = kelly(b, p) +0.1
    winAlotRate = 5
    winAlot = pre_money * winAlotRate
    count = 0
    if rank == 1:
        print("本金%.4f\n赔率为%.4f\n胜率为%.4f\n投注比例 %.4f" % (pre_money, b, p, bidRate))

    money = pre_money
    bidMoney =0
    x = [1]
    y = [pre_money]
    for i in range(2, n + 1):
        te = random.random()
        bidMoney  = round ( money * bidRate *1.0)
        #print (bidMoney)
        if bidMoney == 0:
            bidMoney = money
       
        if te <= p:
            money = money + bidMoney
            fee = round(bidMoney * 0.01)
            if (fee >0 ) :
                money = money - fee
        else:
            money = money - bidMoney

        x.append(i)
        y.append(money)
        count = count + 1
        if money <=0 :
            break
        if money > winAlot:
            break
    
        #if n <= 2000:
        #    print("%d #" % i, "随机 %.2f" % te, "错" if te > 0.5 else "对", "剩：%.2f" % money , "赌:%.2f" % bidMoney)
    #print("第%d轮, 运行%d次后，最后剩余%.2f" % (rank, count, money))    
    filename = "P"+ str(10000+rank)
    #print(filename)
    plt.plot(x, y)
    #plt.show()
    #plt.savefig(filename)

    return count, money


def drawHistGram(title ='fig1', data=[0], color='#0504aa'):
    n, bins, patches = plt.hist(x=data, bins='auto', color='#0504aa', alpha=0.7, rwidth=0.85)
    plt.grid(axis='y', alpha=0.75)
    plt.xlabel('Value')
    plt.ylabel('Frequency')
    plt.title(title)
    maxfreq = n.max()
    plt.ylim(ymax=np.ceil(maxfreq / 10) * 10 if maxfreq % 10 else maxfreq + 10)

 

if __name__ == '__main__':
    x = []
    eachrankCount = []
    eachrankMoney = []
    totalPlayer = 100
    predefineMoney = 100
    winner = 0
    for r in range(1, totalPlayer):
        count, money = playAndSave (rank =r, b = 2.0, p=0.5, n=250, pre_money=predefineMoney)
        print("玩家%-2d, 玩 %-3d轮后，钱包还剩 %5.0f" % (r, count, money))
        x.append(r)
        eachrankCount.append(count)
        eachrankMoney.append(money)
        if money > predefineMoney:
            winner = winner + 1
     
    payToPlayer = sum(eachrankMoney)
    playerPaid =  totalPlayer * 100
    winorloss  = playerPaid - payToPlayer

    print ("赌场赔付 %d 赌资收入 %d 盈利 %d  赢家人数 %-3d" %(payToPlayer,playerPaid, winorloss, winner ))
    plt.savefig("f1.png")
    plt.figure()
    
    #eachrankMoney = [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 575.0, 525.0, 0.0, 0.0, 0.0, 0.0, 0.0, 525.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 325.0, 0.0, 0.0]
    print(eachrankCount)
    print(eachrankMoney)
    drawHistGram('Money Balance', eachrankMoney)
    plt.savefig("f2.png")
    #plt.figure()
    #drawHistGram('Play ranks', eachrankCount)
    #plt.savefig("f3.png")
    plt.show()
    
    