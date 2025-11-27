package use_case.runtime;

import entity.GameObject;
import entity.Property;
import entity.scripting.Trigger;
import entity.scripting.action.PrintAction;
import entity.scripting.environment.Environment;
import entity.scripting.event.OnClickEvent;
import entity.scripting.event.OnKeyPressEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for TriggerExecutionInteractor.
 *
 * @author Wanru Cheng
 */
class TriggerExecutionInteractorTest {

    private TriggerExecutionInteractor interactor;
    private TestTriggerExecutionPresenter presenter;

    @BeforeEach
    void setUp() {
        presenter = new TestTriggerExecutionPresenter();
        interactor = new TriggerExecutionInteractor(presenter);
    }

    @Test
    void execute_triggerWithNoConditionsAndOneAction_executesSuccessfully() {
        // Arrange
        Trigger trigger = new Trigger(new OnClickEvent());
        trigger.addAction(new PrintAction("Test action"));

        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();

        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.successCalled);
        assertFalse(presenter.errorCalled);
        assertFalse(presenter.conditionsNotMetCalled);
        assertEquals(1, presenter.lastOutputData.getActionsExecuted());
        assertTrue(presenter.lastOutputData.isExecuted());
    }
    @Test
    void execute_withNullEnvironmentInGameObject_createsNewEnvironment() {
        // Arrange
        Trigger trigger = new Trigger(new OnClickEvent());
        trigger.addAction(new PrintAction("Test"));

        // Create GameObject with null environment
        GameObject obj = new GameObject(
                "obj-1",
                "TestObject",
                true,
                new ArrayList<>(),
                null  // null environment
        );

        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv);

        // Act
        interactor.execute(inputData);

        // Assert - should succeed even with null local environment
        assertTrue(presenter.successCalled);
    }
    @Test
    void execute_withConditionThatThrowsException_returnsFalse() {
        // Arrange
        Trigger trigger = new Trigger(new OnClickEvent());
        trigger.addAction(new PrintAction("Test"));

        // Add a condition that throws exception
        trigger.addCondition(new entity.scripting.condition.Condition() {
            @Override
            public boolean evaluate(Environment globalEnv, Environment localEnv) {
                throw new RuntimeException("Test condition exception");
            }
        });

        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();

        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv);

        // Act
        interactor.execute(inputData);

        // Assert - condition exception should result in conditions not met
        assertTrue(presenter.conditionsNotMetCalled);
        assertFalse(presenter.successCalled);
    }
    @Test
    void execute_withConditionThatFails_presentsConditionsNotMet() {
        // Arrange
        Trigger trigger = new Trigger(new OnClickEvent());
        trigger.addAction(new PrintAction("Test"));

        // Add a condition that always fails
        trigger.addCondition(new entity.scripting.condition.Condition() {
            @Override
            public boolean evaluate(Environment globalEnv, Environment localEnv) {
                return false;  // Always fails
            }
        });

        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();

        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.conditionsNotMetCalled);
        assertFalse(presenter.successCalled);
        assertFalse(presenter.lastOutputData.isExecuted());
        assertEquals(0, presenter.lastOutputData.getActionsExecuted());
    }

    @Test
    void execute_withConditionThatPasses_executesActions() {
        // Arrange
        Trigger trigger = new Trigger(new OnClickEvent());
        trigger.addAction(new PrintAction("Test"));

        // Add a condition that always passes
        trigger.addCondition(new entity.scripting.condition.Condition() {
            @Override
            public boolean evaluate(Environment globalEnv, Environment localEnv) {
                return true;  // Always passes
            }
        });

        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();

        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.successCalled);
        assertTrue(presenter.lastOutputData.isExecuted());
        assertEquals(1, presenter.lastOutputData.getActionsExecuted());
    }

    @Test
    void execute_withMultipleConditionsAllPass_executesActions() {
        // Arrange
        Trigger trigger = new Trigger(new OnClickEvent());
        trigger.addAction(new PrintAction("Test"));

        // Add multiple conditions that all pass
        for (int i = 0; i < 3; i++) {
            trigger.addCondition(new entity.scripting.condition.Condition() {
                @Override
                public boolean evaluate(Environment globalEnv, Environment localEnv) {
                    return true;
                }
            });
        }

        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();

        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.successCalled);
    }

    @Test
    void execute_withMultipleConditionsOneFails_doesNotExecuteActions() {
        // Arrange
        Trigger trigger = new Trigger(new OnClickEvent());
        trigger.addAction(new PrintAction("Test"));

        // Add conditions - one will fail
        trigger.addCondition(new entity.scripting.condition.Condition() {
            @Override
            public boolean evaluate(Environment globalEnv, Environment localEnv) {
                return true;
            }
        });
        trigger.addCondition(new entity.scripting.condition.Condition() {
            @Override
            public boolean evaluate(Environment globalEnv, Environment localEnv) {
                return false;  // This one fails
            }
        });

        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();

        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.conditionsNotMetCalled);
        assertFalse(presenter.successCalled);
    }

    @Test
    void execute_withActionThatThrowsException_presentsError() {
        // Arrange
        Trigger trigger = new Trigger(new OnClickEvent());

        // Add an action that throws exception
        trigger.addAction(new entity.scripting.action.Action() {
            @Override
            public void execute(Environment globalEnv, Environment localEnv) throws Exception {
                throw new Exception("Test exception");
            }
        });

        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();

        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.errorCalled);
        assertTrue(presenter.lastErrorMessage.contains("Test exception"));
    }
    @Test
    void execute_triggerWithMultipleActions_executesAllActions() {
        // Arrange
        Trigger trigger = new Trigger(new OnClickEvent());
        trigger.addAction(new PrintAction("Action 1"));
        trigger.addAction(new PrintAction("Action 2"));
        trigger.addAction(new PrintAction("Action 3"));

        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();

        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.successCalled);
        assertEquals(3, presenter.lastOutputData.getActionsExecuted());
    }

    @Test
    void execute_triggerWithNoActions_executesSuccessfullyWithZeroActions() {
        // Arrange
        Trigger trigger = new Trigger(new OnClickEvent());
        // No actions added

        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();

        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.successCalled);
        assertEquals(0, presenter.lastOutputData.getActionsExecuted());
    }

    @Test
    void execute_withNullGameObject_executesWithUnknownName() {
        // Arrange
        Trigger trigger = new Trigger(new OnClickEvent());
        trigger.addAction(new PrintAction("Test"));

        Environment globalEnv = new Environment();

        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, null, globalEnv);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.successCalled);
        assertEquals("Unknown", presenter.lastOutputData.getGameObjectName());
    }

    @Test
    void execute_withOnKeyPressEvent_returnsCorrectEventName() {
        // Arrange
        Trigger trigger = new Trigger(new OnKeyPressEvent("W"));
        trigger.addAction(new PrintAction("Test"));

        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();

        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.successCalled);
        assertEquals("OnKeyPressEvent", presenter.lastOutputData.getEventName());
    }

    @Test
    void execute_withOnClickEvent_returnsCorrectEventName() {
        // Arrange
        Trigger trigger = new Trigger(new OnClickEvent());
        trigger.addAction(new PrintAction("Test"));

        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();

        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv);

        // Act
        interactor.execute(inputData);

        // Assert
        assertTrue(presenter.successCalled);
        assertEquals("OnClickEvent", presenter.lastOutputData.getEventName());
    }

    @Test
    void getters_returnCorrectValues() {
        // Arrange
        Trigger trigger = new Trigger(new OnClickEvent());
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();

        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv);

        // Assert
        assertEquals(trigger, inputData.getTrigger());
        assertEquals(obj, inputData.getGameObject());
        assertEquals(globalEnv, inputData.getGlobalEnvironment());
    }

    @Test
    void outputData_gettersReturnCorrectValues() {
        // Arrange
        TriggerExecutionOutputData outputData = new TriggerExecutionOutputData(
                "TestObject",
                "OnClickEvent",
                true,
                5
        );

        // Assert
        assertEquals("TestObject", outputData.getGameObjectName());
        assertEquals("OnClickEvent", outputData.getEventName());
        assertTrue(outputData.isExecuted());
        assertEquals(5, outputData.getActionsExecuted());
    }

    // Helper methods

    private GameObject createTestGameObject() {
        return new GameObject(
                "obj-1",
                "TestObject",
                true,
                new ArrayList<>(),
                new Environment()
        );
    }

    // Test double for TriggerExecutionOutputBoundary
    private static class TestTriggerExecutionPresenter implements TriggerExecutionOutputBoundary {
        boolean successCalled = false;
        boolean errorCalled = false;
        boolean conditionsNotMetCalled = false;
        TriggerExecutionOutputData lastOutputData = null;
        String lastErrorMessage = null;

        @Override
        public void presentSuccess(TriggerExecutionOutputData outputData) {
            successCalled = true;
            lastOutputData = outputData;
        }

        @Override
        public void presentError(String errorMessage) {
            errorCalled = true;
            lastErrorMessage = errorMessage;
        }

        @Override
        public void presentConditionsNotMet(TriggerExecutionOutputData outputData) {
            conditionsNotMetCalled = true;
            lastOutputData = outputData;
        }
    }
}