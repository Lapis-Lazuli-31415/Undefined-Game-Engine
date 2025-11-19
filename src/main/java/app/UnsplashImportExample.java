package app;

import data_access.FileSystemSpriteDataAccessObject;
import data_access.UnsplashApiDataAccessObject;
import entity.AssetLib;
import interface_adapter.ImportSpriteFromUnsplashController;
import interface_adapter.ImportSpriteFromUnsplashPresenter;
import interface_adapter.ImportSpriteFromUnsplashViewModel;
import use_case.Sprites.ImportSpriteFromUnsplashInteractor;

import java.io.IOException;

/**
 * Example application demonstrating how to wire up the Unsplash import feature.
 * This class shows the dependency injection setup following Clean Architecture.
 */
public class UnsplashImportExample {

    public static void main(String[] args) {
        // NOTE: Replace with your actual Unsplash API access key
        // Get your free API key at: https://unsplash.com/developers
        String unsplashAccessKey = System.getenv("UNSPLASH_ACCESS_KEY");

        if (unsplashAccessKey == null || unsplashAccessKey.isEmpty()) {
            System.err.println("Please set the UNSPLASH_ACCESS_KEY environment variable");
            System.err.println("Get your API key at: https://unsplash.com/developers");
            return;
        }

        try {
            // Create the asset library (entity layer)
            AssetLib assetLib = new AssetLib();

            // Create data access objects (interface adapters / frameworks layer)
            FileSystemSpriteDataAccessObject fileSystemDAO = new FileSystemSpriteDataAccessObject();
            UnsplashApiDataAccessObject unsplashDAO = new UnsplashApiDataAccessObject(unsplashAccessKey);

            // Create view model (interface adapter layer)
            ImportSpriteFromUnsplashViewModel viewModel = new ImportSpriteFromUnsplashViewModel();

            // Create presenter (interface adapter layer)
            ImportSpriteFromUnsplashPresenter presenter = new ImportSpriteFromUnsplashPresenter(viewModel);

            // Create interactor (use case layer)
            ImportSpriteFromUnsplashInteractor interactor = new ImportSpriteFromUnsplashInteractor(
                    unsplashDAO,
                    fileSystemDAO,
                    presenter,
                    assetLib
            );

            // Create controller (interface adapter layer)
            ImportSpriteFromUnsplashController controller = new ImportSpriteFromUnsplashController(interactor);

            // Example usage: Search for a nature image
            System.out.println("Searching for 'nature' images on Unsplash...");
            controller.execute("nature", "nature_landscape.jpg");

            // Wait a moment for the async operation
            Thread.sleep(2000);

            // Check the result
            System.out.println("Status: " + viewModel.getStatusMessage());
            System.out.println("Total assets in library: " + assetLib.getAll().size());

            // Example usage: Import a specific image by ID
            // controller.executeWithImageId("specific-image-id", "custom_name.jpg");

        } catch (IOException e) {
            System.err.println("Failed to initialize: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted: " + e.getMessage());
        }
    }
}

