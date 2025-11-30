package interface_adapter.variable.get;

import interface_adapter.variable.LocalVariableViewModel;
import interface_adapter.variable.VariableState;
import use_case.variable.get.GetAllVariablesOutputBoundary;
import use_case.variable.get.GetAllVariablesOutputData;


public class GetAllLocalVariablesPresenter implements GetAllVariablesOutputBoundary {

    private final LocalVariableViewModel viewModel;

    public GetAllLocalVariablesPresenter(LocalVariableViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void presentVariables(GetAllVariablesOutputData outputData) {
        VariableState state = viewModel.getState();

        state.getVariables().clear();

        for (GetAllVariablesOutputData.VariableData varData : outputData.getVariables()) {
            state.getVariables().add(new VariableState.VariableRow(
                    varData.getName(),
                    varData.getType(),
                    varData.isGlobal(),
                    varData.getValue()
            ));
        }

        viewModel.firePropertyChange();
    }
}