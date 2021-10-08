# -*- coding: utf-8 -*-
"""insecttpu2.ipynb

Automatically generated by Colaboratory.

Original file is located at
    https://colab.research.google.com/drive/1_fQ2-6a9reSc77QyX6iedLdhsWhjN96_
"""

from google.colab import drive
drive.mount('/content/drive')

import tensorflow as tf
from sklearn.linear_model import LogisticRegression
from sklearn.model_selection import GridSearchCV
from sklearn.metrics import classification_report
from imutils import paths
from keras.applications import imagenet_utils
from tensorflow.keras.preprocessing.image import img_to_array
from tensorflow.keras.preprocessing.image import load_img
from sklearn.preprocessing import LabelEncoder
from sklearn.preprocessing import LabelBinarizer
from sklearn.model_selection import train_test_split
from tensorflow.keras.preprocessing.image import ImageDataGenerator
from tensorflow.keras.optimizers import RMSprop, SGD
from tensorflow.keras.layers import Dense, Dropout, Flatten, BatchNormalization,Lambda
from tensorflow.keras import Model, Input
from tensorflow.keras.callbacks import ModelCheckpoint
from tensorflow.keras.models import Sequential,load_model
from tensorflow.keras import layers
import numpy as np
import random
import os
from matplotlib import pyplot

image_path = list(paths.list_images('./drive/My Drive/dataset/'))

random.shuffle(image_path)

labels = [p.split(os.path.sep)[-2] for p in image_path]

le = LabelEncoder()
labels = le.fit_transform(labels)

# One-hot encoding
lb = LabelBinarizer()
labels = lb.fit_transform(labels)

from PIL import ImageFile
ImageFile.LOAD_TRUNCATED_IMAGES = True

list_image = []
for (j, imagePath) in enumerate(image_path):
    image = load_img(imagePath, target_size=(224, 224))
    image = img_to_array(image)
    
    image = np.expand_dims(image, 0)
    image = imagenet_utils.preprocess_input(image)
    
    list_image.append(image)
    
list_image = np.vstack(list_image)

model = Sequential()
model.add(layers.Conv2D(32, (3, 3), activation='relu',
                        input_shape=(224, 224, 3)))
model.add(layers.MaxPooling2D((2, 2)))
model.add(layers.Conv2D(64, (3, 3), activation='relu'))
model.add(layers.MaxPooling2D((2, 2)))
model.add(layers.Conv2D(128, (3, 3), activation='relu'))
model.add(layers.MaxPooling2D((2, 2)))
model.add(layers.Conv2D(128, (3, 3), activation='relu'))
model.add(layers.MaxPooling2D((2, 2)))
model.add(layers.Flatten())
model.add(layers.Dense(512, activation='relu'))
model.add(layers.Dropout(0.5))
model.add(layers.Dense(15, activation='softmax'))
    
model.compile(optimizer=tf.train.AdamOptimizer(learning_rate=0.001), loss='categorical_crossentropy',metrics=['accuracy'])

TPU_WORKER = 'grpc://' + os.environ['COLAB_TPU_ADDR']
tf.logging.set_verbosity(tf.logging.INFO)

tpu_model = tf.contrib.tpu.keras_to_tpu_model(
    model,
    strategy=tf.contrib.tpu.TPUDistributionStrategy(
        tf.contrib.cluster_resolver.TPUClusterResolver(TPU_WORKER)))

X_train, X_test, y_train, y_test = train_test_split(list_image, labels, test_size=0.2, random_state=0)

aug_train = ImageDataGenerator(rescale = 1./255,          
                                shear_range=0.2,
                                horizontal_flip = True,
                                fill_mode = "nearest",
                                zoom_range = 0.2,
                                width_shift_range = 0.1,
                                height_shift_range=0.1,
                                rotation_range=30)
# aug_train = ImageDataGenerator(rescale=1./255, rotation_range=30, width_shift_range=0.1, height_shift_range=0.1, shear_range=0.2, 
#                          zoom_range=0.2, horizontal_flip=True, fill_mode='nearest')
# augementation cho test
aug_test= ImageDataGenerator(rescale = 1./255)

numOfEpoch = 50
filepath="weights.hdf5"
checkpoint = ModelCheckpoint(filepath, monitor='val_acc', verbose=1, save_best_only=True, save_weights_only=True,mode='max')
callbacks_list = [checkpoint]
H = tpu_model.fit_generator(aug_train.flow(X_train, y_train, batch_size=128), 
                        steps_per_epoch=len(X_train)//128,
                        validation_data=(aug_test.flow(X_test, y_test, batch_size=128)),
                        validation_steps=len(X_test)//128,
                        callbacks=callbacks_list,
                        epochs=numOfEpoch)

pyplot.plot(H.history['acc'], label='acc_train')
pyplot.plot(H.history['val_acc'], label='acc_test')
pyplot.legend()
pyplot.show()

pyplot.plot(H.history['loss'], label='loss_train')
pyplot.plot(H.history['val_loss'], label='loss_test')
pyplot.legend()
pyplot.show()