import gen_roberta
import gen_bert
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

    if modelType == 'roberta':
        res = gen_roberta.getPoetry(geType, poeType, keyword, num, count)
    elif modelType == 'bert':
        res = gen_bert.getPoetry(geType, poeType, keyword, num, count)
    
    return res


if __name__ == '__main__':
    WSGIServer(('127.0.0.1', 5000), app).serve_forever()