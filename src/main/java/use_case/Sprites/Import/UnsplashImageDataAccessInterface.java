package use_case.Sprites.Import;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Data Access Interface for fetching images from Unsplash API.
 * This interface is part of the use case layer and abstracts external API calls.
 */
public interface UnsplashImageDataAccessInterface {

    /**
     * Searches for images on Unsplash based on a query.
     *
     * @param query the search term
     * @param perPage number of results per page (max 30)
     * @return list of UnsplashImageInfo containing image metadata
     * @throws IOException if the API call fails
     */
    List<UnsplashImageInfo> searchImages(String query, int perPage) throws IOException;

    /**
     * Downloads an image from Unsplash by its download URL.
     *
     * @param downloadUrl the URL to download the image from
     * @return InputStream of the image data
     * @throws IOException if the download fails
     */
    InputStream downloadImage(String downloadUrl) throws IOException;

    /**
     * Gets a specific image by its ID from Unsplash.
     *
     * @param imageId the Unsplash image ID
     * @return UnsplashImageInfo containing image metadata
     * @throws IOException if the API call fails
     */
    UnsplashImageInfo getImageById(String imageId) throws IOException;

    /**
     * Data class to hold Unsplash image information.
     */
    class UnsplashImageInfo {
        public String id;
        public String description;
        public String downloadUrl;
        public String regularUrl;
        public String photographer;
        public int width;
        public int height;

        public UnsplashImageInfo(String id, String description, String downloadUrl,
                                String regularUrl, String photographer, int width, int height) {
            this.id = id;
            this.description = description;
            this.downloadUrl = downloadUrl;
            this.regularUrl = regularUrl;
            this.photographer = photographer;
            this.width = width;
            this.height = height;
        }
    }
}

