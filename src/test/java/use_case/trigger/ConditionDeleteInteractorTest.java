package use_case.trigger;

import entity.*;
import entity.scripting.Trigger;
import entity.scripting.condition.EmptyCondition;
import entity.scripting.event.EmptyEvent;
import interface_adapter.EditorState;
import org.junit.jupiter.api.Test;
import use_case.trigger.condition.delete.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConditionDeleteInteractorTest {

    @Test
    void successTest() {

        Trigger trigger = new Trigger(new EmptyEvent(), true);
        trigger.addCondition(new EmptyCondition());
        trigger.addCondition(new EmptyCondition());

        GameObject gameObject = new GameObject("Test");
        gameObject.getTriggerManager().addTrigger(trigger);
        EditorState.setCurrentGameObject(gameObject);

        ConditionDeleteInputData input = new ConditionDeleteInputData(0, 1);

        ConditionDeleteOutputBoundary presenter = new ConditionDeleteOutputBoundary() {
            @Override
            public void prepareSuccessView(ConditionDeleteOutputData data) {
                assertEquals(0, data.getTriggerIndex());
                assertEquals(1, data.getConditionIndex());
            }
        };

        ConditionDeleteInputBoundary interactor =
                new ConditionDeleteInteractor(presenter);

        interactor.execute(input);

        assertEquals(1, trigger.getConditions().size());
    }
}
