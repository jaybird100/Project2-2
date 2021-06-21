import cv2
import face_recognition as fr
import numpy as np

image = cv2.imread('biden2.jpg')
rgb = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

face_locations = fr.face_locations(rgb)
face_encodings = fr.face_encodings(rgb, face_locations)

# process images from dataset
known_encodings = []
names = ['Biden', 'Obama']

# set names array manually
# imgs contains file names of each image
imgs = ['biden.jpg', 'obama.jpg']
for name in imgs:
    known_image = cv2.imread(name)
    known_rgb = cv2.cvtColor(known_image, cv2.COLOR_BGR2RGB)
    cur_face_locations = fr.face_locations(known_rgb)
    cur_face_encodings = fr.face_encodings(known_rgb, cur_face_locations)
    # add first face data found on the image
    known_encodings.append(cur_face_encodings[0])

    #print(cur_face_encodings[0])

print('number of faces detected:')
print(len(face_locations))
#if there are multiple faces just take the first one

#this 3d parameter here is a key (matching tolerancy)
#adjust it from 0 to 1, lower is stricter
match = fr.compare_faces(known_encodings, face_encodings[0], 0.6)
name = "Unknown"

#print(face_encodings[0])
#print(match)

if len(known_encodings) != 0:
    face_distances = fr.face_distance(known_encodings, face_encodings[0])
    id = np.argmin(face_distances)
    if match[id]:
        name = names[id]
    else:
        #comment if you dont want it
        print('enter new guy name\n')
        name = input()

top, right, bottom, left = face_locations[0]
cv2.rectangle(image, (left, top), (right, bottom), (0, 255, 0), 2)
cv2.putText(image, name, (left + 6, bottom + 30), cv2.FONT_HERSHEY_DUPLEX, 1.0, (0, 255, 0), 1)

cv2.imshow('', image)
cv2.waitKey(0)