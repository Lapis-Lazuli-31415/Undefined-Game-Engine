package entity;

import java.awt.*;

public class SpriteRenderer extends Component {

    private Image sprite;
    final boolean visible;

    public SpriteRenderer(Image sprite, boolean visible) {
        this.sprite = sprite;
        this.visible = visible;
    }
}
