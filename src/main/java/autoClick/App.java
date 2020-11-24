package autoClick;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.scene.web.HTMLEditor;

public class App extends Application {

    public static final Pane mainPane = new Pane();
    public static final Scene mainScene = new Scene(mainPane);
    public static final Label MOUSE_X_LB = new Label("X:");
    public static final Label MOUSE_Y_LB = new Label("Y:");
    public static final Label EMAIL_LB = new Label("Email format:");
    public static final Label TOTAL_COUNT_LB = new Label("Total count:");
    public static final Label COUNTER_LB = new Label("Counter:");
    public static final Label START_RECORD_LB = new Label("Start Record: Ctrl + F1");
    public static final Label STOP_RECORD_LB = new Label("Stop Record: Ctrl + F2");
    public static final Label START_OPERATION_LB = new Label("Start Operation: Ctrl + F12");
    public static final Label STOP_OPERATION_LB = new Label("Stop Operation: Pause Break");
    public static final Label CLIPBOARD_0 = new Label("Clipboard 0:");
    public static final Label CLIPBOARD_1 = new Label("Clipboard 1:");
    public static final Label CLIPBOARD_2 = new Label("Clipboard 2:");

    public static final Button BROWSE_BT = new Button("Browse");
    public static final Button RECORD_BT = new Button("Record");
    public static final Button GO_BT = new Button("GO");

    public static final Font DEFAULT_FONT = Font.font("Segoe UI", 14);

    public static Label mousePosX_LB = new Label("0");
    public static Label mousePosY_LB = new Label("0");
    public static Label emailExample_LB = new Label("thomas@");
    public static Label totalCount_LB = new Label("0");
    public static Label counter_LB = new Label("0");
    public static TextField srcDir_TF = new TextField("C:/");
    public static TextField emailSuffix_TF = new TextField("siswa.um.edu.my");
    public static HTMLEditor HTMLEditor_0 = new HTMLEditor();
    public static HTMLEditor HTMLEditor_1 = new HTMLEditor();
    public static HTMLEditor HTMLEditor_2 = new HTMLEditor();

