package interface_adapter.Sprites;

import use_case.Sprites.ImportSpriteResponse;
import use_case.Sprites.SpriteOutputBoundary;

/**
 * The Presenter for import sprite from Unsplash.
 * Formats the response from the use case for the view.
 */
public class ImportSpriteFromUnsplashPresenter implements SpriteOutputBoundary {

    private final ImportSpriteFromUnsplashViewModel viewModel;
    private final interface_adapter.assets.AssetLibViewModel assetLibViewModel;

    public ImportSpriteFromUnsplashPresenter(ImportSpriteFromUnsplashViewModel viewModel,
                                             interface_adapter.assets.AssetLibViewModel assetLibViewModel) {
        this.viewModel = viewModel;
        this.assetLibViewModel = assetLibViewModel;
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

        // Notify AssetLibViewModel so HomeView can update its UI
        // The asset was already added to entity AssetLib by the interactor,
        // but we need to fire the PropertyChange event for views to update
        assetLibViewModel.notifyAssetAdded(response.importedSprite);
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

