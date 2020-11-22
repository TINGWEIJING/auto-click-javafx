package autoClick;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
    public static final Label MOUSE_X_LB = new Label("Position X:");
    public static final Label MOUSE_Y_LB = new Label("Position Y:");
    public static final Button GO_BT = new Button("GO");

    public static final Font DEFAULT_FONT = Font.font("Arial", 18);

    public static Label mousePosX = new Label("0");
    public static Label mousePosY = new Label("0");

    @Override
    public void start(Stage stage) throws Exception {

        customizePane();
        setButtonAction();
        ClickDetector.startCollectMousePointThread();

        Scene scene = new Scene(mainPane);

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.setHeight(240);
        stage.setWidth(280);
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
        MOUSE_X_LB.setPrefSize(100, 20);
        MOUSE_X_LB.setFont(DEFAULT_FONT);
        setEleSize(MOUSE_X_LB, 20, 20);

        MOUSE_Y_LB.setPrefSize(100, 20);
        MOUSE_Y_LB.setFont(DEFAULT_FONT);
        setEleSize(MOUSE_Y_LB, 20, 50);

        mousePosX.setPrefSize(80, 20);
        mousePosX.setFont(DEFAULT_FONT);
        setEleSize(mousePosX, 120, 20);

        mousePosY.setPrefSize(80, 20);
        mousePosY.setFont(DEFAULT_FONT);
        setEleSize(mousePosY, 120, 50);

        GO_BT.setPrefSize(120, 60);
        GO_BT.setFont(DEFAULT_FONT);
        setEleSize(GO_BT, 80, 160);

        mainPane.getChildren().addAll(MOUSE_X_LB,
                                      MOUSE_Y_LB,
                                      mousePosX,
                                      mousePosY,
                                      GO_BT);

    }

    public void setButtonAction() {
        GO_BT.setOnMouseClicked((MouseEvent mouseEvent) -> {

            ClickDetector.robot.mouseMove(1260, 10);
            ClickDetector.robot.mouseClick(MouseButton.PRIMARY);

        });

    }

    public void setEleSize(Region element, int x, int y) {
        element.setLayoutX(x);
        element.setLayoutY(y);
    }

    public static void main(String[] args) {
        Application.launch(args);

    }

}
