package UI;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.ml.SVM;
import org.opencv.objdetect.HOGDescriptor;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;


public class FaceDetector {

    public static boolean foundFace;
    private Mat matrix;
    Size dim = new Size(128,64);

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


    public void findFace() {
        SVM svm = SVM.load("src/model.xml");
        Mat bicubic = new Mat();
        Imgproc.resize(matrix,bicubic,dim,0,0,Imgproc.INTER_CUBIC);
        Mat grey = new Mat();
        Imgproc.cvtColor(bicubic,grey,Imgproc.COLOR_BGR2GRAY);
        MatOfInt uint8 = new MatOfInt();
        grey.convertTo(uint8,CvType.CV_8U);

        HOGDescriptor hog = new HOGDescriptor(dim,new Size(16,16),new Size(8,8),new Size(8,8),9,1,4.,0,2.0000000000000001e-01,false,64);
        MatOfFloat hogimage = new MatOfFloat();
        hog.compute(uint8,hogimage);


        float val = svm.predict(hogimage.reshape(1,1));
        System.out.println(val);
        foundFace= val == 1;

    }
}
