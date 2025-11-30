package use_case.sprites;

import entity.Image;

/**
 * Response model for importing a sprite. (Output data)
 */

public class ImportSpriteResponse {
    public boolean success;
    public String message;
    public Image importedSprite;
}
