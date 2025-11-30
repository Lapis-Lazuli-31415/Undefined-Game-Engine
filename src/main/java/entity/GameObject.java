package entity;

import entity.scripting.TriggerManager;
import entity.scripting.environment.Environment;

import java.util.ArrayList;
// import entity.Property;
import java.util.List;


/**
 * GameObject is the basic entity in the scene.
 * It has an id, name, active flag, properties, spriterenderer, and belongs to an Environment.
 */

public class GameObject {

    private final String id;
    private String name;
    private boolean active;
    private ArrayList<Property> properties;
    private Environment environments;
    private Transform transform;
    private TriggerManager triggerManager;
    private SpriteRenderer spriteRenderer;

    public GameObject(String id,
                      String name,
                      boolean active,
                      ArrayList<Property> properties,
                      Environment environments,
                      SpriteRenderer spriteRenderer) {
        this.id =id;
        this.name=name;
        this.active=active;
        this.spriteRenderer=spriteRenderer;

        // keep signature, but handle null safely
        if (properties != null) {
            this.properties = properties;
        } else {
            this.properties = new ArrayList<>();
        }

        this.environments=environments;
    }

    @com.fasterxml.jackson.annotation.JsonCreator
    public GameObject(@com.fasterxml.jackson.annotation.JsonProperty("id") String id,
                      @com.fasterxml.jackson.annotation.JsonProperty("name") String name,
                      @com.fasterxml.jackson.annotation.JsonProperty("active") boolean active,
                      @com.fasterxml.jackson.annotation.JsonProperty("properties") ArrayList<Property> properties,
                      @com.fasterxml.jackson.annotation.JsonProperty("environment") Environment environments, // Map "environment" key to this param
                      @com.fasterxml.jackson.annotation.JsonProperty("transform") Transform transform,
                      @com.fasterxml.jackson.annotation.JsonProperty("trigger_manager") TriggerManager triggerManager) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.properties = (properties != null) ? properties : new ArrayList<>();
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

}
