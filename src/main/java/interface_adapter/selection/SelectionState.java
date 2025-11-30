package interface_adapter.selection;

public class SelectionState {

    private String selectedGameObjectId;
    private String selectedGameObjectName;

    private boolean hasTransform;
    private boolean hasEnvironment;
    private boolean hasTriggerManager;

    public SelectionState() {
        clear();
    }


    public String getSelectedGameObjectId() {
        return selectedGameObjectId;
    }

    public void setSelectedGameObjectId(String selectedGameObjectId) {
        this.selectedGameObjectId = selectedGameObjectId;
    }

    public String getSelectedGameObjectName() {
        return selectedGameObjectName;
    }

    public void setSelectedGameObjectName(String selectedGameObjectName) {
        this.selectedGameObjectName = selectedGameObjectName;
    }


    public String getSelectedObjectId() {
        return selectedGameObjectId;
    }

    public void setSelectedObjectId(String selectedObjectId) {
        this.selectedGameObjectId = selectedObjectId;
    }

    public String getSelectedObjectName() {
        return selectedGameObjectName;
    }

    public void setSelectedObjectName(String selectedObjectName) {
        this.selectedGameObjectName = selectedObjectName;
    }


    public boolean hasTransform() {
        return hasTransform;
    }

    public void setHasTransform(boolean hasTransform) {
        this.hasTransform = hasTransform;
    }

    public boolean hasEnvironment() {
        return hasEnvironment;
    }

    public void setHasEnvironment(boolean hasEnvironment) {
        this.hasEnvironment = hasEnvironment;
    }

    public boolean hasTriggerManager() {
        return hasTriggerManager;
    }

    public void setHasTriggerManager(boolean hasTriggerManager) {
        this.hasTriggerManager = hasTriggerManager;
    }


    public boolean hasSelection() {
        return selectedGameObjectId != null;
    }

    public void clear() {
        this.selectedGameObjectId = null;
        this.selectedGameObjectName = null;
        this.hasTransform = false;
        this.hasEnvironment = false;
        this.hasTriggerManager = false;
    }
}
