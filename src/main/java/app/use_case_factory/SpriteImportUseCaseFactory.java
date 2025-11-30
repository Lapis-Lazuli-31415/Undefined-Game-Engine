package app.use_case_factory;

import javax.swing.JOptionPane;

import data_access.FileSystemSpriteDataAccessObject;
import data_access.UnsplashApiDataAccessObject;
import entity.AssetLib;
import interface_adapter.sprites.ImportSpriteController;
import interface_adapter.sprites.ImportSpriteFromUnsplashController;
import interface_adapter.sprites.ImportSpriteFromUnsplashPresenter;
import interface_adapter.sprites.ImportSpriteFromUnsplashViewModel;
import interface_adapter.sprites.ImportSpritePresenter;
import interface_adapter.sprites.ImportSpriteViewModel;
import interface_adapter.assets.AssetLibViewModel;
import use_case.sprites.ImportSpriteFromUnsplashInteractor;
import use_case.sprites.ImportSpriteInteractor;

import java.io.IOException;

public class SpriteImportUseCaseFactory {

    public static ImportSpriteController createLocalImportUseCase(
            AssetLibViewModel assetLibViewModel,
            ImportSpriteViewModel importSpriteViewModel) {
        try {
            FileSystemSpriteDataAccessObject spriteDAO = new FileSystemSpriteDataAccessObject();
            ImportSpritePresenter presenter = new ImportSpritePresenter(
                    importSpriteViewModel,
                    assetLibViewModel
            );
            ImportSpriteInteractor interactor = new ImportSpriteInteractor(
                    spriteDAO,
                    presenter,
                    assetLibViewModel.getAssetLib()
            );
            return new ImportSpriteController(interactor);
        }
        catch (java.io.IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to initialize sprite import: " + e.getMessage(),
                    "Initialization Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    public static ImportSpriteFromUnsplashController createUnsplashImportUseCase(
            AssetLibViewModel assetLibViewModel,
            ImportSpriteFromUnsplashViewModel unsplashViewModel,
            String apiKey) {

        try {
            if (apiKey == null || apiKey.trim().isEmpty()) {
                System.err.println("Unsplash API key is missing.");
                return null;
            }

            final ImportSpriteFromUnsplashInteractor interactor = getImportSpriteFromUnsplashInteractor(
                    assetLibViewModel,
                    unsplashViewModel,
                    apiKey);

            return new ImportSpriteFromUnsplashController(interactor);

        }
        catch (Exception e) {
            System.err.println("Failed to initialize Unsplash import: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(null,
                    "Failed to initialize Unsplash import: " + e.getMessage(),
                    "Initialization Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private static ImportSpriteFromUnsplashInteractor getImportSpriteFromUnsplashInteractor(
            AssetLibViewModel assetLibViewModel,
            ImportSpriteFromUnsplashViewModel unsplashViewModel,
            String apiKey) throws IOException {

        UnsplashApiDataAccessObject unsplashDAO = new UnsplashApiDataAccessObject(apiKey);
        FileSystemSpriteDataAccessObject spriteDAO = new FileSystemSpriteDataAccessObject();

        final ImportSpriteFromUnsplashPresenter presenter = new ImportSpriteFromUnsplashPresenter(
                unsplashViewModel,
                assetLibViewModel
        );
        final ImportSpriteFromUnsplashInteractor interactor = new ImportSpriteFromUnsplashInteractor(
                unsplashDAO,
                spriteDAO,
                presenter,
                assetLibViewModel.getAssetLib()
        );
        return interactor;
    }

    public static void loadExistingAssets(AssetLib assetLib) {
        try {
            FileSystemSpriteDataAccessObject spriteDAO = new FileSystemSpriteDataAccessObject();

            final java.util.List<java.io.File> existingImages = spriteDAO.getAllExistingImages();

            for (java.io.File imageFile : existingImages) {
                try {
                    final entity.Image image = new entity.Image(imageFile.toPath());

                    assetLib.add(image);
                }
                catch (Exception e) {
                    // in case of corrupted image, only log the error then proceed with the rest
                    System.err.println("Failed to load image: " + imageFile.getName() + " - " + e.getMessage());
                }
            }
        }
        catch (java.io.IOException e) {
            // in case all of them fail
            JOptionPane.showMessageDialog(null,
                    "Failed to load existing sprites: " + e.getMessage(),
                    "Loading Error",
                    JOptionPane.WARNING_MESSAGE);
        }
    }
}
