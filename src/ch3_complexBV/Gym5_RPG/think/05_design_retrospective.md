# Gym5_RPG 設計回顧

整題完成後的全局回顧。記錄每個設計決策的 **Force（壓力／問題）→ Solution（解法）** 對應，
以及為什麼選這個 pattern、不選另一個。

---

## 全局概觀

### 題目需求複雜度
- **2 軍 × 多角色**的回合制對戰
- **11 種技能**，行為差異巨大（單體、群體、無目標、自我、自爆、狀態施加、依狀態分叉）
- **4 種狀態**，每種改變 Role 的回合行為
- **觀察者需求**：Slime 死亡回血召喚者、Curse 死亡回血施咒者
- **複雜技能 OnePunch**：4 條由上往下取第一個成立的規則
- **人類 vs AI 雙 Player 體系**

### 最終落地的設計模式
1. **State Pattern** — Role 的狀態行為多變
2. **Template Method** — Player.chooseAction / .chooseTargets 的流程骨架
3. **Observer Pattern** — 死亡事件觸發多種反應（Slime / Curse）
4. **Chain of Responsibility** — OnePunch 的規則依序比對

### 最終 testcase 結果
10 / 10 全通過：
```
only-basic-attack, poison, cheerup, petrochemical, self-healing,
waterball-and-fireball-1v2, self-explosion, summon, curse, one-punch
```

---

## Force → Solution 對照表

### Force 1：Role 的行為依狀態劇烈變化

**表現症狀**
- 中毒：回合開始 -30 HP
- 石化：本回合跳過所有行動（S1/S2/S3）
- 受到鼓舞：每次造成傷害時每位被害者 +50 加成
- 正常：無特殊效果

**如果不套 pattern 會怎樣？**

```java
// 想像的反面教材：Game.playTurn 裡
if (role.state == POISONED) {
    role.hp -= 30;
    if (role.hp <= 0) ...
} else if (role.state == PETROCHEMICAL) {
    return;  // skip turn
}

// 然後在 BasicAttack.execute 裡
if (actor.state == CHEERUP) {
    damage += 50;
}
// 每個 Skill 都要這樣 if...
```

**痛點**
- 加新狀態 → 改 Game / 改每個 Skill → 違反 OCP
- `instanceof ConcreteState` 遍佈高層邏輯 → code smell
- 狀態的效果散在多個檔案 → 違反 SRP

**解法：State Pattern**
```java
interface State {
    String getName();
    default void onTurnEffect(Role role) { }
    default State nextState() { return this; }
    default boolean canAct() { return true; }
    default int bonusDamagePerVictim() { return 0; }
}
```
- 每個狀態 override 需要的 method
- `NormalState` 全部用 default（**Null Object Pattern**）
- Game 只呼叫 `state.onTurnEffect(role)`、`state.canAct()`
- Skill 只呼叫 `actor.getState().bonusDamagePerVictim()`

**OCP 驗證**：新增 `FrozenState` → 只新增一個檔案，不動既有 class。

---

### Force 2：Skill 的 target pool 各自不同

**表現症狀**
- BasicAttack / Waterball / Poison / Curse / OnePunch：1 位敵軍
- Fireball：全部敵軍
- Cheerup：3 位友軍（不含自己）
- SelfHealing：自己
- SelfExplosion：全場活人（不含自己）
- Summon：無目標

**不套 pattern 的反面教材**
```java
// Game.determineCandidates
if (skill instanceof Cheerup) return ally.alive;
else if (skill instanceof SelfHealing) return List.of(actor);
else if (skill instanceof SelfExplosion) return allAlive - actor;
// ... 11 個 if-else
```

**解法：Skill 自己知道打誰**
```java
public abstract class Skill {
    public List<Role> targetPool(Role actor, Troop ally, Troop enemy) {
        return enemy.getAliveRoles();   // 預設：打敵人
    }
}

// Cheerup 覆寫
public List<Role> targetPool(Role actor, Troop ally, Troop enemy) {
    return ally.getAliveRoles().stream()
        .filter(r -> r != actor).toList();
}
```

**為什麼不給 Skill 整個 Battle 物件？**
- 權力過大違反 LoD（迪米特法則／最小知識原則）
- Skill 會依賴 Battle 的 API，耦合度飆升

**給 `(actor, ally, enemy)` 剛剛好** — Skill 需要什麼就拿什麼，不多不少。

---

### Force 3：Player 有共同流程但決策方式不同

**表現症狀**
- 人類：從 Scanner 讀 int
- AI：用 seed 公式算 index
- 但兩者都要：印選單、組 skill 物件、處理 multi-target

