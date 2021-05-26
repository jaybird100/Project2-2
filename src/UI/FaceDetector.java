package UI;

import javafx.stage.Stage;

public interface FaceDetector {
    public void init(Stage stage);
    public void findFace();
    public boolean isFaceFound();
}
