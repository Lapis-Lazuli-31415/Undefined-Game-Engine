package use_case.trigger;

import entity.*;
import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.scripting.condition.Condition;
import entity.scripting.condition.NumericComparisonCondition;
import interface_adapter.EditorState;
import org.junit.jupiter.api.Test;
import use_case.trigger.condition.edit.ConditionEditSaveInputBoundary;
import use_case.trigger.condition.edit.ConditionEditSaveInputData;
import use_case.trigger.condition.edit.ConditionEditSaveInteractor;
import use_case.trigger.condition.edit.ConditionEditSaveOutputBoundary;

import static org.junit.jupiter.api.Assertions.*;

class ConditionEditSaveInteractorTest {

    @Test
    void successTest() {

        GameObject gameObject = new GameObject("Test");
        TriggerManager manager = new TriggerManager();

        Trigger trigger = new Trigger();
        Condition condition = new NumericComparisonCondition();
        trigger.addCondition(condition);

        manager.addTrigger(trigger);
        gameObject.setTriggerManager(manager);

        EditorState.setCurrentGameObject(gameObject);

        ConditionEditSaveInputData inputData =
                new ConditionEditSaveInputData(0, 0, "number:(10); >; number:(5)");

        ConditionEditSaveOutputBoundary presenter = new ConditionEditSaveOutputBoundary() {
            @Override
            public void prepareSuccessView() {
                assertTrue(true);
            }

            @Override
            public void prepareFailureView(String error) {
                fail("Unexpected failure: " + error);
            }
        };

        ConditionEditSaveInputBoundary interactor =
                new ConditionEditSaveInteractor(presenter);

        interactor.execute(inputData);
    }

    @Test
    void failureSyntaxErrorTest() {

        GameObject gameObject = new GameObject("Test");
        TriggerManager manager = new TriggerManager();

        Trigger trigger = new Trigger();
        Condition condition = new NumericComparisonCondition();
        trigger.addCondition(condition);

        manager.addTrigger(trigger);
        gameObject.setTriggerManager(manager);

        EditorState.setCurrentGameObject(gameObject);

        ConditionEditSaveInputData inputData =
                new ConditionEditSaveInputData(0, 0, "INVALID^SYNTAX");

        ConditionEditSaveOutputBoundary presenter = new ConditionEditSaveOutputBoundary() {
            @Override
            public void prepareSuccessView() {
                fail("Unexpected success.");
            }

            @Override
            public void prepareFailureView(String error) {
                assertNotNull(error);
            }
        };

        ConditionEditSaveInputBoundary interactor =
                new ConditionEditSaveInteractor(presenter);

        interactor.execute(inputData);
    }
}