**解法：Template Method Pattern**
```java
public abstract class Player {
    public Skill chooseAction(List<Skill> skills) {
        printActionMenu(skills);                 // 共同
        int idx = pickIndex(skills.size());      // 抽象
        return skills.get(idx);                  // 共同
    }
    protected abstract int pickIndex(int count);
}
```

**踩過的坑（testcase 發現的）**
原本以為印「選擇目標」選單也放父類共用，結果 testcase 顯示 **AI 不印目標選單**。改成：
- 動作選單：父類印（Human + AI 都印）
- 目標選單：HumanPlayer 自己在 `pickTargets` 裡印（AI 不呼叫）

**教訓**：Template Method 的 template（固定部分）要確保真的是共同的。如果只有某個子類才做，就該下放到子類。

---

### Force 4：死亡事件觸發多種反應

**表現症狀**
- Slime 死亡 → 召喚者 +30 HP
- Curse 目標死亡 → 施咒者 += target.mp
- 未來可能：死亡觸發復活、掉寶、成就...

**不套 pattern 的反面教材**
```java
// Role.receiveDamage 裡
if (hp <= 0) {
    if (this is Slime and summoner.isAlive()) summoner.heal(30);
    if (this.curseStarters != null) for (c : curseStarters) c.heal(mp);
    // 每加一種就改這裡 ← 違反 OCP
}
```

**解法：Observer Pattern**
```java
interface DeathObserver {
    void onDeath(Role dead);
}

class Role {
    private final List<DeathObserver> deathObservers = new ArrayList<>();
    public void addDeathObserver(DeathObserver o) { ... }
    public void receiveDamage(int damage) {
        ...
        if (hp <= 0) {
            System.out.println(display() + " 死亡。");
            for (DeathObserver o : deathObservers) o.onDeath(this);
        }
    }
}
```

**兩個 observer 的實作**
- `SlimeObserver(summoner)` — onDeath 回血 summoner
- `CurseObserver(caster)` — onDeath 把 dead.mp 給 caster

**OCP 驗證**：加「死亡掉寶」功能 → 新增 `DropLootObserver`，不動 Role。

**踩到的 edge case**：同施咒者多次 Curse 不疊加。在 `Curse.execute` 裡用 instanceof + caster 比對做去重，**不污染 Role**：
```java
private boolean alreadyCursedBy(Role target, Role caster) {
    for (DeathObserver o : target.getDeathObservers()) {
        if (o instanceof CurseObserver c && c.getCaster() == caster) return true;
    }
    return false;
}
```

---

### Force 5：OnePunch 依目標狀態有 4 種效果

**表現症狀**
```
1. HP ≥ 500 → 300 damage
2. 中毒 or 石化 → 3 次 80 damage
3. 受到鼓舞 → 100 damage + 轉 Normal
4. 正常 → 100 damage
```
**從上往下取第一個成立的規則**。

**為什麼 State Pattern 在這裡不夠？**
- State Pattern 的 method 是「**狀態被問問題時回答**」（例如 `bonusDamagePerVictim`）
- 這裡是「**來自外部的技能依狀態分叉**」→ 邏輯歸 Skill，不歸 State
- 如果硬塞進 State，會變成 `state.onBeingPunched(...)` — 狀態承擔不該有的責任（State 不該知道 OnePunch 這個具體技能）

**解法：Chain of Responsibility**
```java
abstract class PunchHandler {
    private PunchHandler next;
    public PunchHandler setNext(PunchHandler next) { this.next = next; return next; }
    public final void handle(Role actor, Role target) {
        if (applies(target)) apply(actor, target);
        else if (next != null) next.handle(actor, target);
    }
    protected abstract boolean applies(Role target);
    protected abstract void apply(Role actor, Role target);
}
```

4 個 concrete handler + OnePunch 組裝：
```java
chain = new HighHpHandler();
chain.setNext(new PoisonedPetroHandler())
     .setNext(new CheerupHandler())
     .setNext(new NormalHandler());
```

**為什麼不用 List + first-match loop？**
功能上等價，但 CoR 用**鏈式結構**明白宣告「誰接誰」，順序感更清楚。對教學／展示 pattern 這題，classical CoR 是更好的示範。

**NormalHandler 當 default fallback**：`applies()` 永遠回 true，代替「chain 尾端 null check」。

---

## 不該做的事（抵抗過的誘惑）

### 誘惑 1：Skill 回傳 `List<String>` 讓 Game 印 log
**抵抗理由**（參見 `think/04`）
- Game 拿到 list 也只是原封不動 println → **零價值抽象**
- YAGNI 凌駕 SRP

