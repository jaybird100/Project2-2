package UI;

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

public class FaceDetector{

    public  void init() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        VideoCapture image = new VideoCapture(0);
        Mat imageMat = new Mat();
        if (image.isOpened()) {
            image.retrieve(imageMat);
            analyseImage(imageMat);
            //System.out.println(imageMat.toString());
            image.release();
        } else {
            System.out.println("Error.");
        }
    }

    public boolean analyseImage(Mat mat){
        String faceCascade = "C:\\Users\\ranja\\Downloads\\opencv\\sources\\data\\haarcascades\\haarcascade_frontalface_alt.xml";
        String eyeCascade = "C:\\Users\\ranja\\Downloads\\opencv\\sources\\data\\haarcascades\\haarcascade_eye_tree_eyeglasses.xml";
        CascadeClassifier faceClassifier = new CascadeClassifier(faceCascade);
        CascadeClassifier eyeClassifier = new CascadeClassifier(eyeCascade);
        MatOfRect detect = new MatOfRect();
        classifier.detectMultiScale(mat, detect);
        System.out.println(String.format("Detected %s faces",
                detect.toArray().length));
        for (Rect rect : detect.toArray())
        {
            Imgproc.rectangle(
                    mat,
                    new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0, 0, 255) );
        }
        //Imgcodecs.imwrite("C:/facedetect_output1.jpg", mat);
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
