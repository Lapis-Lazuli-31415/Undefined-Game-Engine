package use_case.runtime;

import entity.Scene;
import entity.GameObject;
import entity.scripting.Trigger;
import entity.scripting.action.Action;
import entity.scripting.condition.Condition;
import entity.scripting.environment.Environment;

/**
 * Interactor for trigger execution use case.
 * Part of Use Case layer (pink ring in CA diagram).
 *
 * Contains the business logic for executing triggers:
 * 1. Check all conditions
 * 2. Execute all actions if conditions are met
 *
 * @author Wanru Cheng
 */
public class TriggerExecutionInteractor implements TriggerExecutionInputBoundary {

    private final TriggerExecutionOutputBoundary outputBoundary;

    /**
     * Constructor.
     *
     * @param outputBoundary The output boundary (presenter)
     */
    public TriggerExecutionInteractor(TriggerExecutionOutputBoundary outputBoundary) {
        this.outputBoundary = outputBoundary;
    }

    @Override
    public void execute(TriggerExecutionInputData inputData) {
        Trigger trigger = inputData.getTrigger();
        GameObject obj = inputData.getGameObject();
        Environment globalEnv = inputData.getGlobalEnvironment();
        Scene scene = inputData.getScene();  // ✅ 从 inputData 获取 scene

        try {
            // Get local environment from GameObject
            Environment localEnv = (obj != null) ? obj.getEnvironment() : new Environment();
            if (localEnv == null) {
                localEnv = new Environment();
            }

            // Check all conditions
            boolean allConditionsMet = checkConditions(trigger, globalEnv, localEnv);

            String objName = (obj != null) ? obj.getName() : "Unknown";
            String eventName = trigger.getEvent().getClass().getSimpleName();

            if (allConditionsMet) {
                // Execute all actions
                int actionsExecuted = executeActions(trigger, globalEnv, localEnv, scene);  // ✅ 传递 scene

                TriggerExecutionOutputData outputData = new TriggerExecutionOutputData(
                        objName,
                        eventName,
                        true,
                        actionsExecuted
                );
                outputBoundary.presentSuccess(outputData);
            } else {
                // Conditions not met
                TriggerExecutionOutputData outputData = new TriggerExecutionOutputData(
                        objName,
                        eventName,
                        false,
                        0
                );
                outputBoundary.presentConditionsNotMet(outputData);
            }

        } catch (Exception e) {
            outputBoundary.presentError("Error executing trigger: " + e.getMessage());
        }
    }

    /**
     * Check all conditions for a trigger.
     *
     * @param trigger The trigger
     * @param globalEnv Global environment
     * @param localEnv Local environment
     * @return true if all conditions are met
     */
    private boolean checkConditions(Trigger trigger, Environment globalEnv, Environment localEnv) {
        for (Condition condition : trigger.getConditions()) {
            try {
                if (!condition.evaluate(globalEnv, localEnv)) {
                    return false;
                }
            } catch (Exception e) {
                System.err.println("Error evaluating condition: " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    /**
     * Execute all actions for a trigger.
     *
     * @param trigger The trigger
     * @param globalEnv Global environment
     * @param localEnv Local environment
     * @param scene The current scene  ✅ 添加 scene 参数
     * @return Number of actions executed
     */
    private int executeActions(Trigger trigger, Environment globalEnv, Environment localEnv, Scene scene) throws Exception {
        int count = 0;
        for (Action action : trigger.getActions()) {
            action.execute(globalEnv, localEnv, scene);  // ✅ 传递 scene
            count++;
        }
        return count;
    }
}