package interface_adapter.runtime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import use_case.runtime.TriggerExecutionOutputData;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for TriggerExecutionPresenter.
 *
 * @author Wanru Cheng
 */
class TriggerExecutionPresenterTest {

    private TriggerExecutionPresenter presenter;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;
    private PrintStream originalOut;
    private PrintStream originalErr;

    @BeforeEach
    void setUp() {
        presenter = new TriggerExecutionPresenter();

        // Capture System.out and System.err
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        originalOut = System.out;
        originalErr = System.err;
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        // Restore original streams
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void presentSuccess_printsExecutionInfo() {
        // Arrange
        TriggerExecutionOutputData outputData = new TriggerExecutionOutputData(
                "TestObject",
                "OnClickEvent",
                true,
                3
        );

        // Act
        presenter.presentSuccess(outputData);

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("Executed"));
        assertTrue(output.contains("OnClickEvent"));
        assertTrue(output.contains("TestObject"));
        assertTrue(output.contains("3"));
    }

    @Test
    void presentError_printsErrorMessage() {
        // Arrange
        String errorMessage = "Test error message";

        // Act
        presenter.presentError(errorMessage);

        // Assert
        String output = errContent.toString();
        assertTrue(output.contains("Trigger error"));
        assertTrue(output.contains("Test error message"));
    }

    @Test
    void presentConditionsNotMet_doesNotPrintAnything() {
        // Arrange
        TriggerExecutionOutputData outputData = new TriggerExecutionOutputData(
                "TestObject",
                "OnClickEvent",
                false,
                0
        );

        // Act
        presenter.presentConditionsNotMet(outputData);

        // Assert - should be silent
        assertEquals("", outContent.toString());
        assertEquals("", errContent.toString());
    }

    @Test
    void presentSuccess_withZeroActions_printsCorrectly() {
        // Arrange
        TriggerExecutionOutputData outputData = new TriggerExecutionOutputData(
                "EmptyObject",
                "OnKeyPressEvent",
                true,
                0
        );

        // Act
        presenter.presentSuccess(outputData);

        // Assert
        String output = outContent.toString();
        assertTrue(output.contains("0 actions"));
    }
}