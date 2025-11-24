package data_access;

import use_case.Sprites.Import.SpriteUserDataAccessInterface;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Implementation of SpriteUserDataAccessInterface that saves sprites to the local file system.
 * This class saves imported sprites to an "uploads" directory in the root directory of the project.
 */
public class FileSystemSpriteDataAccessObject implements SpriteUserDataAccessInterface {

    private final Path uploadsDirectory;

    /**
     * Constructor that creates a FileSystemSpriteDataAccessObject with the default uploads directory.
     * The uploads directory will be created in the project root if it doesn't already exist.
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

    public Path saveSprite(File sourceFile, String targetFileName) throws IOException {
        // Ensure uploads directory exists
        ensureUploadsDirectoryExists();

        // Resolve the target path
        Path targetPath = uploadsDirectory.resolve(targetFileName);

        // Copy the file to the uploads directory
        // REPLACE_EXISTING will overwrite if file exists (checked in interactor)
        Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        return targetPath;
    }

    @Override
    public Path saveSpriteFromStream(java.io.InputStream inputStream, String targetFileName) throws IOException {
        // Ensure uploads directory exists
        ensureUploadsDirectoryExists();

        // Resolve the target path
        Path targetPath = uploadsDirectory.resolve(targetFileName);

        // Copy the stream to the uploads directory
        // REPLACE_EXISTING will overwrite if file exists (cheecked in interactor)
        Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);

        return targetPath;
    }

    public Path getUploadsDirectory() {
        return uploadsDirectory;
    }

    @Override
    public void deleteSprite(File spriteFile) throws IOException {
        Path targetPath = uploadsDirectory.resolve(spriteFile.getName());
        Files.deleteIfExists(targetPath);
    }

    /**
     * Gets all existing image files from the uploads directory.
     * Supports .png, .jpg, and .jpeg extensions.
     *
     * @return a list of File objects representing all images in the uploads directory
     * @throws IOException if an error occurs while reading the directory
     */
    public java.util.List<File> getAllExistingImages() throws IOException {
        java.util.List<File> imageFiles = new java.util.ArrayList<>();

        if (!Files.exists(uploadsDirectory)) {
            return imageFiles; // Return empty list if directory doesn't exist
        }

        try (java.util.stream.Stream<Path> paths = Files.list(uploadsDirectory)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> {
                        String fileName = path.getFileName().toString().toLowerCase();
                        return fileName.endsWith(".png") ||
                                fileName.endsWith(".jpg") ||
                                fileName.endsWith(".jpeg");
                    })
                    .forEach(path -> imageFiles.add(path.toFile()));
        }

        return imageFiles;
    }
}

