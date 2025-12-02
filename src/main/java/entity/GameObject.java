package entity;

import entity.scripting.TriggerManager;
import entity.scripting.environment.Environment;

import java.util.ArrayList;
// import entity.Property;
import java.util.List;


/**
 * GameObject is the basic entity in the scene.
 * It has an id, name, active flag, properties, and belongs to an Environment.
 *
 * No UI / JavaFX imports here.
 */

public class GameObject {

    private final String id;
    private String name;
    private boolean active;
    private Environment environments;
    private Transform transform;
    private SpriteRenderer spriteRenderer;
    private TriggerManager triggerManager;

    @com.fasterxml.jackson.annotation.JsonCreator
    public GameObject(@com.fasterxml.jackson.annotation.JsonProperty("id") String id,
                      @com.fasterxml.jackson.annotation.JsonProperty("name") String name,
                      @com.fasterxml.jackson.annotation.JsonProperty("active") boolean active,
                      @com.fasterxml.jackson.annotation.JsonProperty("environment") Environment environments,
                      @com.fasterxml.jackson.annotation.JsonProperty("transform") Transform transform,
                      @com.fasterxml.jackson.annotation.JsonProperty("sprite_renderer") SpriteRenderer spriteRenderer,
                      @com.fasterxml.jackson.annotation.JsonProperty("trigger_manager") TriggerManager triggerManager) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.environments = environments;
        this.transform = transform;
        this.spriteRenderer = spriteRenderer;
        this.triggerManager = triggerManager;
    }

    // This constructor is for testing
    public GameObject(String id) {
        this.id = id;
        this.name = id;
        this.active = true;
        this.environments = new Environment();
        this.transform = new Transform(0, 0, 0, 1, 1);
        this.spriteRenderer = new SpriteRenderer(null, true);
        this.triggerManager = new TriggerManager();
    }

    // --- Basic getters/setters ---

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Environment getEnvironment() {
        return environments;
    }

    public void setEnvironment(Environment environment) {
        this.environments = environment;
    }

    public Transform getTransform() {
        return transform;
    }

    public void setTransform(Transform transform) {
        this.transform = transform;
    }


    public TriggerManager getTriggerManager() {
        return triggerManager;
    }

    public void setTriggerManager(TriggerManager triggerManager) {
        this.triggerManager = triggerManager;
    }
    public SpriteRenderer getSpriteRenderer() {
        return spriteRenderer;
    }

    public void setSpriteRenderer(SpriteRenderer spriteRenderer) {
        this.spriteRenderer = spriteRenderer;
    }

// --- Copy method for preview isolation ---

    /**
     * Create a deep copy of this GameObject.
     *
     * @return A new GameObject with copied state
     */
    public GameObject copy() {

        // Copy transform
        Transform copiedTransform = (this.transform != null) ? this.transform.copy() : null;

        // Copy environment
        Environment copiedEnvironment = (this.environments != null) ? this.environments.copy() : new Environment();
        SpriteRenderer copiedSpriteRenderer = (this.spriteRenderer != null) ? this.spriteRenderer.copy() : null;
        // Copy trigger manager
        TriggerManager copiedTriggerManager = (this.triggerManager != null)
                ? this.triggerManager.copy()
                : new TriggerManager();

        return new GameObject(
                this.id,
                this.name,
                this.active,
                copiedEnvironment,        // Environment
                copiedTransform,          // Transform
                copiedSpriteRenderer,     // SpriteRenderer
                copiedTriggerManager      // TriggerManager

        );
    }

}
