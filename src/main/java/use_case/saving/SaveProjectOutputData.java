package use_case.saving;

public class SaveProjectOutputData {
    String projectName;
    String message;
    boolean successful = false;


    public SaveProjectOutputData(String projectName, String message, boolean successful) {
        this.projectName = projectName;
        this.message = message;
        this.successful = successful;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccessful() {
        return successful;
    }
}
