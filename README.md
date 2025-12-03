# Undefined-Game-Engine
**Note: JDK Version 17**
## Summary
Undefined is a 2D Game Engine built in Java, designed to allow users to create, edit, and play games.
## <img width="1292" height="689" alt="Screenshot 2025-12-01 at 6 59 27 PM" src="https://github.com/user-attachments/assets/a8122bc3-dafa-4c2d-a548-85c29bcb9d14" />
## How to Run Game
- To run the program, run app/Main
- **IMPORTANT**: need to include "UNSPLASH_ACCESS_KEY=OSHzuf4ybMZXfL1ddpzvxGCZ-yMMfcb2TmY2ys7VpTM" in environment variables for Unsplash API to work

## Game Engine Capabilities Guide

### 1. Asset Management (Adding Images)
You can import images to be used as sprites for your game objects.

**What you can do:**
* **Import Local Files:** Load images (`.png`, `.jpg`, `.jpeg`) from your computer.
* **Import from Unsplash:** Search for and download images directly from the Unsplash API.
* **Manage Assets:** View a list of imported sprites in the "Assets" panel.

**How to do it (in the Editor):**
1.  Look for the **"Assets"** panel on the left sidebar.
2.  Click the **"+" (Add)** button in the Sprites header.
3.  Select either **"Import from Local"** to browse files or **"Import from Unsplash"** to search online.
4.  Once imported, the sprite appears as a card in the list. Clicking it adds it to the scene as a new GameObject.

### 2. Game Object Properties
You can modify the physical and visual attributes of Game Objects in the scene.

**What you can do:**
* **Transform:** Change position (X, Y), rotation, and scale.
* **Sprite Rendering:** Change the sprite image, toggle visibility, adjust opacity, and set Z-Index (layering).

**How to do it:**
1.  **Select** a GameObject in the Scene view (center panel) by clicking on it.
2.  The **Properties Panel** on the right side will update.
3.  Edit the fields in the **Transform** section to move/resize the object.
4.  Use the **Sprite Renderer** section to swap the image or change opacity.

### 3. Game Logic (Scripting)
You can define interactive behavior using a Trigger system without writing raw code.

**What you can do:**
* **Create Triggers:** Define "When [Event] happens, if [Condition] is true, do [Actions]".
* **Events:** `On Click`, `On Key Press`.
* **Conditions:** Compare numbers (`>`, `=`, etc.) or booleans to decide if actions should run.
* **Actions:**
    * `Wait`: Pause execution.
    * `Assign`: Change variable values.
    * `Change Position`: Move objects.
    * `Change Visibility`: Hide/show objects.

**How to do it:**
1.  With an object selected, look at the **Trigger Manager** in the Properties Panel.
2.  Click **"+"** to add a new Trigger.
3.  Select an **Event** (e.g., "On Click").
4.  Add **Conditions** (optional) and **Actions** using their respective "+" buttons.
5.  Click the **Edit (Pencil)** button on an action/condition to open the script editor dialog and define the logic.

### 4. Variables
You can store data (like health, score, or flags) to manage game state.

**What you can do:**
* **Global Variables:** Shared across the entire game (e.g., `PlayerScore`).
* **Local Variables:** Specific to a single object (e.g., `MyHealth`).
* **Types:** Supports `Numeric` (doubles) and `Boolean` (true/false).

**How to do it:**
1.  In the **Properties Panel**, scroll to the **Variable Section**.
2.  Click the **"+"** button next to "Global Variables" or "Local Variables".
3.  Enter a name and initial value.

### 5. Preview & Testing
You can play the game instantly to test your logic.

**What you can do:**
* Launch a standalone window running the current scene.
* Interact with objects using the defined Events (Click, Key Press).
* Switch between **Sprite Mode** (visuals) and **Button Mode** (debug buttons).

**How to do it:**
1.  Click the **Green Play Button (▶)** in the top toolbar of the editor.
2.  A new window will open running the game loop.
3.  Click the **Stop Button (■)** to close the preview.


## Core Project Data Hierarchy
 - **Project**: The root container, managing Scenes, Assets, and a Global Environment.
 - **Scene**: Contains a list of GameObjects. 
 - **GameObject**: The fundamental entity representing items in the game. Each has:
   - **Transform**: Position (X, Y), Rotation, and Scale. 
   - **SpriteRenderer**: Renders images (sprites) with properties like visibility, opacity, and Z-index. 
   - **TriggerManager**: Handles game logic via scripts. 
   - **Environment**: Stores local variables specific to the object.

## List of User Stories
- **User story 1**: As a user, I want to be able to create my own unique sprites, so that I can customize my game’s characters and objects to my liking.
- **User story 2**: As a user, I want game objects to be interactable during gameplay, so that I can create a more diverse and interactive experience for players.
- **User story 3**: As a user, I want to be able to change and adjust the properties of the components so that I can customize the appearance and behavior according to my preference.
- **User story 4**: As a user, I want to add different scenes to my game.
- **User story 5**: As a user, I want to be able to test my game before releasing / publishing it.
- **User story 6**: As a user, I want my game to autosave, so I can work on my project at another time.

## API Information
- This project utilizes the Unsplash API to fetch images that can be used within the game engine. 
  - Link to documentation: [Unsplash API Documentation](https://unsplash.com/documentation). 
  - DONT FORGET TO CONFIGURE YOUR API KEY SO THAT ALL FEATURES WORK AS INTENDED
## Software Design Principles

### 1. Open–Closed Principle (OCP) & Extensibility
**Goal:** Enable future developers to add gameplay capabilities (Conditions, Actions, Expressions) without modifying existing code.

* **Abstract Base Classes:** The system uses abstract classes to define the required behavior for all gameplay logic elements.
    * **Implementation:** Every specific logic type inherits from one of these base abstractions:
        * `Condition` (e.g., `NumericComparisonCondition`)
        * `Action` (e.g., `WaitAction`, `ChangeVisibilityAction`)
        * `Expression` (e.g., `NumericExpression`)

* **Factory Design Pattern:** Factories are used to manage the creation of these objects dynamically.
    * **Registry Map:** Each factory (`ConditionFactory`, `ActionFactory`, `DefaultVariableFactoryRegistry`) maintains a map pairing a unique name (key) with a supplier function (value).
    * **Functionality:**
        * Automatically lists available types for the Editor UI to populate menus.
        * Constructs the correct instance based on user selection without hardcoded `switch` statements in the client code.
    * **Reference:** See `ConditionFactory` and `ActionFactory`.

* **Workflow for New Features:**
    1.  **Inherit:** Create a new subclass extending the appropriate abstract base class.
    2.  **Register:** Add the new type to the Factory’s registry map.
    * **Result:** The system is **open for extension** (new features can be added) but **closed for modification** (core logic remains untouched).

### 2. Single Responsibility Principle (SRP) & Architecture
**Context:** Adherence to SRP is a direct result of implementing **Clean Architecture**.

* **Separation of Concerns:** Business logic is strictly separated into distinct Use Case classes, preventing "God classes" that handle too many responsibilities.
* **Granular Use Cases:** Operations are broken down into specific classes:
    * **Trigger Management:** Handled by specific interactors like `TriggerCreateInteractor` and `TriggerDeleteInteractor`.
    * **Scene Management:** Split into isolated actions such as `CreateSceneInteractor`, `ListScenesInteractor`, and `SelectSceneInteractor`.
## Screenshots/DEMO
