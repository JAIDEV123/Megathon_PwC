# Raasta
## Our submission for the problem statement by PwC at Megathon'19 

'Raasta' solves the problems of water drainage in modern cities by sending drones to waterlogged areas.
 
This repository includes the algorithm for where to send the drones and also an android application that simulates how the drone redirects traffic.

### The Android App Client / Flask server
The app is written using Java on Android Studio. The app takes 3 pictures using the device's camera
and then sends 3 POST requests to the server with the images encoded in base64

The server, after receiving the image decodes it and then uses opencv to count the number of faces in that image and sends that number as a response
