package sprites;

import data_access.FileSystemSpriteDataAccessObject;
import entity.AssetLib;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import use_case.Sprites.Import.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ImportSpriteInteractorTest {

    @TempDir
    Path tempDir;

    @Test
    void successTest() throws IOException {
        File testImage = createTestImageFile("test_sprite.png", 100, 100);
        Path uploadsDir = tempDir.resolve("uploads");
        SpriteUserDataAccessInterface dataAccess = new FileSystemSpriteDataAccessObject(uploadsDir);
        AssetLib assetLib = new AssetLib();

        SpriteOutputBoundary successPresenter = new SpriteOutputBoundary() {
            @Override
            public void prepareSuccessView(ImportSpriteResponse response) {
                assertEquals(1, assetLib.getAll().size());
                assertTrue(response.success);
                assertNotNull(response.importedSprite);
                assertTrue(response.message.contains("successfully"));
            }

            @Override
            public void prepareFailView(String errorMessage) {
                fail("Use case failure is unexpected.");
            }
        };

        ImportSpriteInteractor interactor = new ImportSpriteInteractor(dataAccess, successPresenter, assetLib);
        ImportSpriteRequest request = new ImportSpriteRequest();
        request.spriteFile = testImage;
        interactor.execute(request);
    }

    @Test
    void invalidFileTypeTest() throws IOException {
        File invalidFile = tempDir.resolve("invalid_file.txt").toFile();
        Files.writeString(invalidFile.toPath(), "This is not an image");
        Path uploadsDir = tempDir.resolve("uploads");
        SpriteUserDataAccessInterface dataAccess = new FileSystemSpriteDataAccessObject(uploadsDir);
        ImportSpriteInteractor interactor = getImportSpriteInteractor("Invalid file extension",
                dataAccess);
        ImportSpriteRequest request = new ImportSpriteRequest();
        request.spriteFile = invalidFile;
        interactor.execute(request);
    }

    private static ImportSpriteInteractor getImportSpriteInteractor(String Invalid_file_extension,
                                                                    SpriteUserDataAccessInterface dataAccess) {
        AssetLib assetLib = new AssetLib();

        SpriteOutputBoundary failurePresenter = new SpriteOutputBoundary() {
            @Override
            public void prepareSuccessView(ImportSpriteResponse response) {
                fail("Use case success is unexpected.");
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertTrue(errorMessage.contains(Invalid_file_extension));
                assertEquals(0, assetLib.getAll().size());
            }
        };

        return new ImportSpriteInteractor(dataAccess, failurePresenter, assetLib);
    }

    @Test
    void invalidFileSizeTest() throws IOException {
        File largeFile = createLargeFile();
        Path uploadsDir = tempDir.resolve("uploads");
        SpriteUserDataAccessInterface dataAccess = new FileSystemSpriteDataAccessObject(uploadsDir);
        ImportSpriteInteractor interactor = getImportSpriteInteractor("exceeds 250 MB limit",
                dataAccess);
        ImportSpriteRequest request = new ImportSpriteRequest();
        request.spriteFile = largeFile;
        interactor.execute(request);
    }

    @Test
    void duplicateNameTest() throws IOException {
        File image1 = createTestImageFile("duplicate_sprite.png", 100, 100);
        File image2 = createTestImageFile("duplicate_sprite_2.png", 150, 150);
        ImportSpriteInteractor interactor = getImportSpriteInteractor();

        ImportSpriteRequest request1 = new ImportSpriteRequest();
        request1.spriteFile = image1;
        interactor.execute(request1);
        Files.deleteIfExists(image1.toPath());

        File renamedFile = tempDir.resolve("duplicate_sprite.png").toFile();
        Files.copy(image2.toPath(), renamedFile.toPath());
        ImportSpriteRequest request2 = new ImportSpriteRequest();
        request2.spriteFile = renamedFile;
        interactor.execute(request2);
    }

    private ImportSpriteInteractor getImportSpriteInteractor() throws IOException {
        Path uploadsDir = tempDir.resolve("uploads");
        SpriteUserDataAccessInterface dataAccess = new FileSystemSpriteDataAccessObject(uploadsDir);
        AssetLib assetLib = new AssetLib();

        SpriteOutputBoundary presenter = new SpriteOutputBoundary() {
            @Override
            public void prepareSuccessView(ImportSpriteResponse response) {
            }

            @Override
            public void prepareFailView(String errorMessage) {
                assertTrue(errorMessage.contains("already exists"));
                assertEquals(1, assetLib.getAll().size());
            }
        };

        return new ImportSpriteInteractor(dataAccess, presenter, assetLib);
    }

    private File createTestImageFile(String filename, int width, int height) throws IOException {
        BufferedImage testImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        File imageFile = tempDir.resolve(filename).toFile();
        ImageIO.write(testImage, "png", imageFile);
        return imageFile;
    }

    private File createLargeFile() throws IOException {
        File largeFile = tempDir.resolve("large_sprite.png").toFile();
        byte[] data = new byte[1024 * 1024];
        try (var fos = new java.io.FileOutputStream(largeFile)) {   // creating a file w size > 250 mb
            for (int i = 0; i < 251; i++) {
                fos.write(data);
            }
        }
        return largeFile;
    }
}
