package use_case.trigger;

import entity.*;
import entity.scripting.Trigger;
import entity.scripting.condition.EmptyCondition;
import entity.scripting.event.EmptyEvent;
import interface_adapter.EditorState;
import org.junit.jupiter.api.Test;
import use_case.trigger.condition.create.*;

import static org.junit.jupiter.api.Assertions.*;

class ConditionCreateInteractorTest {

    @Test
    void successTest() {

        Trigger trigger = new Trigger(new EmptyEvent(), true);
        GameObject gameObject = new GameObject("Test");
        gameObject.getTriggerManager().addTrigger(trigger);
        EditorState.setCurrentGameObject(gameObject);

        ConditionCreateInputData input = new ConditionCreateInputData(0);

        ConditionCreateOutputBoundary presenter = new ConditionCreateOutputBoundary() {
            @Override
            public void prepareSuccessView(ConditionCreateOutputData outputData) {
                assertEquals(0, outputData.getIndex());
                assertEquals(EmptyCondition.CONDITION_TYPE, outputData.getCondition());
            }
        };

        ConditionCreateInputBoundary interactor =
                new ConditionCreateInteractor(presenter);

        // Act
        interactor.execute(input);

        // Assert side-effect
        assertEquals(1, trigger.getConditions().size());
        assertInstanceOf(EmptyCondition.class, trigger.getCondition(0));
    }
}
