package interface_adapter.Sprites;

import interface_adapter.ViewModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * View Model for import sprite.
 * Follows the Observer pattern to notify views when the state changes.
 * TODO: may need to make changes according to the final view implementation.
 */
public class ImportSpriteViewModel {

    public static final String IMPORT_SPRITE_PROPERTY = "importSprite"; // add abstract class view model extension

    private ImportSpriteState state = new ImportSpriteState();
    private final PropertyChangeSupport support = new PropertyChangeSupport(this);

    public ImportSpriteViewModel() {
    }

    public ImportSpriteState getState() {
        return state;
    }

    public void setState(ImportSpriteState state) {
        this.state = state;
    }

    /**
     * notifies all registered listeners that the state has changed.
     */
    public void firePropertyChanged() {
        support.firePropertyChange(IMPORT_SPRITE_PROPERTY, null, this.state);
    }

    /**
     * adds a listener to be notified of state changes.
     * @param listener the property change listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    /**
     * removes a listener.
     * @param listener the property change listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}