package entity;

import java.util.*;

/**
 * A library to manage assets.
 */

public class AssetLib {
    private Map<UUID, Asset> assets = new HashMap<>();

    public void add(Asset asset) {
        // TODO: implement
        assets.put(asset.getId(), asset);
    }

    public Optional<Asset> get(UUID id) {
    // TODO: implement
    }

    public Collection<Asset> getAll() {
        // TODO: implement
    }
}
