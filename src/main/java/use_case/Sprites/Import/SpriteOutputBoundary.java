package use_case.Sprites.Import;

/**
 * Output Boundary for actions related to importing and managing sprites.
 */

public interface SpriteOutputBoundary {
    /**
     * Prepares the success view for the sprite import use case.
     * @param response the response model containing the result of the import operation
     */
    void prepareSuccessView(ImportSpriteResponse response);

    /**
     * Prepares the failure view for the sprite import use case.
     * @param errorMessage the error message to display
     */
    void prepareFailView(String errorMessage);
}
