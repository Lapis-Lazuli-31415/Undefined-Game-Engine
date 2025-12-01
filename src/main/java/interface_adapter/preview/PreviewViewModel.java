package interface_adapter.preview;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * ViewModel for preview feature.
 * Part of Interface Adapter layer (green ring in CA diagram).
 * Holds state that View observes.
 *
 * @author Wanru Cheng
 */
public class PreviewViewModel {

    private final PropertyChangeSupport support;
    private PreviewState state;

    /**
     * Constructor.
     */
    public PreviewViewModel() {
        this.support = new PropertyChangeSupport(this);
        this.state = new PreviewState();
    }

    /**
     * Get current state.
     *
     * @return The preview state
     */
    public PreviewState getState() {
        return state;
    }

    /**
     * Set state.
     *
     * @param state The new state
     */
    public void setState(PreviewState state) {
        this.state = state;
    }

    /**
     * Notify observers of state change.
     */
    public void firePropertyChanged() {
        support.firePropertyChange("state", null, state);
    }

    /**
     * Add property change listener.
     *
     * @param listener The listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }
}