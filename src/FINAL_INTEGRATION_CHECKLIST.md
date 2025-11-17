# Preview Feature Integration Checklist - FINAL VERSION
**Date:** November 14, 2025  
**Developer:** Wanru Cheng  
**Status:** ~45% Complete

---

## ✅ SOLVED - Ready to Use

### 1. Vector Position Access ✅
**Question:** How to access x, y coordinates from Transform.position?

**Answer:** Transform uses `java.util.Vector<Double>`
```java
Transform transform = ...;
double x = transform.position.get(0);  // x coordinate
double y = transform.position.get(1);  // y coordinate
float rotation = transform.rotation;
```

**Source:** Yanlin's code
**Status:** ✅ Can implement immediately

---

### 2. Image Type ✅
**Question:** What type is SpriteRenderer.image?

**Answer:** `java.awt.Image`
```java
import java.awt.Image;

SpriteRenderer sr = ...;
Image image = sr.image;  // Direct field access (final field)
Color tint = sr.tint;
boolean visible = sr.visible;
```

**Source:** SpriteRenderer.java code
**Status:** ✅ Can implement immediately

---

### 3. Transform and SpriteRenderer Structure ✅
**Question:** What fields do these classes have?

**Answer:**
```java
public class Transform {
    final Vector<Double> position;
    final float rotation;
    final Vector<Double> scale;
}

public class SpriteRenderer {
    final Image image;
    final Color tint;
    final int zIndex;
    final boolean visible;
}
```

**Source:** Team's code
**Status:** ✅ Structure known, can access final fields directly

---

### 4. Trigger System Structure ✅
**Question:** How is the trigger system organized?

**Answer:** From entity diagram:
```
GameObject → Environment → Triggers → ArrayList<Trigger>

Trigger {
    Event event
    ArrayList<Condition> conditions
    ArrayList<Action> actions
}

Interfaces:
- Condition.evaluate(): boolean
- Action.execute(): void
- Event (interface for OnKeyPress, OnClick, etc.)

OnKeyPress {
    String key
}
```

**Source:** Entity diagram
**Status:** ✅ Structure known, but method parameters unknown

---

## ❌ CRITICAL BLOCKERS - Must Solve Immediately

### 5. GameObject Getter Methods ❌ BLOCKING EVERYTHING
**Question:** How to access GameObject properties?

**Problem:** GameObject has private fields with NO getter methods:
```java
public class GameObject {
    private String id;                    // ← No getId()
    private String name;                  // ← No getName()
    private boolean active;               // ← No isActive()
    private ArrayList<Property> properties;  // ← No way to access!
    private Environment environents;      // ← Typo! + No getEnvironment()
}
```

**What I Need:**
```java
// CRITICAL - Without these, I CANNOT render or process triggers
public boolean isActive();
public Transform getTransform();
public SpriteRenderer getSpriteRenderer();
public Environment getEnvironment();

// Optional but recommended
public String getId();
public String getName();
```

**Blocking:**
- ❌ Cannot render objects (no way to get Transform/SpriteRenderer)
- ❌ Cannot process triggers (no way to get Environment)
- ❌ Cannot check if object is active

**Who to ask:** Anyone on the team / whoever owns GameObject
**Urgency:** 🔴 CRITICAL - Blocks 50% of my work

**Suggested Fix:**
```java
public class GameObject {
    // ... existing fields ...
    
    public boolean isActive() { 
        return active; 
    }
    
    public Transform getTransform() {
        for (Property p : properties) {
            if (p instanceof Transform) {
                return (Transform) p;
            }
        }
        return null;
    }
    
    public SpriteRenderer getSpriteRenderer() {
        for (Property p : properties) {
            if (p instanceof SpriteRenderer) {
                return (SpriteRenderer) p;
            }
        }
        return null;
    }
    
    public Environment getEnvironment() {
        return environents;  // Fix typo: should be "environments"
    }
}
```

---

## 🟡 HIGH PRIORITY - Need for Trigger Integration

### 6. Triggers Class Access Methods 🟡
**Question:** How to get trigger list from Triggers class?

**From diagram, I know:**
```java
Environment → Triggers → ArrayList<Trigger>

public class Triggers {
    ArrayList<Trigger> triggers;  // ← How to access this?
}
```

**What I need to know:**

