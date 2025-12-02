package use_case.trigger;

import entity.*;
import entity.scripting.Trigger;
import entity.scripting.condition.Condition;
import entity.scripting.condition.EmptyCondition;
import entity.scripting.event.EmptyEvent;
import interface_adapter.EditorState;
import org.junit.jupiter.api.Test;
import use_case.trigger.condition.edit.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConditionEditInteactorTest {

    @Test
    void successTest() {

        Condition fakeCondition = new EmptyCondition() {
            @Override
            public String format() {
                return "formatted-script";
            }
        };

        Trigger trigger = new Trigger(new EmptyEvent(), true);
        trigger.addCondition(fakeCondition);

        GameObject gameObject = new GameObject("Test");
        gameObject.getTriggerManager().addTrigger(trigger);
        EditorState.setCurrentGameObject(gameObject);

        ConditionEditInputData input = new ConditionEditInputData(0, 0);

        ConditionEditOutputBoundary presenter = new ConditionEditOutputBoundary() {
            @Override
            public void prepareSuccessView(ConditionEditOutputData data) {
                assertEquals("formatted-script", data.getScript());
            }
        };

        ConditionEditInputBoundary interactor =
                new ConditionEditInteractor(presenter);

        interactor.execute(input);
    }
}
