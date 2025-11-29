package interface_adapter.variable;
//
//import java.util.List;
//
//import use_case.variable.UpdateVariableOutputBoundary;
//import use_case.variable.UpdateVariableOutputData;
//
//public class UpdateVariablePresenter implements UpdateVariableOutputBoundary {
//
//    private final GlobalVariableViewModel viewModel;
//
//    public UpdateVariablePresenter(GlobalVariableViewModel viewModel) {
//        this.viewModel = viewModel;
//    }
//
//    @Override
//    public void prepareSuccessView(UpdateVariableOutputData data) {
//        VariableState state = viewModel.getState();
//        List<VariableState.VariableRow> rows = state.getVariables();
//
//        // Find existing row with same (name, type, global)
//        VariableState.VariableRow target = null;
//        for (VariableState.VariableRow row : rows) {
//            if (row.getName().equals(data.getName())
//                    && row.getType().equals(data.getType())
//                    && row.isGlobal() == data.isGlobal()) {
//                target = row;
//                break;
//            }
//        }
//
//        if (target == null) {
//            rows.add(new VariableState.VariableRow(
//                    data.getName(),
//                    data.getType(),
//                    data.isGlobal(),
//                    data.getValue()
//            ));
//        } else {
//            target.setType(data.getType());
//            target.setGlobal(data.isGlobal());
//            target.setValue(data.getValue());
//        }
//
//        state.setErrorMessage(null);
//        viewModel.firePropertyChange();
//    }
//
//
//    @Override
//    public void prepareFailureView(String errorMessage) {
//        VariableState state = viewModel.getState();
//        state.setErrorMessage(errorMessage);
//        viewModel.firePropertyChange();
//    }
//}
