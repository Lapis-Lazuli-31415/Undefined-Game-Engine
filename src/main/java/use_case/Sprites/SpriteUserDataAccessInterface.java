package use_case.Sprites;

/**
 * DAO for accessing and managing sprite data.
 */

public interface SpriteUserDataAccessInterface {

    /**
     * Saves the imported sprite data.
     * @param spriteData byte array representing the sprite data
     * @return true if the sprite exists (was accepted), false otherwise
     */
    boolean existsByName(byte[] spriteData);

    /**
     * Saves the imported sprite data.
     * #
     */

}
