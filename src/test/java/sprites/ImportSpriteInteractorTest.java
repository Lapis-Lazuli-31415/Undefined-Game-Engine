package sprites;

import data_access.FileSystemSpriteDataAccessObject;
import entity.AssetLib;
import org.junit.jupiter.api.Test;
import use_case.Sprites.Import.ImportSpriteInteractor;
import use_case.Sprites.Import.ImportSpriteRequest;
import use_case.Sprites.Import.SpriteUserDataAccessInterface;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ImportSpriteInteractorTest {

    @Test
    void successTest() throws IOException {
        // init relevant objs
        ImportSpriteRequest importSpriteRequest = new ImportSpriteRequest();
        AssetLib assetLib = new AssetLib();
        SpriteUserDataAccessInterface FileName = new FileSystemSpriteDataAccessObject();
        ImportSpriteInteractor importSpriteInteractor = new ImportSpriteInteractor(FileName, null, assetLib);

        importSpriteInteractor.execute(importSpriteRequest);
        // making sure that the requested object has been successfully added to the asset library.
        assertEquals(1, assetLib.getAll().size());
    }

    @Test
    void invalidFileTypeTest() throws IOException {
        // init relevant objects
        SpriteUserDataAccessInterface FileName = new FileSystemSpriteDataAccessObject();
        ImportSpriteRequest importSpriteRequest = new ImportSpriteRequest();
        AssetLib assetLib = new AssetLib();
        ImportSpriteInteractor importSpriteInteractor = new ImportSpriteInteractor(FileName, null, assetLib);

        // ensuring that the asset library remains empty cuz it shouldnt accept
        importSpriteInteractor.execute(importSpriteRequest);
        assertNull(assetLib.getAll());

    }

    @Test
    void invalidFileSizeTest() throws IOException {
        // initializing relevant objects
        SpriteUserDataAccessInterface FileName = new FileSystemSpriteDataAccessObject();
        ImportSpriteRequest importSpriteRequest = new ImportSpriteRequest();
        AssetLib assetLib = new AssetLib();
        ImportSpriteInteractor importSpriteInteractor = new ImportSpriteInteractor(FileName, null, assetLib);


        importSpriteInteractor.execute(importSpriteRequest);
        assertNull(assetLib.getAll());
    }

    @Test
    void duplicateNameTest() throws IOException {
        SpriteUserDataAccessInterface FileName = new FileSystemSpriteDataAccessObject();
        ImportSpriteRequest importSpriteRequest = new ImportSpriteRequest();
        AssetLib assetLib = new AssetLib();
        ImportSpriteInteractor importSpriteInteractor = new ImportSpriteInteractor(FileName, null, assetLib);


        importSpriteInteractor.execute(importSpriteRequest);
        // check that there exists only the original file, and not two with duplicate names
        assertEquals(1, assetLib.getAll().size());


    }
}
