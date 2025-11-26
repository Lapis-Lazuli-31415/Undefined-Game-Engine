package sprites;

import entity.AssetLib;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import use_case.Sprites.Import.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class ImportSpriteFromUnsplashInteractorTest {
    @TempDir
    Path tempDir;

    private AssetLib assetLib;
    private TestPresenter testPresenter;
    private MockUnsplashDataAccess mockUnsplashDataAccess;
    private MockSpriteDataAccess mockSpriteDataAccess;

    @BeforeEach
    void setUp() throws IOException {
        assetLib = new AssetLib();
        testPresenter = new TestPresenter();
        mockUnsplashDataAccess = new MockUnsplashDataAccess();
        mockSpriteDataAccess = new MockSpriteDataAccess(tempDir);
    }

    @Test
    void successTest() {
        ImportSpriteFromUnsplashInteractor interactor = new ImportSpriteFromUnsplashInteractor(
            mockUnsplashDataAccess, mockSpriteDataAccess, testPresenter, assetLib
        );
        ImportSpriteFromUnsplashRequest request = new ImportSpriteFromUnsplashRequest();
        request.searchQuery = "random";
        request.targetFileName = "test_image.jpg";

        interactor.execute(request);

        assertTrue(testPresenter.isSuccessCalled());
        assertFalse(testPresenter.isFailureCalled());
        assertEquals(1, assetLib.getAll().size());
        assertNotNull(testPresenter.getResponse());
        assertTrue(testPresenter.getResponse().success);
        assertNotNull(testPresenter.getResponse().importedSprite);
        assertTrue(testPresenter.getResponse().message.contains("successfully"));
    }

    @Test
    void noImagesFoundTest() {
        mockUnsplashDataAccess.setReturnEmptySearchResults(true);
        ImportSpriteFromUnsplashInteractor interactor = new ImportSpriteFromUnsplashInteractor(
            mockUnsplashDataAccess, mockSpriteDataAccess, testPresenter, assetLib
        );
        ImportSpriteFromUnsplashRequest request = new ImportSpriteFromUnsplashRequest();
        request.searchQuery = "woawowoownoooodontshowanyimagespzlz";

        interactor.execute(request);

        assertTrue(testPresenter.isFailureCalled());
        assertFalse(testPresenter.isSuccessCalled());
        assertEquals(0, assetLib.getAll().size());
        assertNotNull(testPresenter.getErrorMessage());
        assertTrue(testPresenter.getErrorMessage().contains("No images found"));
    }

    @Test
    void invalidImageIdTest() {
        mockUnsplashDataAccess.setThrowExceptionOnGetById(true);
        ImportSpriteFromUnsplashInteractor interactor = new ImportSpriteFromUnsplashInteractor(
            mockUnsplashDataAccess, mockSpriteDataAccess, testPresenter, assetLib
        );
        ImportSpriteFromUnsplashRequest request = new ImportSpriteFromUnsplashRequest();
        request.imageId = "invalid";

        interactor.execute(request);

        assertTrue(testPresenter.isFailureCalled());
        assertFalse(testPresenter.isSuccessCalled());
        assertEquals(0, assetLib.getAll().size());
        assertNotNull(testPresenter.getErrorMessage());
        assertTrue(testPresenter.getErrorMessage().contains("Failed to import sprite from Unsplash"));
    }

    @Test
    void duplicateNameTest() {
        ImportSpriteFromUnsplashInteractor interactor = new ImportSpriteFromUnsplashInteractor(
            mockUnsplashDataAccess, mockSpriteDataAccess, testPresenter, assetLib
        );

        ImportSpriteFromUnsplashRequest request1 = new ImportSpriteFromUnsplashRequest();
        request1.searchQuery = "random";
        request1.targetFileName = "duplicate_name.jpg";

        interactor.execute(request1);
        testPresenter.reset();

        ImportSpriteFromUnsplashRequest request2 = new ImportSpriteFromUnsplashRequest();
        request2.searchQuery = "bear";
        request2.targetFileName = "duplicate_name.jpg";
        interactor.execute(request2);

        assertTrue(testPresenter.isFailureCalled());
        assertEquals(1, assetLib.getAll().size());
        assertNotNull(testPresenter.getErrorMessage());
        assertTrue(testPresenter.getErrorMessage().contains("already exists"));
    }

    private static class MockUnsplashDataAccess implements UnsplashImageDataAccessInterface {
        boolean returnEmptySearchResults = false;
        boolean throwExceptionOnGetById = false;
        public void setReturnEmptySearchResults(boolean value) {
            this.returnEmptySearchResults = value;
        }

        public void setThrowExceptionOnGetById(boolean value) {
            this.throwExceptionOnGetById = value;
        }

        @Override
        public List<UnsplashImageInfo> searchImages(String query, int perPage) {
            if (returnEmptySearchResults) {
                return new ArrayList<>();
            }
            return List.of(new UnsplashImageInfo("test-id", "Test image", "url", "url", "photographer", 100, 100));
        }

        @Override
        public InputStream downloadImage(String downloadUrl) throws IOException {
            BufferedImage img = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ImageIO.write(img, "jpg", out);
            return new ByteArrayInputStream(out.toByteArray());
        }

        @Override
        public UnsplashImageInfo getImageById(String imageId) throws IOException {
            if (throwExceptionOnGetById) {
                throw new IOException("Image not found");
            }
            return new UnsplashImageInfo(imageId, "Test image", "url", "url", "photographer", 100, 100);
        }
    }

    private static class MockSpriteDataAccess implements SpriteUserDataAccessInterface {
        private final Path uploadsDir;
        private final Set<String> existingFiles = new HashSet<>();
        MockSpriteDataAccess(Path tempDir) throws IOException {
            uploadsDir = tempDir.resolve("uploads");
            Files.createDirectories(uploadsDir);
        }

        @Override
        public boolean existsByName(String name) {
            return existingFiles.contains(name);
        }

        @Override
        public Path saveSprite(File sourceFile, String targetFileName) throws IOException {
            Path target = uploadsDir.resolve(targetFileName);
            Files.copy(sourceFile.toPath(), target);
            existingFiles.add(targetFileName);
            return target;
        }

        @Override
        public Path saveSpriteFromStream(InputStream inputStream, String targetFileName) throws IOException {
            Path target = uploadsDir.resolve(targetFileName);
            Files.copy(inputStream, target);
            existingFiles.add(targetFileName);
            return target;
        }

        @Override
        public Path getUploadsDirectory() {
            return uploadsDir;
        }
    }

    private static class TestPresenter implements SpriteOutputBoundary {
        private boolean successCalled = false;
        private boolean failureCalled = false;
        private ImportSpriteResponse response;
        private String errorMessage;

        @Override
        public void prepareSuccessView(ImportSpriteResponse response) {
            this.successCalled = true;
            this.response = response;
        }

        @Override
        public void prepareFailView(String errorMessage) {
            this.failureCalled = true;
            this.errorMessage = errorMessage;
        }

        public boolean isSuccessCalled() {
            return successCalled;
        }

        public boolean isFailureCalled() {
            return failureCalled;
        }

        public ImportSpriteResponse getResponse() {
            return response;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void reset() {
            this.successCalled = false;
            this.failureCalled = false;
            this.response = null;
            this.errorMessage = null;
        }
    }
}
