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
    return (p * (b + 1) - 1) / b

def playAndSave(round,b, p, n, pre_money):
    # b 赔率，p 获胜概率 n 下注次数
    # 赔率计算方式：赚的钱(包含本金) / 亏损的钱
    # pre_money 本金
    # result 投注比例
    # r 每次投入本金比例    
    bidRate = kelly(b, p)
    count = 0
    if round == 1:
        print("本金%.4f\n赔率为%.4f\n胜率为%.4f\n投注比例 %.4f" % (pre_money, b, p, bidRate))

    money = pre_money
    
    x = [1]
    y = [pre_money]
    for i in range(2, n + 1):
        te = random.random()
        bidMoney  = pre_money * bidRate;
        if te <= p:
            money = money + bidMoney
        else:
            money = money - bidMoney
            x.append(i)
            y.append(money)
        count = count + 1
        if money <=0 :
            break

        #if n <= 2000:
        #    print("%d #" % i, "随机 %.2f" % te, "错" if te > 0.5 else "对", "剩：%.2f" % money , "赌:%.2f" % bidMoney)
    #print("第%d轮, 运行%d次后，最后剩余%.2f" % (round, count, money))    
    filename = "Pic_"+ str(round)
    #print(filename)
    plt.plot(x, y)
    #plt.show()
    plt.savefig(filename)

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
    eachRoundCount = []
    eachRoundMoney = []
    totalPlayer = 100
    for r in range(1, totalPlayer):
        count, money = playAndSave (round =r, b = 2.0, p=0.5, n=250, pre_money=100)
        print("玩家%-2d, 玩 %-3d轮后，钱包还剩 %5.0f" % (r, count, money))
        x.append(r)
        eachRoundCount.append(count)
        eachRoundMoney.append(money)

    payToPlayer = sum(eachRoundMoney)
    playerPaid =  totalPlayer * 100
    winorloss  = playerPaid - payToPlayer

    print ("赌场赔付 %d 赌资收入 %d 盈利 %d" %(payToPlayer,playerPaid, winorloss ))
    plt.savefig("f1.png")
    plt.figure()
    
    #eachRoundMoney = [0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 575.0, 525.0, 0.0, 0.0, 0.0, 0.0, 0.0, 525.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 325.0, 0.0, 0.0]
    print(eachRoundCount)
    print(eachRoundMoney)
    drawHistGram('Money Balance', eachRoundMoney)
    plt.savefig("f2.png")
    plt.figure()
    drawHistGram('Play Rounds', eachRoundCount)
    plt.savefig("f3.png")
    plt.show()
    
    