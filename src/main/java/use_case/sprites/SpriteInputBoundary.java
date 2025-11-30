package use_case.sprites;

/**
 * Input Boundary for actions related to importing and managing sprites.
 */

public interface SpriteInputBoundary {

    /**
     * Executes the sprite import use case
     * @param ImportSpriteRequest request containing the sprite file to be imported
     */
    void execute(ImportSpriteRequest request);
}
