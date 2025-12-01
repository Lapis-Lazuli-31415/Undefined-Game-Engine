package use_case.preview;

/**
 * Output Boundary (Output Port) for preview use case.
 * Part of Use Case layer (pink ring in CA diagram).
 * Defines how use case communicates results.
 * Implemented by Presenter in Interface Adapter layer.
 *
 * @author Wanru Cheng
 */
public interface PreviewOutputBoundary {

    /**
     * Present successful preview.
     *
     * @param outputData Output data
     */
    void presentSuccess(PreviewOutputData outputData);

    /**
     * Present error.
     *
     * @param errorMessage Error message
     */
    void presentError(String errorMessage);

    /**
     * Present warning.
     *
     * @param warningMessage Warning message
     * @param outputData Output data
     */
    void presentWarning(String warningMessage, PreviewOutputData outputData);
}