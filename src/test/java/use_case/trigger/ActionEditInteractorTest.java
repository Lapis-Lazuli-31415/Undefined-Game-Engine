package use_case.trigger;

import entity.*;
import entity.scripting.Trigger;
import entity.scripting.action.Action;
import entity.scripting.action.EmptyAction;
import interface_adapter.EditorState;
import org.junit.jupiter.api.Test;
import use_case.trigger.action.edit.ActionEditInputData;
import use_case.trigger.action.edit.ActionEditInteractor;
import use_case.trigger.action.edit.ActionEditOutputBoundary;
import use_case.trigger.action.edit.ActionEditOutputData;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionEditInteractorTest {
    @Test
    void actionEditInteractorTest() {

        GameObject gameObject = new GameObject("Test");
        Trigger trigger = new Trigger();
        Action action = new EmptyAction() {
            @Override
            public String format() {
                return "formatted-script";
            }
        };
        trigger.addAction(action);
        gameObject.getTriggerManager().addTrigger(trigger);
        EditorState.setCurrentGameObject(gameObject);

        ActionEditInputData inputData = new ActionEditInputData(0, 0);

        ActionEditOutputBoundary presenter = new ActionEditOutputBoundary() {
            @Override
            public void prepareSuccessView(ActionEditOutputData data) {
                assertEquals("formatted-script", data.getScript());
            }
        };

        ActionEditInteractor interactor = new ActionEditInteractor(presenter);

        interactor.execute(inputData);
    }
}
