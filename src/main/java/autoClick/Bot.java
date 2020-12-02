package autoClick;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.concurrent.*;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.concurrent.*;
import javafx.scene.input.*;
import javafx.scene.robot.Robot;
import javafx.scene.web.HTMLEditor;
import javafx.stage.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Whitelist;
//import 

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
    public static BufferedWriter writter;
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
    public static ObjectProperty<Integer> overallWait = new SimpleObjectProperty<>(500);

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
            if(event.getCode().equals(KeyCode.PAUSE) && botService.isRunning()) {
                System.out.println("PAUSE!");
                botService.cancel();
                Platform.runLater(() -> {
                    App.minimize(false);
                    App.setDisableInput(false);
                });
            }
        }
        );

        App.mainStage.focusedProperty().addListener((ov, oldValue, newValue) -> {

            if(newValue && !botService.isRunning()) {
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

    static void checkClipboard() {
        if(clipboard.hasFiles()) {
            System.out.println("Files: " + clipboard.getFiles().toString());
        }
        if(clipboard.hasImage()) {
            System.out.println("Image: " + clipboard.getImage().toString());
        }
        if(clipboard.hasRtf()) {
            System.out.println("Rtf: " + clipboard.getRtf());
        }
        if(clipboard.hasUrl()) {
            System.out.println("URL: " + clipboard.getUrl());
        }
        if(clipboard.hasHtml()) {
            System.out.println("HTML: " + clipboard.getHtml());
        }
        if(clipboard.hasString()) {
            System.out.println("String: " + clipboard.getString());
        }

    }

    public static void configureFileChooser() {
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.setTitle("Choose Email Text File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text File", "*.txt")
        );
    }

    public static void openFileChooser() {
        App.setDisableInput(true);
        emailTextFile = fileChooser.showOpenDialog(App.mainStage);
        if(emailTextFile != null) {
            loadFile();
        }
        App.setDisableInput(false);
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
                    Platform.runLater(() -> {
                        robot.mouseMove(step.mouseX, step.mouseY);
                    });
                    break;

                case LEFT_CLICK:
                    Platform.runLater(() -> {
                        robot.mouseClick(MouseButton.PRIMARY);
                    });
                    break;

                case MOVE_LEFT_CLICK:
                    Platform.runLater(() -> {
                        robot.mouseMove(step.mouseX, step.mouseY);
                        robot.mouseClick(MouseButton.PRIMARY);
                    });
                    break;

                case HOLD_KEYS:
                    Platform.runLater(() -> {
                        for(KeyCode key : step.keys) {
                            robot.keyPress(key);
                        }
                    });
                    break;

                case RELEASE_KEYS:
                    Platform.runLater(() -> {
                        for(KeyCode key : step.keys) {
                            robot.keyRelease(key);
                        }
                    });
                    break;

                case PRESS_KEYS:
                    Platform.runLater(() -> {
                        for(KeyCode key : step.keys) {
                            robot.keyPress(key);
                        }
                        for(KeyCode key : step.keys) {
                            robot.keyRelease(key);
                        }
                    });
                    break;

                case ENTER:
                    Platform.runLater(() -> {
                        robot.keyType(KeyCode.ENTER);
                    });
                    break;

                case PASTE_EMAIL:
                    if(useEmail && emailListItr.hasNext()) {
                        Platform.runLater(() -> {
                            String email = emailListItr.next();
                            App.emailExample_LB.setText(email + "@");
                            email += "@" + App.emailSuffix_TF.getText();
                            content.clear();
                            content.putString(email);
                            clipboard.setContent(content);
                        });
                        Platform.runLater(() -> {
                            robot.keyPress(KeyCode.CONTROL);
                            robot.keyPress(KeyCode.V);
                            robot.keyRelease(KeyCode.CONTROL);
                            robot.keyRelease(KeyCode.V);
                        });
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
            System.out.println("STOP ACTION!");
            botService.cancel();
        }
    }

    public static void resetAll() {
        StepTable.tableView.getItems().clear();
        counter.set(0);
        overallWait.set(500);
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
        Platform.runLater(() -> {
            content.clear();
            content.putHtml(htmlEditor.getHtmlText());
            content.putString(stripHTMLTags(htmlEditor.getHtmlText()));
            clipboard.setContent(content);
        });
        Platform.runLater(() -> {
            robot.keyPress(KeyCode.CONTROL);
            robot.keyPress(KeyCode.V);
            robot.keyRelease(KeyCode.CONTROL);
            robot.keyRelease(KeyCode.V);
        });
    }

    private static String stripHTMLTags(String html) {
        if(html == null) {
            return html;
        }
        Document jsoupDoc = Jsoup.parse(html);
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(false);
        jsoupDoc.outputSettings(outputSettings);
        jsoupDoc.select("br").before("\\n");
        jsoupDoc.select("p").before("\\n");
        String str = jsoupDoc.html().replaceAll("\\\\n", "\n");
        String strWithNewLines
               = Jsoup.clean(str, "", Whitelist.none(), outputSettings);
        return strWithNewLines;
    }

    public static void destroy() {
        scheduledFuture.cancel(true);
        scheduledExecutorService.shutdownNow();
        botService.cancel();
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
                            });
                            Bot.performStepAction(step);
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
                        App.minimize(false);
                        App.setDisableInput(false);
                    });
                }
                catch(Exception e) {
                    System.out.println("STOP!");
                    cancel();
                }
                return null;
            }

        };
    }

}
