package use_case.trigger;

import entity.*;
import entity.scripting.Trigger;
import entity.scripting.action.EmptyAction;
import interface_adapter.EditorState;
import org.junit.jupiter.api.Test;
import use_case.trigger.action.delete.ActionDeleteInputData;
import use_case.trigger.action.delete.ActionDeleteInteractor;
import use_case.trigger.action.delete.ActionDeleteOutputBoundary;
import use_case.trigger.action.delete.ActionDeleteOutputData;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionDeleteInteractorTest {
    @Test
    void actionDeleteInteractorTest() {

        GameObject gameObject = new GameObject("Test");
        Trigger trigger = new Trigger();
        trigger.addAction(new EmptyAction());
        trigger.addAction(new EmptyAction());
        gameObject.getTriggerManager().addTrigger(trigger);
        EditorState.setCurrentGameObject(gameObject);

        ActionDeleteInputData inputData = new ActionDeleteInputData(0, 1);

        ActionDeleteOutputBoundary presenter = new ActionDeleteOutputBoundary() {
            @Override
            public void prepareSuccessView(ActionDeleteOutputData data) {
                assertEquals(0, data.getTriggerIndex());
                assertEquals(1, data.getActionIndex());
            }
        };

        ActionDeleteInteractor interactor = new ActionDeleteInteractor(presenter);

        interactor.execute(inputData);

        assertEquals(1, trigger.getActions().size());
    }
}
