package entity.scripting.action;

import entity.GameObject;
import entity.Scene;
import entity.SpriteRenderer;
import entity.Transform;
import entity.scripting.environment.Environment;
import entity.scripting.error.GameObjectException;
import entity.scripting.error.ParseSyntaxException;
import entity.scripting.expression.BooleanExpression;
import entity.scripting.expression.Expression;
import entity.scripting.expression.ExpressionFactory;
import entity.scripting.expression.NumericExpression;
import entity.scripting.expression.value.BooleanValue;

public class ChangeVisibilityAction extends Action{
    public static final String ACTION_TYPE = "Change Visibility";

    private String gameObjectName;
    private BooleanExpression visibility;

    public ChangeVisibilityAction(String gameObjectName, BooleanExpression visibility) {
        this.gameObjectName = gameObjectName;
        this.visibility = visibility;
    }

    public ChangeVisibilityAction() {
        this("_", new BooleanValue(false));
    }

    @Override
    public String getActionType() {
        return ACTION_TYPE;
    }

    public String getGameObjectName() {
        return gameObjectName;
    }

    public void setGameObjectName(String gameObjectName) {
        this.gameObjectName = gameObjectName;
    }

    public BooleanExpression getVisibility() {
        return visibility;
    }

    public void setVisibility(BooleanExpression visibility) {
        this.visibility = visibility;
    }

    @Override
    public void execute(Environment globalEnvironment, Environment localEnvironment, Scene scene) throws Exception {
        if (gameObjectName == null) {
            return;
        }

        GameObject gameObject = scene.getGameObjectByName(gameObjectName);

        if (gameObject == null) {
            throw new GameObjectException(gameObjectName + " not found in current scene");
        }

        boolean visibilityValue = visibility.evaluate(globalEnvironment, localEnvironment);

        SpriteRenderer spriteRenderer = gameObject.getSpriteRenderer();
        spriteRenderer.setVisible(visibilityValue);
    }

    @Override
    public void parse(String string) throws ParseSyntaxException {
        String[] parts = string.split(";");
        final int requiredLength = 2;

        if (parts.length != requiredLength) {
            throw new ParseSyntaxException("Invalid Syntax (ChangeVisibilityAction): expected " + requiredLength +
                    " parameters but got " + parts.length);
        }

        String gameObjectName = parts[0].trim();
        String visibility = parts[1].trim();

        Expression<?> visibilityResult = ExpressionFactory.parse(visibility);

        if (!(visibilityResult instanceof BooleanExpression)) {
            throw new ParseSyntaxException("Invalid Syntax: " + visibilityResult
                    + " does not evaluate to a BooleanExpression");
        } else {
            this.gameObjectName = gameObjectName;
            this.visibility = (BooleanExpression) visibilityResult;
        }
    }

    @Override
    public String format() {
        return gameObjectName + "; " + visibility.format();
    }
}
