package use_case.trigger;

import entity.*;
import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.scripting.event.EmptyEvent;
import interface_adapter.EditorState;
import org.junit.jupiter.api.Test;
import use_case.trigger.delete.*;

import static org.junit.jupiter.api.Assertions.*;

class TriggerDeleteInteractorTest {

    @Test
    void successTest() {

        GameObject gameObject = new GameObject("Test");
        TriggerManager manager = new TriggerManager();

        Trigger t1 = new Trigger(new EmptyEvent(), true);
        Trigger t2 = new Trigger(new EmptyEvent(), true);
        manager.addTrigger(t1);
        manager.addTrigger(t2);

        gameObject.setTriggerManager(manager);
        EditorState.setCurrentGameObject(gameObject);

        TriggerDeleteInputData inputData = new TriggerDeleteInputData(0);

        TriggerDeleteOutputBoundary presenter = new TriggerDeleteOutputBoundary() {
            @Override
            public void prepareSuccessView(TriggerDeleteOutputData outputData) {
                assertEquals(0, outputData.getIndex());
            }
        };

        TriggerDeleteInputBoundary interactor =
                new TriggerDeleteInteractor(presenter);

        interactor.execute(inputData);

        assertEquals(1, manager.getAllTriggers().size());
        assertEquals(t2, manager.getTrigger(0));
    }
}