import os
import sys
from flask import Flask, request, jsonify
import numpy as np
from PIL import Image
import tensorflow as tf
import io
import logging

app = Flask(__name__)
logging.basicConfig(level=logging.INFO)

def resource_path(relative_path):
    # if this script is run from a PyInstaller
    if hasattr(sys, "_MEIPASS"):
        return os.path.join(sys._MEIPASS, relative_path)
    else:
        # if in develop mode, python predict_server.python
        return os.path.join(os.path.abspath("."), relative_path)

# load the .tflite model
try:
    interpreter = tf.lite.Interpreter(model_path=resource_path("mnist_cnn_keras.tflite"))
    interpreter.allocate_tensors()
except Exception as e:
    raise Exception(f"unable to load TFLite model!: {str(e)}")

input_details = interpreter.get_input_details() # [1, 28, 28, 1], dtype=int32
output_details = interpreter.get_output_details() # [1, 10], dtype=int32

app.logger.info(f"Input shape: {input_details[0]['shape']}")
app.logger.info(f"Output shape: {output_details[0]['shape']}")

# recieve image and return inference result
@app.route("/predict", methods=["POST"])
def predict():
    if 'image' not in request.files:
        return jsonify({"error": "No image provided"}), 400

    image_file = request.files["image"]
    if not image_file.filename.lower().endswith(('.png', '.jpg', '.jpeg')):
        return jsonify({"error": "only support PNG and JPEG format"}), 400

    try:
        # pre processing image
        image = Image.open(image_file).convert("L").resize((28, 28)) # convert("L") -> grayscale
        image_array = np.array(image, dtype=np.float32) / 255.0
        image_array = image_array.reshape(1, 28, 28, 1)  # Batch x H x W x C

        # inference
        interpreter.set_tensor(input_details[0]['index'], image_array) # input tensor (0, (1, 28, 28, 1))
        interpreter.invoke() # calculate
        output_data = interpreter.get_tensor(output_details[0]['index'])

        prediction = int(np.argmax(output_data))
        confidence = float(output_data[0][prediction])
        return jsonify({"digit": prediction, "confidence": confidence})

    except Exception as e:
        app.logger.error(f"Inference failed: {str(e)}")
        return jsonify({"error": str(e)}), 500

if __name__ == "__main__":
    app.run(host='0.0.0.0', port=5050, debug=False, use_reloader=False)

