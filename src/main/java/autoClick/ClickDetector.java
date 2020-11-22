package autoClick;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import javafx.application.Platform;
import javafx.scene.robot.Robot;

/**
 *
 * @author TING WEI JING
 */
public class ClickDetector {

    public static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    public static ScheduledFuture scheduledFuture;
    public static Robot robot = new Robot();

    public static final long UPDATE_RATE = (long) 33333.3;

    public static void startCollectMousePointThread() {
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {
                App.mousePosX.setText((int) robot.getMouseX() + "");
                App.mousePosY.setText((int) robot.getMouseY() + "");
            });
        },
                                                                       0, UPDATE_RATE,
                                                                       TimeUnit.MICROSECONDS);
    }

    public static void destroy() {
        scheduledFuture.cancel(true);
        scheduledExecutorService.shutdownNow();
    }

}
