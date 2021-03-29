import configparser
import os
import json



# 预训练的模型参数
CONFIG_PATH = './logs/bert_config.json'
CHECKPOINT_PATH = './logs/bert_model.ckpt'
DICT_PATH = './logs/vocab.txt'
# 禁用词，包含如下字符的唐诗将被忽略
DISALLOWED_WORDS = ['（', '）', '(', ')', '__', '《', '》', '【', '】', '[', ']']
# 句子最大长度
MAX_LEN = 64
# 最小词频
MIN_WORD_FREQUENCY = 5
# 训练的batch size
BATCH_SIZE = 32
# 数据集路径
DATASET_PATH = './ccpc_train_v1.0.json'
# 共训练多少个epoch
TRAIN_EPOCHS = 20
# 最佳权重保存路径
BEST_MODEL_PATH = './best_model_roberta.h5'
# 学习率
LR=1e-4
