# State Pattern – 2-D Adventure Game Design Document

> Based on `OOA_v2.png` and `requirement.md`

---

## 1. Requirements Summary

The game is a text-based, turn-based 2-D adventure on a grid.
Key rules that drive the design:

| Concern | Rule |
|---|---|
| Map | 2-D char array; each cell holds one MapObj |
| Character | Arrow symbol (↑→↓←) = facing direction; HP 300; one of {move, attack} per turn |
| Monster | Symbol M; HP 1; attacks adjacent Character for 50 dmg; otherwise moves randomly |
| Treasure | Symbol x; picked up by touching (role stays, treasure disappears) |
| Obstacle | Symbol □; blocks movement and attack lines |
| States | Character/Monster can hold exactly **one** state at a time; new state **overwrites** old |
| OCP requirement | Adding a new state must **not** modify Role / Character / Monster / Map |

---

## 2. OOA – Forces Identified

From the requirements we observe two forces:

**F-BV (Behaviour Varies with State)**
`Role` exhibits radically different behaviour in each state:
- HP deducted per turn (Poisoned)
- 2 actions per turn (Accelerated)
- No damage taken (Invincible)
- Movement restricted to one axis (Orderless)
- Attack range = entire map (Erupting)

**F-OCP (Open-Closed Principle)**
A naïve implementation puts `if (state == POISONED) … else if (state == INVINCIBLE) …` inside `Role`.
Every new state means editing `Role`, `Character`, and `Monster` – violating OCP.

**Design Problem:**
> How do we let `Role` change its runtime behaviour per state, while keeping
> `Role` / `Character` / `Monster` / `Map` **closed for modification** when new
> states are added?

---

## 3. OOD – Finite State Machine (FSM)

Before writing classes we model the legal state transitions:

```
                     touch KingRock
  Normal ──────────────────────────────► Stockpile
    ▲                                        │ 2 turns (or hit → Normal)
    │  expires / full HP                     ▼
    │◄──────────────────────────────── Erupting
    │                                        │ 3 turns
    │  touch Dokodemo / Erupting ends        ▼
    │◄──────────────────────────────── Teleport
    │                                        │ 1 turn → warp + Normal
    │
    ├── touch SuperStar ──► Invincible (2 turns)
    ├── touch Poison ──────► Poisoned  (3 turns)
    ├── touch AccPotion ───► Accelerated (3 turns; hit → Normal)
    ├── touch HealingPotion► Healing   (5 turns; full HP → Normal early)
    └── touch DevilFruit ──► Orderless  (3 turns)
```

**Special hit rules:**
- Any state except Stockpile / Accelerated: Character gains **Invincible** after taking damage.
- Stockpile / Accelerated: state reverts to **Normal**; no Invincible granted.
- Invincible: damage = 0; state stays.

---

## 4. OOD – Class Diagram

```
MapObj (abstract)
│  - symbol: char
│  + getSymbol(): char
│
├── Treasure (abstract)          ← one per cell; disappears on touch
│   + getGenerationRate(): float
│   + onTouch(Role): void        ← calls role.setState(new XxxState())
│   │
│   ├── SuperStar  (rate 0.10) → InvincibleState
│   ├── Poison     (rate 0.25) → PoisonedState
│   ├── AccPotion  (rate 0.20) → AcceleratedState
│   ├── HealingPotion (0.15)  → HealingState
│   ├── DevilFruit (rate 0.10)→ OrderlessState
│   ├── KingRock   (rate 0.10)→ StockpileState
│   └── Dokodemo   (rate 0.10)→ TeleportState
│
├── Obstacle                     ← impassable, blocks attack lines
│
└── Role (abstract)              ← STATE PATTERN CONTEXT
    │  - state: RoleState        ← the current state object
    │  - hp, maxHp, row, col
    │  + setState(RoleState)
    │  + onTurnStart()           ── delegates to state.onTurnStart(this)
    │  + onTurnEnd()             ── delegates to state.onTurnEnd(this)
    │  + receiveAttack(int)      ── delegates to state.onReceiveDamage(...)
    │  + takeDamage(int)         ← direct HP deduction (env damage: poison)
    │  + heal(int)               ← direct HP recovery  (env recovery: healing)
    │
    ├── Character
    │   - facing: Direction      ← symbol = facing.getSymbol()  (↑→↓←)
    │   + afterReceiveDamage()   ← grants Invincible unless prevented by state
    │
    └── Monster
        + getAttackDamage(): 50  ← AI handled by Map.monsterAct()


«interface» RoleState            ← STATE PATTERN STATE
│  + getName(): String
│  + onTurnStart(Role)           ← poison damage, heal recovery, orderless coin flip
│  + onReceiveDamage(int, Role)  ← Invincible→0, Acc/Stock→revert+dmg
│  + getActionsPerTurn(): int    ← Accelerated=2, others=1
│  + getAllowedMoveDirections()  ← Orderless={UP,DOWN} or {LEFT,RIGHT}; others null
│  + canAttack(): boolean        ← Orderless=false; others=true
│  + preventInvincibleAfterHit()← Stockpile, Accelerated = true
│  + isFullMapAttack(): boolean  ← Erupting=true; others=false
│  + onTurnEnd(Role)             ← decrement counter; fire state transitions
│
├── NormalState
├── InvincibleState   (2 turns)
├── PoisonedState     (3 turns)
├── AcceleratedState  (3 turns)
├── HealingState      (5 turns)
├── OrderlessState    (3 turns)
├── StockpileState    (2 turns → EruptingState)
├── EruptingState     (3 turns → TeleportState)
└── TeleportState     (1 turn  → needsTeleport flag + NormalState)


Map
│  - grid: MapObj[][]
│  - character: Character
│  - monsters: List<Monster>
│  + moveRole(Role, Direction)   ← touch resolution
│  + attackLine(Character)       ← normal attack
│  + attackAll(int damage)       ← Erupting full-map attack
│  + monsterAct(Monster)         ← adjacent→attack; else random move
│  + teleportRole(Role)
│  + spawnTreasure() / spawnMonster()
│  + printMap()


Game  (turn loop + Scanner input)
Direction  (enum: UP/RIGHT/DOWN/LEFT, symbol + dRow/dCol)
```

