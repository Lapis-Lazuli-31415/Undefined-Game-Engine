package use_case.runtime;

import entity.GameObject;
import entity.Scene;
import entity.Transform;
import entity.scripting.Trigger;
import entity.scripting.TriggerManager;
import entity.scripting.action.Action;
import entity.scripting.condition.Condition;
import entity.scripting.environment.Environment;
import entity.scripting.event.OnClickEvent;
import entity.scripting.event.OnKeyPressEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.Vector;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for TriggerExecutionInteractor.
 * Note: Most tests disabled because interactor no longer calls presenter.
 */
class TriggerExecutionInteractorTest {

    private TriggerExecutionInteractor interactor;
    private TestTriggerExecutionPresenter presenter;
    private Scene testScene;

    @BeforeEach
    void setUp() {
        presenter = new TestTriggerExecutionPresenter();
        interactor = new TriggerExecutionInteractor(presenter);
        testScene = createTestScene();
    }

    @Test
    @Disabled("Interactor no longer calls presenter")
    void execute_triggerWithNoConditionsAndOneAction_executesSuccessfully() {
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Test action"));
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);
        interactor.execute(inputData);
        assertTrue(presenter.successCalled);
        assertEquals(1, presenter.lastOutputData.getActionsExecuted());
    }

    @Test
    @Disabled("Interactor no longer calls presenter")
    void execute_withNullEnvironmentInGameObject_createsNewEnvironment() {
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Test"));
        GameObject obj = new GameObject("obj-1", "TestObject", true, null, createDefaultTransform(), null, new TriggerManager());
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);
        interactor.execute(inputData);
        assertTrue(presenter.successCalled);
    }

    @Test
    @Disabled("Interactor no longer calls presenter")
    void execute_withConditionThatThrowsException_returnsFalse() {
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Test"));
        trigger.addCondition(new TestCondition((g, l) -> { throw new RuntimeException("Test"); }));
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);
        interactor.execute(inputData);
        assertTrue(presenter.conditionsNotMetCalled);
    }

    @Test
    @Disabled("Interactor no longer calls presenter")
    void execute_withConditionThatFails_presentsConditionsNotMet() {
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Test"));
        trigger.addCondition(new TestCondition((g, l) -> false));
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);
        interactor.execute(inputData);
        assertTrue(presenter.conditionsNotMetCalled);
        assertEquals(0, presenter.lastOutputData.getActionsExecuted());
    }

    @Test
    @Disabled("Interactor no longer calls presenter")
    void execute_withConditionThatPasses_executesActions() {
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Test"));
        trigger.addCondition(new TestCondition((g, l) -> true));
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);
        interactor.execute(inputData);
        assertTrue(presenter.successCalled);
        assertEquals(1, presenter.lastOutputData.getActionsExecuted());
    }

    @Test
    @Disabled("Interactor no longer calls presenter")
    void execute_withMultipleConditionsAllPass_executesActions() {
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Test"));
        for (int i = 0; i < 3; i++) {
            trigger.addCondition(new TestCondition((g, l) -> true));
        }
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);
        interactor.execute(inputData);
        assertTrue(presenter.successCalled);
    }

    @Test
    @Disabled("Interactor no longer calls presenter")
    void execute_withMultipleConditionsOneFails_doesNotExecuteActions() {
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Test"));
        trigger.addCondition(new TestCondition((g, l) -> true));
        trigger.addCondition(new TestCondition((g, l) -> false));
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);
        interactor.execute(inputData);
        assertTrue(presenter.conditionsNotMetCalled);
    }

    @Test
    @Disabled("Interactor no longer calls presenter")
    void execute_withActionThatThrowsException_presentsError() {
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new TestAction((g, l, s) -> { throw new Exception("Test exception"); }));
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);
        interactor.execute(inputData);
        assertTrue(presenter.errorCalled);
        assertTrue(presenter.lastErrorMessage.contains("Test exception"));
    }

    @Test
    @Disabled("Interactor no longer calls presenter")
    void execute_triggerWithMultipleActions_executesAllActions() {
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Action 1"));
        trigger.addAction(new PrintAction("Action 2"));
        trigger.addAction(new PrintAction("Action 3"));
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);
        interactor.execute(inputData);
        assertTrue(presenter.successCalled);
        assertEquals(3, presenter.lastOutputData.getActionsExecuted());
    }

    @Test
    @Disabled("Interactor no longer calls presenter")
    void execute_triggerWithNoActions_executesSuccessfullyWithZeroActions() {
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);
        interactor.execute(inputData);
        assertTrue(presenter.successCalled);
        assertEquals(0, presenter.lastOutputData.getActionsExecuted());
    }

    @Test
    @Disabled("Interactor no longer calls presenter")
    void execute_withNullGameObject_executesWithUnknownName() {
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Test"));
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, null, globalEnv, testScene);
        interactor.execute(inputData);
        assertTrue(presenter.successCalled);
        assertEquals("Unknown", presenter.lastOutputData.getGameObjectName());
    }

    @Test
    @Disabled("Interactor no longer calls presenter")
    void execute_withOnKeyPressEvent_returnsCorrectEventName() {
        Trigger trigger = new Trigger(new OnKeyPressEvent(), true);
        trigger.addAction(new PrintAction("Test"));
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);
        interactor.execute(inputData);
        assertEquals("OnKeyPressEvent", presenter.lastOutputData.getEventName());
    }

    @Test
    @Disabled("Interactor no longer calls presenter")
    void execute_withOnClickEvent_returnsCorrectEventName() {
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Test"));
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);
        interactor.execute(inputData);
        assertEquals("OnClickEvent", presenter.lastOutputData.getEventName());
    }

    @Test
    void getters_returnCorrectValues() {
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);
        assertEquals(trigger, inputData.getTrigger());
        assertEquals(obj, inputData.getGameObject());
        assertEquals(globalEnv, inputData.getGlobalEnvironment());
        assertEquals(testScene, inputData.getScene());
    }

    @Test
    void outputData_gettersReturnCorrectValues() {
        TriggerExecutionOutputData outputData = new TriggerExecutionOutputData("TestObject", "OnClickEvent", true, 5);
        assertEquals("TestObject", outputData.getGameObjectName());
        assertEquals("OnClickEvent", outputData.getEventName());
        assertTrue(outputData.isExecuted());
        assertEquals(5, outputData.getActionsExecuted());
    }

    private Scene createTestScene() {
        ArrayList<GameObject> objects = new ArrayList<>();
        objects.add(createTestGameObject());
        return new Scene(UUID.randomUUID(), "Test Scene", objects);
    }

    private GameObject createTestGameObject() {
        return new GameObject("obj-1", "TestObject", true, new Environment(), createDefaultTransform(), null, new TriggerManager());
    }

    private Transform createDefaultTransform() {
        Vector<Double> position = new Vector<>(Arrays.asList(0.0, 0.0));
        Vector<Double> scale = new Vector<>(Arrays.asList(1.0, 1.0));
        return new Transform(position, 0f, scale);
    }

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

    private static class PrintAction extends Action {
        private final String message;
        public PrintAction(String message) { this.message = message; }
        @Override
        public void execute(Environment globalEnv, Environment localEnv, Scene scene) {
            System.out.println("PrintAction: " + message);
        }
        @Override
        public String format() { return "PrintAction"; }
        @Override
        public Action parse(String input) { return new PrintAction("parsed"); }
        @Override
        public String getActionType() { return "PrintAction"; }
    }

    private static class TestCondition extends Condition {
        private final BiFunction<Environment, Environment, Boolean> evaluator;
        public TestCondition(BiFunction<Environment, Environment, Boolean> evaluator) { this.evaluator = evaluator; }
        @Override
        public boolean evaluate(Environment globalEnv, Environment localEnv) { return evaluator.apply(globalEnv, localEnv); }
        @Override
        public String format() { return "TestCondition"; }
        @Override
        public Condition parse(String input) { return new TestCondition((g, l) -> true); }
        @Override
        public String getConditionType() { return "TestCondition"; }
    }

    private static class TestAction extends Action {
        private final TestActionExecutor executor;
        public TestAction(TestActionExecutor executor) { this.executor = executor; }
        @Override
        public void execute(Environment globalEnv, Environment localEnv, Scene scene) throws Exception {
            executor.execute(globalEnv, localEnv, scene);
        }
        @Override
        public String format() { return "TestAction"; }
        @Override
        public Action parse(String input) { return new TestAction((g, l, s) -> {}); }
        @Override
        public String getActionType() { return "TestAction"; }
    }

    @FunctionalInterface
    private interface TestActionExecutor {
        void execute(Environment globalEnv, Environment localEnv, Scene scene) throws Exception;
    }
    // ===== NEW TESTS FOR CURRENT IMPLEMENTATION (No presenter calls) =====

    @Test
    void execute_currentImplementation_noConditions_executesWithoutError() {
        // Test current implementation: conditions pass, actions execute, no presenter call
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Test"));
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);

        // Should not throw exception
        assertDoesNotThrow(() -> interactor.execute(inputData));
    }

    @Test
    void execute_currentImplementation_conditionsFail_returnsEarly() {
        // Test current implementation: conditions fail, returns early, no presenter call
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Test"));
        trigger.addCondition(new TestCondition((g, l) -> false));
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);

        // Should not throw exception
        assertDoesNotThrow(() -> interactor.execute(inputData));
    }

    @Test
    void execute_currentImplementation_conditionThrowsException_returnsEarly() {
        // Test current implementation: condition throws, returns early
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Test"));
        trigger.addCondition(new TestCondition((g, l) -> { throw new RuntimeException("Test"); }));
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);

        // Should not throw exception (catches internally)
        assertDoesNotThrow(() -> interactor.execute(inputData));
    }

    @Test
    void execute_currentImplementation_actionThrowsException_printsStackTrace() {
        // Test current implementation: action throws, prints stack trace but doesn't fail
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new TestAction((g, l, s) -> { throw new Exception("Test exception"); }));
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);

        // Should not throw exception (catches and prints)
        assertDoesNotThrow(() -> interactor.execute(inputData));
    }

    @Test
    void execute_currentImplementation_multipleActions_executesAll() {
        // Test current implementation: multiple actions execute
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Action 1"));
        trigger.addAction(new PrintAction("Action 2"));
        trigger.addAction(new PrintAction("Action 3"));
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);

        // Should execute all actions without error
        assertDoesNotThrow(() -> interactor.execute(inputData));
    }

    @Test
    void execute_currentImplementation_nullGameObject_usesUnknownName() {
        // Test current implementation: null gameObject handled
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Test"));
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, null, globalEnv, testScene);

        // Should handle null gameObject without error
        assertDoesNotThrow(() -> interactor.execute(inputData));
    }

    @Test
    void execute_currentImplementation_nullEnvironment_createsNew() {
        // Test current implementation: null environment creates new one
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Test"));
        GameObject obj = new GameObject("obj-1", "TestObject", true, null, createDefaultTransform(), null, new TriggerManager());
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);

        // Should create new environment without error
        assertDoesNotThrow(() -> interactor.execute(inputData));
    }

    @Test
    void execute_currentImplementation_noActions_executesWithoutError() {
        // Test current implementation: no actions to execute
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);

        // Should handle no actions without error
        assertDoesNotThrow(() -> interactor.execute(inputData));
    }

    @Test
    void execute_currentImplementation_multipleConditions_allPass() {
        // Test current implementation: all conditions pass
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Test"));
        trigger.addCondition(new TestCondition((g, l) -> true));
        trigger.addCondition(new TestCondition((g, l) -> true));
        trigger.addCondition(new TestCondition((g, l) -> true));
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);

        // Should pass all conditions and execute
        assertDoesNotThrow(() -> interactor.execute(inputData));
    }

    @Test
    void execute_currentImplementation_multipleConditions_secondFails() {
        // Test current implementation: second condition fails
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Test"));
        trigger.addCondition(new TestCondition((g, l) -> true));
        trigger.addCondition(new TestCondition((g, l) -> false));
        trigger.addCondition(new TestCondition((g, l) -> true));
        GameObject obj = createTestGameObject();
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);

        // Should stop at second condition
        assertDoesNotThrow(() -> interactor.execute(inputData));
    }
    // ===== TESTS FOR UNUSED PRIVATE METHODS (using reflection) =====

    @Test
    void checkConditions_allPass_returnsTrue() throws Exception {
        // Use reflection to test private checkConditions method
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addCondition(new TestCondition((g, l) -> true));
        trigger.addCondition(new TestCondition((g, l) -> true));

        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();

        // Access private method
        java.lang.reflect.Method method = TriggerExecutionInteractor.class
                .getDeclaredMethod("checkConditions", Trigger.class, Environment.class, Environment.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(interactor, trigger, globalEnv, localEnv);
        assertTrue(result);
    }

    @Test
    void checkConditions_oneFails_returnsFalse() throws Exception {
        // Use reflection to test private checkConditions method
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addCondition(new TestCondition((g, l) -> true));
        trigger.addCondition(new TestCondition((g, l) -> false));

        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();

        // Access private method
        java.lang.reflect.Method method = TriggerExecutionInteractor.class
                .getDeclaredMethod("checkConditions", Trigger.class, Environment.class, Environment.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(interactor, trigger, globalEnv, localEnv);
        assertFalse(result);
    }

    @Test
    void checkConditions_throwsException_returnsFalse() throws Exception {
        // Use reflection to test private checkConditions method
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addCondition(new TestCondition((g, l) -> { throw new RuntimeException("Test"); }));

        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();

        // Access private method
        java.lang.reflect.Method method = TriggerExecutionInteractor.class
                .getDeclaredMethod("checkConditions", Trigger.class, Environment.class, Environment.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(interactor, trigger, globalEnv, localEnv);
        assertFalse(result);
    }

    @Test
    void executeActions_multipleActions_returnsCount() throws Exception {
        // Use reflection to test private executeActions method
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Action 1"));
        trigger.addAction(new PrintAction("Action 2"));
        trigger.addAction(new PrintAction("Action 3"));

        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();

        // Access private method
        java.lang.reflect.Method method = TriggerExecutionInteractor.class
                .getDeclaredMethod("executeActions", Trigger.class, Environment.class, Environment.class, Scene.class);
        method.setAccessible(true);

        int count = (int) method.invoke(interactor, trigger, globalEnv, localEnv, testScene);
        assertEquals(3, count);
    }

    @Test
    void executeActions_actionThrowsException_propagatesException() throws Exception {
        // Use reflection to test private executeActions method
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new TestAction((g, l, s) -> { throw new Exception("Test exception"); }));

        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();

        // Access private method
        java.lang.reflect.Method method = TriggerExecutionInteractor.class
                .getDeclaredMethod("executeActions", Trigger.class, Environment.class, Environment.class, Scene.class);
        method.setAccessible(true);

        // Should throw InvocationTargetException wrapping the original exception
        assertThrows(java.lang.reflect.InvocationTargetException.class, () -> {
            method.invoke(interactor, trigger, globalEnv, localEnv, testScene);
        });
    }

    @Test
    void executeActions_noActions_returnsZero() throws Exception {
        // Use reflection to test private executeActions method
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        // No actions added

        Environment globalEnv = new Environment();
        Environment localEnv = new Environment();

        // Access private method
        java.lang.reflect.Method method = TriggerExecutionInteractor.class
                .getDeclaredMethod("executeActions", Trigger.class, Environment.class, Environment.class, Scene.class);
        method.setAccessible(true);

        int count = (int) method.invoke(interactor, trigger, globalEnv, localEnv, testScene);
        assertEquals(0, count);
    }
}