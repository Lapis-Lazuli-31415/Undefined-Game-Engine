package interface_adapter.saving;

public class SaveProjectState {
    private String message = "";
    private String error = null;

    public SaveProjectState(SaveProjectState copy) {
        this.message = copy.message;
        this.error = copy.error;
    }

    public SaveProjectState() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}