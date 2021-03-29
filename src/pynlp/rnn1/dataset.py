from collections import defaultdict
from bert4keras.tokenizers import Tokenizer, load_vocab
from bert4keras.snippets import sequence_padding, DataGenerator
import numpy as np
import gen_roberta_config
import json

config = gen_roberta_config

# 预训练模型参数
config_path = config.CONFIG_PATH
checkpoint_path = config.CHECKPOINT_PATH
dict_path = config.DICT_PATH

# 禁用词
disallowed_words = config.DISALLOWED_WORDS
# 句子最大长度
max_len = config.MAX_LEN
# 最小词频
min_word_frequency = config.MIN_WORD_FREQUENCY
# mini batch 大小
batch_size = config.BATCH_SIZE

# # 加载数据集 结果放在poetry 列表中
# with open(config.DATASET_PATH, 'r', encoding='utf-8') as f:
#     lines = f.readlines()
#     # 将冒号统一成相同格式
#     lines = [line.replace('：', ':') for line in lines]
# 数据集列表
poetry = []

max_poems = 800
load_poems = 1

with open(config.DATASET_PATH, 'r', encoding='utf-8') as f:
    for line in f.readlines():
        print (line )
        poetry_line = json.loads(line)
        poemline =  poetry_line["content"]
        #print (poemline)
        poemline = poemline.replace('|',':')
        load_poems = load_poems + 1
        print (load_poems, poemline)
        poetry.append(poemline)
        if load_poems > max_poems :
            break

# 预训练模型中的词典和分词器
_token_dict = load_vocab(dict_path)
_tokenizer = Tokenizer(dict_path, do_lower_case=True)

print ("loaded.")
exit

# 统计所有词的词频
word_frequency_count = defaultdict(int)
for line in poetry:
    for t in _tokenizer.tokenize(line):
        word_frequency_count[t] += 1
# 过滤掉低频词
tokens = [(token, count) for token, count in word_frequency_count.items() if count >= min_word_frequency]
# 按词频排序
tokens = sorted(tokens, key=lambda x: -x[1])
# 去掉词频，只保留词列表
tokens = [token for token, count in tokens]

# 构建新的token->id映射关系、和新词表
token_id_dict = {}
keep_words = []

# 将特殊词加入到词典中
for token in ['[PAD]', '[UNK]', '[CLS]', '[SEP]']:
    token_id_dict[token] = len(token_id_dict)
    keep_words.append(_token_dict[token])
# 将唐诗数据集中的词加入到词典中
for token in tokens:
    # 在bert的词典里，但还没有被加载到新词典里的词，才会被加入
    if token in _token_dict and token not in token_id_dict:
        token_id_dict[token] = len(token_id_dict)
        keep_words.append(_token_dict[token])

# 使用新词典重新建立分词器
tokenizer = Tokenizer(token_id_dict, do_lower_case=True)
# 混洗数据
np.random.shuffle(poetry)

# print(len(poetry))
# print(len(keep_words))


class PoetryDataGenerator(DataGenerator):
    """
    数据生成器
    """

    def __iter__(self, random=False):
        # 根据需求混洗数据
        if random:
            np.random.shuffle(self.data)
        # 数据总量
        total = len(self.data)
        # 切分成若干个batch
        for start in range(0, total, self.batch_size):
            end = min(start + self.batch_size, total)
            # 缓存这个batch数据的列表
            batch_token_ids = []
            batch_segment_ids = []
            # 对于每一个batch
            for single_data in self.data[start:end]:
                # 处理数据并加入到缓存列表中
                token_ids, segment_ids = tokenizer.encode(single_data)
                batch_token_ids.append(token_ids)
                batch_segment_ids.append(segment_ids)
            # 填充数据
            batch_token_ids = sequence_padding(batch_token_ids)
            batch_segment_ids = sequence_padding(batch_segment_ids)
            # yield本batch数据
            yield [batch_token_ids, batch_segment_ids], None
