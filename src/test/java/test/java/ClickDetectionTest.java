package test.java;

import entity.*;
import entity.scripting.Trigger;
import entity.scripting.event.OnClickEvent;
import entity.scripting.action.PrintAction;
import entity.scripting.environment.Environment;
import view.PreviewWindow;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Vector;

public class ClickDetectionTest {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Vector<Double> position = new Vector<>();
                position.add(400.0);
                position.add(300.0);

                Vector<Double> scale = new Vector<>();
                scale.add(1.0);
                scale.add(1.0);

                Transform transform = new Transform(position, 0f, scale);

                ArrayList<Property> properties = new ArrayList<>();
                GameObject testObject = new GameObject(
                        "test-1",
                        "ClickableBox",
                        true,
                        properties,
                        new Environment()
                );
                testObject.setTransform(transform);

                OnClickEvent clickEvent = new OnClickEvent();
                Trigger clickTrigger = new Trigger(clickEvent);

                clickTrigger.addAction(new PrintAction("SUCCESS! Object was clicked!"));

                testObject.getTriggerManager().getAllTriggers().add(clickTrigger);

                ArrayList<GameObject> gameObjects = new ArrayList<>();
                gameObjects.add(testObject);

                Scene testScene = new Scene(
                        "test-scene",
                        "Click Test Scene",
                        gameObjects,
                        null
                );

                Environment globalEnv = new Environment();
                PreviewWindow preview = new PreviewWindow(testScene, globalEnv);
                preview.display();

                System.out.println("===================================");
                System.out.println("Click Detection Test");
                System.out.println("===================================");
                System.out.println("1. You will see a gray box (50x50) in the center of the canvas");
                System.out.println("2. Click the box with your mouse");
                System.out.println("3. If successful, the console will print:");
                System.out.println("   'SUCCESS! Object was clicked!'");
                System.out.println("===================================");

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}