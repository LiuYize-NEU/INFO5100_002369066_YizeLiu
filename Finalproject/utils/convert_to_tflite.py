import tensorflow as tf

model = tf.keras.models.load_model('mnist_cnn_keras.h5')
converter = tf.lite.TFLiteConverter.from_keras_model(model)
converter.optimizations = [tf.lite.Optimize.DEFAULT]
tflite_model = converter.convert()

with open('mnist_cnn_keras.tflite', 'wb') as f:
    f.write(tflite_model)

import os
tflite_size = os.path.getsize('mnist_cnn_keras.tflite') / 1024 / 1024  # MB
print(f"TFLite model size: {tflite_size:.2f} MB")


