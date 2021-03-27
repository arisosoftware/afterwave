import numpy as np
from collections import Counter
import json
import requests

TOKEN_BEGIN = '^'
TOKEN_END = '$'
TOKEN_EMPTY = ' '

with open('./data/keywords_dict_out.txt', encoding='utf-8') as f:
    keyword_dict = json.load(f)

def process_poems(path_to_file):
    poems = []
    with open(path_to_file, encoding='utf-8') as f:
        poetry_list = json.load(f)

        for line in poetry_list:
            content = line['content']
            keyw = keyword_dict[line['keyword']] * 10 + line['poetry_type']
            poems.append((str(keyw), content))

    # create voc.txt
    '''
    print(poems)
    vocabulary = set()
    vocabulary.add(TOKEN_EMPTY)
    
    vocabulary = []
    for topic, content in poems:
        vocabulary += [w for w in topic]
        vocabulary += [w for w in content]

    vocabulary.append(TOKEN_EMPTY)

    counter = Counter(vocabulary)
    vocabulary, vocabulary_counts = zip(*sorted(counter.items(), key=lambda x: -x[1]))
    vocabulary = dict(zip(vocabulary, range(len(vocabulary))))
    '''

    with open('./data/voc.txt', encoding='utf-8') as f:
        vocabulary = json.load(f)
    return vocabulary, poems

def word2int(vocabulary, poems):
    poem_vectors = []
    for poem in poems:
        topic, content = poem
        v_topic = [vocabulary.get(topic, TOKEN_EMPTY)]
        v_content = [vocabulary.get(t, TOKEN_EMPTY) for t in content]
        poem_vectors.append((v_topic, v_content))

    return poem_vectors, vocabulary


def load_data(path):
    voc, poems = process_poems(path)
    data, _ = word2int(voc, poems)
    data_size = len(data)

    content = list(map(lambda x: x[1], data))
    max_content_length = max(map(len, content))
    x_data = np.full((data_size, max_content_length), voc[TOKEN_EMPTY], np.int32)

    for row in range(data_size):
        x_data[row, :len(content[row])] = content[row]

    y_data = x_data.copy()
    y_data[:, :-1] = x_data[:, 1:]

    return (x_data, y_data), voc


def load_data_with_topic(path):
    voc, poems = process_poems(path)
    data, _ = word2int(voc, poems)
    data_size = len(data)

    topic = list(map(lambda x: x[0], data))
    content = list(map(lambda x: x[1], data))
    max_topic_length = max(map(len, topic))
    max_content_length = max(map(len, content))

    x_topic = np.full((data_size, max_topic_length), voc[TOKEN_EMPTY], np.int32)
    x_content = np.full((data_size, max_content_length), voc[TOKEN_EMPTY], np.int32)

    for row in range(data_size):
        x_topic[row, :len(topic[row])] = topic[row]
        x_content[row, :len(content[row])] = content[row]

    y_content = x_content.copy()
    y_content[:, :-1] = x_content[:, 1:]

    assert x_topic.shape[0] == x_content.shape[0] == y_content.shape[0]
    assert x_topic.shape[1] == max_topic_length
    assert x_content.shape[1] == max_content_length == y_content.shape[1]
    return (x_topic, x_content, y_content), voc


if __name__ == '__main__':
    print('prepeocessor')
