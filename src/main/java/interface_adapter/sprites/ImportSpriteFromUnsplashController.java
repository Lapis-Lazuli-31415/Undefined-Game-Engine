package interface_adapter.sprites;

import use_case.sprites.SpriteInputBoundary;
import use_case.sprites.ImportSpriteFromUnsplashRequest;

/**
 * Controller for importing sprites from Unsplash.
 * Converts user input into a format suitable for the use case.
 */
public class ImportSpriteFromUnsplashController {

    private final SpriteInputBoundary interactor;

    public ImportSpriteFromUnsplashController(SpriteInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Executes the import sprite from Unsplash use case with a search query.
     *
     * @param searchQuery the search term to find images
     * @param targetFileName optional custom filename for the downloaded image
     */
    public void execute(String searchQuery, String targetFileName) {
        ImportSpriteFromUnsplashRequest request = new ImportSpriteFromUnsplashRequest();
        request.searchQuery = searchQuery;
        request.targetFileName = targetFileName;

        interactor.execute(request);
    }

    /**
     * Executes the import sprite from Unsplash use case with a specific image ID.
     *
     * @param imageId the Unsplash image ID
     * @param targetFileName optional custom filename for the downloaded image
     */
    public void executeWithImageId(String imageId, String targetFileName) {
        ImportSpriteFromUnsplashRequest request = new ImportSpriteFromUnsplashRequest();
        request.imageId = imageId;
        request.targetFileName = targetFileName;

        interactor.execute(request);
    }
}

