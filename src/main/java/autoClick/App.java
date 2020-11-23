package autoClick;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class App extends Application {

    public static final Pane mainPane = new Pane();
    public static final Label MOUSE_X_LB = new Label("X:");
    public static final Label MOUSE_Y_LB = new Label("Y:");
    public static final Label EMAIL_LB = new Label("Email format:");
    public static final Label TOTAL_COUNT_LB = new Label("Total count:");
    public static final Label COUNTER_LB = new Label("Counter:");
    public static final Label START_RECORD_LB = new Label("Start Record: Ctrl + F1");
    public static final Label STOP_RECORD_LB = new Label("Stop Record: Ctrl + F2");
    public static final Label START_OPERATION_LB = new Label("Start Operation: Ctrl + F12");
    public static final Label STOP_OPERATION_LB = new Label("Stop Operation: Pause Break");

    public static final Button BROWSE_BT = new Button("Browse");
    public static final Button RECORD_BT = new Button("Record");
    public static final Button GO_BT = new Button("GO");

    public static final Font DEFAULT_FONT = Font.font("Segoe UI", 14);

    public static Label mousePosX = new Label("0");
    public static Label mousePosY = new Label("0");
    public static Label emailExample = new Label("thomas@");
    public static Label totalCount = new Label("0");
    public static Label counter = new Label("0");
    public static TextField srcDir = new TextField("C:/");
    public static TextField emailSuffix = new TextField("siswa.um.edu.my");

    @Override
    public void start(Stage stage) throws Exception {

        customizePane();
        setButtonAction();
        ClickDetector.startCollectMousePointThread();

        Scene scene = new Scene(mainPane);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.setHeight(480);
        stage.setWidth(400);
        stage.setTitle("Auto Click");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest((WindowEvent t) -> {
            ClickDetector.destroy();
            Platform.exit();
            System.exit(0);
        });

        mainPane.setOnMousePressed(pressEvent -> {
            mainPane.setOnMouseDragged(dragEvent -> {
                stage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                stage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
            });
        });
    }

    private void customizePane() {
        MOUSE_X_LB.setFont(DEFAULT_FONT);
        customizeElement(MOUSE_X_LB, 20, 20, 20, 10);

        MOUSE_Y_LB.setFont(DEFAULT_FONT);
        customizeElement(MOUSE_Y_LB, 20, 20, 100, 10);

        BROWSE_BT.setFont(DEFAULT_FONT);
        customizeElement(BROWSE_BT, 80, 20, 300, 40);
        
        EMAIL_LB.setFont(DEFAULT_FONT);
        customizeElement(EMAIL_LB, 100, 20, 20, 70);
        
        TOTAL_COUNT_LB.setFont(DEFAULT_FONT);
        customizeElement(TOTAL_COUNT_LB, 100, 20, 20, 100);
        
        COUNTER_LB.setFont(DEFAULT_FONT);
        customizeElement(COUNTER_LB, 100, 20, 20, 130);
        
        RECORD_BT.setFont(DEFAULT_FONT);
        customizeElement(RECORD_BT, 180, 20, 20, 390);
        
        GO_BT.setFont(DEFAULT_FONT);
        customizeElement(GO_BT, 180, 20, 200, 390);
        
        START_RECORD_LB.setFont(DEFAULT_FONT);
        customizeElement(START_RECORD_LB, 180, 20, 20, 420);
        
        STOP_RECORD_LB.setFont(DEFAULT_FONT);
        customizeElement(STOP_RECORD_LB, 180, 20, 20, 450);
        
        START_OPERATION_LB.setFont(DEFAULT_FONT);
        customizeElement(START_OPERATION_LB, 180, 20, 200, 420);
        
        STOP_OPERATION_LB.setFont(DEFAULT_FONT);
        customizeElement(STOP_OPERATION_LB, 180, 20, 200, 450);
        
        mousePosX.setFont(DEFAULT_FONT);
        customizeElement(mousePosX, 40, 20, 40, 10);

        mousePosY.setFont(DEFAULT_FONT);
        customizeElement(mousePosY, 120, 20, 120, 10);


        mainPane.getChildren().addAll(MOUSE_X_LB,
                                      MOUSE_Y_LB,
                                      EMAIL_LB,
                                      COUNTER_LB,
                                      TOTAL_COUNT_LB,
                                      START_RECORD_LB,
                                      STOP_RECORD_LB,
                                      START_OPERATION_LB,
                                      STOP_OPERATION_LB,
                                      BROWSE_BT,
                                      RECORD_BT,
                                      GO_BT,
                                      mousePosX,
                                      mousePosY);

    }

    public void setButtonAction() {
        GO_BT.setOnMouseClicked((MouseEvent mouseEvent) -> {

            ClickDetector.robot.mouseMove(1260, 10);
            ClickDetector.robot.mouseClick(MouseButton.PRIMARY);

        });

    }

    public void customizeElement(Region element, int width, int height, int x, int y) {
        element.setPrefSize(width, height);
        element.setLayoutX(x);
        element.setLayoutY(y);
    }

    public static void main(String[] args) {
        Application.launch(args);

    }

}
