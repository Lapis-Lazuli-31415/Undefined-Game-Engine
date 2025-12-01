package entity.scripting.action;

import entity.GameObject;
import entity.Scene;
import entity.Transform;
import entity.scripting.environment.Environment;
import entity.scripting.error.GameObjectException;
import entity.scripting.error.ParseSyntaxException;
import entity.scripting.expression.Expression;
import entity.scripting.expression.ExpressionFactory;
import entity.scripting.expression.NumericExpression;
import entity.scripting.expression.value.NumericValue;

public class ChangePositionAction extends Action{
    public static final String ACTION_TYPE = "Change Position";

    private String gameObjectName;
    private NumericExpression x;
    private NumericExpression y;

    public ChangePositionAction(String gameObjectName, NumericExpression x, NumericExpression y) {
        this.gameObjectName = gameObjectName;
        this.x = x;
        this.y = y;
    }

    public ChangePositionAction() {
        this("_", new NumericValue(0), new NumericValue(0));
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

    public NumericExpression getX() {
        return x;
    }

    public void setX(NumericExpression x) {
        this.x = x;
    }

    public NumericExpression getY() {
        return y;
    }

    public void setY(NumericExpression y) {
        this.y = y;
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

        double xValue = x.evaluate(globalEnvironment, localEnvironment);
        double yValue = y.evaluate(globalEnvironment, localEnvironment);

        Transform transform = gameObject.getTransform();
        transform.setX(xValue);
        transform.setY(yValue);
    }

    @Override
    public Action parse(String string) throws ParseSyntaxException {
        String[] parts = string.split(";");
        final int requiredLength = 3;

        if (parts.length != requiredLength) {
            throw new ParseSyntaxException("Invalid Syntax (ChangePositionAction): expected " + requiredLength +
                    " parameters but got " + parts.length);
        }

        String gameObjectName = parts[0].trim();
        String x = parts[1].trim();
        String y = parts[2].trim();

        Expression<?> xResult = ExpressionFactory.parse(x);
        Expression<?> yResult = ExpressionFactory.parse(y);

        if (!(xResult instanceof NumericExpression)) {
            throw new ParseSyntaxException("Invalid Syntax: " + x + " does not evaluate to a NumericExpression");
        } else if (!(yResult instanceof NumericExpression)) {
            throw new ParseSyntaxException("Invalid Syntax: " + y + " does not evaluate to a NumericExpression");
        } else {
            this.gameObjectName = gameObjectName;
            this.x = (NumericExpression) xResult;
            this.y = (NumericExpression) yResult;
        }
        return null;
    }

    @Override
    public String format() {
        return gameObjectName + "; " + x.format() + "; " + y.format();
    }
}
