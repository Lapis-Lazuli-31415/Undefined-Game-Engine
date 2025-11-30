package interface_adapter.variable;

import java.util.List;
import use_case.variable.DeleteVariableOutputBoundary;
import use_case.variable.DeleteVariableOutputData;

public class LocalVariableDeletePresenter implements DeleteVariableOutputBoundary {

    private final LocalVariableViewModel viewModel;

    public LocalVariableDeletePresenter(LocalVariableViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(DeleteVariableOutputData data) {
        // Only handle local variables
        if (data.isGlobal()) {
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
                    && !row.isGlobal()) {  // Only local variables
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