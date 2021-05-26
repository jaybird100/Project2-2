package UI;

import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ClassifierFaceDetector implements FaceDetector {
    private boolean foundFace = false;

    @Override
    public void init(Stage stage) {
        // Since video capturing and face detection in this case are not separable, they both are moved to findFace method
    }

    public void analyseImage(Mat mat){
        //String faceCascade = "C:\\Users\\ranja\\Downloads\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml";
        //String eyeCascade = "C:\\Users\\ranja\\Downloads\\opencv\\sources\\data\\haarcascades\\haarcascade_eye_tree_eyeglasses.xml";
        String faceCascade = "./res/haarcascade_frontalface_alt.xml";
        String eyeCascade = "./res/haarcascade_eye_tree_eyeglasses.xml";
        CascadeClassifier faceClassifier = new CascadeClassifier(faceCascade);
        CascadeClassifier eyeClassifier = new CascadeClassifier(eyeCascade);
        if(!faceClassifier.load(faceCascade))
        {
            System.out.println("Error loading face cascade");
        }
        else
        {
            System.out.println("Success loading face cascade");
        }
        if(!eyeClassifier.load(eyeCascade))
        {
            System.out.println("Error loading eyes cascade");
        }
        else
        {
            System.out.println("Success loading eyes cascade");
        }

        MatOfRect faces = new MatOfRect();
        faceClassifier.detectMultiScale(mat, faces);


        Rect[] facesArray = faces.toArray();

        for(int i=0; i<facesArray.length; i++)
        {
            Imgproc.rectangle(mat, new Point(facesArray[i].x, facesArray[i].y), new Point(facesArray[i].x + facesArray[i].width, facesArray[i].y + facesArray[i].height),
                    new Scalar(0, 100, 0),3);

            MatOfRect eyes = new MatOfRect();
            eyeClassifier.detectMultiScale(mat, eyes);
            Rect[] eyesArray = eyes.toArray();
            for (int j = 0; j < eyesArray.length; j++)
            {
                Imgproc.rectangle(mat, new Point(eyesArray[i].x, eyesArray[i].y), new Point(eyesArray[i].x + eyesArray[i].width, eyesArray[i].y + eyesArray[i].height),
                        new Scalar(200, 200, 100),2);
            }

        }
        if(facesArray.length != 0){
            foundFace = true;
            System.out.println("Not empty");
        }
        /*
        showResult(mat);  //this displays the camera feed and i don't know how to close it and it's also not super necessary
        if(foundFace){
            return;
        }
         */
    }

    public void showResult(Mat img) {
        MatOfByte m = new MatOfByte();
        Imgcodecs.imencode(".jpg", img, m);
        byte[] imageData = m.toArray();
        BufferedImage buffImage = null;
        try {
            InputStream in = new ByteArrayInputStream(imageData);
            buffImage = ImageIO.read(in);
            JFrame frame = new JFrame();
            frame.getContentPane().add(new JLabel(new ImageIcon(buffImage)));
            frame.pack();
            frame.setVisible(true);
            if(foundFace){
                frame.dispose(); //Destroy the JFrame object
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void findFace() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        VideoCapture image = new VideoCapture(0);
        if (image.isOpened()) {
            while (true & !foundFace) {
                Mat imageMat = new Mat();
                image.read(imageMat);
                analyseImage(imageMat);
            }
            //System.out.println(imageMat.toString());
            //image.release();
        } else {
            System.out.println("Error.");
        }
        if(foundFace){
            System.out.println("hi7");
        }
    }

    @Override
    public boolean isFaceFound() {
        return foundFace;
    }
}
