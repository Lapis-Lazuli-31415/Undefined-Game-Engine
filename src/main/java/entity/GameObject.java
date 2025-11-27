package entity;

import entity.scripting.TriggerManager;
import entity.scripting.environment.Environment;

import java.util.ArrayList;
// import entity.Property;
import java.util.List;
import entity.scripting.TriggerManager;


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
    private TriggerManager triggerManager;

    public GameObject(String id,
                      String name,
                      boolean active,
                      ArrayList<Property> properties,
                      Environment environments) {
        this.id =id;
        this.name=name;
        this.active=active;
        this.triggerManager = new TriggerManager();

        // keep signature, but handle null safely
        if (properties != null) {
            this.properties = properties;
        } else {
            this.properties = new ArrayList<>();
        }

        this.environments=environments;
    }

    public GameObject(String id,
                      String name,
                      boolean active,
                      ArrayList<Property> properties,
                      Environment environments,
                      Transform transform,
                      TriggerManager triggerManager) {
        this.id = id;
        this.name = name;
        this.active = active;

        // keep signature, but handle null safely
        if (properties != null) {
            this.properties = properties;
        } else {
            this.properties = new ArrayList<>();
        }

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
