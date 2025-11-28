package use_case.variable.factory;

/**
 * How the UI should ask the user for a raw value string for this type.
 * This is UI-agnostic: just a hint the view can interpret.
 */
public enum ValuePromptKind {
    TEXT,      // generic text field / input dialog
    BOOLEAN    // yes/no choice
    // (in the future could add: INTEGER_ONLY, DROPDOWN, COLOR_PICKER, etc.)
}
