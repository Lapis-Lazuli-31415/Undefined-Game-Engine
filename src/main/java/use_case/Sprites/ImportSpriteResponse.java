package use_case.Sprites;

import entity.Image;

/**
 * Response model for importing a sprite. (Output data)
 */

public class ImportSpriteResponse {
    public boolean success;
    public String message;
    public Image importedSprite;
}
