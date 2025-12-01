package use_case.runtime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for TriggerExecutionOutputData.
 *
 * @author Wanru Cheng
 */
class TriggerExecutionOutputDataTest {

    @Test
    void constructor_storesAllParameters() {
        // Act
        TriggerExecutionOutputData outputData = new TriggerExecutionOutputData(
                "TestObject",
                "OnClickEvent",
                true,
                5
        );

        // Assert
        assertEquals("TestObject", outputData.getGameObjectName());
        assertEquals("OnClickEvent", outputData.getEventName());
        assertTrue(outputData.isExecuted());
        assertEquals(5, outputData.getActionsExecuted());
    }

    @Test
    void constructor_withNullGameObjectName_storesNull() {
        // Act
        TriggerExecutionOutputData outputData = new TriggerExecutionOutputData(
                null,
                "OnClickEvent",
                true,
                1
        );

        // Assert
        assertNull(outputData.getGameObjectName());
    }

    @Test
    void constructor_withNullEventName_storesNull() {
        // Act
        TriggerExecutionOutputData outputData = new TriggerExecutionOutputData(
                "TestObject",
                null,
                true,
                1
        );

        // Assert
        assertNull(outputData.getEventName());
    }

    @Test
    void constructor_withExecutedFalse_storesFalse() {
        // Act
        TriggerExecutionOutputData outputData = new TriggerExecutionOutputData(
                "TestObject",
                "OnClickEvent",
                false,
                0
        );

        // Assert
        assertFalse(outputData.isExecuted());
    }

    @Test
    void constructor_withZeroActions_storesZero() {
        // Act
        TriggerExecutionOutputData outputData = new TriggerExecutionOutputData(
                "TestObject",
                "OnClickEvent",
                true,
                0
        );

        // Assert
        assertEquals(0, outputData.getActionsExecuted());
    }

    @Test
    void getGameObjectName_returnsCorrectValue() {
        // Arrange
        TriggerExecutionOutputData outputData = new TriggerExecutionOutputData(
                "MyObject",
                "Event",
                true,
                1
        );

        // Assert
        assertEquals("MyObject", outputData.getGameObjectName());
    }

    @Test
    void getEventName_returnsCorrectValue() {
        // Arrange
        TriggerExecutionOutputData outputData = new TriggerExecutionOutputData(
                "Object",
                "MyEvent",
                true,
                1
        );

        // Assert
        assertEquals("MyEvent", outputData.getEventName());
    }

    @Test
    void isExecuted_returnsCorrectValue() {
        // Test true
        TriggerExecutionOutputData outputData1 = new TriggerExecutionOutputData("O", "E", true, 1);
        assertTrue(outputData1.isExecuted());

        // Test false
        TriggerExecutionOutputData outputData2 = new TriggerExecutionOutputData("O", "E", false, 0);
        assertFalse(outputData2.isExecuted());
    }

    @Test
    void getActionsExecuted_returnsCorrectValue() {
        // Arrange
        TriggerExecutionOutputData outputData = new TriggerExecutionOutputData(
                "Object",
                "Event",
                true,
                10
        );

        // Assert
        assertEquals(10, outputData.getActionsExecuted());
    }
}