package autoClick;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.input.*;
import javafx.scene.robot.Robot;
import javafx.scene.web.HTMLEditor;
import javafx.stage.*;

/**
 *
 * @author TING WEI JING
 */
public class Bot {

    public static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    public static ScheduledFuture scheduledFuture;
    public static Robot robot = new Robot();
    public static BotService botService = new BotService();

    public static Clipboard clipboard = Clipboard.getSystemClipboard();
    public static ClipboardContent content = new ClipboardContent();

    public static final FileChooser fileChooser = new FileChooser();
    public static BufferedReader reader;
    public static File emailTextFile;
    public static FileTime emailTextFileTime;
    public static LinkedList<String> emailList = new LinkedList<>();
    public static ListIterator<String> emailListItr;

    public static LinkedList<KeyCode> detectedKeyList = new LinkedList<>();

    public static final long UPDATE_RATE = (long) 33333.3;
    public static boolean useEmail = true;

    public static SimpleIntegerProperty maxEmail = new SimpleIntegerProperty(0);
    public static ObjectProperty<Integer> totalEmail = new SimpleObjectProperty<>(0);
    public static ObjectProperty<Integer> totalRepeat = new SimpleObjectProperty<>(0);
    public static SimpleIntegerProperty counter = new SimpleIntegerProperty(0);
    public static ObjectProperty<Integer> overallWait = new SimpleObjectProperty<>(100);

    public static void startBotThread() {

        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                App.mousePosX_LB.setText((int) robot.getMouseX() + "");
                App.mousePosY_LB.setText((int) robot.getMouseY() + "");
            });
        },
                                                                       0, UPDATE_RATE,
                                                                       TimeUnit.MICROSECONDS);
        App.mainPane.setOnKeyPressed(event -> {
            if(event.getCode().equals(KeyCode.PAUSE)) {
                System.out.println("PAUSE!");
                botService.cancel();
                Platform.runLater(() -> {
                    App.setDisableInput(false);
                });
            }
        }
        );

        App.mainStage.focusedProperty().addListener((ov, oldValue, newValue) -> {
            if(newValue) {
                System.out.println("Focus!");
                if(emailTextFile != null && emailTextFileTime != null) {
                    try {
                        FileTime tempTime = Files.getLastModifiedTime(emailTextFile.toPath());
                        if(!emailTextFileTime.equals(tempTime)) {
                            loadFile();
                        }
                    }
                    catch(IOException ex) {
                        resetEmailSettings();
                    }
                }
            }
        });

    }

    public static void configureFileChooser() {
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.setTitle("Choose Email Text File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text File", "*.txt")
        );
    }

    public static void openFileChooser() {
        emailTextFile = fileChooser.showOpenDialog(App.mainStage);
        if(emailTextFile != null) {
            loadFile();
        }
    }

    static void loadFile() {
        try {
            reader = new BufferedReader(new FileReader(emailTextFile));
            emailList.clear();

            String hold;
            while((hold = reader.readLine()) != null) {
                emailList.add(hold);
                System.out.println(emailList.getLast());
            }
            reader.close();

            maxEmail.set(emailList.size());
            totalEmail.set(emailList.size());
            emailTextFileTime = Files.getLastModifiedTime(emailTextFile.toPath());
            fileChooser.setInitialDirectory(emailTextFile.getParentFile().getCanonicalFile());
            App.srcDir_TF.setText(emailTextFile.getParentFile().getCanonicalFile().toString());
        }
        catch(FileNotFoundException e) {
            resetEmailSettings();
        }
        catch(IOException e) {
            resetEmailSettings();
        }
    }

    static void performStepAction(Step step) {
        try {
            switch(step.stepType.get()) {
                case MOVE:
                    robot.mouseMove(step.mouseX, step.mouseY);
                    break;

                case LEFT_CLICK:
                    robot.mouseClick(MouseButton.PRIMARY);
                    break;

                case MOVE_LEFT_CLICK:
                    robot.mouseMove(step.mouseX, step.mouseY);
                    robot.mouseClick(MouseButton.PRIMARY);
                    break;

                case HOLD_KEYS:
                    for(KeyCode key : step.keys) {
                        robot.keyPress(key);
                    }
                    break;

                case RELEASE_KEYS:
                    for(KeyCode key : step.keys) {
                        robot.keyRelease(key);
                    }
                    break;

                case PRESS_KEYS:
                    for(KeyCode key : step.keys) {
                        robot.keyPress(key);
                    }
                    for(KeyCode key : step.keys) {
                        robot.keyRelease(key);
                    }
                    break;

                case PASTE_EMAIL:
                    if(useEmail && emailListItr.hasNext()) {
                        String email = emailListItr.next();
                        App.emailExample_LB.setText(email);
                        email += "@" + App.emailSuffix_TF.getText();
                        System.out.println(email);
                        content.putString(email);
                        robot.keyPress(KeyCode.CONTROL);
                        robot.keyPress(KeyCode.V);
                        robot.keyRelease(KeyCode.CONTROL);
                        robot.keyRelease(KeyCode.V);
                    }
                    break;

                case PASTE_CLIP_0:
                    pasteClipAction(App.HTMLEditor_0);
                    break;

                case PASTE_CLIP_1:
                    pasteClipAction(App.HTMLEditor_1);
                    break;

                case PASTE_CLIP_2:
                    pasteClipAction(App.HTMLEditor_2);
                    break;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            botService.cancel();
        }
        System.out.println("Performed! " + step.getStepTypeStr());
    }

    public static void resetAll() {
        StepTable.tableView.getItems().clear();
        counter.set(0);
        overallWait.set(100);
        loadFile();
    }

    public static void resetEmailSettings() {
        App.srcDir_TF.setText("C:/");
        emailTextFile = null;
        emailTextFileTime = null;
        emailList.clear();
        totalEmail.set(0);
    }

    public static void go() {
        botService.reset();
        botService.start();
    }

    static void pasteClipAction(HTMLEditor htmlEditor) {
        double definedPosX = App.mainStage.getX();
        double definedPosY = App.mainStage.getY();
        double oriMouseX = robot.getMouseX();
        double oriMouseY = robot.getMouseY();
        if(htmlEditor.equals(App.HTMLEditor_0)) {
            definedPosX += 490;
            definedPosY += 50;
        }
        else if(htmlEditor.equals(App.HTMLEditor_1)) {
            definedPosX += 490;
            definedPosY += 200;
        }
        else if(htmlEditor.equals(App.HTMLEditor_2)) {
            definedPosX += 490;
            definedPosY += 350;
        }
        robot.mouseMove(definedPosX, definedPosY);
        robot.mouseClick(MouseButton.PRIMARY);
        robot.keyPress(KeyCode.CONTROL);
        robot.keyPress(KeyCode.A);
        robot.keyRelease(KeyCode.CONTROL);
        robot.keyRelease(KeyCode.A);
        robot.keyPress(KeyCode.CONTROL);
        robot.keyPress(KeyCode.C);
        robot.keyRelease(KeyCode.CONTROL);
        robot.keyRelease(KeyCode.C);
            System.out.println("Clip: " + clipboard.getString());
        robot.mouseMove(oriMouseX, oriMouseY);
        robot.mouseClick(MouseButton.PRIMARY);
        robot.keyPress(KeyCode.CONTROL);
        robot.keyPress(KeyCode.V);
        robot.keyRelease(KeyCode.CONTROL);
        robot.keyRelease(KeyCode.V);
//        content.putHtml(htmlEditor.getHtmlText());
//        clipboard.setContent(content);
//        robot.keyPress(KeyCode.CONTROL);
//        robot.keyPress(KeyCode.V);
//        robot.keyRelease(KeyCode.CONTROL);
//        robot.keyRelease(KeyCode.V);
    }

    public static void destroy() {
        scheduledFuture.cancel(true);
        scheduledExecutorService.shutdownNow();
    }

}

