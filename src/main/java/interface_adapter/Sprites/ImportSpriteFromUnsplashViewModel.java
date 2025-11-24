package interface_adapter.Sprites;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * View Model for Unsplash sprite import.
 * Follows the Observer pattern to notify views when the state changes.
 */
public class ImportSpriteFromUnsplashViewModel {

    public static final String IMPORT_SPRITE_FROM_UNSPLASH_PROPERTY = "importSpriteFromUnsplash";

    private ImportSpriteFromUnsplashState state = new ImportSpriteFromUnsplashState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public ImportSpriteFromUnsplashViewModel() {
    }

    public ImportSpriteFromUnsplashState getState() {
        return state;
    }

    public void setState(ImportSpriteFromUnsplashState state) {
        this.state = state;
    }

    /**
     * Notifies all registered listeners that the state has changed.
     */
    public void firePropertyChanged() {
        support.firePropertyChange(IMPORT_SPRITE_FROM_UNSPLASH_PROPERTY, null, this.state);
    }

    /**
     * Adds a listener to be notified of state changes.
     * @param listener the property change listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * Removes a listener.
     * @param listener the property change listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

    // Convenience methods for backward compatibility
    public void setSearchQuery(String searchQuery) {
        state.setSearchQuery(searchQuery);
    }

    public void setSelectedImageId(String selectedImageId) {
        state.setSelectedImageId(selectedImageId);
    }

    public void setTargetFileName(String targetFileName) {
        state.setTargetFileName(targetFileName);
    }

    public void setStatusMessage(String statusMessage) {
        state.setMessage(statusMessage);
    }

    public void setLoading(boolean loading) {
        state.setLoading(loading);
    }
}

