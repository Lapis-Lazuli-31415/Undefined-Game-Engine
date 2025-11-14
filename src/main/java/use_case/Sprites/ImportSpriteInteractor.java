package use_case.Sprites;

import entity.AssetLib;
import entity.AssetRepository;

public class ImportSpriteInteractor {

    private final AssetRepository assetRepo;
    private final AssetLib assetLib;;

    public ImportSpriteInteractor(AssetRepository repo, AssetLib library) {
        this.assetRepo = repo;
        this.assetLib = library;
    }

//    @Override
    public void execute()  {
        // 1. validate extension
        // 2. validate file type
        // 3. call assetRepo.saveImage()
        // 4. add to assetLibrary
        // 5. return response model


    }

}
