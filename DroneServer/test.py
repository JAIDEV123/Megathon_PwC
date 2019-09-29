import cv2 as cv
import numpy as np

def get_image():
    cap = cv.VideoCapture(0)

    while True:
        ret, frame = cap.read()
        cv.imshow('img', frame)

        if cv.waitKey(1) & 0xFF == ord(' '):
            break

    cap.release()
    cv.destroyAllWindows()

    return frame

def gray(img):
    return cv.cvtColor(img, cv.COLOR_BGR2GRAY)

def count_face(img):
    img = gray(img)
    face_cascade = cv.CascadeClassifier('front_face.xml')

    faces = face_cascade.detectMultiScale(img)

    if len(faces) == 0 or type(faces) == tuple:
        return 0

    return int(faces.shape[0])

if __name__ == "__main__":
    print(count_face(get_image()))
