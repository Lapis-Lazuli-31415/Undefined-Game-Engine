package interface_adapter.preview;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.beans.PropertyChangeListener;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for PreviewViewModel.
 *
 * @author Wanru Cheng
 */
class PreviewViewModelTest {

    private PreviewViewModel viewModel;

    @BeforeEach
    void setUp() {
        viewModel = new PreviewViewModel();
    }

    @Test
    void getState_initialState_returnsNonNull() {
        // Assert
        assertNotNull(viewModel.getState());
    }

    @Test
    void setState_updatesState() {
        // Arrange
        PreviewState newState = new PreviewState();
        newState.setError("Test error");

        // Act
        viewModel.setState(newState);

        // Assert
        assertEquals(newState, viewModel.getState());
        assertEquals("Test error", viewModel.getState().getError());
    }

    @Test
    void firePropertyChanged_notifiesListeners() {
        // Arrange
        final boolean[] listenerCalled = {false};
        PropertyChangeListener listener = evt -> listenerCalled[0] = true;
        viewModel.addPropertyChangeListener(listener);

        // Act
        viewModel.firePropertyChanged();

        // Assert
        assertTrue(listenerCalled[0]);
    }

    @Test
    void addPropertyChangeListener_addsListener() {
        // Arrange
        final int[] callCount = {0};
        PropertyChangeListener listener = evt -> callCount[0]++;

        // Act
        viewModel.addPropertyChangeListener(listener);
        viewModel.firePropertyChanged();
        viewModel.firePropertyChanged();

        // Assert
        assertEquals(2, callCount[0]);
    }

    @Test
    void setState_withNull_storesNull() {
        // Act
        viewModel.setState(null);

        // Assert
        assertNull(viewModel.getState());
    }
}