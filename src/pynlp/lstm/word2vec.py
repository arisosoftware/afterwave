import os
import gensim
import json
import numpy as np
from sys import exit
import urllib

'''
from flask import Flask, jsonify
from flask import request
from gevent.pywsgi import WSGIServer

import sys
defaultencoding = 'utf-8'

if sys.getdefaultencoding() != defaultencoding:
    reload(sys)
    sys.setdefaultencoding(defaultencoding)

app = Flask(__name__)
'''

class ModelLoader():
    def __init__(self):
        self.word2vec_model = self.load_word2vec()

    def load_word2vec(self):
        model = gensim.models.KeyedVectors.load_word2vec_format('./lstm/models/sgns.sikuquanshu.bigram')
        print("word2vec model load successful!!")
        return model

    def get_word_vector(self, word):
        try:
            output_vector = self.word2vec_model[word]
        except KeyError:
            if len(word) == 1:
                return np.empty(0)
            count = 0
            output_vector = np.zeros(300)
            for char in word:
                try:
                    vector = self.word2vec_model[char]
                except KeyError:
                    continue

                output_vector = output_vector + vector
                count = count + 1
            if count == 0:
                return np.empty(0)
            output_vector = output_vector / count

        return output_vector
    
    def get_word2vec_model(self):
        return self.word2vec_model

    # 余弦相似度
    def cos_sim(self, vector_a, vector_b):
        vector_a = np.mat(vector_a)
        vector_b = np.mat(vector_b)
        num = float(vector_a * vector_b.T)
        denom = np.linalg.norm(vector_a) * np.linalg.norm(vector_b)
        cos = num / denom
        sim = 0.5 + 0.5 * cos
        return sim

    def get_similarity(self, keyword):
        vector1 = self.get_word_vector(keyword)
        with open('./lstm/data/key_center.txt', encoding='utf-8') as f:
            keyword_center = json.load(f)
        best_similar = 0
        best_class = -1
        for (k,v) in  keyword_center.items():
            vector2 = self.word2vec_model[v]
            similar = self.cos_sim(vector1, vector2)
            if similar > best_similar:
                best_class = k
        return best_class

'''
@app.route('/getVector', methods=['GET'])
def get_vector():
    word = request.args.get("word")
    word = urllib.parse.unquote(word)
    res = model_loader.get_word_vector(word)

    return json.dumps(res.tolist(),ensure_ascii=False)

@app.route('/getCenter', methods=['GET'])
def get_center():
    content = request.args.get("content")
    content = urllib.parse.unquote(content)
    words = []
    for i in content:
        if i != '，' and i != '。':
            words.append(i)

    word = model_loader.get_word2vec_model().doesnt_match(words)

    return json.dumps(word,ensure_ascii=False)
'''

if __name__ == '__main__':
    model_loader = ModelLoader()
    # WSGIServer(('127.0.0.1', 5000), app).serve_forever()