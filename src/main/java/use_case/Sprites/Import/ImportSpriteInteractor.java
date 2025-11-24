package use_case.Sprites.Import;

import entity.Asset;
import entity.AssetLib;
import entity.Image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ImportSpriteInteractor implements SpriteInputBoundary {

    private static final long MAX_FILE_SIZE = 250L * 1024 * 1024; // 250 MB
    private static final List<String> VALID_EXTENSIONS = Arrays.asList(".png", ".jpg", ".jpeg");

    private final SpriteUserDataAccessInterface dataAccess;
    private final SpriteOutputBoundary outputBoundary;
    private final AssetLib assetLib;

    public ImportSpriteInteractor(SpriteUserDataAccessInterface dataAccess,
                                  SpriteOutputBoundary outputBoundary,
                                  AssetLib library) {
        this.dataAccess = dataAccess;
        this.outputBoundary = outputBoundary;
        this.assetLib = library;
    }

    @Override
    public void execute(ImportSpriteRequest request) {
        File spriteFile = request.spriteFile;

        // 1. validate the existence of the selected file
        if (spriteFile == null || !spriteFile.exists()) {
            outputBoundary.prepareFailView("File does not exist. Please try again.");
            return;
        }

        // 2. validate file type using extension
        String fileName = spriteFile.getName();
        String extension = getFileExtension(fileName);
        if (!isValidExtension(extension)) {
            outputBoundary.prepareFailView("Invalid file extension. Supported formats are: " + VALID_EXTENSIONS);
            return;
        }

        // 3. validate file size, currently set to 250 MB
        if (spriteFile.length() > MAX_FILE_SIZE) {
            outputBoundary.prepareFailView("File size exceeds 250 MB limit.");
            return;
        }

        // 4. check if the sprite already exists in the uploads directory
        if (dataAccess.existsByName(fileName)) {
            outputBoundary.prepareFailView("A sprite with this name already exists. Please try again.");
            return;
        }

        try {
            // 5. save to uploads directory
            Path savedPath = dataAccess.saveSprite(spriteFile, fileName);

            // 6. create new Image entity
            Image importedImage = new Image(savedPath);

            // 7. add sprite to asset library
            assetLib.add(importedImage);

            // 8. prepare and return success response
            ImportSpriteResponse response = new ImportSpriteResponse();
            response.success = true;
            response.message = "Sprite imported successfully: " + fileName;
            response.importedSprite = importedImage;

            outputBoundary.prepareSuccessView(response);

        } catch (IOException e) {
            outputBoundary.prepareFailView("Failed to import sprite: " + e.getMessage());
        }
    }

    @Override
    public void executeDelete(DeleteSpriteRequest request) throws IOException {
        if (!dataAccess.existsByName(request.spriteFile.getName())) {
            outputBoundary.prepareFailView("Sprite does not exist.");
            return;
        }
        // delete from data storage
        dataAccess.deleteSprite(request.spriteFile);

        // delete from asset lib
        assetLib.remove(getFileId(request.spriteFile.getName()));

        // prepare success response
        DeleteSpriteResponse response = new DeleteSpriteResponse();
        response.success = true;
        response.message = "Sprite deleted successfully: " + request.spriteFile.getName();

        outputBoundary.prepareDeleteSuccessView(response);
    }

    private UUID getFileId(String fileName) {
        for (Asset asset : assetLib.getAll()) {
            if (asset.getName().equals(fileName)) {
                return asset.getId();
            }
        }
        return null;
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex).toLowerCase();
    }

    private boolean isValidExtension(String extension) {
        return VALID_EXTENSIONS.contains(extension);
    }
}
