package interface_adapter.variable;

import java.util.List;
import use_case.variable.UpdateVariableOutputBoundary;
import use_case.variable.UpdateVariableOutputData;

public class GlobalVariableUpdatePresenter implements UpdateVariableOutputBoundary {

    private final GlobalVariableViewModel viewModel;

    public GlobalVariableUpdatePresenter(GlobalVariableViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(UpdateVariableOutputData data) {
        // Only handle global variables
        if (!data.isGlobal()) {
            // This shouldn't happen, but ignore if it does
            return;
        }

        VariableState state = viewModel.getState();
        List<VariableState.VariableRow> rows = state.getVariables();

        // Find existing row with same (name, type, global=true)
        VariableState.VariableRow target = null;
        for (VariableState.VariableRow row : rows) {
            if (row.getName().equals(data.getName())
                    && row.getType().equals(data.getType())
                    && row.isGlobal()) {  // Only global variables
                target = row;
                break;
            }
        }

        if (target == null) {
            // Add new global variable
            rows.add(new VariableState.VariableRow(
                    data.getName(),
                    data.getType(),
                    true,  // isGlobal = true
                    data.getValue()
            ));
        } else {
            // Update existing global variable
            target.setType(data.getType());
            target.setGlobal(true);
            target.setValue(data.getValue());
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