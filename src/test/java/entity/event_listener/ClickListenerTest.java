package entity.event_listener;
import entity.SpriteRenderer;
import entity.GameObject;
import entity.InputManager;
import entity.Property;
import entity.Transform;
import entity.scripting.environment.Environment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import entity.Image;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Vector;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for ClickListener.
 *
 * @author Wanru Cheng
 */
class ClickListenerTest {

    private InputManager inputManager;
    private JPanel dummyComponent;

    @BeforeEach
    void setUp() {
        inputManager = new InputManager();
        dummyComponent = new JPanel();
    }
    @Test
    void collisionMode_withSpriteRenderer_usesSpriteBounds() throws Exception {
        // Create a temporary test image file
        Path tempFile = Files.createTempFile("test_image", ".png");
        BufferedImage bufferedImage = new BufferedImage(80, 60, BufferedImage.TYPE_INT_ARGB);
        ImageIO.write(bufferedImage, "png", tempFile.toFile());

        try {
            // Create Image from temp file
            Image image = new Image(tempFile);

            // Create SpriteRenderer with the image
            SpriteRenderer spriteRenderer = new SpriteRenderer(image, true);

            // Create GameObject with SpriteRenderer
            Vector<Double> position = new Vector<>();
            position.add(100.0);
            position.add(100.0);

            Vector<Double> scale = new Vector<>();
            scale.add(1.0);
            scale.add(1.0);

            Transform transform = new Transform(position, 0f, scale);

            ArrayList<Property> properties = new ArrayList<>();
            properties.add(spriteRenderer);

            GameObject obj = new GameObject(
                    "obj-1",
                    "TestObject",
                    true,
                    properties,
                    new Environment()
            );
            obj.setTransform(transform);

            ClickListener listener = new ClickListener(obj, inputManager);

            // Simulate click at center
            MouseEvent mouseEvent = new MouseEvent(
                    dummyComponent, MouseEvent.MOUSE_PRESSED,
                    System.currentTimeMillis(), 0,
                    100, 100, 1, false, MouseEvent.BUTTON1
            );
            inputManager.getMouseListener().mousePressed(mouseEvent);

            // This triggers getSpriteRenderer() and the for loop
            assertTrue(listener.isTriggered());
        } finally {
            // Clean up temp file
            Files.deleteIfExists(tempFile);
        }
    }

//    @Test
//    void collisionMode_withSpriteRenderer_usesSpriteBounds() {
//        // Arrange - create GameObject with SpriteRenderer
//        Vector<Double> position = new Vector<>();
//        position.add(100.0);
//        position.add(100.0);
//
//        Vector<Double> scale = new Vector<>();
//        scale.add(1.0);
//        scale.add(1.0);
//
//        Transform transform = new Transform(position, 0f, scale);
//
//        // Create SpriteRenderer and add to properties
//        ArrayList<Property> properties = new ArrayList<>();
//        SpriteRenderer spriteRenderer = new SpriteRenderer();
//        properties.add(spriteRenderer);
//
//        GameObject obj = new GameObject(
//                "obj-1",
//                "TestObject",
//                true,
//                properties,
//                new Environment()
//        );
//        obj.setTransform(transform);
//
//        ClickListener listener = new ClickListener(obj, inputManager);
//
//        // Simulate click
//        MouseEvent mouseEvent = new MouseEvent(
//                dummyComponent, MouseEvent.MOUSE_PRESSED,
//                System.currentTimeMillis(), 0,
//                100, 100, 1, false, MouseEvent.BUTTON1
//        );
//        inputManager.getMouseListener().mousePressed(mouseEvent);
//
//        // This will trigger getSpriteRenderer() and the for loop
//        listener.isTriggered();
//    }
    // ========== Button Mode Tests ==========

    @Test
    void buttonMode_isTriggered_initialState_returnsFalse() {
        ClickListener listener = new ClickListener("TestButton");
        assertFalse(listener.isTriggered());
    }

    @Test
    void buttonMode_isTriggered_afterNotifyClicked_returnsTrue() {
        ClickListener listener = new ClickListener("TestButton");
        listener.notifyClicked();
        assertTrue(listener.isTriggered());
    }

    @Test
    void buttonMode_isTriggered_afterReset_returnsFalse() {
        ClickListener listener = new ClickListener("TestButton");
        listener.notifyClicked();
        listener.reset();
        assertFalse(listener.isTriggered());
    }

    @Test
    void buttonMode_isUsingCollisionDetection_returnsFalse() {
        ClickListener listener = new ClickListener("TestButton");
        assertFalse(listener.isUsingCollisionDetection());
    }

    @Test
    void buttonMode_getButtonLabel_returnsLabel() {
        ClickListener listener = new ClickListener("MyButton");
        assertEquals("MyButton", listener.getButtonLabel());
    }

