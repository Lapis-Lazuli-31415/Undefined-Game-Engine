package interface_adapter.Sprites;

import use_case.Sprites.ImportSpriteResponse;
import use_case.Sprites.SpriteOutputBoundary;

/**
 * The Presenter for import sprite from Unsplash.
 * Formats the response from the use case for the view.
 */
public class ImportSpriteFromUnsplashPresenter implements SpriteOutputBoundary {

    private final ImportSpriteFromUnsplashViewModel viewModel;

    public ImportSpriteFromUnsplashPresenter(ImportSpriteFromUnsplashViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void prepareSuccessView(ImportSpriteResponse response) {
        // Format the response for the view
        ImportSpriteFromUnsplashState state = new ImportSpriteFromUnsplashState();
        state.setSuccess(true);
        state.setMessage(response.message);
        state.setSpriteName(response.importedSprite.getName());
        state.setSpriteId(response.importedSprite.getId().toString());
        state.setLoading(false);

        // Clear the search fields after successful import
        state.setSearchQuery("");
        state.setSelectedImageId("");
        state.setTargetFileName("");

        // Update the view model
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String errorMessage) {
        // Format the error for the view
        ImportSpriteFromUnsplashState state = viewModel.getState();
        state.setSuccess(false);
        state.setMessage("Error: " + errorMessage);
        state.setLoading(false);

        // Update the view model
        viewModel.setState(state);
        viewModel.firePropertyChanged();
    }
}

