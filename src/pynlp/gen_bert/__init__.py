import os
os.environ["TF_CPP_MIN_LOG_LEVEL"] = "2"
def warn(*args, **kwargs):
    pass
import warnings
warnings.warn = warn

from gen_bert.dataset import tokenizer
from gen_bert.model import model
import gen_bert.config
import gen_bert.utils
import argparse

config = gen_bert.config
utils = gen_bert.utils

import json

# 加载训练好的模型
model.load_weights(config.BEST_MODEL_PATH)

def getPoetry(geType, poeType, keyword, num = 5, count = 4):
    if geType == '藏头诗':
        outline = utils.generate_acrostic(tokenizer, model, head=keyword)
        while len(outline) != len(keyword) * (num + 1):
            outline = utils.generate_acrostic(tokenizer, model, head=keyword)
    elif geType == '开头诗':
        outline = utils.generate_random_poetry(tokenizer, model, s=keyword, num=num, count=count)
    else:
        outline = utils.generate_poetry(tokenizer, model, num=num, count=count)

    return json.dumps({'poetry': outline},ensure_ascii=False)

if __name__ == '__main__':
    print('bert')