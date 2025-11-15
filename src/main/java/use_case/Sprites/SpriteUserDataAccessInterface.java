package use_case.Sprites;

/**
 * DAO for accessing and managing sprite data.
 */

public interface SpriteUserDataAccessInterface {

    /**
     * Saves the imported sprite data.
     * @param spriteName String representing the sprite data
     * @return true if the sprite exists (was accepted), false otherwise
     */

    boolean existsByName(String spriteName);

    /**
     * Saves the imported sprite data.
     * #
     */

}
