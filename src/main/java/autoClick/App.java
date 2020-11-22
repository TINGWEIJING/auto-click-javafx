package autoClick;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class App extends Application {

    public static final Pane mainPane = new Pane();
    public static final Label MOUSE_X = new Label("Position X:");
    public static final Label MOUSE_Y = new Label("Position Y:");
    
    public static Label mousePosX = new Label("0");
    public static Label mousePosY = new Label("0");
    
    
    public static final Font defaultFont = Font.font("Arial", 18);
    
    @Override
    public void start(Stage stage) throws Exception {

        customizePane();
        Scene scene = new Scene(mainPane);

        stage.setResizable(false);
        stage.setHeight(240);
        stage.setWidth(280);
        stage.setTitle("Auto Click");
        stage.setScene(scene);
        stage.show();
    }

    private void customizePane() {
        MOUSE_X.setPrefSize(100, 20);
        MOUSE_X.setFont(defaultFont);
        setEleSize(MOUSE_X, 20, 20);

        MOUSE_Y.setPrefSize(100, 20);
        MOUSE_Y.setFont(defaultFont);
        setEleSize(MOUSE_Y, 20, 50);

        mousePosX.setPrefSize(80, 20);
        mousePosX.setFont(defaultFont);
        setEleSize(mousePosX, 120, 20);
        
        mousePosY.setPrefSize(80, 20);
        mousePosY.setFont(defaultFont);
        setEleSize(mousePosY, 120, 50);
        
        mainPane.getChildren().addAll(MOUSE_X,
                                      MOUSE_Y);
    }

    public void setEleSize(Region element, int x, int y) {
        element.setLayoutX(x);
        element.setLayoutY(y);
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}
