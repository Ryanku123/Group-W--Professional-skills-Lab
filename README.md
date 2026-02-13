# Group-W--Professional-skills-Lab

A simple and modular text-based Java game hub where users can register, choose from four different games, play them, and view scoreboards sorted from highest to lowest. The project showcases object‑oriented programming, interface‑based design, and menu-driven console interaction.

🔹 Features

1. User Registration
Users enter a username to register. Duplicate usernames are prevented.
2. Main Menu
Displays four available games:
Game A
Game B
Game C
Game D
3. Game Menu
Each game provides:
Start → plays the game and generates a score
Scoreboard → shows all scores (highest → lowest)
4. Score Management
Scores are stored in memory using ScoreManager
Sorted using Collections.reverseOrder()
Displayed per game

🔹 Code Architecture

Main.java
Controls program flow
Displays menus
Handles user input
Connects to game classes
User.java
Stores username
UserManager.java
Handles registration logic
Prevents duplicate usernames
Game.java (Interface)
Defines two required methods:

void start(User user);
void showScores();
ScoreManager.java
Stores scores per game
Sorts scores in descending order
Prints scoreboards
GameA–D.java
Each game implements Game
Generates random scores (can be replaced with real gameplay)