### 誘惑 2：用 null 代表「正常狀態」
**抵抗理由**（參見 `think/02`）
- 呼叫端會長滿 null check
- Null Object 讓 NormalState 是一等公民

### 誘惑 3：Role 存 `isAlive` / Troop 存 `aliveRoles`
**抵抗理由**（Single Source of Truth）
- 能從 `hp > 0` 推導 → 不該存欄位
- 兩個欄位表達同一件事 = 兩個 bug 來源

### 誘惑 4：Skill 做成 interface（看起來純粹）
**抵抗理由**（參見 `think/03`）
- 11 個 Skill 共用 name/mpCost/targetAmount → interface 版本會有 33 次 getter 重複
- 沒有多重繼承需求 → abstract class 更適合

### 誘惑 5：`instanceof ConcreteState` 散落在 Skill / Role
**抵抗理由**
- 違反 State Pattern 精神
- 唯一允許的地方：OnePunch 的 PunchHandler（為了「特定技能依狀態分叉」這個 Force 而存在的合理邊界）

---

## 各層依賴關係

```
Main
 └─ Game
     ├─ Scanner (input)
     ├─ Troop × 2 ──── Role (many)
     │                  ├─ State (current)
     │                  ├─ List<Skill>
     │                  └─ List<DeathObserver>
     ├─ Map<Role, Player>
     │        └─ HumanPlayer / AIPlayer
     └─ (uses) Skill, State, DeathObserver via polymorphism
```

**依賴方向**：Game → (Troop, Role, Player, Skill, State, Observer)
**沒有循環依賴**。

---

## 可攜帶的心法

### 心法 1：Force 決定 Pattern，不是 Pattern 決定設計
每個 Pattern 都先問：**它解決的是什麼 Force？我現在有這個 Force 嗎？**
- 沒有 Force 就套 Pattern = 過度設計
- 有 Force 才套 Pattern = 解決實際問題

### 心法 2：每個 Pattern 要通得過「加 1」的 OCP 測試
- 新加 1 個 State → 只加 1 個檔案？
- 新加 1 個 Skill → 只加 1 個檔案（+ SkillFactory 1 行）？
- 新加 1 個 Observer → 只加 1 個檔案？
- 新加 1 個 Punch Rule → 只加 1 個檔案 + chain 裝配 1 行？

全部通過 ✅。

### 心法 3：Bottom-Up 開發搭配小切片 + testcase
- 寫完底層就寫 testcase 驗證
- 早期 testcase 幫你**發現規格假設錯誤**（例如 AI 不印目標選單）
- 最小垂直切片能 run → 再往上堆

### 心法 4：抽象化要有「實質價值」
- 加一層抽象前先問：有誰會真的用到這層換取的能力？
- 答案是「沒有人」= 零價值抽象，不加
- 答案是「未來也許」= YAGNI，不加
- 答案是「現在就有 2+ 個實作需要」= 加

### 心法 5：規格藏在 testcase 細節裡
- 文件不會寫的：AI 不印目標選單、MP 不足的重選流程、空 targets 的 announce 格式
- testcase 就是第二份規格 — **差一個字都不能放過**

---

## 檔案清單（最終）

```
src/
├─ Main.java
├─ Game.java
├─ Role.java
├─ Troop.java
├─ state/
│  ├─ State.java                (interface + default methods)
│  ├─ NormalState.java          (Null Object)
│  ├─ PoisonedState.java
│  ├─ PetrochemicalState.java
│  └─ CheerupState.java
├─ skill/
│  ├─ Skill.java                (abstract)
│  ├─ SkillFactory.java
│  ├─ BasicAttack.java
│  ├─ Waterball.java
│  ├─ Fireball.java
│  ├─ SelfHealing.java
│  ├─ SelfExplosion.java
│  ├─ Poison.java
│  ├─ Petrochemical.java
│  ├─ Cheerup.java
│  ├─ Summon.java
│  ├─ Curse.java
│  ├─ OnePunch.java
│  └─ punch/
│     ├─ PunchHandler.java      (Chain of Responsibility base)
│     ├─ HighHpHandler.java
│     ├─ PoisonedPetroHandler.java
│     ├─ CheerupHandler.java
│     └─ NormalHandler.java
├─ player/
│  ├─ Player.java               (abstract, Template Method)
│  ├─ HumanPlayer.java
│  └─ AIPlayer.java
└─ observer/
   ├─ DeathObserver.java        (Observer interface)
   ├─ SlimeObserver.java
   └─ CurseObserver.java

think/
├─ 01_state_pattern_QA.md
├─ 02_null_object_pattern.md
├─ 03_interface_vs_abstract_class.md
├─ 04_pushback_skill_printing.md
└─ 05_design_retrospective.md   ← 本檔
```
