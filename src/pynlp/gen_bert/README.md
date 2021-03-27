```python
# 安装依赖(也可以使用tf cpu版本)
!pip install -r requirements.txt
```

```python
# 训练
!python train.py
```

```python
# 随机生成一首诗
# 生成五言绝句
!python eval.py -n 5 -c 4
# 生成七言绝句
!python eval.py -n 7 -c 4
```

    Using TensorFlow backend.
    黄王府中人，无事九衢尘。忽奉莲华座，高堂独受身。
    Using TensorFlow backend.
    水底分明天上云，可怜形影似吾身。应从海上春风便，凡卉飘零是我神。



```python
# 续写诗
# 登台不见月|空有列星光|北斗涌地出|西风吹众芳
!python eval.py -s 登台不见月 -n 5 -c 4
# 江楼寒笛起春声|蜀客扁舟万里行|吹尽落梅还折柳|新春残腊正关情
!python eval.py -s 江楼寒笛起春声 -n 7 -c 4
```

    Using TensorFlow backend.
    登台不见月，半日在云南。惆怅桂花落，清歌开曲潭。
    Using TensorFlow backend.
    江楼寒笛起春声，洛水烟花送客情。不为此来同棹宿，五湖烟月两乡情。



```python
# 藏头诗
!python eval.py -a 花好月圆 -n 5
!python eval.py -a 花好月圆 -n 7
```

    Using TensorFlow backend.
    花向南楼发，好风能起时。月明必有夜，圆缺必无期。
    Using TensorFlow backend.
    花香僧慢叶间间，好挂禅衣一带风。月皎清宵穿衲带，圆光盖顶对松山。



```python

```