class BotService extends Service<Void> {

    public BotService() {

    }

    @Override
    protected Task<Void> createTask() {

        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    int maxCounter;
                    Platform.runLater(() -> {
                        Bot.clipboard.setContent(Bot.content);
                    });
                    if(Bot.useEmail) {
                        Bot.emailListItr = Bot.emailList.listIterator();
                        maxCounter = Bot.totalEmail.get();
                    }
                    else {
                        maxCounter = Bot.totalRepeat.get();
                    }

                    for(; Bot.counter.get() < maxCounter;) {

                        Platform.runLater(() -> {
                            Bot.counter.set(Bot.counter.get() + 1);
                        });
                        for(Object item : StepTable.tableView.getItems()) {
                            Step step = (Step) item;
                            Platform.runLater(() -> {
                                StepTable.tableView.getSelectionModel().clearSelection();
                                StepTable.tableView.getSelectionModel().select(item);
                                Bot.performStepAction(step);
//                            App.mainPane.requestFocus();
                            });
                            Thread.sleep(step.waitMilisec);
                        }

                        if(Bot.useEmail) {
                            if(Bot.totalEmail.get() - Bot.counter.get() > 0) {
                                Platform.runLater(() -> {
                                    App.mainStage.requestFocus();
                                });
                                Thread.sleep(Bot.overallWait.get());
                            }
                        }
                        else {
                            if(Bot.totalRepeat.get() - Bot.counter.get() > 0) {
                                Platform.runLater(() -> {
                                    App.mainStage.requestFocus();
                                });
                                Thread.sleep(Bot.overallWait.get());
                            }
                        }

                    }
                    Platform.runLater(() -> {
                        App.setDisableInput(false);
                    });
                }
                catch(Exception e) {
                    e.printStackTrace();
                    cancel();
                }
                return null;
            }

        };
    }

}
