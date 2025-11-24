package interface_adapter.assets;

import entity.Asset;
import entity.AssetLib;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Interface Adapter layer wrapper for AssetLib.
 * Adds observer pattern capabilities without polluting the entity layer.
 */
public class AssetLibViewModel {
    public static final String ASSET_ADDED = "assetAdded";
    public static final String ASSET_REMOVED = "assetRemoved";

    private final AssetLib assetLib;
    private final PropertyChangeSupport support;

    public AssetLibViewModel(AssetLib assetLib) {
        this.assetLib = assetLib;
        this.support = new PropertyChangeSupport(this);
    }

    /**
     * Adds an asset to the library and notifies all listeners.
     * @param asset the asset to add
     */
    public void addAsset(Asset asset) {
        assetLib.add(asset);
        support.firePropertyChange(ASSET_ADDED, null, asset);
    }

    /**
     * Notifies listeners that an asset was added externally
     * @param asset the asset that was added
     */
    public void notifyAssetAdded(Asset asset) {
        support.firePropertyChange(ASSET_ADDED, null, asset);
    }

    /**
     * Notifies listeners that an asset was removed externally
     * @param asset the asset that was removed
     */
    public void notifyAssetRemoved(Asset asset) { support.firePropertyChange(ASSET_REMOVED, null, asset); }

    /**
     * Gets the underlying AssetLib entity.
     * Use this when you need to access the entity directly (e.g., for use cases).
     * @return the AssetLib entity
     */
    public AssetLib getAssetLib() {
        return assetLib;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }
}

