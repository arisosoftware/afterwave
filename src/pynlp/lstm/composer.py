from keras.models import load_model
from preprocessor import load_data
import numpy as np
import json
import requests

print(123123)
with open('./data/voc.txt', encoding='utf-8') as f:
    VOC = json.load(f)
r_VOC = dict(zip(VOC.values(), VOC.keys()))

print(123123)
model = load_model('./models/model4.h5')

print(VOC)

topic = ''

# url = "http://127.0.0.1:5000/getSimilar?word="+topic
# # #发送get请求
# topic_int = requests.get(url)

# topic_int = topic_int.json()

topic_int = []
# for t in topic:
#     i = VOC.get(t)
#     if i is None:
#         raise Exception('{} is not in VOC'.format(t))
#     topic_int.append(i)
# topic_int = np.array([VOC.get(topic)])
print("topic_int")
print(topic_int)

# topic_int = np.array([topic_int])

# print(topic_int)

content = '^天'
c_index = 2

while content[-1] != '$' and len(content) < 66:
    c_content = content
    if (content[-1] == '，' or content[-1] == '。') and len(content) != 65:
        content += '下'
        c_index += 1
        continue
    for i in range(66 - c_index):
        c_content += ' '

    p = [VOC[i] for i in c_content]
    x = np.array([p])
    p = model.predict(x)
    index = np.argmax(p[-1], axis=1)
    next_word = r_VOC.get(index[c_index - 1])
    content += next_word

    c_index += 1

# print(r_VOC)
print(content)
