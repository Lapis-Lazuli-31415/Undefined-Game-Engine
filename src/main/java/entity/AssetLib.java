package entity;

import java.util.*;

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
