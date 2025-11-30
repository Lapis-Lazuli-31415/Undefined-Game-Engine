package use_case.runtime;

import entity.GameObject;
import entity.Scene;
import entity.scripting.Trigger;
import entity.scripting.action.Action;
import entity.scripting.condition.Condition;
import entity.scripting.environment.Environment;
import entity.scripting.event.OnClickEvent;
import entity.scripting.event.OnKeyPressEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.UUID;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

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
    void execute_withNullEnvironmentInGameObject_createsNewEnvironment() {
        Trigger trigger = new Trigger(new OnClickEvent(), true);
        trigger.addAction(new PrintAction("Test"));

        GameObject obj = new GameObject("obj-1", "TestObject", true, new ArrayList<>(), null);
        Environment globalEnv = new Environment();
        TriggerExecutionInputData inputData = new TriggerExecutionInputData(trigger, obj, globalEnv, testScene);

        interactor.execute(inputData);
        assertTrue(presenter.successCalled);
    }

    @Test
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
        return new GameObject("obj-1", "TestObject", true, new ArrayList<>(), new Environment());
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

    private static class PrintAction extends entity.scripting.action.Action {
        private final String message;

        public PrintAction(String message) {
            this.message = message;
        }

        @Override
        public void execute(Environment globalEnv, Environment localEnv, Scene scene) {
            System.out.println("PrintAction: " + message);
        }

        @Override
        public String format() {
            return "PrintAction";
        }

        @Override
        public Action parse(String input) {
            return new PrintAction("parsed");
        }
    }

    private static class TestCondition extends entity.scripting.condition.Condition {
        private final BiFunction<Environment, Environment, Boolean> evaluator;

        public TestCondition(BiFunction<Environment, Environment, Boolean> evaluator) {
            this.evaluator = evaluator;
        }

        @Override
        public boolean evaluate(Environment globalEnv, Environment localEnv) {
            return evaluator.apply(globalEnv, localEnv);
        }

        @Override
        public String format() {
            return "TestCondition";
        }

        @Override
        public Condition parse(String input) {
            return new TestCondition((g, l) -> true);
        }
    }

    private static class TestAction extends entity.scripting.action.Action {
        private final TestActionExecutor executor;

        public TestAction(TestActionExecutor executor) {
            this.executor = executor;
        }

        @Override
        public void execute(Environment globalEnv, Environment localEnv, Scene scene) throws Exception {
            executor.execute(globalEnv, localEnv, scene);
        }

        @Override
        public String format() {
            return "TestAction";
        }

        @Override
        public Action parse(String input) {
            return new TestAction((g, l, s) -> {});
        }
    }

    @FunctionalInterface
    private interface TestActionExecutor {
        void execute(Environment globalEnv, Environment localEnv, Scene scene) throws Exception;
    }
}