**Option A:** Getter method?
```java
public class Triggers {
    private ArrayList<Trigger> triggers;
    
    public ArrayList<Trigger> getTriggers() {
        return triggers;
    }
}

// Usage:
Triggers container = env.getTriggers();
ArrayList<Trigger> list = container.getTriggers();
```

**Option B:** Public field?
```java
public class Triggers {
    public ArrayList<Trigger> triggers;
}

// Usage:
Triggers container = env.getTriggers();
ArrayList<Trigger> list = container.triggers;
```

**Who to ask:** Enqi
**Urgency:** 🟡 High - Need for trigger processing

---

### 7. Environment.getTriggers() Method 🟡
**Question:** How to get Triggers object from Environment?

**What I need:**
```java
Environment env = ...;
Triggers triggersContainer = env.???;  // What method?
```

**Possible answers:**
- `env.getTriggers()`
- `env.triggers` (if public field)
- Other?

**Who to ask:** Enqi
**Urgency:** 🟡 High - Need for trigger processing

---

### 8. Event.check() Method Signature 🟡
**Question:** What parameters does Event.check() need?

**My InputManager provides:**
```java
public boolean isKeyPressed(String keyName);
```

**Possible options:**

**Option A:** InputManager as parameter
```java
public interface Event {
    boolean check(InputManager input, Environment env);
}

// OnKeyPress implementation:
public class OnKeyPress implements Event {
    public String key;
    
    @Override
    public boolean check(InputManager input, Environment env) {
        return input.isKeyPressed(key);
    }
}

// My usage:
if (trigger.event.check(inputManager, env)) { ... }
```

**Option B:** OnKeyPress stores InputManager
```java
public interface Event {
    boolean check(Environment env);
}

// OnKeyPress stores reference:
public class OnKeyPress implements Event {
    public String key;
    private InputManager inputManager;
    
    public OnKeyPress(String key, InputManager inputManager) {
        this.key = key;
        this.inputManager = inputManager;
    }
    
    @Override
    public boolean check(Environment env) {
        return inputManager.isKeyPressed(key);
    }
}

// My usage:
if (trigger.event.check(env)) { ... }
```

**Who to ask:** Enqi
**Urgency:** 🟡 High - Need for keyboard input integration

---

### 9. Condition.evaluate() Parameters 🟡
**Question:** What parameters does Condition.evaluate() need?

**From diagram:** `boolean evaluate(???)`

**Possible options:**
- `evaluate(Environment env)`
- `evaluate(GameObject target, Environment env)`
- `evaluate()` (no parameters)

**Who to ask:** Enqi
**Urgency:** 🟡 Medium - Need for condition checking

---

### 10. Action.execute() Parameters 🟡
**Question:** What parameters does Action.execute() need?

**From diagram:** `void execute(???)`

**Example - ChangePositionAction needs to modify GameObject:**
```java
public class ChangePositionAction implements Action {
    private GameObject gameObject;
    private Vector<Expression> newPosition;
    
    @Override
    public void execute(???) {
        // Need to modify gameObject position
        // Need access to Environment for expressions?
    }
}
```

**Possible options:**
- `execute(GameObject target, Environment env)`
- `execute(Environment env)`
- `execute(GameObject target)`

**Who to ask:** Enqi
**Urgency:** 🟡 Medium - Need for action execution

---

## 🟢 LOW PRIORITY - Nice to Have

### 11. Property Interface/Class Definition 🟢
**Question:** What does the Property base class look like?

**Current usage:**
```java
ArrayList<Property> properties;  // What is Property?
```

**Need to know:**
```java
// Is it an interface or abstract class?
public interface Property { ... }
// or
public abstract class Property { ... }
```

**Who to ask:** Yanlin or anyone
**Urgency:** 🟢 Low - Not blocking, but good to know

---

### 12. Scene Structure and Methods 🟢
**Question:** How does Scene class work?

**Need to know:**
```java
public class Scene {
    // How to get GameObjects?
    public ArrayList<GameObject> getGameObjects() { ... }
    
    // Any other important methods?
}
```

**Who to ask:** Anyone
**Urgency:** 🟢 Low - Assume getGameObjects() exists

---

### 13. Main Editor Integration Point 🟢
**Question:** Where to add the Play button?

