package entity;

import java.util.Set;

/**
 * InputState - Interface for querying input state.
 * Part of Entity layer - defines input state contract.
 *
 * This interface allows Entity layer components (EventListeners)
 * to query input state without depending on UI frameworks.
 *
 * @author Wanru Cheng
 */
public interface InputState {

    // ===== Keyboard Queries =====

    boolean isKeyPressed(String key);
    boolean isKeyJustPressed(String key);
    Set<String> getPressedKeys();

    // ===== Mouse Queries =====

    int getMouseX();
    int getMouseY();
    boolean isMouseClicked();
    boolean isMouseJustClicked();
    int getMouseButton();
    boolean isLeftClickJustPressed();
    boolean isRightClickJustPressed();

    // ===== Canvas Dimension Queries =====

    int getCanvasWidth();
    int getCanvasHeight();

    // ===== Debug =====

    String getMouseStateString();
}