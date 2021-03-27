from PoemGen.generator import Generator
import json

mygen = Generator(modelPath='./PoemGen/bestModel',tokenPath='./PoemGen/token')
# random='random:'+mygen.random(form=0)   # 不限体裁时也可以省略参数，form含义可以看类中注释
# acrostic='acrostic:'+mygen.acrostic('天道酬勤',5)
# firstLine='firstLine:'+mygen.firstLine('明月几时有')
# keywords='keywords:'+mygen.keywords(keys='明月 玉兔',form=7)
# print(random,acrostic,firstLine,keywords,sep='\n')

def getPoetry(geType, poeType, keyword, num = 5, count = 4):
    if geType == '藏头诗':
        outline = mygen.acrostic(keyword, num)
    elif geType == '开头诗':
        outline = mygen.startWith(keyword, form=num)
    elif geType == '主题':
        outline = mygen.keywords(keys=keyword, form=num)
    else:
        outline = mygen.random(num)

    return json.dumps({'poetry': outline},ensure_ascii=False)

if __name__ == '__main__':
    print('PoemGan')
    # content = getPoetry('藏头诗', '五言绝句', '风调雨顺', 5, 4)
    # print(content)
