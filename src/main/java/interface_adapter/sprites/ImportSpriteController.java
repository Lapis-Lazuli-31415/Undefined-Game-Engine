package interface_adapter.sprites;

import use_case.sprites.ImportSpriteRequest;
import use_case.sprites.SpriteInputBoundary;

import java.io.File;

/**
 * Controller for importing sprites from a local file.
 * This class is responsible for accepting user input from the view layer
 * and passing it to the use case interactor.
 */
public class ImportSpriteController {

    private final SpriteInputBoundary interactor;

    public ImportSpriteController(SpriteInputBoundary interactor) {
        this.interactor = interactor;
    }

    /**
     * Imports a sprite from a local file.
     * @param file the file to import
     */
    public void importSprite(File file) {
        ImportSpriteRequest request = new ImportSpriteRequest();
        request.spriteFile = file;
        interactor.execute(request);
    }
}

