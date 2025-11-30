package use_case.saving;

public class SaveProjectOutputData {
    String projectName;
    String message;
    boolean successful;

    /**
     * Constructs a new SaveProjectOutputData object.
     *
     * @param projectName The name of the project involved in the use case.
     * @param message     A text message describing the result (e.g., "Saved successfully" or an error message).
     * @param successful  A boolean flag indicating if the save operation succeeded (true) or failed (false).
     */
    public SaveProjectOutputData(String projectName, String message, boolean successful) {
        this.projectName = projectName;
        this.message = message;
        this.successful = successful;
    }

    public String getProjectName() {return projectName;}

    public String getMessage() {
        return message;
    }

    /**
     * Checks if the use case was successful.
     *
     * @return true if the project was saved successfully; false otherwise.
     */
    public boolean isSuccessful() {
        return successful;
    }
}
