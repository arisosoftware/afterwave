from tensorflow.keras.models import load_model
import numpy as np
import json
import requests
from lstm.word2vec import ModelLoader

with open('./lstm/data/voc.txt', encoding='utf-8') as f:
    VOC = json.load(f)
r_VOC = dict(zip(VOC.values(), VOC.keys()))

def getPoetry(geType, poeType, keyword, num = 5, count = 4):
    lstm_model_type = 1
    po_len = 26
    # 1 -> 五言绝句 2 -> 七言绝句 3 -> 五言律诗 4 -> 七言律诗
    if num == 5 and count == 4:
        lstm_model_type = 1
        po_len = 34
        std_len = 24
    elif num == 7 and count == 4:
        lstm_model_type = 2
        po_len = 50
        std_len = 32
    elif num == 5 and count == 8:
        lstm_model_type = 3
        po_len = 66
        std_len = 48
    elif num == 7 and count == 8:
        lstm_model_type = 4
        po_len = 66
        std_len = 64
    
    lstm_model_name = 'model'
    if geType == '主题':
        lstm_model_name = 'model_topic_based'
    
    model = load_model('./lstm/models/' + lstm_model_name + str(lstm_model_type) + '.h5')
    
    content = '^'
    c_index = 1
    
    if geType == '主题':
        topic_int = []
        classfication = ModelLoader().get_similarity(keyword)
        topic_int = np.array([VOC.get(classfication + str(lstm_model_type))])

        while content[-1] != '$' and len(content) < po_len:
            c_content = content
            for _ in range(po_len - c_index):
                c_content += ' '

            p = [VOC[i] for i in c_content]
            x = np.array([p])
            p = model.predict([topic_int, x])
            index = np.argmax(p[-1], axis=1)
            next_word = r_VOC.get(index[c_index - 1])
            content += next_word

            c_index += 1
    
    elif geType == '藏头诗':
        content = '^' + keyword[0]
        c_index = 2
        key_index = 1

        while content[-1] != '$' and len(content) < po_len:
            c_content = content
            if (content[-1] == '，' or content[-1] == '。') and len(content) != po_len - 1 and key_index < len(keyword):
                content += keyword[key_index]
                key_index += 1
                c_index += 1
                continue
            
            for _ in range(po_len - c_index):
                c_content += ' '

            p = [VOC[i] for i in c_content]
            x = np.array([p])
            p = model.predict(x)
            index = np.argmax(p[-1], axis=1)
            next_word = r_VOC.get(index[c_index - 1])
            content += next_word

            c_index += 1

    else:
        if geType == '开头诗':
            content = '^' + keyword
            c_index = 1 + len(keyword)

        while content[-1] != '$' and len(content) < po_len:
            c_content = content
            for _ in range(po_len - c_index):
                c_content += ' '

            p = [VOC[i] for i in c_content]
            x = np.array([p])
            p = model.predict(x)
            index = np.argmax(p[-1], axis=1)
            next_word = r_VOC.get(index[c_index - 1])
            content += next_word

            c_index += 1
    
    return json.dumps({'poetry': content[1:std_len+1]},ensure_ascii=False)

if __name__ == '__main__':
    # content = getPoetry('主题', '五言绝句', '春天', 7, 8)
    # print(content)
    print('lstm')