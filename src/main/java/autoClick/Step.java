package autoClick;

import java.util.*;
import java.util.stream.Collectors;
import javafx.beans.property.*;
import javafx.scene.input.*;

/**
 *
 * @author TING WEI JING
 */
public class Step {

    ObjectProperty<Type> stepType;
    int mouseX;
    int mouseY;
    LinkedList<KeyCode> keys;
    int waitMilisec;
    StringProperty description;

    public Step(Type stepType, int mouseX, int mouseY, int waitMilisec) {
        this(stepType, mouseX, mouseY, waitMilisec, new KeyCode[0]);
    }

    public Step(Type stepType, int waitMilisec) {
        this(stepType, 0, 0, waitMilisec, new KeyCode[0]);
    }

    public Step(Type stepType, int mouseX, int mouseY, int waitMilisec, KeyCode... keys) {
        this.stepType = new SimpleObjectProperty<>(stepType);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.keys = new LinkedList<>(Arrays.asList(keys));
        this.waitMilisec = waitMilisec;
        this.description = new SimpleStringProperty("");
        updateDescription();
    }

    public void changeType(Type newType) {
        stepType.set(newType);
        updateDescription();
    }
    
    public String getStepTypeStr() {
        return stepType.toString();
    }

    public LinkedList<KeyCode> getKeys() {
        return keys;
    }

    public int getWaitMilisec() {
        return waitMilisec;
    }

    public void updateDescription() {
        switch(stepType.get()) {

            case MOVE:
            case LEFT_CLICK:
            case MOVE_LEFT_CLICK:
                setDescription("X:" + mouseX + " Y:" + mouseY);
                break;

            case HOLD_KEYS:
            case RELEASE_KEYS:
            case PRESS_KEYS:
                setDescription("Key: "+keys.stream().map(Object::toString).collect(Collectors.joining(" + ")));
                break;

            case PASTE_EMAIL:
                setDescription("Paste emails");
                break;

            case PASTE_CLIP_0:
            case PASTE_CLIP_1:
            case PASTE_CLIP_2:
                setDescription("");
                break;
        }
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String value) {
        description.set(value);
    }
    
    public void setWaitMilisec(int waitMilisec) {
        this.waitMilisec = waitMilisec;
    }

    public Type getStepType() {
        return stepType.get();
    }
    
    public void setStepType(Type type) {
        stepType.set(type);
    }
    
    public ObjectProperty<Type> stepTypeProperty() {
        return this.stepType;
    }
    
}

enum Type {
    MOVE("Move"),
    LEFT_CLICK("Left click"),
    MOVE_LEFT_CLICK("Move & Left click"),
    HOLD_KEYS("Hold key(s)"),
    RELEASE_KEYS("Release key(s)"),
    PRESS_KEYS("Press key(s)"),
    PASTE_EMAIL("Paste next email"),
    PASTE_CLIP_0("Paste content 0"),
    PASTE_CLIP_1("Paste content 1"),
    PASTE_CLIP_2("Paste content 2");

    private String name;

    private Type(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
