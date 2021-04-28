package UI;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class FaceDetector{

    public  void init() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        VideoCapture image = new VideoCapture(0);
        if (image.isOpened()) {
            while(true) {
                Mat imageMat = new Mat();
                image.read(imageMat);
                analyseImage(imageMat);
            }
            //System.out.println(imageMat.toString());
            //image.release();
        } else {
            System.out.println("Error.");
        }
    }

    public boolean analyseImage(Mat mat){
        String faceCascade = "C:\\Users\\ranja\\Downloads\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml";
        String eyeCascade = "C:\\Users\\ranja\\Downloads\\opencv\\sources\\data\\haarcascades\\haarcascade_eye_tree_eyeglasses.xml";
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

        MatOfRect greyMat = new MatOfRect();
        Imgproc.cvtColor(greyMat, mat, Imgproc.COLOR_BGRA2GRAY);
        Imgproc.equalizeHist(greyMat, mat);

        MatOfRect faces = new MatOfRect();
        faceClassifier.detectMultiScale(mat, faces);


        Rect[] facesArray = faces.toArray();

        for(int i=0; i<facesArray.length; i++)
        {
            Point center = new Point(facesArray[i].x + facesArray[i].width * 0.5, facesArray[i].y + facesArray[i].height * 0.5);
            Imgproc.ellipse(mat, center, new Size(facesArray[i].width * 0.5, facesArray[i].height * 0.5), 0, 0, 360, new Scalar(255, 0, 255), 4, 8, 0);

            Mat faceROI = greyMat.submat(facesArray[i]);
            MatOfRect eyes = new MatOfRect();

            //-- In each face, detect eyes
            eyeClassifier.detectMultiScale(faceROI, eyes, 1.1, 2, 0,new Size(30,30), new Size());

            Rect[] eyesArray = eyes.toArray();

            for (int j = 0; j < eyesArray.length; j++)
            {
                Point center1 = new Point(facesArray[i].x + eyesArray[i].x + eyesArray[i].width * 0.5, facesArray[i].y + eyesArray[i].y + eyesArray[i].height * 0.5);
                int radius = (int) Math.round((eyesArray[i].width + eyesArray[i].height) * 0.25);
                Imgproc.circle(mat, center1, radius, new Scalar(255, 0, 0), 4, 8, 0);
            }

        }
        HighGui.imshow("Camera feed", mat);
        return true;
    }

    public void showResult(Mat img) {
        Imgproc.resize(img, img, new Size(640, 480));
        MatOfByte m = new MatOfByte();
        Imgcodecs.imencode(".jpg", img, m);
        byte[] imageData = m.toArray();
        BufferedImage bufImage = null;
        try {
            InputStream in = new ByteArrayInputStream(imageData);
            bufImage = ImageIO.read(in);
            JFrame frame = new JFrame();
            frame.getContentPane().add(new JLabel(new ImageIcon(bufImage)));
            frame.pack();
            frame.setVisible(true);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}
