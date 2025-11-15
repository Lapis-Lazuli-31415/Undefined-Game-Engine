package data_access;

import use_case.Sprites.SpriteUserDataAccessInterface;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Data Access Object for managing sprite files in the file system.
 * This implementation saves imported sprites to an "uploads" directory in the project.
 */
public class FileSystemSpriteDataAccessObject implements SpriteUserDataAccessInterface {

    private final Path uploadsDirectory;

    /**
     * Constructor that creates a FileSystemSpriteDataAccessObject with the default uploads directory.
     * The uploads directory will be created in the project root if it doesn't exist.
     *
     * @throws IOException if the uploads directory cannot be created
     */
    public FileSystemSpriteDataAccessObject() throws IOException {
        this(Paths.get("uploads"));
    }

    /**
     * Constructor that creates a FileSystemSpriteDataAccessObject with a custom uploads directory.
     *
     * @param uploadsDirectory the path to the uploads directory
     * @throws IOException if the uploads directory cannot be created
     */
    public FileSystemSpriteDataAccessObject(Path uploadsDirectory) throws IOException {
        this.uploadsDirectory = uploadsDirectory;
        ensureUploadsDirectoryExists();
    }

    /**
     * Ensures that the uploads directory exists, creating it if necessary.
     *
     * @throws IOException if the directory cannot be created
     */
    private void ensureUploadsDirectoryExists() throws IOException {
        if (!Files.exists(uploadsDirectory)) {
            Files.createDirectories(uploadsDirectory);
        }
    }

    @Override
    public boolean existsByName(String spriteName) {
        Path targetPath = uploadsDirectory.resolve(spriteName);
        return Files.exists(targetPath);
    }

    @Override
    public Path saveSprite(File sourceFile, String targetFileName) throws IOException {
        // Ensure uploads directory exists
        ensureUploadsDirectoryExists();

        // Resolve the target path
        Path targetPath = uploadsDirectory.resolve(targetFileName);

        // Copy the file to the uploads directory
        // REPLACE_EXISTING will overwrite if file exists (though we check this in interactor)
        Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return targetPath;
    }

    @Override
    public Path getUploadsDirectory() {
        return uploadsDirectory;
    }
}

