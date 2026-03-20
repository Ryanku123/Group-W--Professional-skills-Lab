# 🎲 Group W — Dice Game Hub

**PSCO Team-Based Project | University of Sussex**

A modular, menu-driven Java console application featuring four dice-based mini-games. Players register with a username, play games, track their scores, and compete on per-game leaderboards. All player data persists between sessions via a text file.

---

## 📋 Table of Contents

- [Project Overview](#project-overview)
- [How to Run](#how-to-run)
- [Project Structure](#project-structure)
- [Class Descriptions](#class-descriptions)
- [Games](#games)
- [Menu Structure](#menu-structure)
- [Data Persistence](#data-persistence)
- [Team](#team)

---

## Project Overview

- **Language:** Java
- **Version Control:** Git via GitHub Codespaces
- **Players:** Multiple players can register and play on the same machine
- **Persistence:** Player stats are saved to `players.txt` and reloaded on every startup
- **Leaderboard:** Per-game leaderboard showing all players ranked by best score

---

## How to Run

```bash
# 1. Compile all files
javac *.java

# 2. Run the program
java Main
```

> Make sure all `.java` files are in the same directory before compiling.

---

## Project Structure

```
📁 Group-W--Professional-skills-Lab/
│
├── Main.java                   ← Program entry point
├── User.java                   ← Player data model
├── UserManager.java            ← Registration + file I/O
├── ScoreManager.java           ← Per-game leaderboard
├── Game.java                   ← Game interface
│
├── Game1_DicePatterns.java     ← Game 1: Dice Patterns Challenge
├── Game2_DiceGrid.java         ← Game 2: Dice Grid Puzzle
├── Game3_DiceCodebreaker.java  ← Game 3: Dice Codebreaker
├── Game4_DiceBlackjack.java    ← Game 4: Dice Blackjack
│
└── players.txt                 ← Auto-generated player data file
```

---

## Class Descriptions

### `Main.java`
The program entry point and central controller.
- Launches the application and handles all menu navigation
- Prompts for username registration on startup
- Routes menu choices to the correct game or feature
- After every game, calls `user.recordScore()` then `userManager.saveToFile()` to persist results immediately
- Supports switching between players without restarting

---

### `User.java`
Stores all data for a single registered player.

| Field | Description |
|---|---|
| `username` | Player's unique name |
| `totalGamesPlayed` | Total number of games played across all 4 games |
| `highestScore[4]` | Best score achieved per game (index 0–3) |
| `recentScore[4]` | Most recent score per game (index 0–3) |
| `lastPlayedDateTime` | Timestamp of the last gameplay session |

Key method: `recordScore(gameIndex, score)` — called after every game to update all stats and timestamp automatically.

---

### `UserManager.java`
Handles player registration and persistent file storage.
- On startup, reads `players.txt` and reconstructs all saved `User` objects
- `register(username)` — returns existing player or creates a new one
- `saveToFile()` — writes all player data to `players.txt` in CSV format
- `getAllUsers()` — returns the full list of players (used by `ScoreManager`)
- Duplicate usernames are prevented automatically

**File format (`players.txt`):**
```
# username,totalGames,high0,high1,high2,high3,recent0,recent1,recent2,recent3,lastDateTime
ryan,7,50,72,80,200,25,60,0,100,2026-02-26 14:32:01
```

---

### `ScoreManager.java`
Builds and displays a ranked leaderboard for any game.
- `showLeaderboard(gameIndex, gameName, allUsers)` — sorts all players by best score for the chosen game and prints a ranked table
- Does not store scores itself — reads from `User` objects on demand
- Top 3 players receive medal indicators (🥇 🥈 🥉)

---

### `Game.java`
Interface that all four game classes must implement.

```java
public interface Game {
    int    play(User user); // runs the game, returns the score earned
    String getName();       // returns the display name of the game
}
```

Using an interface allows `Main` to store all four games in a single `Game[]` array and call `game.play(user)` uniformly regardless of which game is selected.

---

### `Game1_DicePatterns.java`
**Dice Patterns Challenge** — inspired by Yahtzee.

- Roll 5 dice, re-roll up to 2 times choosing which dice to keep
- Score is based on the final pattern of all 5 dice
- Type `done` at any re-roll prompt to stop early

| Pattern | Score |
|---|---|
| Five of a Kind | 50 pts |
| Four of a Kind | 40 pts |
| Full House | 35 pts |
| Straight (1–5 or 2–6) | 30 pts |
| Three of a Kind | 25 pts |
| Two Pairs | 20 pts |
| One Pair | 10 pts |
| No Match | 0 pts |

---

### `Game2_DiceGrid.java`
**Dice Grid Puzzle** — strategic placement game.

- Roll one die at a time and place it in an empty cell of a 3×3 grid
- Once all 9 cells are filled, each row and column is scored independently
- Maximum possible score: **90 pts** (6 lines × 15 pts)

| Pattern | Score |
|---|---|
| Three of a Kind | 15 pts |
| Straight (3 consecutive) | 12 pts |
| Pair | 8 pts |
| All Different | 5 pts |

---

### `Game3_DiceCodebreaker.java`
**Dice Codebreaker** — inspired by Mastermind.

- The computer generates a secret 4-dice code (each value 1–6)
- Player has 8 attempts to guess the correct code
- After each guess, feedback is given for every position:
  - `[O]` = correct number, correct position
  - `[?]` = correct number, wrong position
  - `[X]` = wrong number
- Score decreases with each attempt used

| Attempts Used | Score |
|---|---|
| 1 | 80 pts |
| 2 | 70 pts |
| ... | ... |
| 8 | 10 pts |
| Failed | 0 pts |

---

### `Game4_DiceBlackjack.java`
**Dice Blackjack** — custom team game.

- Dice replace cards: roll of `1` = Ace (1 or 11), roll of `6` = 10 (face card), rolls `2–5` = face value
- Player and dealer each start with 2 dice; one dealer die is hidden
- Player chooses to **hit** (`h`) or **stay** (`s`) each turn
- Dealer must hit on 16 or less, stands on 17+
- Goal: get as close to 21 as possible without busting

| Outcome | Score |
|---|---|
| Blackjack (21 on first 2 dice) | 150 pts |
| Win | 100 pts |
| Push (tie) | 50 pts |
| Bust or Loss | 0 pts |

---

## Menu Structure

```
MAIN MENU
├── 1. Dice Patterns Challenge
│     ├── 1. Play
│     ├── 2. My best score
│     ├── 3. Leaderboard
│     └── 4. Back
├── 2. Dice Grid Puzzle        (same sub-menu)
├── 3. Dice Codebreaker        (same sub-menu)
├── 4. Dice Blackjack          (same sub-menu)
├── 5. View My Stats
├── 6. View Leaderboard
│     ├── 1. Dice Patterns Challenge
│     ├── 2. Dice Grid Puzzle
│     ├── 3. Dice Codebreaker
│     ├── 4. Dice Blackjack
│     └── 5. Back
├── 7. Switch Player
└── 8. Exit
```

---

## Data Persistence

All player data is automatically saved to `players.txt` after every game. If the file does not exist, it is created on first run. When the program starts, existing data is loaded so returning players keep all their stats.

Stats tracked per player:
- Total games played (all games combined)
- Highest score per game
- Most recent score per game
- Date and time of last gameplay

---

## Team

| Member | Game |
|---|---|
| Ryan Ku | Project structure, Main, User, UserManager, ScoreManager, Game interface, Game 4 |
| [Ibet | Game 1: Dice Patterns Challenge |
| [Liaquat] | Game 2: Dice Grid Puzzle |
| [Youssef] | Game 3: Dice Codebreaker |
| [Muhammad] | Game 4: Dice Blackjack |

---

*Group W | PSCO Team-Based Project | University of Sussex*
