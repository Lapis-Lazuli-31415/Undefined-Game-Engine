package interface_adapter.assets;

import entity.Asset;
import entity.AssetLib;
import interface_adapter.ViewModel;

/**
 * Interface Adapter layer wrapper for AssetLib.
 * Adds observer pattern capabilities without polluting the entity layer.
 */
public class AssetLibViewModel extends ViewModel<AssetLib> {

    public static final String ASSET_ADDED = "assetAdded";
    public static final String ASSET_REMOVED = "assetRemoved";

    public AssetLibViewModel(AssetLib assetLib) {
        super("assetLibViewModel");
        this.state = assetLib;
    }

    /**
     * Adds an asset to the library and notifies all listeners.
     * This is called by presenters after use case execution.
     * @param asset the asset to add
     */
    public void addAsset(Asset asset) {
        state.add(asset);
        firePropertyChange(ASSET_ADDED);
    }

    /**
     * Notifies listeners that an asset was added externally
     * @param asset the asset that was added
     */
    public void removeAsset(Asset asset) {
        state.remove(asset.getId());
        firePropertyChange(ASSET_REMOVED);
    }

    /**
     * Gets the underlying AssetLib entity.
     * Use this when you need to access the entity directly (e.g., for use cases).
     * @return the AssetLib entity
     */
    public AssetLib getAssetLib() {
        return state;
    }

    public void fireAssetLibraryChanged() {
        firePropertyChange(ASSET_ADDED);
    }

    public void notifyAssetAdded(Asset asset) {
        support.firePropertyChange(ASSET_ADDED, null, asset);
    }
}

