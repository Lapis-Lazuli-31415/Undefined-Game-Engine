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
    private ArrayList<Property> properties;
    private Environment environments;
    private Transform transform;
    private SpriteRenderer spriteRenderer;
    private TriggerManager triggerManager;

    public GameObject(String id, String name, boolean active, Environment environment, Transform transform,
                      SpriteRenderer spriteRenderer, TriggerManager triggerManager) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.transform = transform;
        this.spriteRenderer = spriteRenderer;
        this.triggerManager = triggerManager;
        this.environments = environment;
    }

    @com.fasterxml.jackson.annotation.JsonCreator
    public GameObject(@com.fasterxml.jackson.annotation.JsonProperty("id") String id,
                      @com.fasterxml.jackson.annotation.JsonProperty("name") String name,
                      @com.fasterxml.jackson.annotation.JsonProperty("active") boolean active,
                      @com.fasterxml.jackson.annotation.JsonProperty("environment") Environment environments, // Map "environment" key to this param
                      @com.fasterxml.jackson.annotation.JsonProperty("transform") Transform transform,
                      @com.fasterxml.jackson.annotation.JsonProperty("trigger_manager") TriggerManager triggerManager) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.environments = environments;
        this.transform = transform;
        this.triggerManager = triggerManager;
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
    // --- Property helpers ---

    public List<Property> getProperties() {
        return new ArrayList<>(properties);
    }

    public void addProperty(Property property) {
        // If your Property has a key, you can de-duplicate by key:
        properties.add(property);
    }

    public void removeProperty(Property property) {
        properties.remove(property);
    }

// --- Copy method for preview isolation ---

    /**
     * Create a deep copy of this GameObject.
     *
     * @return A new GameObject with copied state
     */
    public GameObject copy() {
        // Copy properties
        ArrayList<Property> copiedProperties = new ArrayList<>();
        for (Property prop : this.properties) {
            if (prop instanceof SpriteRenderer) {
                copiedProperties.add(((SpriteRenderer) prop).copy());
            } else {
                copiedProperties.add(prop);
            }
        }

        // Copy transform
        Transform copiedTransform = (this.transform != null) ? this.transform.copy() : null;

        // Copy environment
        Environment copiedEnvironment = (this.environments != null) ? this.environments.copy() : new Environment();

        // Copy trigger manager
        TriggerManager copiedTriggerManager = (this.triggerManager != null)
                ? this.triggerManager.copy()
                : new TriggerManager();

        return new GameObject(
                this.id,
                this.name,
                this.active,
                copiedProperties,
                copiedEnvironment,
                copiedTransform,
                copiedTriggerManager
        );
    }

}
