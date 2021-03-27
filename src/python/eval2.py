# -*- coding: utf-8 -*-
# @File    : eval.py
# @Author  : AaronJny
# @Time    : 2020/01/01
# @Desc    :
import tensorflow as tf
from dataset import tokenizer
import settings
import utils

# 加载训练好的模型
model = tf.keras.models.load_model(settings.BEST_MODEL_PATH)
# 随机生成一首诗
#print(utils.generate_random_poetry(tokenizer, model))
# 给出部分信息的情况下，随机生成剩余部分
#print(utils.generate_random_poetry(tokenizer, model, s='高山水流深'))
#print(utils.generate_random_poetry(tokenizer, model, s='长笑出深谷。'))
#print(utils.generate_random_poetry(tokenizer, model, s='莫'))
      
# 生成藏头诗
##print(utils.generate_acrostic(tokenizer, model, head='桂花酒高深莫测'))

pg = "桂酒花香"
count = 0
while (count < 100):      
    count = count + 1
    #print("#"  , count)
    
    str = "#", count
    g = input(str)
                
    if (len(g) > 0 ) :
        pg = g 
    print(utils.generate_acrostic(tokenizer, model, head=pg))
    
