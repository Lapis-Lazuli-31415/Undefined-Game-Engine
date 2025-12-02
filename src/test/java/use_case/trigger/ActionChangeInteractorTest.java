package use_case.trigger;

import entity.*;
import entity.scripting.Trigger;
import entity.scripting.action.ActionFactory;
import entity.scripting.action.DefaultActionFactory;
import entity.scripting.action.EmptyAction;
import interface_adapter.EditorState;
import org.junit.jupiter.api.Test;
import use_case.trigger.action.change.ActionChangeInputData;
import use_case.trigger.action.change.ActionChangeInteractor;
import use_case.trigger.action.change.ActionChangeOutputBoundary;
import use_case.trigger.action.change.ActionChangeOutputData;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionChangeInteractorTest {
    @Test
    void actionChangeInteractorTest() {
        // Arrange
        GameObject gameObject = new GameObject("Test");
        Trigger trigger = new Trigger();
        trigger.addAction(new EmptyAction());
        gameObject.getTriggerManager().addTrigger(trigger);
        EditorState.setCurrentGameObject(gameObject);

        ActionFactory actionFactory = new DefaultActionFactory();

        ActionChangeInputData inputData = new ActionChangeInputData(0, 0, "Wait");

        ActionChangeOutputBoundary presenter = new ActionChangeOutputBoundary() {
            @Override
            public void prepareSuccessView(ActionChangeOutputData data) {
                assertEquals(0, data.getTriggerIndex());
                assertEquals(0, data.getActionIndex());
                assertEquals("Wait", data.getAction());
            }
        };

        ActionChangeInteractor interactor = new ActionChangeInteractor(presenter, actionFactory);

        interactor.execute(inputData);

        assertEquals("Wait", trigger.getAction(0).getActionType());
    }
}
