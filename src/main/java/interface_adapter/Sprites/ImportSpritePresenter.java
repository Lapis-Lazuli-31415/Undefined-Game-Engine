package interface_adapter.Sprites;

import interface_adapter.assets.AssetLibViewModel;
import use_case.Sprites.Import.DeleteSpriteResponse;
import use_case.Sprites.Import.ImportSpriteResponse;
import use_case.Sprites.Import.SpriteOutputBoundary;

/**
 * Presenter for the Import Sprite use case.
 * This class implements the Output Boundary and is responsible for
 * formatting the response data for the view layer.
 */
public class ImportSpritePresenter implements SpriteOutputBoundary {

    private final ImportSpriteViewModel viewModel;
    private final AssetLibViewModel assetLibViewModel;

    public ImportSpritePresenter(ImportSpriteViewModel viewModel, AssetLibViewModel assetLibViewModel) {
        this.viewModel = viewModel;
        this.assetLibViewModel = assetLibViewModel;
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

        // notify AssetLibViewModel that an asset was added
        assetLibViewModel.notifyAssetAdded(response.importedSprite);
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

    @Override
    public void prepareDeleteFailView(String errorMessage) {
        // TODO: implement
    }

    @Override
    public void prepareDeleteSuccessView(DeleteSpriteResponse response) {
        // TODO: implement
    }
}