**Need to know:**
- Main editor class name
- File location
- How to add button to toolbar
- How to get current Scene from editor

**Who to ask:** UI person / Welcent
**Urgency:** 🟢 Low - Can do this last

---

## 📊 Priority Summary

### 🔴 CRITICAL (Blocking 50% of work):
1. **GameObject getter methods** - Blocks rendering AND triggers

### 🟡 HIGH (Blocking trigger system):
2. **Triggers.getTriggers()** - How to access trigger list
3. **Environment.getTriggers()** - How to get Triggers object
4. **Event.check()** parameters - InputManager integration
5. **Condition.evaluate()** parameters
6. **Action.execute()** parameters

### 🟢 LOW (Can work around):
7. Property interface
8. Scene structure
9. Main editor integration

---

## 📝 What I Can Do Right Now

### ✅ Immediately (No dependencies):
- [x] InputManager - Complete
- [x] Game loop framework - Complete
- [x] Preview window - Complete
- [x] Test infrastructure - Complete

### 🟡 Waiting for GameObject getters:
- [ ] Implement rendering (have Vector/Image access, need getters)
- [ ] Display game objects
- [ ] Test with real data

### 🟡 Waiting for Enqi's answers (Q6-Q10):
- [ ] Implement trigger checking
- [ ] Implement trigger execution
- [ ] Test keyboard controls
- [ ] Full integration test

---

## 🎯 Recommended Action Plan

### Immediate (Today):

**Step 1:** Send to team (5 min)
```
Subject: GameObject Missing Getters - Blocking Preview Feature

Hi team,

GameObject is missing critical getter methods. Without these, 
I cannot render objects or process triggers.

Please add these methods to GameObject (someone can do this 
in 5 minutes):

public boolean isActive() { return active; }
public Transform getTransform() { 
    for (Property p : properties) {
        if (p instanceof Transform) return (Transform) p;
    }
    return null;
}
public SpriteRenderer getSpriteRenderer() { 
    for (Property p : properties) {
        if (p instanceof SpriteRenderer) return (SpriteRenderer) p;
    }
    return null;
}
public Environment getEnvironment() { return environents; }

Who can add these?

Thanks,
Wanru
```

**Step 2:** Send to Enqi (5 min)
```
Subject: Trigger System Integration Questions

Hi Enqi,

I understand the trigger structure from the diagram, but need 
method signatures to integrate:

1. How to access Triggers.triggers field? (getter or public?)
2. Event.check() parameters? (does it need InputManager?)
3. Condition.evaluate() parameters?
4. Action.execute() parameters?

My InputManager provides: boolean isKeyPressed(String keyName)
How does OnKeyPress use it?

See attached: Questions_For_Enqi_Updated.md

Thanks!
```

**Step 3:** Commit current work to Git (15 min)

---

### Next (After responses):

**If GameObject getters added:**
- Implement rendering (1-2 hours)
- Test with mock objects
- Show progress to team

**If Enqi answers questions:**
- Implement trigger processing (2-3 hours)
- Test keyboard controls
- Full integration

---

## 📞 Quick Reference

### Who to Ask What:

| Question | Person | Urgency |
|----------|--------|---------|
| GameObject getters | Anyone | 🔴 CRITICAL |
| Triggers class | Enqi | 🟡 High |
| Event/Condition/Action signatures | Enqi | 🟡 High |
| Property interface | Yanlin | 🟢 Low |
| Scene structure | Anyone | 🟢 Low |
| Editor integration | UI/Welcent | 🟢 Low |

---

## ✅ Success Criteria

**Can start rendering when:**
- [x] Know Vector access (✅ DONE)
- [x] Know Image type (✅ DONE)
- [ ] GameObject has getTransform()
- [ ] GameObject has getSpriteRenderer()
- [ ] GameObject has isActive()

**Can start trigger processing when:**
- [ ] GameObject has getEnvironment()
- [ ] Know how to get Triggers from Environment
- [ ] Know Event.check() signature
- [ ] Know Condition.evaluate() signature
- [ ] Know Action.execute() signature

**Ready for full integration when:**
- [ ] Rendering works
- [ ] Triggers work
- [ ] Know where to add Play button

---

**Last Updated:** November 14, 2025  
**Next Update:** After receiving GameObject getters and Enqi's responses
