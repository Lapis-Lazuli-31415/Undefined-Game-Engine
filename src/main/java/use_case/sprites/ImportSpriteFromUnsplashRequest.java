package use_case.sprites;

/**
 * Request model for importing a sprite from Unsplash API. (Input data)
 */
public class ImportSpriteFromUnsplashRequest extends ImportSpriteRequest {
        public String targetFileName;
        // desired filename for the downloaded image

        public String imageId;
        // specific image ID from Unsplash

        public String searchQuery;
        // The search query to find images on Unsplash
    }
