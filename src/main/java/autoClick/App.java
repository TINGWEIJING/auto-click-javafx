package autoClick;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.stage.*;
import javafx.scene.web.HTMLEditor;
import javafx.util.converter.NumberStringConverter;

public class App extends Application {

    public static final Stage mainStage = new Stage();
    public static final Pane mainPane = new Pane();
    public static final Scene mainScene = new Scene(mainPane);
    public static final Label MOUSE_X_LB = new Label("X:");
    public static final Label MOUSE_Y_LB = new Label("Y:");
    public static final Label EMAIL_LB = new Label("Email format:");
    public static final Label TOTAL_COUNT_LB = new Label("Total email:");
    public static final Label COUNTER_LB = new Label("Counter:");
    public static final Label OVERALL_WAIT_LB = new Label("Overall wait:");
    public static final Label STOP_OPERATION_LB = new Label("Stop Operation: Pause Break");
    public static final Label CLIPBOARD_0 = new Label("Clipboard 0:");
    public static final Label CLIPBOARD_1 = new Label("Clipboard 1:");
    public static final Label CLIPBOARD_2 = new Label("Clipboard 2:");

    public static final CheckBox EMAIL_CB = new CheckBox("Email");
    public static final CheckBox ON_TOP_CB = new CheckBox("On top");
    public static final CheckBox MINI_CB = new CheckBox("Mini");
    public static final Button BROWSE_BT = new Button("Browse");
    public static final Button SAVE_BT = new Button("Save");
    public static final Button LOAD_BT = new Button("Load");
    public static final Button MOVE_UP_BT = new Button("\u25B2");
    public static final Button MOVE_DOWN_BT = new Button("\u25BC");
    public static final Button ADD_BT = new Button("+");
    public static final Button DELETE_BT = new Button("-");
    public static final Button RESET_BT = new Button("Reset");
    public static final Button GO_BT = new Button("GO");

    public static final Font DEFAULT_FONT = Font.font("Segoe UI", 14);
    public static final BooleanProperty minimizable = new SimpleBooleanProperty(true);

    public static Label mousePosX_LB = new Label("0");
    public static Label mousePosY_LB = new Label("0");
    public static Label emailExample_LB = new Label("thomas@");
    public static Spinner<Integer> totalCount_SP = new Spinner<>();
    public static Label counter_LB = new Label("0");
    public static Spinner<Integer> overallWait_SP = new Spinner<>();
    public static TextField srcDir_TF = new TextField("C:/");
    public static TextField emailSuffix_TF = new TextField("siswa365.um.edu.my");
    public static HTMLEditor HTMLEditor_0 = new HTMLEditor();
    public static HTMLEditor HTMLEditor_1 = new HTMLEditor();
    public static HTMLEditor HTMLEditor_2 = new HTMLEditor();

