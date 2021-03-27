import os
os.environ["TF_CPP_MIN_LOG_LEVEL"] = "2"
def warn(*args, **kwargs):
    pass
import warnings
warnings.warn = warn

from dataset import tokenizer
from model import model
import config
import utils
import argparse

# 加载训练好的模型
model.load_weights(config.BEST_MODEL_PATH)

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description='诗歌生成器  功能：1.随机生成一首诗  2.根据开头生成后面的诗句  3.生成藏头诗')
    parser.add_argument('--start','-s', help='生成的开头')
    parser.add_argument('--acrostic','-a' ,help='藏头诗')
    parser.add_argument('--number','-n' ,help='五言、七言')
    parser.add_argument('--count','-c' ,help='律诗、绝句')
    args = parser.parse_args()
    
    # 给出部分信息的情况下，随机生成剩余部分
    if args.start:
        print(utils.generate_random_poetry(tokenizer, model, s=args.start, num=args.number, count=args.count))
    # 生成藏头诗
    elif args.acrostic:
        print(utils.generate_acrostic(tokenizer, model, head=args.acrostic, num=args.number))
    # 随机生成一首诗
    else:
        print(utils.generate_poetry(tokenizer, model, num=args.number, count=args.count))
