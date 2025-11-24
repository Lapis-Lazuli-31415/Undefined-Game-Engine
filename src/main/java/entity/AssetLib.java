package entity;

import java.util.*;

/**
 * A library to manage assets.
 * Pure entity class with no framework dependencies.
 */

public class AssetLib {
    private final Map<UUID, Asset> assets = new HashMap<>();

    public void add(Asset asset) {
        assets.put(asset.getId(), asset);
    }

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
