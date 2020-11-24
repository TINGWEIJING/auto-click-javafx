package autoClick;

import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.robot.Robot;

/**
 *
 * @author TING WEI JING
 */
public class Bot {

    public static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    public static ScheduledFuture scheduledFuture;
    public static Robot robot = new Robot();

    public static ArrayList<Step> stepList = new ArrayList<>();

    public static final long UPDATE_RATE = (long) 33333.3;

    public static void startCollectMousePointThread() {

        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                App.mousePosX_LB.setText((int) robot.getMouseX() + "");
                App.mousePosY_LB.setText((int) robot.getMouseY() + "");
            });
        },
                                                                       0, UPDATE_RATE,
                                                                       TimeUnit.MICROSECONDS);
    }

    public static void performStepAction(Step step) {
        switch(step.stepType) {

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
                
                break;

            case PASE_CLIP_0:

                break;

            case PASE_CLIP_1:

                break;

            case PASE_CLIP_2:

                break;

        }
    }

    public static void destroy() {
        scheduledFuture.cancel(true);
        scheduledExecutorService.shutdownNow();
    }

}
