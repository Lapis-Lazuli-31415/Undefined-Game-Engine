package interface_adapter.preview;

import entity.event_listener.ClickListener;
import entity.event_listener.EventListener;
import entity.event_listener.KeyPressListener;
import entity.GameObject;
import entity.InputManager;
import entity.Scene;
import entity.scripting.event.Event;
import entity.scripting.event.OnClickEvent;
import entity.scripting.event.OnKeyPressEvent;
import use_case.preview.PreviewInputBoundary;
import use_case.preview.PreviewInputData;

/**
 * Controller for preview feature.
 * Part of Interface Adapter layer (green ring in CA diagram).
 * Receives user input from View and converts to Use Case input.
 *
 * @author Wanru Cheng
 */
public class PreviewController {

    private final PreviewInputBoundary previewInteractor;

    /**
     * Constructor.
     *
     * @param previewInteractor The use case interactor
     */
    public PreviewController(PreviewInputBoundary previewInteractor) {
        this.previewInteractor = previewInteractor;
    }

    /**
     * Execute preview use case.
     * Called when user clicks Play button.
     *
     * @param scene The scene to preview
     */
    public void execute(Scene scene) {
        PreviewInputData inputData = new PreviewInputData(scene);
        previewInteractor.execute(inputData);
    }

    /**
     * Stop preview.
     * Called when user clicks Stop button.
     */
    public void stop() {
        previewInteractor.stop();
    }

    /**
     * Factory for creating EventListener objects.
     * Part of Interface Adapter layer (green ring in CA diagram).
     *
     * Responsibilities:
     * - Create appropriate EventListener based on Event type
     * - Encapsulate listener creation logic
     * - Allow View layer to get listeners without direct instantiation
     *
     * Design Pattern: Factory Pattern
     *
     * @author Wanru Cheng
     */
    public static class EventListenerFactory {

        private final InputManager inputManager;

        /**
         * Constructor.
         *
         * @param inputManager The input manager for keyboard events
         */
        public EventListenerFactory(InputManager inputManager) {
            this.inputManager = inputManager;
        }

        /**
         * Create an EventListener for a keyboard event.
         *
         * @param event The OnKeyPressEvent
         * @return KeyPressListener for this event
         */
        public EventListener createKeyPressListener(OnKeyPressEvent event) {
            return new KeyPressListener(event, inputManager);
        }

        /**
         * Create an EventListener for a click event.
         *
         * @param buttonName The name of the button/GameObject
         * @return ClickListener for this button
         */
        public EventListener createClickListener(String buttonName) {
            return new ClickListener(buttonName);
        }

        /**
         * Create an EventListener based on Event type (generic factory method).
         *
         * @param event The event to create listener for
         * @param gameObject The GameObject (needed for OnClickEvent)
         * @return Appropriate EventListener, or null if event type unknown
         */
        public EventListener createListener(Event event, GameObject gameObject) {
            if (event instanceof OnKeyPressEvent) {
                return createKeyPressListener((OnKeyPressEvent) event);
            } else if (event instanceof OnClickEvent) {
                // For click events, use GameObject name as button identifier
                String buttonName = gameObject != null ? gameObject.getName() : "Unknown";
                return createClickListener(buttonName);
            }

            // Unknown event type
            System.err.println("Unknown event type: " + event.getClass().getName());
            return null;
        }
    }
}