package use_case.Sprites.Import;

import entity.Image;

/**
 * Response model for deleting a sprite.
 */

public class DeleteSpriteResponse {
    public boolean success;
    public String message;
    public Image deletedSprite;
}
