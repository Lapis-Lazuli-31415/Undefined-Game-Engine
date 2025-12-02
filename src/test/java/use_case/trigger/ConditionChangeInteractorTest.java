package use_case.trigger;

import entity.*;
import entity.scripting.Trigger;
import entity.scripting.condition.ConditionFactory;
import entity.scripting.condition.DefaultConditionFactory;
import entity.scripting.condition.EmptyCondition;
import entity.scripting.event.EmptyEvent;
import interface_adapter.EditorState;
import org.junit.jupiter.api.Test;
import use_case.trigger.condition.change.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConditionChangeInteractorTest {
    @Test
    void successTest() {

        ConditionFactory conditionFactory = new DefaultConditionFactory();

        Trigger trigger = new Trigger(new EmptyEvent(), true);
        trigger.addCondition(new EmptyCondition());

        GameObject gameObject = new GameObject("Test");
        gameObject.getTriggerManager().addTrigger(trigger);
        EditorState.setCurrentGameObject(gameObject);

        ConditionChangeInputData input =
                new ConditionChangeInputData(0, 0, "Numeric Comparison");

        ConditionChangeOutputBoundary presenter = new ConditionChangeOutputBoundary() {
            @Override
            public void prepareSuccessView(ConditionChangeOutputData data) {
                assertEquals(0, data.getTriggerIndex());
                assertEquals(0, data.getConditionIndex());
                assertEquals("Numeric Comparison", data.getCondition());
            }
        };

        ConditionChangeInputBoundary interactor =
                new ConditionChangeInteractor(presenter, conditionFactory);

        // Act
        interactor.execute(input);

        // Assert
        assertEquals("Numeric Comparison", trigger.getCondition(0).getConditionType());
    }
}
