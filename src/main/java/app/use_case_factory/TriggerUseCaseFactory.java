package app.use_case_factory;

import entity.scripting.action.ActionFactory;
import entity.scripting.action.DefaultActionFactory;
import entity.scripting.condition.ConditionFactory;
import entity.scripting.condition.DefaultConditionFactory;
import entity.scripting.event.DefaultEventFactory;
import entity.scripting.event.EventFactory;
import interface_adapter.trigger.TriggerManagerViewModel;
import interface_adapter.trigger.action.change.ActionChangeController;
import interface_adapter.trigger.action.change.ActionChangePresenter;
import interface_adapter.trigger.action.create.ActionCreateController;
import interface_adapter.trigger.action.create.ActionCreatePresenter;
import interface_adapter.trigger.action.delete.ActionDeleteController;
import interface_adapter.trigger.action.delete.ActionDeletePresenter;
import interface_adapter.trigger.condition.change.ConditionChangeController;
import interface_adapter.trigger.condition.change.ConditionChangePresenter;
import interface_adapter.trigger.condition.create.ConditionCreateController;
import interface_adapter.trigger.condition.create.ConditionCreatePresenter;
import interface_adapter.trigger.condition.delete.ConditionDeleteController;
import interface_adapter.trigger.condition.delete.ConditionDeletePresenter;
import interface_adapter.trigger.create.TriggerCreateController;
import interface_adapter.trigger.create.TriggerCreatePresenter;
import interface_adapter.trigger.delete.TriggerDeleteController;
import interface_adapter.trigger.delete.TriggerDeletePresenter;
import interface_adapter.trigger.event.change.EventChangeController;
import interface_adapter.trigger.event.change.EventChangePresenter;
import interface_adapter.trigger.event.parameter_change.EventParameterChangeController;
import interface_adapter.trigger.event.parameter_change.EventParameterChangePresenter;
import use_case.trigger.action.change.ActionChangeInteractor;
import use_case.trigger.action.create.ActionCreateInteractor;
import use_case.trigger.action.delete.ActionDeleteInteractor;
import use_case.trigger.condition.change.ConditionChangeInteractor;
import use_case.trigger.condition.create.ConditionCreateInteractor;
import use_case.trigger.condition.delete.ConditionDeleteInteractor;
import use_case.trigger.create.TriggerCreateInteractor;
import use_case.trigger.delete.TriggerDeleteInteractor;
import use_case.trigger.event.change.EventChangeInteractor;
import use_case.trigger.event.parameter_change.EventParameterChangeInteractor;

public class TriggerUseCaseFactory {

    // Shared ViewModels
    private final TriggerManagerViewModel triggerManagerViewModel;

    // Shared Factories
    private final EventFactory eventFactory;
    private final ConditionFactory conditionFactory;
    private final ActionFactory actionFactory;

    public TriggerUseCaseFactory(TriggerManagerViewModel triggerManagerViewModel) {
        this.triggerManagerViewModel = triggerManagerViewModel;

        this.eventFactory = new DefaultEventFactory();
        this.conditionFactory = new DefaultConditionFactory();
        this.actionFactory = new DefaultActionFactory();
    }

    // Trigger Use Cases

    public TriggerCreateController createTriggerCreateController() {
        TriggerCreatePresenter presenter =
                new TriggerCreatePresenter(triggerManagerViewModel);

        TriggerCreateInteractor interactor =
                new TriggerCreateInteractor(presenter);

        return new TriggerCreateController(interactor);
    }

    public TriggerDeleteController createTriggerDeleteController() {
        TriggerDeletePresenter presenter =
                new TriggerDeletePresenter(triggerManagerViewModel);

        TriggerDeleteInteractor interactor =
                new TriggerDeleteInteractor(presenter);

        return new TriggerDeleteController(interactor);
    }


    // Event Use Cases

    public EventChangeController createEventChangeController() {
        EventChangePresenter presenter =
                new EventChangePresenter(triggerManagerViewModel);

        EventChangeInteractor interactor =
                new EventChangeInteractor(presenter, eventFactory);

        return new EventChangeController(interactor);
    }

    public EventParameterChangeController createEventParameterChangeController() {
        EventParameterChangePresenter presenter =
                new EventParameterChangePresenter(triggerManagerViewModel);

        EventParameterChangeInteractor interactor =
                new EventParameterChangeInteractor(presenter);

        return new EventParameterChangeController(interactor);
    }


    // Condition Use Case

    public ConditionCreateController createConditionCreateController() {
        ConditionCreatePresenter presenter =
                new ConditionCreatePresenter(triggerManagerViewModel);

        ConditionCreateInteractor interactor =
                new ConditionCreateInteractor(presenter);

        return new ConditionCreateController(interactor);
    }

    public ConditionDeleteController createConditionDeleteController() {
        ConditionDeletePresenter presenter =
                new ConditionDeletePresenter(triggerManagerViewModel);

        ConditionDeleteInteractor interactor =
                new ConditionDeleteInteractor(presenter);

        return new ConditionDeleteController(interactor);
    }

    public ConditionChangeController createConditionChangeController() {
        ConditionChangePresenter presenter =
                new ConditionChangePresenter(triggerManagerViewModel);

        ConditionChangeInteractor interactor =
                new ConditionChangeInteractor(presenter, conditionFactory);

        return new ConditionChangeController(interactor);
    }


    // Action Use Cases

    public ActionCreateController createActionCreateController() {
        ActionCreatePresenter presenter =
                new ActionCreatePresenter(triggerManagerViewModel);

        ActionCreateInteractor interactor =
                new ActionCreateInteractor(presenter);

        return new ActionCreateController(interactor);
    }

    public ActionDeleteController createActionDeleteController() {
        ActionDeletePresenter presenter =
                new ActionDeletePresenter(triggerManagerViewModel);

        ActionDeleteInteractor interactor =
                new ActionDeleteInteractor(presenter);

        return new ActionDeleteController(interactor);
    }

    public ActionChangeController createActionChangeController() {
        ActionChangePresenter presenter =
                new ActionChangePresenter(triggerManagerViewModel);

        ActionChangeInteractor interactor =
                new ActionChangeInteractor(presenter, actionFactory);

        return new ActionChangeController(interactor);
    }
}