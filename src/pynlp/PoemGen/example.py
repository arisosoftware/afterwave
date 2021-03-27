from generator import Generator

mygen=Generator(modelPath='./bestModel',tokenPath='./token')
random='random:'+mygen.random(form=0)   # 不限体裁时也可以省略参数，form含义可以看类中注释
acrostic='acrostic:'+mygen.acrostic('天道酬勤',5)
firstLine='firstLine:'+mygen.firstLine('明月几时有')
keywords='keywords:'+mygen.keywords(keys='明月 玉兔',form=7)
print(random,acrostic,firstLine,keywords,sep='\n')
