import lstm
import PoemGen
import argparse

from flask import Flask, jsonify
from flask import request
import json

# from gevent import monkey
from gevent.pywsgi import WSGIServer
# monkey.patch_all()

app = Flask(__name__)

@app.route('/get-poetry', methods=['GET'])
def getPoetry():
    modelType = request.args.get("model")
    geType = request.args.get("generationType")
    poeType = request.args.get("poetryType")
    keyword = request.args.get("keyword")
    num = 5
    count = 4
    if poeType == '七言绝句':
        num = 7
        count = 4
    elif poeType == '五言律诗':
        num = 5
        count = 8
    elif poeType == '七言律诗':
        num = 7
        count = 8
    
    if modelType == 'LSTM':
        res = lstm.getPoetry(geType, poeType, keyword, num, count)
    elif modelType == 'GPT2':
        res = PoemGen.getPoetry(geType, poeType, keyword, num, count)

    return res

if __name__ == '__main__':
    WSGIServer(('127.0.0.1', 5001), app).serve_forever()