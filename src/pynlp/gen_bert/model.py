from bert4keras.models import build_transformer_model
from bert4keras.layers import Loss
from bert4keras.backend import keras, K
from bert4keras.optimizers import Adam
from keras.models import Model
from gen_bert.dataset import keep_words
import gen_bert.config
config = gen_bert.config

class CrossEntropy(Loss):
    """交叉熵作为loss，并mask掉padding部分
    """
    def compute_loss(self, inputs, mask=None):
        y_true, y_pred = inputs
        if mask[1] is None:
            y_mask = 1.0
        else:
            y_mask = K.cast(mask[1], K.floatx())[:, 1:]
        y_true = y_true[:, 1:]  # 目标token_ids
        y_pred = y_pred[:, :-1]  # 预测序列，错开一位
        loss = K.sparse_categorical_crossentropy(y_true, y_pred)
        loss = K.sum(loss * y_mask) / K.sum(y_mask)
        return loss

model = build_transformer_model(config.CONFIG_PATH, config.CHECKPOINT_PATH, application='lm', keep_tokens=keep_words)

output = CrossEntropy(1)([model.inputs[0], model.outputs[0]])
model = Model(model.inputs, output)
model.compile(optimizer=Adam(config.LR))

# model.summary()
