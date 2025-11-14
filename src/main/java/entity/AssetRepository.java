package entity;

import java.io.IOException;

public interface AssetRepository {
    Image saveImage(Image image) throws IOException;
}