    @Override
    public void start(Stage stage) throws Exception {

        customizePane();
        setButtonAction();
        Bot.startCollectMousePointThread();

        stage.initStyle(StageStyle.TRANSPARENT);
        stage.setResizable(false);
        stage.setWidth(640);
        stage.setHeight(480);
        stage.setTitle("Auto Click");
        stage.setScene(mainScene);
        stage.show();

        stage.setOnCloseRequest((WindowEvent t) -> {
            Bot.destroy();
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

        mousePosX_LB.setFont(DEFAULT_FONT);
        mousePosX_LB.setAlignment(Pos.CENTER_RIGHT);
        customizeElement(mousePosX_LB, 40, 20, 40, 10);

        MOUSE_Y_LB.setFont(DEFAULT_FONT);
        customizeElement(MOUSE_Y_LB, 20, 20, 100, 10);

        mousePosY_LB.setFont(DEFAULT_FONT);
        mousePosY_LB.setAlignment(Pos.CENTER_RIGHT);
        customizeElement(mousePosY_LB, 40, 20, 120, 10);

        srcDir_TF.setFont(DEFAULT_FONT);
        srcDir_TF.setPadding(Insets.EMPTY);
        srcDir_TF.setEditable(false);
        customizeElement(srcDir_TF, 280, 20, 20, 40);

        BROWSE_BT.setFont(DEFAULT_FONT);
        BROWSE_BT.setPadding(Insets.EMPTY);
        customizeElement(BROWSE_BT, 80, 20, 300, 40);

        EMAIL_LB.setFont(DEFAULT_FONT);
        customizeElement(EMAIL_LB, 100, 20, 20, 70);

        emailExample_LB.setFont(DEFAULT_FONT);
        emailExample_LB.setAlignment(Pos.CENTER_RIGHT);
        customizeElement(emailExample_LB, 80, 20, 120, 70);

        emailSuffix_TF.setFont(DEFAULT_FONT);
        emailSuffix_TF.setPadding(Insets.EMPTY);
        customizeElement(emailSuffix_TF, 180, 20, 200, 70);

        TOTAL_COUNT_LB.setFont(DEFAULT_FONT);
        customizeElement(TOTAL_COUNT_LB, 100, 20, 20, 100);

        totalCount_LB.setFont(DEFAULT_FONT);
        totalCount_LB.setAlignment(Pos.CENTER_RIGHT);
        customizeElement(totalCount_LB, 40, 20, 120, 100);

        COUNTER_LB.setFont(DEFAULT_FONT);
        customizeElement(COUNTER_LB, 100, 20, 20, 130);

        counter_LB.setFont(DEFAULT_FONT);
        counter_LB.setAlignment(Pos.CENTER_RIGHT);
        customizeElement(counter_LB, 40, 20, 120, 130);

        RECORD_BT.setFont(DEFAULT_FONT);
        RECORD_BT.setPadding(Insets.EMPTY);
        customizeElement(RECORD_BT, 180, 20, 20, 390);

        GO_BT.setFont(DEFAULT_FONT);
        GO_BT.setPadding(Insets.EMPTY);
        customizeElement(GO_BT, 180, 20, 200, 390);

        START_RECORD_LB.setFont(DEFAULT_FONT);
        customizeElement(START_RECORD_LB, 180, 20, 20, 420);

        STOP_RECORD_LB.setFont(DEFAULT_FONT);
        customizeElement(STOP_RECORD_LB, 180, 20, 20, 450);

        START_OPERATION_LB.setFont(DEFAULT_FONT);
        customizeElement(START_OPERATION_LB, 180, 20, 200, 420);

        STOP_OPERATION_LB.setFont(DEFAULT_FONT);
        customizeElement(STOP_OPERATION_LB, 180, 20, 200, 450);

        CLIPBOARD_0.setFont(DEFAULT_FONT);
        customizeElement(CLIPBOARD_0, 100, 20, 400, 10);

        hideHTMLEditorToolbars(HTMLEditor_0);
        customizeElement(HTMLEditor_0, 220, 120, 400, 30);

        CLIPBOARD_1.setFont(DEFAULT_FONT);
        customizeElement(CLIPBOARD_1, 100, 20, 400, 160);

        hideHTMLEditorToolbars(HTMLEditor_1);
        customizeElement(HTMLEditor_1, 220, 120, 400, 180);

        CLIPBOARD_2.setFont(DEFAULT_FONT);
        customizeElement(CLIPBOARD_2, 100, 20, 400, 310);

        hideHTMLEditorToolbars(HTMLEditor_2);
        customizeElement(HTMLEditor_2, 220, 120, 400, 330);

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
                                      srcDir_TF,
                                      emailSuffix_TF,
                                      mousePosX_LB,
                                      mousePosY_LB,
                                      emailExample_LB,
                                      totalCount_LB,
                                      counter_LB,
                                      CLIPBOARD_0,
                                      CLIPBOARD_1,
                                      CLIPBOARD_2,
                                      HTMLEditor_0,
                                      HTMLEditor_1,
                                      HTMLEditor_2);

    }

    public void setButtonAction() {
        GO_BT.setOnMouseClicked((MouseEvent mouseEvent) -> {

            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            HTMLEditor_0.setHtmlText(clipboard.getHtml());
            Platform.runLater(() -> {
                for(int i = 0; i < 100; i++) {
                    Bot.robot.mouseMove(100 * i, 300);
                    try {
                        Thread.sleep(1000);
                    }
                    catch(InterruptedException ex) {
                    }
                }
            });

        });

    }

    public void customizeElement(Region element, int width, int height, int x, int y) {
        element.setPrefSize(width, height);
        element.setLayoutX(x);
        element.setLayoutY(y);
    }

    private void hideHTMLEditorToolbars(final HTMLEditor editor) {
        editor.setVisible(false);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Node[] nodes = editor.lookupAll(".tool-bar").toArray(new Node[0]);
                for(Node node : nodes) {
                    node.setVisible(false);
                    node.setManaged(false);
                }
                editor.setVisible(true);
            }

        });
    }

    public static void main(String[] args) {
        Application.launch(args);

    }

}
