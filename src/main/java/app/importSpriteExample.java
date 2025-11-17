package app;

import data_access.FileSystemSpriteDataAccessObject;
import entity.AssetLib;
import interface_adapter.*;
        import use_case.Sprites.*;
        import java.io.IOException;

public class importSpriteExample {

    public static ImportSpriteController create(AssetLib assetLibrary) throws IOException {
        // init data access
        SpriteUserDataAccessInterface spriteDAO = new FileSystemSpriteDataAccessObject();

        // init view model
        ImportSpriteViewModel viewModel = new ImportSpriteViewModel();

        // init presenter
        SpriteOutputBoundary presenter = new ImportSpritePresenter(viewModel);

        // init interactor
        SpriteInputBoundary interactor = new ImportSpriteInteractor(
                spriteDAO,
                presenter,
                assetLibrary
        );

        // init controller
        return new ImportSpriteController(interactor);
    }

    public static void main(String[] args) throws IOException {
        AssetLib assetLibrary = new AssetLib();
        ImportSpriteController controller = ImportSpriteUseCaseFactory.create(assetLibrary);
        create(assetLibrary);
    }
}