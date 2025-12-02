package use_case.trigger;

import entity.*;
import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.scripting.event.EmptyEvent;
import interface_adapter.EditorState;
import org.junit.jupiter.api.Test;
import use_case.trigger.create.TriggerCreateInputBoundary;
import use_case.trigger.create.TriggerCreateInteractor;
import use_case.trigger.create.TriggerCreateOutputBoundary;
import use_case.trigger.create.TriggerCreateOutputData;

import static org.junit.jupiter.api.Assertions.*;

class TriggerCreateInteractorTest {

    @Test
    void successTest() {

        GameObject gameObject = new GameObject("Test");
        TriggerManager manager = new TriggerManager();
        gameObject.setTriggerManager(manager);

        EditorState.setCurrentGameObject(gameObject);

        TriggerCreateOutputBoundary presenter = new TriggerCreateOutputBoundary() {
            @Override
            public void prepareSuccessView(TriggerCreateOutputData outputData) {
                assertEquals(EmptyEvent.EVENT_TYPE, outputData.getEvent());
            }

            @Override
            public void prepareFailureView(String error) {
                fail("Unexpected failure: " + error);
            }
        };

        TriggerCreateInputBoundary interactor =
                new TriggerCreateInteractor(presenter);

        interactor.execute();

        assertEquals(1, manager.getAllTriggers().size());
        Trigger created = manager.getTrigger(0);

        assertNotNull(created);
        assertInstanceOf(EmptyEvent.class, created.getEvent());
    }


    @Test
    void failureNoGameObjectTest() {

        EditorState.setCurrentGameObject(null);

        TriggerCreateOutputBoundary presenter = new TriggerCreateOutputBoundary() {
            @Override
            public void prepareSuccessView(TriggerCreateOutputData outputData) {
                fail("Unexpected success.");
            }

            @Override
            public void prepareFailureView(String error) {
                assertNotNull(error);
            }
        };

        TriggerCreateInputBoundary interactor =
                new TriggerCreateInteractor(presenter);

        interactor.execute();
    }
}
