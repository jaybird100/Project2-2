package FacialRecognition;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.opencv.core.Core.PCACompute;

public class PCAtoMLP implements FaceDetector{
    Mat matrix;
    Boolean foundFace;
    Size dim = new Size(128,64);

    @Override
    public boolean isFaceFound() {
        return foundFace;
    }

    @Override
    public void init(Stage stage) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        WritableImage writableImage = captureSnapShot();
        saveImage();
        ImageView imageView = new ImageView(writableImage);
        imageView.setFitHeight(400);
        imageView.setFitWidth(600);

        // Setting the preserve ratio of the image view
        imageView.setPreserveRatio(true);

        // Creating a Group object
        Group root = new Group(imageView);

        // Creating a scene object
        Scene scene = new Scene(root, 600, 400);

        // Setting title to the Stage
        stage.setTitle("Capturing an image");

        // Adding scene to the stage
        stage.setScene(scene);

        // Displaying the contents of the stage
        stage.show();

    }

    public WritableImage captureSnapShot() {
        WritableImage WritableImage = null;

        // Loading the OpenCV core library
        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );

        // Instantiating the VideoCapture class (camera:: 0)
        VideoCapture capture = new VideoCapture(0);

        // Reading the next video frame from the camera
        Mat matrix = new Mat();
        capture.read(matrix);

        // If camera is opened
        if( capture.isOpened()) {
            // If there is next video frame
            if (capture.read(matrix)) {
                // Creating BuffredImage from the matrix
                BufferedImage image = new BufferedImage(matrix.width(), matrix.height(), BufferedImage.TYPE_3BYTE_BGR);
                WritableRaster raster = image.getRaster();
                DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
                byte[] data = dataBuffer.getData();
                matrix.get(0, 0, data);
                this.matrix = matrix;

                // Creating the Writable Image
                WritableImage = SwingFXUtils.toFXImage(image, null);
            }
        }
        return WritableImage;
    }
    public void saveImage() {
        // Saving the Image
        //String file = "E:/OpenCV/chap22/sanpshot.jpg";
        String file = "./res/snapshot.jpg";

        // Instantiating the imgcodecs class
        Imgcodecs imageCodecs = new Imgcodecs();

        // Saving it again
        imageCodecs.imwrite(file, matrix);
    }


    @Override
    public void findFace() {
        ArrayList<Double> means = new ArrayList<>();
        ArrayList<ArrayList<Double>> eigenVectors = new ArrayList<>();
        try {
            Scanner br = new Scanner(new FileReader("pcaMean.txt"));
            while(br.hasNext()){
                means.add(Double.parseDouble(br.nextLine()));
            }
            Scanner br1 = new Scanner(new FileReader("pcaEigen.txt"));
            while(br1.hasNext()){
                ArrayList<Double> row = new ArrayList<>();
                while(br1.hasNextDouble()){
                    row.add(br.nextDouble());
                }
                eigenVectors.add(row);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Mat bicubic = new Mat();
        Imgproc.resize(matrix,bicubic,dim,0,0,Imgproc.INTER_CUBIC);
        Mat grey = new Mat();
        Imgproc.cvtColor(bicubic,grey,Imgproc.COLOR_BGR2GRAY);
        Mat mean = new Mat();
        Mat eigen = new Mat();
        PCACompute(grey,mean,eigen);

    }
}
