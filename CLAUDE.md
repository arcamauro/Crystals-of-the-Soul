# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Run Commands

```bash
./gradlew lwjgl3:run          # Run the game
./gradlew build               # Build all modules
./gradlew clean               # Clean build artifacts
./gradlew lwjgl3:jar          # Create runnable JAR
./gradlew lwjgl3:jarLinux     # Linux-specific JAR
./gradlew lwjgl3:jarMac       # macOS JAR
./gradlew lwjgl3:jarWin       # Windows JAR
```

No test framework is currently set up — there is no `test/` directory and no test dependencies in the build files.

## Project Structure

Two-module Gradle project:
- `core/` — All game logic (platform-independent libGDX)
- `lwjgl3/` — Desktop launcher and LWJGL3 backend

All game code lives in `core/src/main/java/io/github/crystals_of_the_soul/`.

Key packages:
- `screens/` — libGDX Screen implementations (view layer)
- `states/` — Game state data objects (model layer)
- `battle/` — Turn-based combat with a listener/observer pattern
- `entity/` — Enemy base class and Boss (7 types)
- `collision/` — Tile-based collision and portal management
- `save/` — JSON serialization with integrity validation
- `player/`, `input/` — Player entity and keyboard input

Assets: `assets/maps/` contains 15 `.tmx` TiledMap files (5 floors × crystal color variants).

## Architecture Overview

**Screen flow:** `MainMenuScreen → NewGameScreen → LoadingScreen → GameScreen → EndingScreen`

`GameScreen` is the central controller (~475 LOC). It owns a `CollisionManager`, `BattleManager`, and `GameHud`, and switches between world-exploration and turn-based battle modes via `inBattle` / `playerTurn` flags.

**Moral consequence system** — the central mechanic driving most branching:
- `killCount` vs `spareCount` determines `CrystalType` (RED/GREEN/BLUE)
- Crystal type sets the companion class (`Player2Class`: ASSASSIN/ARCHER/PROTECTOR) and restricts player abilities (RED = can't talk/spare; BLUE = can't attack; GREEN = both)
- Floors 3–5 have crystal-specific map variants (`lvl3_r.tmx`, `lvl3_b.tmx`, `lvl3_v.tmx`)
- Boss type on floors 3 and 5 is chosen via `Boss.createForFloor()` based on floor + crystal
- Five endings (`EndingType`: HERO, CITIZEN, WANDERER, GUARDIAN, CHOICE) are determined from crystal and counts in `GameState`

**Boss defeat conditions vary by type** (not all are HP-based):
- PROTECTOR: 5 talks
- GUARDIAN: 3 talks + 3 attacks
- MIRROR_RED: 7 talks
- MIRROR_BLUE: deal all remaining HP in a single attack
- ASSASSIN / ARCHER / MIRROR_GREEN: HP depletion

**Save system:** JSON-based, auto-saves on floor transition, manual save via pause menu. `SaveManager` writes to a local file and `SaveIntegrity` validates on load. On resume, the more-recent of auto/manual save is used.

**Event decoupling:** `BattleManager.Listener` and `GameHud.Callbacks` interfaces keep UI and game logic separate — `GameScreen` implements both.

## Key Files

| File | Role |
|------|------|
| `GameScreen.java` | Main game loop, rendering, event wiring (~475 LOC) |
| `GameState.java` | All persistent game state; crystal/ending logic |
| `Boss.java` | 7 boss types, factory method, per-type dialogue/defeat logic |
| `BattleManager.java` | Turn-based combat state machine |
| `CollisionManager.java` | Tile collision + portal trigger conditions |
| `SaveManager.java` | JSON save/load with `SaveIntegrity` validation |
| `GameHud.java` | Battle UI, pause menu, HUD callbacks |

## Dependencies

- **libGDX 1.14.0** — rendering, input, asset loading
- **Ashley 1.7.4** — entity-component system (used for entity management)
- **Blade-Ink 1.3.2** — Ink-based dialogue system
- **Universal Tween Engine 6.3.3** — animation tweening
- Java 8 source/target compatibility required

# code conventions
- Use model view controller (MVC). The code should be organized in packages:
    - model
    - view
    - controller
