package use_case.Sprites;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

/**
 * DAO for accessing and managing sprite data.
 */

public interface SpriteUserDataAccessInterface {

    /**
     * checks if a sprite with the given name already exists.
     * @param spriteName the name of the sprite to check
     * @return true if the sprite exists, false otherwise
     */
    boolean existsByName(String spriteName);

    /**
     * saves the imported sprite file to the uploads directory (in the backend).
     * @param sourceFile the source file to save
     * @param targetFileName the target file name
     * @return the path to the saved file
     * @throws IOException if an error occurs during file operations
     */
    Path saveSprite(File sourceFile, String targetFileName) throws IOException;

    /**
     * gets the uploads directory path.
     * @return the path to the uploads directory
     */
    Path getUploadsDirectory();
}
