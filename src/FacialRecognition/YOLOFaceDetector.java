package FacialRecognition;

import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class YOLOFaceDetector implements FaceDetector{
    public static boolean foundFace;
    private Mat matrix;
    Stage stage;
    Group root;
    @Override
    public void init(Stage stage) {
        this.stage=stage;
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        WritableImage writableImage = captureSnapShot();
        saveImage();
        ImageView imageView = new ImageView(writableImage);
        imageView.setFitHeight(400);
        imageView.setFitWidth(600);

        // Setting the preserve ratio of the image view
        imageView.setPreserveRatio(true);

        // Creating a Group object
        root = new Group(imageView);

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
        String weightsPath = "yolov4-obj_last.weights";
        String configPath = "yolov4-obj.cfg";
        Net net = Dnn.readNetFromDarknet(configPath,weightsPath);
        Mat grey = new Mat();
        Imgproc.cvtColor(matrix,grey,Imgproc.COLOR_BGR2RGB);
        List<String> ln1 = net.getLayerNames();
        int[] pp = net.getUnconnectedOutLayers().toArray();
        ArrayList<String> ln = new ArrayList<>();
        for(int i:pp){
            ln.add(ln1.get(i-1));
        }
        System.out.println(Arrays.toString(ln.toArray()));
        Mat blob =  Dnn.blobFromImage(grey,1/255.0,new Size(416, 416));
        net.setInput(blob);
        Mat layerOutputs = net.forward(String.valueOf(ln.get(1)));
        double maxConf=-1;
        int maxID=-1;
        for(int i = 0;i<layerOutputs.size().height;i++){
            for(int b = 5;b<10;b++){
                System.out.println(Arrays.toString(layerOutputs.get(i,b)));
                if(layerOutputs.get(i,b)[0]>maxConf){
                    maxConf=layerOutputs.get(i,b)[0];
                    maxID=b-5;
                }
            }
        }
        System.out.println(maxID+" / "+maxConf);
        String name = "";
        if(maxID==0){
            name="Jonathon";
        }else{
            if(maxID==1){
                name = "Alaa";
            }else{
                if(maxID==2){
                    name = "Davit";
                }else{
                    if(maxID==3){
                        name = "Ivan";
                    }else{
                        if(maxID==4){
                            name = "Ranjani";
                        }
                    }
                }
            }
        }
        Label result = new Label(name+" / "+maxConf);
        result.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 80, 0.7), new CornerRadii(5.0), new Insets(-5.0))));
        result.setFont(new Font("Arial", 30));
        result.setTextFill(Color.WHITE);
        root.getChildren().add(result);
        stage.show();
        if(maxConf>0.8){
            foundFace=true;
        }
    }

    @Override
    public boolean isFaceFound() {
        return foundFace;
    }
}
