package use_case.trigger;

import entity.*;
import entity.scripting.Trigger;
import entity.scripting.action.EmptyAction;
import interface_adapter.EditorState;
import org.junit.jupiter.api.Test;
import use_case.trigger.action.create.ActionCreateInputData;
import use_case.trigger.action.create.ActionCreateInteractor;
import use_case.trigger.action.create.ActionCreateOutputBoundary;
import use_case.trigger.action.create.ActionCreateOutputData;

import static org.junit.jupiter.api.Assertions.*;

public class ActionCreateInteractorTest {

    @Test
    void actionCreateInteractorTest() {

        GameObject gameObject = new GameObject("Test");
        Trigger trigger = new Trigger();
        gameObject.getTriggerManager().addTrigger(trigger);
        EditorState.setCurrentGameObject(gameObject);

        ActionCreateInputData inputData = new ActionCreateInputData(0);

        ActionCreateOutputBoundary presenter = new ActionCreateOutputBoundary() {
            @Override
            public void prepareSuccessView(ActionCreateOutputData data) {
                assertEquals(0, data.getTriggerIndex());
                assertEquals(EmptyAction.ACTION_TYPE, data.getActionType());
            }
        };

        ActionCreateInteractor interactor = new ActionCreateInteractor(presenter);

        // Act
        interactor.execute(inputData);

        // Assert side-effect
        assertEquals(1, trigger.getActions().size());
        assertInstanceOf(EmptyAction.class, trigger.getAction(0));
    }
}
