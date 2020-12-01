package autoClick;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import javafx.scene.input.KeyCode;

/**
 *
 * @author TING WEI JING
 */
public class Test {

    public static BufferedWriter output;

    public static void main(String[] args) {
        Step step1 = new Step(Type.ENTER, 100, 200, 1200);
        String text = convertToText(step1);
        System.out.println(text);

        Step step2 = convertToStep(text);
        System.out.println(convertToText(step2));
    }

    public static void saveStepTable(String fileName) {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(fileName));
            output.write("Steady Bom Bi Bi!");
            output.newLine();

            output.close();

        }
        catch(IOException ex) {
            System.out.println("Save preset error");
        }
    }

    static String convertToText(Step step) {
        if(step == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("T=");
        sb.append(step.stepType.get().name());
        sb.append(";X=");
        sb.append(step.mouseX);
        sb.append(";Y=");
        sb.append(step.mouseY);
        sb.append(";K=");
        for(KeyCode key : step.keys) {
            sb.append(key.name()).append(",");
        }
        sb.append(";W=");
        sb.append(step.waitMilisec);
        return sb.toString();
    }

    public static void loadStepTable() {

    }

    static Step convertToStep(String stepText) {
        if(stepText.equals("")) {
            return null;
        }
        String[] data = stepText.split(";");

        Type tmpType = Type.valueOf(data[0].substring(2));

        int tmpMouseX = Integer.parseInt(data[1].substring(2));

        int tmpMouseY = Integer.parseInt(data[2].substring(2));

        LinkedList<KeyCode> tmpKeys = new LinkedList<>();
        String[] keyData = data[3].substring(2).split(",");
        if(!keyData[0].equals("")) {
            for(String key : keyData) {
                tmpKeys.add(KeyCode.valueOf(key));
            }
        }

        int tmpWait = Integer.parseInt(data[4].substring(2));

        Step step = new Step(tmpType, tmpMouseX, tmpMouseY, tmpWait, new KeyCode[0]);
        step.keys = (LinkedList<KeyCode>) tmpKeys.clone();
        return step;
    }

}