---

## 5. State Pattern – How It Works Here

### 5.1 Pattern roles

| Pattern role | This project |
|---|---|
| **Context** | `Role` (holds `state` reference, delegates) |
| **State interface** | `RoleState` |
| **Concrete states** | `NormalState`, `InvincibleState`, … (9 classes) |

### 5.2 Delegation flow

```
Game calls ch.onTurnStart()
  → Role delegates to state.onTurnStart(this)
      NormalState   → no-op
      PoisonedState → role.takeDamage(15)   // direct HP deduction, no filter
      HealingState  → role.heal(30)
      OrderlessState→ randomises allowed directions for this turn

Game calls ch.receiveAttack(50)
  → boolean prevent = state.preventInvincibleAfterHit()
  → int actual = state.onReceiveDamage(50, this)
      InvincibleState  → returns 0        (no damage)
      AcceleratedState → setState(Normal); returns 50  (hits + breaks acc)
      NormalState      → returns 50
  → if actual > 0: takeDamage(actual)
  → afterReceiveDamage(prevent)
      Character.afterReceiveDamage() → if !prevent: setState(InvincibleState)

Game calls ch.onTurnEnd()
  → state.onTurnEnd(this)
      StockpileState  → if(--turns<=0) setState(EruptingState)
      EruptingState   → if(--turns<=0) setState(TeleportState)
      TeleportState   → if(--turns<=0) { needsTeleport=true; setState(Normal) }
```

### 5.3 OCP compliance

