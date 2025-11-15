package entity;

import java.io.IOException;

/**
 * Repository interface for managing asset persistence.
 */

public interface AssetRepository {
    Image saveImage(Image image) throws IOException;
}

