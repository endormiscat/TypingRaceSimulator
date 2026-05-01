# TypingRaceSimulator

Object Oriented Programming  —  ECS414U
Author: Jakhongir Bakhromov

## Project Structure

```
TypingRaceSimulator/
├── Part1/    # Textual simulation (Java, command-line)
└── Part2/    # GUI simulation (to be completed)
```

## Dependencies

Java JDK 11 or higher
No external libraries required — uses standard Java only

## Part 1 — Textual Race Simulator

### How to Compile

```bash
cd Part1
javac *.java
```
### How to run

The race is started by calling `startRace()` on a `TypingRace` object. testing main method is included too.

```bash
java TypingRace
```

### How it Works
Three typists race through a passage of text turn by turn in the terminal.
Each turn a typist may advance one character, mistype and slide back, or burn out
and be frozen for several turns. The first typist to reach the end of the passage wins.

### Features
- Three typists with configurable names, symbols, and accuracy ratings
- Burnout mechanic — high accuracy typists risk burning out from pushing too hard
- Mistype mechanic — less accurate typists slide backwards more often
- Live terminal display updated each turn with position markers
- `[<]` marker shown when a typist just mistyped
- `~` marker shown when a typist is burnt out
- Winner announced at the end of the race

## Part 2 — Graphical Race Simulator

### How to Run
```bash
cd Part2
javac *.java
java RaceGUI
```
This calls `startRaceGUI()` which opens the graphical race simulator.

### How it Works
A full Swing GUI guides the user through three screens:
1. **Config Screen** — choose passage, number of typists, and difficulty modifiers
2. **Typist Config Screen** — customise each typist's name, style, keyboard, symbol, and accessories
3. **Race Screen** — watch the race live with character-by-character highlighting
4. **Stats Screen** — view WPM, accuracy, burnouts, personal bests, and compare typists
5. **Leaderboard Screen** — view cumulative F1-style points and titles across all races

### Features

#### Race Configuration
- Three passage lengths: Short, Medium, Long, or Custom
- 2 to 6 typists
- Difficulty modifiers:
  - **Autocorrect** — slideBack amount is halved
  - **Caffeine Mode** — accuracy boost for first 10 turns, then increased burnout risk
  - **Night Shift** — all typist accuracy reduced by 0.10

#### Typist Customisation
- Name, symbol, typing style, and keyboard type
- Typing styles: Touch Typist, Hunt & Peck, Phone Thumbs, Voice-to-Text
- Keyboard types: Mechanical, Membrane, Touchscreen, Stenography
- Accessories: Wrist Support, Energy Drink, Noise-Cancelling Headphones

#### Statistics
- WPM calculated individually per typist based on their finish time
- Accuracy percentage based on correct keystrokes vs mistypes
- Burnout count per race
- Personal bests and race history saved to `stats.txt`
- Side-by-side comparison view with dropdown selection

#### Leaderboard
- F1-style points system (25, 18, 15, 12, 10, 8, 6, 4, 2, 1)
- Bonus point for fastest WPM
- Penalty point for most burnouts
- Titles awarded based on milestones:
  - **Speed Demon** — 3 consecutive wins
  - **Iron Fingers** — 5 races without a burnout
  - **Veteran** — 20 cumulative points
- Data saved to `leaderboard.txt` and persists across sessions