| Scenario | Files changed |
|---|---|
| Add "Frozen" state (can't move) | Add `FrozenState.java` only |
| Add "FrozenBerry" treasure → Frozen | Add `FrozenBerry.java` + `FrozenState.java` |
| Add full-map state besides Erupting | Add new state; override `isFullMapAttack()=true` |
| **None of the above touch** | `Role`, `Character`, `Monster`, `Map`, `Game` |

This satisfies the OCP requirement from `requirement.md`:
> 在擴充新的狀態時，地圖/主角/怪物類別遵守 OCP

---

## 6. Package Structure

```
ch3_complexBV.StatePattern
├── Main.java               ← entry point
├── Game.java               ← turn loop + Scanner
├── Direction.java          ← enum (UP/RIGHT/DOWN/LEFT)
├── MapObj.java             ← abstract base for all grid objects
├── Map.java                ← grid, movement, attack, display
├── RoleState.java          ← State interface (8 methods)
├── Role.java               ← abstract Context; holds RoleState
├── Character.java          ← player; overrides getSymbol() + afterReceiveDamage()
├── Monster.java            ← enemy; HP=1, attack=50
├── Obstacle.java           ← impassable object
├── Treasure.java           ← abstract; onTouch(Role) sets state
│
├── treasures/
│   ├── SuperStar.java      → InvincibleState   (rate 0.10)
│   ├── Poison.java         → PoisonedState     (rate 0.25)
│   ├── AccPotion.java      → AcceleratedState  (rate 0.20)
│   ├── HealingPotion.java  → HealingState      (rate 0.15)
│   ├── DevilFruit.java     → OrderlessState    (rate 0.10)
│   ├── KingRock.java       → StockpileState    (rate 0.10)
│   └── Dokodemo.java       → TeleportState     (rate 0.10)
│
└── states/
    ├── NormalState.java         ← baseline; no effects
    ├── InvincibleState.java     ← 2 turns; damage = 0
    ├── PoisonedState.java       ← 3 turns; -15 HP/turn (onTurnStart)
    ├── AcceleratedState.java    ← 3 turns; 2 actions; hit → Normal
    ├── HealingState.java        ← 5 turns; +30 HP/turn; ends early at full HP
    ├── OrderlessState.java      ← 3 turns; random axis; no attack
    ├── StockpileState.java      ← 2 turns; hit → Normal; expires → Erupting
    ├── EruptingState.java       ← 3 turns; isFullMapAttack=true; expires → Teleport
    └── TeleportState.java       ← 1 turn; sets needsTeleport; expires → Normal
```

---

## 7. Key Design Decisions

### 7.1 `isFullMapAttack()` on RoleState (not instanceof check)

`Character.attack()` in `Game.playerAction()` does:
```java
if (ch.getState().isFullMapAttack()) {
    map.attackAll(50);
} else {
    map.attackLine(ch);
}
```
If we used `if (ch.getState() instanceof EruptingState)` instead, any future
"GlobalAttack" state would require editing `Game` – violating OCP.
By putting the decision in `isFullMapAttack()`, new states self-declare their
attack behaviour.

### 7.2 `preventInvincibleAfterHit()` avoids instanceof in Character

```java
// Role.receiveAttack()
boolean prevent = state.preventInvincibleAfterHit();  // true for Stockpile/Accelerated
int actual = state.onReceiveDamage(rawDamage, this);
if (actual > 0) {
    takeDamage(actual);
    afterReceiveDamage(prevent);   // Character-specific hook
}

// Character.afterReceiveDamage()
if (!preventInvincible) setState(new InvincibleState());
```
Without this flag, `Character` would need `instanceof StockpileState || instanceof AcceleratedState` – every new "cancels-invincible" state would require editing `Character`.

### 7.3 `takeDamage()` vs `receiveAttack()`

| Method | Purpose |
|---|---|
| `receiveAttack(int)` | External hostile hit; routed through state filter |
| `takeDamage(int)` | Environmental damage (poison, direct); bypasses filter |
| `heal(int)` | Environmental recovery (healing state) |

Poison calls `role.takeDamage(15)` inside `PoisonedState.onTurnStart()`.
If it called `receiveAttack(15)`, an Invincible role would take **no** poison damage – which is wrong per requirements.

### 7.4 Character's `getSymbol()` overrides MapObj

`MapObj` stores `symbol` as a `final char` set at construction.
`Character`'s symbol is the facing direction (changes each move).
By overriding `getSymbol()` to `return facing.getSymbol()`, the grid's
`cellStr()` always renders the correct arrow without any special-case code.

### 7.5 Treasure probabilities

Rates sum to exactly **1.0** (0.10+0.25+0.20+0.15+0.10+0.10+0.10).
`Map.randomTreasure()` uses cumulative thresholds on a single `[0,1)` random float.
`TREASURE_DENSITY = 0.25f` means ~25 % of cells receive a treasure during map init.

---

## 8. Turn Sequence

```
Loop each turn:
  1. ch.onTurnStart()                 ← state effects (Poison -15, Heal +30, etc.)
  2. printHUD() + map.printMap()
  3. for (a in 1..state.getActionsPerTurn()):   ← 2 if Accelerated
       playerAction(ch)               ← WASD move | F attack
  4. ch.onTurnEnd()                   ← state timer; possible transition
  5. if ch.needsTeleport: teleport
  6. for each monster:
       m.onTurnStart() → monsterAct() → m.onTurnEnd() → teleport if needed
  7. win/lose check
  8. if turn % 5 == 0: spawnTreasure + spawnMonster
```

---

## 9. Controls (runtime)

| Key | Action |
|---|---|
| `W` | Move Up (facing changes to ↑) |
| `A` | Move Left (facing changes to ←) |
| `S` | Move Down (facing changes to ↓) |
| `D` | Move Right (facing changes to →) |
| `F` | Attack in current facing direction (or full-map if Erupting) |

Orderless state overrides allowed directions each turn and disables `F`.
Accelerated state grants a second prompt within the same turn.
