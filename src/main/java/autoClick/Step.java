/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoClick;

import java.util.List;
import javafx.scene.input.KeyCode;

/**
 *
 * @author TING WEI JING
 */
public class Step {

    public Type stepType;
    public int mouseX;
    public int mouseY;
    public List<KeyCode> keys;

}

enum Type {
    MOVE("Move"),
    LEFT_CLICK("Left click"),
    MOVE_LEFT_CLICK("Move & Left click"),
    HOLD_KEYS("Hold key(s)"),
    RELEASE_KEYS("Release key(s)"),
    PRESS_KEYS("Press key(s)"),
    PASTE_EMAIL("Paste next email"),
    PASE_CLIP_0("Paste content 0"),
    PASE_CLIP_1("Paste content 1"),
    PASE_CLIP_2("Paste content 2");

    private String name;

    private Type(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

}
