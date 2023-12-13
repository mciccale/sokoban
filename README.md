# Sokoban

## Installation steps

Make sure you have Java 11 (at least) and Maven installed in your system.

```
git clone https://github.com/mciccale/sokoban.git
cd sokoban
mvn compile
mvn exec:java
```

## Files Structure

interfaces: contains interfaces related to the game components.

- Controller: Interfaces for the game controller.
- Frame: Interfaces for the graphical frame.
- Square: Interfaces for the game cells.
- Unmove: Interfaces for undoing moves.

controller: contains the files wich implements the game controller logic.

- GameController: Manages the game flow and interactions.

model: Includes the game model classes.

- Box: Represents the boxes in the game.
- GameMovementCounter: Tracks the movement count for the current game.
- GoalPosition: Represents the goal positions in the game.
- LevelMovementCounter: Tracks the movement count for each level.
- MoveAndTurn: Handles movement and turning operations.
- MovementCounter: Abstract class for movement counters.
- Position: Represents positions on the game board.
- Wall: Represents the walls in the game.
- WarehouseMan: Represents the player character.

view: Contains the GUI-related classes.

- GameFrame: Provides the main game window.
- GameMenuPanel: Displays the game menu options.
- GamePanel: Displays the game board and congratulation message.

App: Contains the main method to run the game.

The sprites directory contains images used in the game's graphical interface. Each image file represents a specific element of the game, such as boxes, the warehouseman, walls, goal positions, and floor cells.

There is also a levels directory that contains text files (level1.txt, level2.txt, level3.txt...) representing different levels of the game. Each text file contains the specific layout and configuration of a level.

The game provides a "Save Game" option that allows the player to save their progress at any point in the game. When selecting the "Save Game" option, a file chooser dialog will open, allowing you to choose the desired location and filename to save the game. This provides the flexibility to save the game file wherever you want on your computer.

The game also provides an "Open Saved Game" option that allows the player to resume a previously saved game. When selecting the "Open Saved Game" option, a file chooser dialog will open, allowing you to choose the saved game file that you want to open. The game will then read the file and load the selected file, restoring the game state to the saved state, allowing you to continue playing from where you left off.

## Game & Controls

1. Launch the game using the App file.
2. Use the arrow keys or WASD keys to move the player character.
3. Push the boxes onto the goal positions to complete each level.
4. Use the menu options to start a new game, restart a level, undo a movement, save a game, open a saved game or exit.

## Format

The levels files parsed are represented using a specific format. Each level consists of a board of characters, where each character represents a different element of the game. The format of a level includes the following elements:

Level name: Indicades the difficulty.

Board Size: The size of the board is specified at the beginning, indicating the number of rows and columns.

Elements: Each character in the board represents a specific element of the game. Common elements include:

- '+' represents walls.
- " "(space) floor.
- '\*' represents boxes.
- 'W' represents the player character (warehouseman).
- '#' represents goal positions.

Open Saved Game File Format
Saved game files in Sokoban follow a specific format. The file includes the following information:

Level: The level being played or saved is indicated. This information helps to identify the specific layout and configuration of the level that has to be parsed.

Game Score: The score achieved in the game is recorded.

Movements: The movements executed by the player are recorded. This information captures the sequence of moves made to reach the current state of the game.

This format allows players to save and resume games from a specific state.

## Authors

- Ramiro Lopez Cento
- Marco Ciccale Baztán
- Esteban Aspe Ruiz
- Ernesto Naval Rodríguez
