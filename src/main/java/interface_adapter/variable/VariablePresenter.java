package interface_adapter.variable;

import java.util.Iterator;
import java.util.List;

import use_case.variable.UpdateVariableOutputBoundary;
import use_case.variable.UpdateVariableOutputData;
import use_case.variable.DeleteVariableOutputBoundary;
import use_case.variable.DeleteVariableOutputData;

public class VariablePresenter implements UpdateVariableOutputBoundary, DeleteVariableOutputBoundary {

    private final VariableViewModel viewModel;

    public VariablePresenter(VariableViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(UpdateVariableOutputData data) {
        VariableState state = viewModel.getState();
        List<VariableState.VariableRow> rows = state.getVariables();

        // Find existing row with same (name, type, global)
        VariableState.VariableRow target = null;
        for (VariableState.VariableRow row : rows) {
            if (row.getName().equals(data.getName())
                    && row.getType().equals(data.getType())
                    && row.isGlobal() == data.isGlobal()) {
                target = row;
                break;
            }
        }

        if (target == null) {
            rows.add(new VariableState.VariableRow(
                    data.getName(),
                    data.getType(),
                    data.isGlobal(),
                    data.getValue()
            ));
        } else {
            target.setType(data.getType());
            target.setGlobal(data.isGlobal());
            target.setValue(data.getValue());
        }

        state.setErrorMessage(null);
        viewModel.firePropertyChange();
    }


    @Override
    public void prepareSuccessView(DeleteVariableOutputData data) {
        VariableState state = viewModel.getState();
        List<VariableState.VariableRow> rows = state.getVariables();

        int indexToRemove = -1;
        for (int i = 0; i < rows.size(); i++) {
            VariableState.VariableRow row = rows.get(i);
            if (row.getName().equals(data.getName())
                    && row.getType().equals(data.getType())
                    && row.isGlobal() == data.isGlobal()) {
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
