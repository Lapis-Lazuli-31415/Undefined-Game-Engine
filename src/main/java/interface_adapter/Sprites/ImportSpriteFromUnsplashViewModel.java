package interface_adapter.Sprites;

import interface_adapter.ViewModel;

/**
 * View Model for Unsplash sprite import.
 * Follows the Observer pattern to notify views when the state changes.
 */
public class ImportSpriteFromUnsplashViewModel extends ViewModel<ImportSpriteFromUnsplashState> {

    public static final String IMPORT_SPRITE_FROM_UNSPLASH_PROPERTY = "importSpriteFromUnsplash";

    public ImportSpriteFromUnsplashViewModel() {
        super("importSpriteFromUnsplash");
        this.state = new ImportSpriteFromUnsplashState();
    }

    /**
     * notifies all registered listeners that the state has changed.
     */
    public void firePropertyChanged() {
        firePropertyChange(IMPORT_SPRITE_FROM_UNSPLASH_PROPERTY);
    }
}

