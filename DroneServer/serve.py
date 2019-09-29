from flask import Flask, request
import numpy as np
import cv2 as cv
import base64

from test import count_face

app = Flask(__name__)

def b64_to_img(s):
    s = "data:image/png;base64," + s
    encoded_data = s.split(',')[1]
    nparr = np.fromstring(base64.b64decode(encoded_data), np.uint8)
    img = cv.imdecode(nparr, cv.IMREAD_COLOR)
    return img

@app.route("/", methods=['POST'])
def upload():
    b64 = request.form.get('img')
    img = b64_to_img(b64)

    return str(count_face(img))
    return "Woo"

if __name__ == '__main__':
   app.run(host='127.0.0.1', port=1234)
