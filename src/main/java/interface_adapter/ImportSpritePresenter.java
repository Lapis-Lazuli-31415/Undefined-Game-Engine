package interface_adapter;

import use_case.Sprites.ImportSpriteResponse;
import use_case.Sprites.SpriteOutputBoundary;

/**
 * Presenter for the Import Sprite use case.
 * This class implements the Output Boundary and is responsible for
 * formatting the response data for the view layer.
 */
public class ImportSpritePresenter implements SpriteOutputBoundary {

    private final ImportSpriteViewModel viewModel;

    public ImportSpritePresenter(ImportSpriteViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ImportSpriteResponse response) {
        // Format the response for the view
        ImportSpriteState state = new ImportSpriteState();
        state.setSuccess(true);
        state.setMessage(response.message);
        state.setSpriteName(response.importedSprite.getName());
        state.setSpriteId(response.importedSprite.getId().toString());

        // Update the view model
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // Format the error for the view
        ImportSpriteState state = new ImportSpriteState();
        state.setSuccess(false);
        state.setMessage(errorMessage);

        // Update the view model
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}