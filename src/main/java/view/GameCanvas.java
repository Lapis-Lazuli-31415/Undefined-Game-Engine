package view;

import entity.GameObject;
import entity.Transform;

import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.scripting.event.OnClickEvent;
import entity.Eventlistener.ClickListener;
import entity.scripting.condition.Condition;
import entity.scripting.action.Action;
import entity.scripting.environment.Environment;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * GameCanvas - Creates UI buttons for GameObjects with OnClickEvent
 *
 * Button label = GameObject.getName()
 *
 * @author Wanru Cheng
 */
public class GameCanvas extends JPanel {

    private ArrayList<GameObject> gameObjects;
    private Map<GameObject, JButton> uiButtons;
    private Map<GameObject, ClickListener> clickListeners;
    private Environment globalEnvironment;

    private int currentFps = 0;
    private boolean showFps = true;

    public GameCanvas() {
        this.gameObjects = new ArrayList<>();
        this.uiButtons = new HashMap<>();
        this.clickListeners = new HashMap<>();

        setPreferredSize(new Dimension(800, 600));
        setBackground(new Color(45, 45, 48));
        setLayout(null);
    }

    public void setGameObjects(ArrayList<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
        updateUIButtons();
        repaint();
    }

    private void updateUIButtons() {
        // Remove old buttons
        for (JButton btn : uiButtons.values()) {
            remove(btn);
        }
        uiButtons.clear();
        clickListeners.clear();

        // Create new buttons
        for (GameObject obj : gameObjects) {
            if (!obj.isActive()) continue;

            if (hasOnClickEvent(obj)) {
                createUIButton(obj);
            }
        }

        revalidate();
    }

    private void createUIButton(GameObject obj) {
        Transform t = obj.getTransform();
        if (t == null) return;

        // Create button with GameObject's name as label
        String label = obj.getName();
        JButton button = new JButton(label);

        // Set position and size
        int x = (int) t.getX();
        int y = (int) t.getY();
        int width = 120;
        int height = 40;

        button.setBounds(x, y, width, height);

        // Create ClickListener for this button
        ClickListener clickListener = new ClickListener(label);
        clickListeners.put(obj, clickListener);

        // Add click action
        button.addActionListener(e -> {
            clickListener.notifyClicked();
            executeOnClickTriggers(obj);
            System.out.println("🖱️ Button clicked: [" + label + "]");
        });

        uiButtons.put(obj, button);
        add(button);
    }

    private boolean hasOnClickEvent(GameObject obj) {
        TriggerManager tm = obj.getTriggerManager();
        if (tm == null) return false;

        for (Trigger trigger : tm.getAllTriggers()) {
            if (trigger.getEvent() instanceof OnClickEvent) {
                return true;
            }
        }
        return false;
    }

    private void executeOnClickTriggers(GameObject obj) {
        TriggerManager tm = obj.getTriggerManager();
        if (tm == null) return;

        for (Trigger trigger : tm.getAllTriggers()) {
            if (trigger.getEvent() instanceof OnClickEvent) {
                executeTrigger(trigger, obj);
            }
        }
    }

    private void executeTrigger(Trigger trigger, GameObject obj) {
        try {
            Environment localEnv = obj.getEnvironment();

            boolean allConditionsMet = true;
            for (Condition condition : trigger.getConditions()) {
                if (!condition.evaluate(globalEnvironment, localEnv)) {
                    allConditionsMet = false;
                    break;
                }
            }

            if (allConditionsMet) {
                for (Action action : trigger.getActions()) {
                    action.execute(globalEnvironment, localEnv);
                }
            }
        } catch (Exception e) {
            System.err.println("Error executing trigger: " + e.getMessage());
        }
    }

    public ClickListener getClickListener(GameObject obj) {
        return clickListeners.get(obj);
    }

    public void resetClickListeners() {
        for (ClickListener listener : clickListeners.values()) {
            listener.reset();
        }
    }

    public void updateClickablePositions() {
        for (Map.Entry<GameObject, JButton> entry : uiButtons.entrySet()) {
            GameObject obj = entry.getKey();
            JButton button = entry.getValue();
            Transform t = obj.getTransform();

            if (t != null) {
                button.setLocation((int) t.getX(), (int) t.getY());
            }
        }
    }

    public void setGlobalEnvironment(Environment env) {
        this.globalEnvironment = env;
    }

    public void setFps(int fps) {
        this.currentFps = fps;
    }

    public void setShowFps(boolean show) {
        this.showFps = show;
    }

    public void setDebugMode(boolean debug) {
        // Optional
    }

    public void setShowGrid(boolean show) {
        // Optional
    }

    public void setBackgroundColor(Color color) {
        setBackground(color);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (showFps) {
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("FPS: " + currentFps, 10, 20);
        }
    }

    public void dispose() {
        for (JButton btn : uiButtons.values()) {
            remove(btn);
        }
        uiButtons.clear();
        clickListeners.clear();
    }
}