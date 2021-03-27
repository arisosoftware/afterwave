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
    年看黄鸟暮，又别白衣人。别后音长旧，俱应赋未已。
    Using TensorFlow backend.
    花映垂杨汉水清，微风林里一枝轻。即今江北还如此，愁杀江南离别情。



```python
# 续写诗
# 登台不见月|空有列星光|北斗涌地出|西风吹众芳
!python eval.py -s 登台不见月 -n 5 -c 4
# 江楼寒笛起春声|蜀客扁舟万里行|吹尽落梅还折柳|新春残腊正关情
!python eval.py -s 江楼寒笛起春声 -n 7 -c 4
```

    Using TensorFlow backend.
    登台不见月，入洞即遥云。但见圆荷处，如在许家纷。
    Using TensorFlow backend.
    江楼寒笛起春声，闻道兰源有瑞英。好去巴山三十里，不曾飞上玉徽城。



```python
# 藏头诗
!python eval.py -a 花好月圆 -n 5
!python eval.py -a 花好月圆 -n 7
```

    Using TensorFlow backend.
    花从井上落，好去县城东。月晓见江网，圆秋闻夜砧。
    Using TensorFlow backend.
    花雪随风不厌看，好闲正是被恩难。月明阶下阶前树，圆映红蕉一两竿。



```python

```
