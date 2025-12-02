package use_case.trigger;

import entity.*;
import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.scripting.event.Event;
import entity.scripting.event.OnKeyPressEvent;
import interface_adapter.EditorState;
import org.junit.jupiter.api.Test;
import use_case.trigger.event.parameter_change.*;

import static org.junit.jupiter.api.Assertions.*;

class EventParameterChangeInteractorTest {

    @Test
    void successTest() {

        GameObject gameObject = new GameObject("Obj");
        TriggerManager manager = new TriggerManager();

        Event event = new OnKeyPressEvent();
        Trigger trigger = new Trigger(event, true);
        manager.addTrigger(trigger);

        gameObject.setTriggerManager(manager);
        EditorState.setCurrentGameObject(gameObject);

        EventParameterChangeInputData inputData =
                new EventParameterChangeInputData(0, "Key", "W");

        EventParameterChangeOutputBoundary presenter = new EventParameterChangeOutputBoundary() {
            @Override
            public void prepareSuccessView(EventParameterChangeOutputData outputData) {
                assertEquals(0, outputData.getIndex());
                assertEquals("Key", outputData.getParameterName());
                assertEquals("W", outputData.getParameterValue());
            }
        };

        EventParameterChangeInputBoundary interactor =
                new EventParameterChangeInteractor(presenter);

        interactor.execute(inputData);

        assertEquals("W", event.getEventParameter("Key"));
    }
}
