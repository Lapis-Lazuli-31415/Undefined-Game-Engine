package entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.*;

/**
 * A library to manage assets.
 * Pure entity class with no framework dependencies.
 */

public class AssetLib {
    private final Map<UUID, Asset> assets = new HashMap<>();

    public AssetLib() {}

    // 2. Jackson Constructor: Loads the JSON list back into the Map
    @JsonCreator
    public AssetLib(Collection<Asset> assetList) {
        if (assetList != null) {
            for (Asset asset : assetList) {
                add(asset);
            }
        }
    }

    public void add(Asset asset) {
        assets.put(asset.getId(), asset);
    }

    // Jackson Saver to saves the Map as a simple List
    @JsonValue
    public Collection<Asset> getAll() {
        return assets.values();
    }

    public Asset getById(UUID id) {
        return assets.get(id);
    }

    public boolean contains(UUID id) {
        return assets.containsKey(id);
    }

    public void remove(UUID id) {
        assets.remove(id);
    }
}
