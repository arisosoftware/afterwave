from nltk.translate.bleu_score import sentence_bleu
import json
import numpy as np

with open('./std_7_100.txt', encoding='utf-8') as f:
    std = json.load(f)
with open('./lstm_7_100.txt', encoding='utf-8') as f:
    data1 = json.load(f)
with open('./gpt_7_100.txt', encoding='utf-8') as f:
    data2 = json.load(f)
with open('./roberta_7_100.txt', encoding='utf-8') as f:
    data3 = json.load(f)
with open('./jiuge_7_100.txt', encoding='utf-8') as f:
    data4 = json.load(f)

score1 = 0
score2 = 0
score3 = 0
score4 = 0
for d in data1:
    score1 += sentence_bleu(std, d)
for d in data2:
    score2 += sentence_bleu(std, d)
for d in data3:
    score3 += sentence_bleu(std, d)
for d in data4:
    score4 += sentence_bleu(std, d)

print('LSTM:', score1 / len(data1))
print('GPT2:', score2 / len(data2))
print('roberta:', score3 / len(data3))
print('jiuge:', score4 / len(data4))