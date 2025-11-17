package entity.scripting.environment;

import entity.scripting.error.EnvironmentException;

/**
 * EnvironmentHelper - Helper methods for common Environment operations
 *
 * Makes it easier to work with the Environment's set/get methods
 *
 * @author Wanru Cheng
 */
public class EnvironmentHelper {

    // Common variable types
    public static final String GLOBAL_TYPE = "global";
    public static final String PLAYER_TYPE = "player";
    public static final String GAME_TYPE = "game";

    /**
     * Set an integer variable
     */
    public static void setInt(Environment env, String variableType, String name, int value) {
        try {
            env.set(variableType, name, Integer.class, value);
        } catch (EnvironmentException e) {
            System.err.println("Error setting int variable: " + e.getMessage());
        }
    }

    /**
     * Get an integer variable
     */
    public static int getInt(Environment env, String variableType, String name, int defaultValue) {
        try {
            return env.get(variableType, name, Integer.class);
        } catch (EnvironmentException e) {
            return defaultValue;
        }
    }

    /**
     * Set a boolean variable
     */
    public static void setBoolean(Environment env, String variableType, String name, boolean value) {
        try {
            env.set(variableType, name, Boolean.class, value);
        } catch (EnvironmentException e) {
            System.err.println("Error setting boolean variable: " + e.getMessage());
        }
    }

    /**
     * Get a boolean variable
     */
    public static boolean getBoolean(Environment env, String variableType, String name, boolean defaultValue) {
        try {
            return env.get(variableType, name, Boolean.class);
        } catch (EnvironmentException e) {
            return defaultValue;
        }
    }

    /**
     * Set a string variable
     */
    public static void setString(Environment env, String variableType, String name, String value) {
        try {
            env.set(variableType, name, String.class, value);
        } catch (EnvironmentException e) {
            System.err.println("Error setting string variable: " + e.getMessage());
        }
    }

    /**
     * Get a string variable
     */
    public static String getString(Environment env, String variableType, String name, String defaultValue) {
        try {
            return env.get(variableType, name, String.class);
        } catch (EnvironmentException e) {
            return defaultValue;
        }
    }

    /**
     * Set a double variable
     */
    public static void setDouble(Environment env, String variableType, String name, double value) {
        try {
            env.set(variableType, name, Double.class, value);
        } catch (EnvironmentException e) {
            System.err.println("Error setting double variable: " + e.getMessage());
        }
    }

    /**
     * Get a double variable
     */
    public static double getDouble(Environment env, String variableType, String name, double defaultValue) {
        try {
            return env.get(variableType, name, Double.class);
        } catch (EnvironmentException e) {
            return defaultValue;
        }
    }
}