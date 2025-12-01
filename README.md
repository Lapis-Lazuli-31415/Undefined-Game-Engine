# Undefined-Game-Engine
**Note: JDK Version 17**
## Summary
Undefined is a 2D Game Engine built in Java, designed to allow users to create, edit, and play games.
## How to Run Game
- To run the program, run HomeView

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
