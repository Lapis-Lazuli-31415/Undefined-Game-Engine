package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Generic base ViewModel with:
 * - viewName: name for this view
 * - state: the data object
 * - PropertyChangeSupport for Observer pattern
 */
public abstract class ViewModel<T> {

    private final String viewName;
    protected final PropertyChangeSupport support;
    protected T state;

    protected ViewModel(String viewName) {
        this.viewName = viewName;
        this.support = new PropertyChangeSupport(this);
    }

    public String getViewName() {
        return viewName;
    }

    public T getState() {
        return state;
    }


    public void setState(T state) {
        this.state = state;
        firePropertyChanged();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        support.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        support.removePropertyChangeListener(listener);
    }

     // Notify observers that the state has changed.

    protected void firePropertyChanged() {
        support.firePropertyChange("state", null, this.state);
    }
}