    public static SpinnerValueFactory.IntegerSpinnerValueFactory totalEmailValFac
                                                                 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000,
                                                                                                                      Bot.maxEmail.get(), 1);
    public static SpinnerValueFactory.IntegerSpinnerValueFactory totalRepeatValFac
                                                                 = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000,
                                                                                                                      Bot.totalRepeat.get(), 1);

    @Override
    public void start(Stage stage) throws Exception {

        customizePane();
        setButtonAction();
        Bot.startBotThread();
        Bot.configureFileChooser();

        mainStage.initStyle(StageStyle.TRANSPARENT);
        mainStage.setResizable(false);
        mainStage.setWidth(640);
        mainStage.setHeight(480);
        mainStage.setTitle("Auto Click");
        mainStage.setScene(mainScene);
        mainStage.setAlwaysOnTop(true);
        mainStage.show();

        mainStage.setOnCloseRequest((WindowEvent t) -> {
            Bot.destroy();
            Platform.exit();
            System.exit(0);
        });

        mainPane.setOnMousePressed(pressEvent -> {
            mainPane.setOnMouseDragged(dragEvent -> {
                mainStage.setX(dragEvent.getScreenX() - pressEvent.getSceneX());
                mainStage.setY(dragEvent.getScreenY() - pressEvent.getSceneY());
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

        EMAIL_CB.setFont(DEFAULT_FONT);
        EMAIL_CB.setAllowIndeterminate(false);
        EMAIL_CB.setSelected(true);
        customizeElement(EMAIL_CB, 60, 20, 170, 10);

        ON_TOP_CB.setFont(DEFAULT_FONT);
        ON_TOP_CB.setAllowIndeterminate(false);
        ON_TOP_CB.setSelected(true);
        customizeElement(ON_TOP_CB, 70, 20, 240, 10);

        MINI_CB.setFont(DEFAULT_FONT);
        MINI_CB.setAllowIndeterminate(false);
        MINI_CB.setSelected(true);
        customizeElement(MINI_CB, 60, 20, 320, 10);

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

        totalEmailValFac.valueProperty().bindBidirectional(Bot.totalEmail);
        totalEmailValFac.maxProperty().bindBidirectional(Bot.maxEmail);
        totalRepeatValFac.valueProperty().bindBidirectional(Bot.totalRepeat);

        totalCount_SP.setValueFactory(totalEmailValFac);
        totalCount_SP.setEditable(true);
        totalCount_SP.getEditor().setAlignment(Pos.CENTER_RIGHT);
        totalCount_SP.getEditor().setOnAction(event -> {
            SpinnerValueFactory.IntegerSpinnerValueFactory valFac = (SpinnerValueFactory.IntegerSpinnerValueFactory) totalCount_SP.getValueFactory();
            String input = totalCount_SP.getEditor().getText();
            valFac.setValue(valFac.getConverter().fromString(input));
        });
        customizeElement(totalCount_SP, 80, 20, 120, 100);

        COUNTER_LB.setFont(DEFAULT_FONT);
        customizeElement(COUNTER_LB, 100, 20, 20, 130);

        counter_LB.setFont(DEFAULT_FONT);
        counter_LB.setAlignment(Pos.CENTER_RIGHT);
        counter_LB.textProperty().bindBidirectional(Bot.counter, new NumberStringConverter());
        customizeElement(counter_LB, 40, 20, 120, 130);

        OVERALL_WAIT_LB.setFont(DEFAULT_FONT);
        customizeElement(OVERALL_WAIT_LB, 100, 20, 20, 160);

        SpinnerValueFactory<Integer> overallWaitValFac
                                     = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000,
                                                                                          Bot.overallWait.get(), 100);
        overallWaitValFac.valueProperty().bindBidirectional(Bot.overallWait);
        overallWait_SP.setValueFactory(overallWaitValFac);
        overallWait_SP.setEditable(true);
        overallWait_SP.getEditor().setAlignment(Pos.CENTER_RIGHT);
        overallWait_SP.getEditor().setOnAction(event -> {
            String input = totalCount_SP.getEditor().getText();
            overallWaitValFac.setValue(overallWaitValFac.getConverter().fromString(input));
        });
        customizeElement(overallWait_SP, 80, 20, 120, 160);

        SAVE_BT.setFont(DEFAULT_FONT);
        SAVE_BT.setPadding(Insets.EMPTY);
        customizeElement(SAVE_BT, 70, 20, 230, 130);

        LOAD_BT.setFont(DEFAULT_FONT);
        LOAD_BT.setPadding(Insets.EMPTY);
        customizeElement(LOAD_BT, 70, 20, 310, 130);

        MOVE_UP_BT.setFont(DEFAULT_FONT);
        MOVE_UP_BT.setPadding(Insets.EMPTY);
        customizeElement(MOVE_UP_BT, 30, 20, 230, 160);

        MOVE_DOWN_BT.setFont(DEFAULT_FONT);
        MOVE_DOWN_BT.setPadding(Insets.EMPTY);
        customizeElement(MOVE_DOWN_BT, 30, 20, 270, 160);

        ADD_BT.setFont(DEFAULT_FONT);
        ADD_BT.setPadding(Insets.EMPTY);
        customizeElement(ADD_BT, 30, 20, 310, 160);

        DELETE_BT.setFont(DEFAULT_FONT);
        DELETE_BT.setPadding(Insets.EMPTY);
        customizeElement(DELETE_BT, 30, 20, 350, 160);

        RESET_BT.setFont(DEFAULT_FONT);
        RESET_BT.setPadding(Insets.EMPTY);
        customizeElement(RESET_BT, 180, 20, 20, 420);

        GO_BT.setFont(DEFAULT_FONT);
        GO_BT.setPadding(Insets.EMPTY);
        customizeElement(GO_BT, 180, 20, 200, 420);

        STOP_OPERATION_LB.setFont(DEFAULT_FONT);
        customizeElement(STOP_OPERATION_LB, 180, 20, 20, 450);

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
                                      mousePosX_LB,
                                      MOUSE_Y_LB,
                                      mousePosY_LB,
                                      EMAIL_CB,
                                      ON_TOP_CB,
                                      MINI_CB,
                                      BROWSE_BT,
                                      srcDir_TF,
                                      EMAIL_LB,
                                      emailExample_LB,
                                      emailSuffix_TF,
                                      TOTAL_COUNT_LB,
                                      totalCount_SP,
                                      COUNTER_LB,
                                      counter_LB,
                                      OVERALL_WAIT_LB,
                                      overallWait_SP,
                                      SAVE_BT,
                                      LOAD_BT,
                                      MOVE_UP_BT,
                                      MOVE_DOWN_BT,
                                      ADD_BT,
                                      DELETE_BT,
                                      StepTable.getTable(),
                                      RESET_BT,
                                      GO_BT,
                                      STOP_OPERATION_LB,
                                      CLIPBOARD_0,
                                      CLIPBOARD_1,
                                      CLIPBOARD_2,
                                      HTMLEditor_0,
                                      HTMLEditor_1,
                                      HTMLEditor_2);

    }

    public void setButtonAction() {
        EMAIL_CB.selectedProperty().addListener((ov, oldValue, newValue) -> {
            srcDir_TF.setDisable(!newValue);
            BROWSE_BT.setDisable(!newValue);
            EMAIL_LB.setDisable(!newValue);
            emailExample_LB.setDisable(!newValue);
            emailSuffix_TF.setDisable(!newValue);
            Bot.useEmail = newValue;

            if(newValue) {
                TOTAL_COUNT_LB.setText("Total email:");
                totalCount_SP.setValueFactory(totalEmailValFac);
            }
            else {
                TOTAL_COUNT_LB.setText("Total repeat:");
                totalCount_SP.setValueFactory(totalRepeatValFac);
            }

        });

        ON_TOP_CB.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
            if(new_val) {
                mainStage.setAlwaysOnTop(true);
            }
            else {
                mainStage.setAlwaysOnTop(false);
            }
        });

        MINI_CB.selectedProperty().bindBidirectional(minimizable);
        
        BROWSE_BT.setOnMouseClicked(event -> {
            Bot.openFileChooser();
        });

        SAVE_BT.setOnMouseClicked(event -> {
            StepTable.saveStepTable();
        });

        LOAD_BT.setOnMouseClicked(event -> {
            StepTable.loadStepTable();
        });

        MOVE_UP_BT.setOnMouseClicked(event -> {
            StepTable.moveRecordUp();
        });

        MOVE_DOWN_BT.setOnMouseClicked(event -> {
            StepTable.moveRecordDown();
        });

        ADD_BT.setOnMouseClicked(event -> {
            StepTable.addRecord();
        });

        DELETE_BT.setOnMouseClicked(event -> {
            StepTable.removeRecord();
        });

        RESET_BT.setOnMouseClicked(event -> {
            Bot.resetAll();
        });

        GO_BT.setOnMouseClicked((MouseEvent mouseEvent) -> {
            minimize(true);
            Bot.counter.set(0);
            Platform.runLater(() -> {
                App.setDisableInput(true);
            });
            Bot.go();
        });

    }

    public static void customizeElement(Region element, int width, int height, int x, int y) {
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

    public static void setDisableInput(boolean trigger) {
        if(Bot.useEmail) {
            srcDir_TF.setDisable(trigger);
            BROWSE_BT.setDisable(trigger);
            emailSuffix_TF.setDisable(trigger);
        }

        EMAIL_CB.setDisable(trigger);
        ON_TOP_CB.setDisable(trigger);
        MINI_CB.setDisable(trigger);
        StepTable.tableView.getSelectionModel().clearSelection();
        totalCount_SP.setDisable(trigger);
        overallWait_SP.setDisable(trigger);
        SAVE_BT.setDisable(trigger);
        LOAD_BT.setDisable(trigger);
        MOVE_UP_BT.setDisable(trigger);
        MOVE_DOWN_BT.setDisable(trigger);
        ADD_BT.setDisable(trigger);
        DELETE_BT.setDisable(trigger);
        StepTable.tableView.setEditable(!trigger);
        RESET_BT.setDisable(trigger);
        GO_BT.setDisable(trigger);
    }

    public static void minimize(boolean trigger) {
        if(!minimizable.get()) {
            return;
        }
        for(Node node : mainPane.getChildren()) {
            node.setVisible(!trigger);
        }
        StepTable.tableView.setVisible(true);
        STOP_OPERATION_LB.setVisible(true);
        if(trigger) {
            mainStage.setWidth(380);
            mainStage.setHeight(270);
            customizeElement(StepTable.tableView, 360, 220, 10, 10);
            customizeElement(STOP_OPERATION_LB, 180, 20, 10, 240);
        }
        else {
            mainStage.setWidth(640);
            mainStage.setHeight(480);
            customizeElement(StepTable.tableView, 360, 220, 20, 190);
            customizeElement(STOP_OPERATION_LB, 180, 20, 20, 450);
        }
    }

    public static void main(String[] args) {
        Application.launch(args);

    }

}
