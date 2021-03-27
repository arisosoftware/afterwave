import requests
import json
import numpy as np

with open('./jiuge_7.txt', encoding='utf-8') as f:
    poes = json.load(f)

yayun_all = 0
pingze_all = 0
for p in poes:
    data = {
        "yun": "psy",
        "type": "qj_p", # qj_p七言 | wj_p五言
        "text": p
    }
    headers = {
        "Host": "www.52shici.com",
        "Origin": "https://www.52shici.com",
        "User-Agent": "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36"
    }
    r = requests.post('https://www.52shici.com/ajax_gl_do.php', headers=headers, data=data)
    # print(r.text)
    text = r.text
    yayun = 0
    pingze = 0
    if '押韵存在' in text:
        yayun = text.split('押韵存在')[1]
        yayun = yayun.split('个问题')[0]
        yayun = int(yayun)
    if '平仄存在' in text:
        pingze = text.split('平仄存在<em id="error_count">')[1]
        pingze = pingze.split('</em>个问题')[0]
        pingze = int(pingze)
    print(yayun, pingze)

    data['type'] = "qj_z" # qj_z 七言 | wj_z 五言
    r = requests.post('https://www.52shici.com/ajax_gl_do.php', headers=headers, data=data)
    # print(r.text)
    text = r.text
    yayun2 = 0
    pingze2 = 0
    if '押韵存在' in text:
        yayun2 = text.split('押韵存在')[1]
        yayun2 = yayun2.split('个问题')[0]
        yayun2 = int(yayun2)
    if '平仄存在' in text:
        pingze2 = text.split('平仄存在<em id="error_count">')[1]
        pingze2 = pingze2.split('</em>个问题')[0]
        pingze2 = int(pingze2)
    if yayun + pingze > yayun2 + pingze2:
        yayun = yayun2
        pingze = pingze2
    yayun_all += yayun
    pingze_all += pingze

print(yayun_all / len(poes), pingze_all / len(poes))


