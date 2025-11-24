package use_case.preview;

/**
 * Input Boundary (Input Port) for preview use case.
 * Part of Use Case layer (pink ring in CA diagram).
 * Defines what the use case can do.
 *
 * @author Wanru Cheng
 */
public interface PreviewInputBoundary {

    /**
     * Execute preview use case.
     *
     * @param inputData Input data containing scene
     */
    void execute(PreviewInputData inputData);

    /**
     * Stop preview.
     */
    void stop();
}