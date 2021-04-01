I am relatively new to python, and while attempting to train a chatbot I received the error: ‘UnboundLocalError: local variable 'logs' referenced before assignment‘. I used model.fit to train:




If you are using tf.data.Dataset as input, you should notice that tf.data.Dataset.take(num) takes num BATCHES, instead of single items, from the original dataset.

For example, if you have 30000 items in the dataset, and apply tf.data.Dataset.batch(256) on the dataset, you will have 30000 / 256 = 118 batches in the dataset. Here if you use tf.data.Dataset.take(2000) to build your validating set, you will have all the 118 batches inside the validating set. After that, if you try tf.data.Dataset.skip(2000) to get the training set, there will be nothing inside of it.

Hence, since there is nothing inside the training set, you will obviously fail when you try fitting on it.

To avoid this problem, you should always pass a num which is less than the total number of batches in your dataset. In the case above, you could try:

validating_set = complete_dataset.take(18)
training_set   = complete_dataset.skip(18)
# Notice that 30000 / 256 = 118 = 18 + 100

##solution:
 
Try setting steps_per_epoch while fitting your model.
i.e., model.fit(x_train, y_train, steps_per_epoch= 5, epochs=7)