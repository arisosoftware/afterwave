import torch
import transformers
import re
from transformers import BertTokenizer,GPT2LMHeadModel,TextGenerationPipeline

class Generator:
    def __init__(self, modelPath, tokenPath):
        self.tokenOrigin=BertTokenizer.from_pretrained("uer/gpt2-chinese-poem")
        self.modelOrigin=GPT2LMHeadModel.from_pretrained("uer/gpt2-chinese-poem")
        self.tokenKeys=BertTokenizer.from_pretrained(tokenPath)
        self.modelKeys=GPT2LMHeadModel.from_pretrained(modelPath)
        self.genOrigin=TextGenerationPipeline(self.modelOrigin, self.tokenOrigin)
        self.genKeys=TextGenerationPipeline(self.modelKeys, self.tokenKeys)

    def __predict(self, prompt, model, tokenizer, max_length, do_sample=True, add_special_tokens=False):
        encoding=tokenizer(prompt,return_tensors='pt',padding=False,add_special_tokens=add_special_tokens)
        predict=model.generate(encoding['input_ids'],max_length=max_length,do_sample=do_sample)
        decoding=tokenizer.decode(predict[0], skip_special_tokens=False,clean_up_tokenization_spaces=False)
        return decoding

    def __checkForm(self, poem, form):
        type=form
        if type==0:
            type=poem.find('，')
        lines=re.split('[，。]',poem)[:-1]    # 最后一个为空字符串，舍去
        for line in lines:
            if len(line)!=type:
                return False
        return True

    def __getBody(self, poem):
        poem=poem.replace(' ','')
        posCls=poem.find('[CLS]')
        posPeriod=poem.find('。')
        posPeriod=poem.find('。',posPeriod+1)    # 找到第二个句号
        return poem[posCls+len('[CLS]'):posPeriod+1]


    def random(self,form=0):
        '''
        随机生成
        :param form:
            0 if 随机体裁
            5 if 五绝
            7 if 七绝
        '''
        while True:
            poem=self.genOrigin("[CLS]", max_length=33, do_sample=True)[0]['generated_text'] # 33为七绝时候的生成长度
            poem=self.__getBody(poem)
            if self.__checkForm(poem, form):
                break
        return poem

    def acrostic(self,prompt,form):
        '''
        :param prompt: 4个字
        '''
        while True:
            acrostic='[CLS]'
            thisLen=1
            for i in range(4):
                acrostic += prompt[i]
                thisLen+=(form)+5   # 5为了留出足够的长度
                temp = self.genOrigin(acrostic, max_length=thisLen, do_sample=True)[0]['generated_text']
                if i % 2 == 0:
                    pos = temp.find('，', len(acrostic))
                else:
                    pos = temp.find('。', len(acrostic))
                if pos != -1:
                    acrostic = temp[:pos + 1]

            acrostic = self.__getBody(acrostic)
            if self.__checkForm(acrostic, form):
                break
        return acrostic

    def firstLine(self,firstline):
        '''
        给出第一句来生成
        :param firstline: 输入的第一句，句尾不用加逗号
        '''
        form=len(firstline)
        firstline='[CLS]'+firstline+'，'
        while True:
            poem=self.genOrigin(firstline, max_length=33, do_sample=True)[0]['generated_text']
            poem=self.__getBody(poem)
            if self.__checkForm(poem, form):
                break
        return poem

    def startWith(self,prompt,form=0):
        '''
        根据任何合法的提示字符串来限定诗歌的开头，以生成诗歌
        :prompt 任意不超过古诗长度的提示字符串，比如一个词、一行诗、两行诗
        :param form:
            当提示词中不存在逗号且提示词长度<7时，此时无法确定生成诗歌的体裁，才需要传入
                0 if 随机体裁
                5 if 五绝
                7 if 七绝
            当提示词存在逗号或者无逗号却len=7时，格式已经由提示词确定，form参数不起作用
        '''
        # 检查非法标点
        if prompt.find(',')!=-1:
            prompt=prompt.replace(',','，')
        if prompt.find('.')!=-1:
            prompt=prompt.replace('.','。')
        # 检查是否格式由prompt确定
        commaPos=prompt.find('，')
        if commaPos!=-1:
            form=commaPos
        elif len(prompt)==7:
            form=7
        prompt = '[CLS]' + prompt
        # generate
        while True:
            poem=self.genOrigin(prompt, max_length=33, do_sample=True)[0]['generated_text']
            poem=self.__getBody(poem)
            if self.__checkForm(poem, form):
                break
        return poem

    def keywords(self,keys,form):
        '''
        根据体裁和keywords生成诗
        :param keys: keywords，多个时用空格隔开，最好不要超过4个keyword
        '''
        type={5:'五绝',7:'七绝'}[form]
        prompt='[TYPE]%s[CP][NULL][THE]%s[CLS]'%(type,keys)
        while True:
            poem=self.genKeys(prompt, max_length=50, do_sample=True)[0]['generated_text']    # 50 一般都够了
            poem=self.__getBody(poem)
            if self.__checkForm(poem, form):
                break
        return poem