    @Test
    void buttonMode_getGameObject_returnsNull() {
        ClickListener listener = new ClickListener("TestButton");
        assertNull(listener.getGameObject());
    }

    // ========== Collision Mode Tests ==========

    @Test
    void collisionMode_isUsingCollisionDetection_returnsTrue() {
        GameObject obj = createTestGameObject(100, 100);
        ClickListener listener = new ClickListener(obj, inputManager);
        assertTrue(listener.isUsingCollisionDetection());
    }

    @Test
    void collisionMode_getGameObject_returnsObject() {
        GameObject obj = createTestGameObject(100, 100);
        ClickListener listener = new ClickListener(obj, inputManager);
        assertEquals(obj, listener.getGameObject());
    }

    @Test
    void collisionMode_getButtonLabel_returnsObjectName() {
        GameObject obj = createTestGameObject(100, 100);
        ClickListener listener = new ClickListener(obj, inputManager);
        assertEquals("TestObject", listener.getButtonLabel());
    }

    @Test
    void collisionMode_isTriggered_noClick_returnsFalse() {
        GameObject obj = createTestGameObject(100, 100);
        ClickListener listener = new ClickListener(obj, inputManager);
        assertFalse(listener.isTriggered());
    }

    @Test
    void collisionMode_isTriggered_clickInsideBounds_returnsTrue() {
        GameObject obj = createTestGameObject(100, 100);
        ClickListener listener = new ClickListener(obj, inputManager);

        // Simulate left click at center of object
        MouseEvent mouseEvent = new MouseEvent(
                dummyComponent, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0,
                100, 100, 1, false, MouseEvent.BUTTON1
        );
        inputManager.getMouseListener().mousePressed(mouseEvent);

        assertTrue(listener.isTriggered());
    }

    @Test
    void collisionMode_isTriggered_clickOutsideBounds_returnsFalse() {
        GameObject obj = createTestGameObject(100, 100);
        ClickListener listener = new ClickListener(obj, inputManager);

        // Simulate left click far from object
        MouseEvent mouseEvent = new MouseEvent(
                dummyComponent, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0,
                500, 500, 1, false, MouseEvent.BUTTON1
        );
        inputManager.getMouseListener().mousePressed(mouseEvent);

        assertFalse(listener.isTriggered());
    }

    @Test
    void collisionMode_isTriggered_rightClick_returnsFalse() {
        GameObject obj = createTestGameObject(100, 100);
        ClickListener listener = new ClickListener(obj, inputManager);

        // Simulate right click (not left)
        MouseEvent mouseEvent = new MouseEvent(
                dummyComponent, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0,
                100, 100, 1, false, MouseEvent.BUTTON3
        );
        inputManager.getMouseListener().mousePressed(mouseEvent);

        assertFalse(listener.isTriggered());
    }

    @Test
    void collisionMode_isTriggered_nullTransform_returnsFalse() {
        // GameObject without transform
        GameObject obj = new GameObject(
                "obj-1",
                "TestObject",
                true,
                new ArrayList<>(),
                new Environment()
        );
        ClickListener listener = new ClickListener(obj, inputManager);

        MouseEvent mouseEvent = new MouseEvent(
                dummyComponent, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0,
                100, 100, 1, false, MouseEvent.BUTTON1
        );
        inputManager.getMouseListener().mousePressed(mouseEvent);

        assertFalse(listener.isTriggered());
    }

    @Test
    void collisionMode_nullGameObject_getButtonLabelReturnsUnknown() {
        ClickListener listener = new ClickListener(null, inputManager);
        assertEquals("Unknown", listener.getButtonLabel());
    }

    @Test
    void collisionMode_nullInputManager_isTriggeredReturnsFalse() {
        GameObject obj = createTestGameObject(100, 100);
        ClickListener listener = new ClickListener(obj, null);
        assertFalse(listener.isTriggered());
    }

    // ========== toString Tests ==========

    @Test
    void toString_buttonMode_containsButtonAndMode() {
        ClickListener listener = new ClickListener("TestButton");
        String str = listener.toString();
        assertTrue(str.contains("TestButton"));
        assertTrue(str.contains("button"));
    }

    @Test
    void toString_collisionMode_containsCollision() {
        GameObject obj = createTestGameObject(100, 100);
        ClickListener listener = new ClickListener(obj, inputManager);
        String str = listener.toString();
        assertTrue(str.contains("collision"));
    }

    // ========== Helper methods ==========

    private GameObject createTestGameObject(double x, double y) {
        Vector<Double> position = new Vector<>();
        position.add(x);
        position.add(y);

        Vector<Double> scale = new Vector<>();
        scale.add(1.0);
        scale.add(1.0);

        Transform transform = new Transform(position, 0f, scale);

        GameObject obj = new GameObject(
                "obj-1",
                "TestObject",
                true,
                new ArrayList<>(),
                new Environment()
        );
        obj.setTransform(transform);

        return obj;
    }
}