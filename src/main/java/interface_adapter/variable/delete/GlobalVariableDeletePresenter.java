package interface_adapter.variable.delete;

import java.util.List;

import interface_adapter.variable.GlobalVariableViewModel;
import interface_adapter.variable.VariableState;
import use_case.variable.delete.DeleteVariableOutputBoundary;
import use_case.variable.delete.DeleteVariableOutputData;

public class GlobalVariableDeletePresenter implements DeleteVariableOutputBoundary {

    private final GlobalVariableViewModel viewModel;

    public GlobalVariableDeletePresenter(GlobalVariableViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(DeleteVariableOutputData data) {
        // Only handle global variables
        if (!data.isGlobal()) {
            // This shouldn't happen, but ignore if it does
            return;
        }

        VariableState state = viewModel.getState();
        List<VariableState.VariableRow> rows = state.getVariables();

        int indexToRemove = -1;
        for (int i = 0; i < rows.size(); i++) {
            VariableState.VariableRow row = rows.get(i);
            if (row.getName().equals(data.getName())
                    && row.getType().equals(data.getType())
                    && row.isGlobal()) {  // Only global variables
                indexToRemove = i;
                break;
            }
        }

        if (indexToRemove != -1) {
            rows.remove(indexToRemove);
        }

        state.setErrorMessage(null);
        viewModel.firePropertyChange();
    }

    @Override
    public void prepareFailureView(String errorMessage) {
        VariableState state = viewModel.getState();
        state.setErrorMessage(errorMessage);
        viewModel.firePropertyChange();
    }
}