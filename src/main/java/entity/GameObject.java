package entity;

import java.awt.*;
import java.util.ArrayList;

public class GameObject {

    private String id;
    private String name;
    private boolean active;
    private ArrayList<Property> properties;
    private Environent environents;

    public GameObject(String id, String name, boolean active, ArrayList<Property> properties, Environment environments) {
        this.id =id;
        this.name=name;
        this.active=true;
        this.properties=new ArrayList<Property>();
        this.environents=environments
    }

}
