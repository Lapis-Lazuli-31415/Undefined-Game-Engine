package use_case.Sprites.Import;

import entity.AssetLib;
import entity.Image;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

/**
 * Interactor for importing sprites from Unsplash API.
 * This use case handles fetching images from Unsplash and saving them to the asset library.
 */
public class ImportSpriteFromUnsplashInteractor implements SpriteInputBoundary {

    private final UnsplashImageDataAccessInterface unsplashDataAccess;
    private final SpriteUserDataAccessInterface spriteDataAccess;
    private final SpriteOutputBoundary outputBoundary;
    private final AssetLib assetLib;

    public ImportSpriteFromUnsplashInteractor(
            UnsplashImageDataAccessInterface unsplashDataAccess,
            SpriteUserDataAccessInterface spriteDataAccess,
            SpriteOutputBoundary outputBoundary,
            AssetLib assetLib) {
        this.unsplashDataAccess = unsplashDataAccess;
        this.spriteDataAccess = spriteDataAccess;
        this.outputBoundary = outputBoundary;
        this.assetLib = assetLib;
    }

    @Override
    public void execute(ImportSpriteRequest request) {
        if (!(request instanceof ImportSpriteFromUnsplashRequest)) {
            outputBoundary.prepareFailView("Invalid request type for Unsplash import");
            return;
        }

        ImportSpriteFromUnsplashRequest unsplashRequest = (ImportSpriteFromUnsplashRequest) request;

        try {
            UnsplashImageDataAccessInterface.UnsplashImageInfo imageInfo;

            // 1. fetch image metadata from Unsplash
            if (unsplashRequest.imageId != null && !unsplashRequest.imageId.isEmpty()) {
                // Get specific image by ID
                imageInfo = unsplashDataAccess.getImageById(unsplashRequest.imageId);
            } else if (unsplashRequest.searchQuery != null && !unsplashRequest.searchQuery.isEmpty()) {
                // Search for images and get the first result
                List<UnsplashImageDataAccessInterface.UnsplashImageInfo> searchResults =
                    unsplashDataAccess.searchImages(unsplashRequest.searchQuery, 1);

                if (searchResults.isEmpty()) {
                    outputBoundary.prepareFailView("No images found for query: " + unsplashRequest.searchQuery);
                    return;
                }

                imageInfo = searchResults.get(0);
            } else {
                outputBoundary.prepareFailView("Either searchQuery or imageId must be provided");
                return;
            }

            // 2. Determine target file name
            String fileName = determineFileName(unsplashRequest.targetFileName, imageInfo);

            // 3. Check if sprite already exists
            if (spriteDataAccess.existsByName(fileName)) {
                outputBoundary.prepareFailView("A sprite with this name already exists: " + fileName);
                return;
            }

            // 4. Download the image as InputStream
            InputStream imageStream = unsplashDataAccess.downloadImage(imageInfo.downloadUrl);

            // 5. Save the image to the uploads directory
            Path savedPath = spriteDataAccess.saveSpriteFromStream(imageStream, fileName);

            // 6. Create new Image entity
            Image importedImage = new Image(savedPath);

            // 7. Add sprite to asset library
            assetLib.add(importedImage);

            // 8. Prepare and return success response
            ImportSpriteResponse response = new ImportSpriteResponse();
            response.success = true;
            response.message = String.format("Sprite imported successfully from Unsplash: %s (by %s)",
                                            fileName, imageInfo.downloadUrl);
            response.importedSprite = importedImage;

            outputBoundary.prepareSuccessView(response);

        } catch (IOException e) {
            outputBoundary.prepareFailView("Failed to import sprite from Unsplash: " + e.getMessage());
        }
    }

    @Override
    public void execute(DeleteSpriteRequest request) {
        // TODO: implement
    }

    /**
     * Determines the file name for the downloaded image.
     */
    private String determineFileName(String targetFileName,
                                    UnsplashImageDataAccessInterface.UnsplashImageInfo imageInfo) {
        if (targetFileName != null && !targetFileName.isEmpty()) {
            // Ensure it has an extension
            if (!targetFileName.contains(".")) {
                return targetFileName + ".jpg";
            }
            return targetFileName;
        }

        // Generate a name based on image ID and description
        String baseName = imageInfo.id;
        if (imageInfo.description != null && !imageInfo.description.isEmpty()) {
            // Sanitize description for filename
            String sanitized = imageInfo.description
                .toLowerCase()
                .replaceAll("[^a-z0-9]+", "_")
                .replaceAll("^_+|_+$", "");

            if (sanitized.length() > 30) {
                sanitized = sanitized.substring(0, 30);
            }

            baseName = sanitized + "_" + imageInfo.id;
        }

        return baseName + ".jpg";
    }
}

