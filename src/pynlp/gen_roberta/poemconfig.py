import configparser
import os
import json



>>> import configparser
 config = configparser.ConfigParser()
 config['DEFAULT'] = {'ServerAliveInterval': '45',
                      'Compression': 'yes',
                      'CompressionLevel': '9'}
 config['bitbucket.org'] = {}
 config['bitbucket.org']['User'] = 'hg'
 config['topsecret.server.com'] = {}
 topsecret = config['topsecret.server.com']
 topsecret['Port'] = '50022'     # mutates the parser
 topsecret['ForwardX11'] = 'no'  # same here
 config['DEFAULT']['ForwardX11'] = 'yes'
 with open('example.ini', 'w') as configfile:
...   config.write(configfile)
 json.loads(config.get("Foo","fibs"))
[1, 1, 2, 3, 5, 8, 13]


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

iniConfig = configparser.ConfigParser(allow_no_value=True)

# File does not exist, let's create it
if os.path.exists('train.ini') == False:
    iniConfig = 
    
    
    f = open('name', 'w')
    f.write('sometext')
    f.close()

# File exists, let's check if it's empty and write into it
# or if there's some text, write nothing
else:
    f = open('name', 'rw')
    prev_text = f.read()

    # File was empty
    if prev_text == '':
        f.write('sometext')
    # File already had some text
    else:
        print 'This name already exists'

    f.close()