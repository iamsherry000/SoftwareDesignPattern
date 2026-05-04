# OOAD 不符合或可改進事項

紀錄日期: 2026-04-15
測試結果: 10/10 全部 PASS(cheerup, curse, one-punch, only-basic-attack, petrochemical, poison, self-explosion, self-healing, summon, waterball-and-fireball-1v2)

---

## 1. Role 是 God Object / 違反 SRP
**位置**: `src/Role.java:11`, `Role.java:57`

Role 同時管 HP/MP/技能/狀態/所屬軍隊/觀察者,又直接 `System.out.println` 印死亡訊息。
領域物件不應處理 I/O — 應交給觀察者或 presenter。

---

## 2. 貧血模型 (Anemic Domain Model) — Game 做太多
**位置**: `src/Game.java:94` (`playTurn`), `Game.java:111` (`chooseAffordableAction`)

`Game.playTurn()` 控制了回合全部流程:印抬頭、觸發狀態、選技能、找目標池、施放、推進狀態。應下放:
- `targetPool()` 和目標選擇邏輯其實屬於 `Skill`/`Role`,而不是 Game 計算 `ally / enemy` 再傳入。
- `chooseAffordableAction()` 的 MP 驗證可以由 Player 或 Role 負責。

---

## 3. Skill 基類有靜態工具方法 `dealDamage`
**位置**: `src/skill/Skill.java:39`

`static` 方法破壞多型,且又做 I/O。傷害結算應該是 `Role.receiveDamage` 的職責,
輸出訊息給觀察者/logger。

---

## 4. BasicAttack 直接 println,與其他技能不一致
**位置**: `src/skill/BasicAttack.java:17-18`

技能類別混入 presentation,與其他用 `announce()` + `dealDamage()` 的技能不一致。

---

## 5. 輸出與業務邏輯耦合
整個專案 `System.out.println` 散落各處 (Game、Role、Skill 子類、Observer)。
沒有抽象 `GameLogger` / `Presenter`,要換成 GUI 或寫檔得改很多檔。

---

## 6. Player.chooseAction 夾帶 UI
**位置**: `src/player/Player.java:27-32`

`printActionMenu` 在 AIPlayer 也會被呼叫 → AI 對局時仍印「選擇行動:」,實際上 AI 不需要這個選單。
UI 應只屬於 `HumanPlayer`。

---

## 7. Game 內為召喚出的 Slime 臨時補 controller
**位置**: `src/Game.java:99`

`controllers.computeIfAbsent(role, r -> new AIPlayer())` 耦合了「召喚」這個特殊情境。
較乾淨的做法是 `Troop.addRole` 事件通知 Game,或讓 Role 自帶 controller。

---

## 8. Role.getTroopId() 依賴 troop 非 null
**位置**: `src/Role.java:38`

Role 建立時 troop 為 null,若在 `addRole` 前呼叫 display/getTroopId 會 NPE — 隱含的時序耦合。

---

## 9. AIPlayer.seed 是實例狀態,但行為屬於「全局順序」
**位置**: `src/player/AIPlayer.java:9`

若專案規格要求全局共享 seed(RPG 題目常見),目前每個 AIPlayer 獨立 seed — 雖然測試過了但邏輯上耦合。
需對照 `requirement.md` 確認。

---

## 10. Summon 直接操作 `actor.getTroop().addRole`
**位置**: `src/skill/Summon.java:25`

技能繞過 Role 直接改 Troop,違反 **Law of Demeter**。

---

## 建議改進優先順序

1. 把所有 `System.out` 抽出 `GameLogger` 介面(最大收穫,改動不難)
2. 把 `printActionMenu` 移入 `HumanPlayer`
3. `Skill.dealDamage` 改為 instance 方法或移入 Role
4. Role 死亡訊息改由 Observer 發布
