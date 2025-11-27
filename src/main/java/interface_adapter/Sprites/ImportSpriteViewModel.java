package interface_adapter.Sprites;

import interface_adapter.ViewModel;

/**
 * View Model for import sprite.
 * Follows the Observer pattern to notify views when the state changes.
 */
public class ImportSpriteViewModel extends ViewModel<ImportSpriteState> {

    public static final String IMPORT_SPRITE_PROPERTY = "importSprite";

    public ImportSpriteViewModel() {
        super("importSprite");
        this.state = new ImportSpriteState();
    }

    /**
     * notifies all registered listeners that the state has changed.
     */
    public void firePropertyChanged() {
        firePropertyChange(IMPORT_SPRITE_PROPERTY);
    }
}