import os
# os.environ["TF_KERAS"]="1"
os.environ["TF_CPP_MIN_LOG_LEVEL"] = "2"
def warn(*args, **kwargs):
    pass
import warnings
warnings.warn = warn

from bert4keras.backend import keras
from dataset import PoetryDataGenerator, tokenizer, poetry
from model import model
import config
import utils

class Evaluator(keras.callbacks.Callback):
    """评估与保存
    """
    def __init__(self):
        self.lowest = 1e10

    def on_epoch_end(self, epoch, logs=None):
        # 保存最优
        if logs['loss'] <= self.lowest:
            self.lowest = logs['loss']
            model.save_weights(config.BEST_MODEL_PATH)
        print(utils.generate_random_poetry(tokenizer, model))

# 创建数据生成器
data_generator = PoetryDataGenerator(poetry, batch_size=config.BATCH_SIZE)
# 开始训练
model.fit(
        data_generator.forfit(),
        steps_per_epoch=data_generator.steps,
        epochs=config.TRAIN_EPOCHS,
        callbacks=[Evaluator()]
    )
