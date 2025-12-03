package use_case.trigger;

import entity.*;
import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.scripting.action.Action;
import entity.scripting.action.WaitAction;
import interface_adapter.EditorState;
import org.junit.jupiter.api.Test;
import use_case.trigger.action.edit.ActionEditSaveInputBoundary;
import use_case.trigger.action.edit.ActionEditSaveInputData;
import use_case.trigger.action.edit.ActionEditSaveInteractor;
import use_case.trigger.action.edit.ActionEditSaveOutputBoundary;


import static org.junit.jupiter.api.Assertions.*;

public class ActionEditSaveInteractorTest {
    @Test
    void successTest() {

        GameObject gameObject = new GameObject("Test");
        TriggerManager manager = new TriggerManager();

        Trigger trigger = new Trigger();
        Action action = new WaitAction();
        trigger.addAction(action);

        manager.addTrigger(trigger);
        gameObject.setTriggerManager(manager);

        EditorState.setCurrentGameObject(gameObject);

        ActionEditSaveInputData inputData =
                new ActionEditSaveInputData(0, 0, "number:(0.0)");

        ActionEditSaveOutputBoundary presenter = new ActionEditSaveOutputBoundary() {
            @Override
            public void prepareSuccessView() {
                assertTrue(true);
            }

            @Override
            public void prepareFailureView(String error) {
                fail("Unexpected failure: " + error);
            }
        };

        ActionEditSaveInputBoundary interactor =
                new ActionEditSaveInteractor(presenter);

        interactor.execute(inputData);
    }

    @Test
    void failureSyntaxErrorTest() {

        GameObject gameObject = new GameObject("Test");
        TriggerManager manager = new TriggerManager();

        Trigger trigger = new Trigger();
        Action action = new WaitAction();
        trigger.addAction(action);

        manager.addTrigger(trigger);
        gameObject.setTriggerManager(manager);

        EditorState.setCurrentGameObject(gameObject);

        ActionEditSaveInputData inputData =
                new ActionEditSaveInputData(0, 0, "INVALID^SYNTAX");

        ActionEditSaveOutputBoundary presenter = new ActionEditSaveOutputBoundary() {
            @Override
            public void prepareSuccessView() {
                fail("Unexpected success.");
            }

            @Override
            public void prepareFailureView(String error) {
                assertNotNull(error);
            }
        };

        ActionEditSaveInputBoundary interactor =
                new ActionEditSaveInteractor(presenter);

        interactor.execute(inputData);
    }
}
