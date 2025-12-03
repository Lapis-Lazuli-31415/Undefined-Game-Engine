package use_case.trigger;

import entity.*;
import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.scripting.event.DefaultEventFactory;
import entity.scripting.event.EmptyEvent;
import entity.scripting.event.Event;
import entity.scripting.event.EventFactory;
import interface_adapter.EditorState;
import org.junit.jupiter.api.Test;
import use_case.trigger.event.change.*;

import static org.junit.jupiter.api.Assertions.*;

class EventChangeInteractorTest {

    @Test
    void successTest() {

        GameObject gameObject = new GameObject("Test");
        TriggerManager manager = new TriggerManager();
        Trigger trigger = new Trigger(new EmptyEvent(), true);
        manager.addTrigger(trigger);
        gameObject.setTriggerManager(manager);

        EditorState.setCurrentGameObject(gameObject);

        EventFactory eventFactory = new DefaultEventFactory();

        EventChangeInputData inputData =
                new EventChangeInputData(0, "On Click");

        EventChangeOutputBoundary presenter = new EventChangeOutputBoundary() {
            @Override
            public void prepareSuccessView(EventChangeOutputData outputData) {
                assertEquals(0, outputData.getIndex());
                assertEquals("On Click", outputData.getEvent());
            }
        };

        EventChangeInputBoundary interactor =
                new EventChangeInteractor(presenter, eventFactory);

        interactor.execute(inputData);

        Event event = manager.getTrigger(0).getEvent();
        assertEquals("On Click", event.getEventLabel());
    }
}