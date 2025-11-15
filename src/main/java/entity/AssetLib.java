package entity;

import java.util.*;

/**
 * A library to manage assets.
 */

public class AssetLib {
    private Map<UUID, Asset> assets = new HashMap<>();

    public void add(Asset asset) {
        assets.put(asset.getId(), asset);
    }

    public Collection<Asset> getAll() {
        return assets.values();
    }
}
