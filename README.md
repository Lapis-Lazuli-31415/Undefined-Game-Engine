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
## Screenshots/DEMO