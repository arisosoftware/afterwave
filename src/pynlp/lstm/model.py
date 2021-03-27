import os
from keras import Sequential
from keras.callbacks import ModelCheckpoint, TensorBoard
from keras.layers import LSTM, Embedding, Dense, TimeDistributed, GRU
from preprocessor import load_data
from keras.utils import to_categorical


def build_graph():
    model = Sequential()
    model.add(Embedding(VOC_SIZE, 10))
    for l in range(NETWORK_SIZE[0]):
        if CELL_TYPE == 'gru':
            model.add(GRU(NETWORK_SIZE[1], return_sequences=True, kernel_initializer='random_uniform'))
        elif CELL_TYPE == 'lstm':
            model.add(LSTM(NETWORK_SIZE[1], return_sequences=True, kernel_initializer='random_uniform'))
    model.add(TimeDistributed(Dense(VOC_SIZE, activation='softmax')))
    model.compile('rmsprop', 'categorical_crossentropy')
    return model


def train(model, x, y):
    # one-hot encode
    y = to_categorical(y, VOC_SIZE)

    try:
        if not os.path.exists(CHECKPOINT_WEIGHTS_DIR):
            os.mkdir(CHECKPOINT_WEIGHTS_DIR)

        # Keras callbacks
        checkpoint = ModelCheckpoint(CHECKPOINT_WEIGHTS_DIR + '/{epoch:d}.hdf5', save_weights_only=True, period=10)
        tensorboard = TensorBoard(batch_size=64)

        def fit_model(initial_epoch):
            nonlocal model

        epoch = 0
        try:
            print('try restoring ...')
            files = os.listdir(CHECKPOINT_WEIGHTS_DIR)
            if len(files) > 0:
                # latest check points
                latest_cp = max(map(lambda f: int(f.replace('.hdf5', '')), files))
                epoch = latest_cp
                print('from epoch:', epoch)
            model.load_weights(CHECKPOINT_WEIGHTS_DIR + '/{}.hdf5'.format(epoch))
        except OSError:
            print('weights not found, continuing start new training')
            pass

        model.fit(x, y, shuffle=True, batch_size=BATCH_SIZE, epochs=EPOCHS, initial_epoch=epoch,
                  callbacks=[checkpoint, tensorboard])

    except KeyboardInterrupt:
        print('')
        print('interrupted by user')
        pass


def run():
    model = build_graph()
    train(model, x_train, y_train)
    model.save(MODEL_FILE_NAME)


if __name__ == '__main__':
    
    EPOCHS = 80
    BATCH_SIZE = 8
    NETWORK_SIZE = (2, 256)
    DATA_SIZE = 10000
    CELL_TYPE = 'gru'

    i = 3
    MODEL_FILE_NAME = './models/model'+str(i)+'.h5'
    CHECKPOINT_WEIGHTS_DIR = './weights/'+str(i)
    file_path = './data/poetry_add'+str(i)+'.txt'
    (x_train, y_train), VOC = load_data(file_path)
    VOC_SIZE = len(VOC)

    print('training set information:')
    print('vocabulary size:', VOC_SIZE)
    print('X shape:', x_train.shape)
    print('Y shape:', y_train.shape)
    run()