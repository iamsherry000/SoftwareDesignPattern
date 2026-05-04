# State Pattern — 概念 QA 整理

本文件整理 Gym5_RPG 題中，關於 **State Pattern** 的 clean code + 設計概念問答。
每一題的目的都是驗證「每一項設計決策都富有根據」。

---

## Q1：State Pattern 守住 OCP 的驗證

**問**：如果新增一個 `FrozenState`（凍結 2 回合無法行動），預期要改動哪些檔案？

**答**：只新增一個 `FrozenState` 類別繼承 `State`，**不動** `Role` / `Skill` 及任何既有狀態類別。

**為什麼這代表 State Pattern 用對了？**

- 新狀態 = 新檔案，既有程式碼**關閉修改、開放擴充** → OCP ✅
- 如果答案裡出現 `Role.java` 或 `Skill.java` 要改，就代表狀態相關的行為沒被多型吃掉，還有 `if-else` 或 `instanceof` 殘留在 Role / Skill 裡。

---

## Q2：CheerupState 的加成傷害要寫在哪裡？

### Context

- `CheerupState` 效果：此角色執行**每一個能造成傷害的行動**中，對**每位被害者** +50 加成傷害。
- 傷害的計算發生在 `Skill.execute()`（例如 `Fireball` 對每個目標扣 50 HP）。
- 但「+50 加成」的規則屬於 `CheerupState`。

**核心問題**：`Fireball.execute()` 要怎麼知道「施法者是 Cheerup 狀態，所以傷害要加成」？

### 三個選項

- **A**：`Fireball` 裡寫 `if (actor.state instanceof CheerupState) damage += 50`
- **B**：`Skill` 呼叫 `actor.state.modifyDamage(baseDamage)`，由 State 自己回傳加成後的傷害
- **C**：其他方式

### 結論：選 B

---

## Q3：為什麼選 B？為什麼不選 A？

### A 方案違反了什麼？

1. **違反 OCP**
   - 今天加一個 `BerserkState`（攻擊 +100），要**回頭改每一個會造成傷害的 Skill**：
     Fireball、Waterball、BasicAttack、SelfExplosion、OnePunch…
   - 一個新狀態 = 修改 N 個既有檔案。這正是 State Pattern 要消滅的問題。

2. **違反 DIP（依賴反轉原則）**
   - `Fireball`（高層策略：火球怎麼打）不該依賴 `CheerupState`（低層具體狀態）。
   - Skill 本來只該知道「我要造成 X 傷害」，現在卻被迫知道所有具體狀態的存在。

3. **`instanceof ConcreteState` 是 code smell**
   - 只要看到 `instanceof` 配上具體子類別，幾乎都意味著：
     「這段行為應該被多型吃掉，但被條件判斷硬塞回來了。」
   - State Pattern 的**唯一目的**就是消滅這種 if-else / instanceof。

4. **違反 SRP（單一職責原則）**
   - `Fireball` 的職責 = 「對所有敵軍造成 50 傷害」
   - 不該兼任「知道 Cheerup 怎麼加成」
   - 加成邏輯的主人是 `CheerupState` 自己。

### B 方案為什麼對？

1. **守住 OCP**
   - 新增 `BerserkState` → 只新增一個類別並 override `modifyDamage()`。
   - Skill 一行不動。

2. **職責歸位**
   - 「加成怎麼算」是 State 的職責 → State 自己回答。
   - Skill 只負責「算出基礎傷害 → 交給狀態過濾」。

3. **對稱性 / 一致風格**
   - `State` 抽象裡已有 `before_action()`、`handle()`、`next()`。
   - 再多一個 `modifyDamage(int baseDamage): int`（預設回傳原值）完全符合既有風格：
     - `NormalState` 不覆寫（或覆寫為 `return baseDamage;`）
     - `CheerupState` 覆寫為加成邏輯

---

## Q4：判斷「某行為是否該放進 State interface」的準則

當某個行為需要**依當前狀態分叉**時，就是訊號。用兩個測試題自問：

1. **加新狀態**時，這個行為會不會被迫動 Role / Skill？
   - 會 → 該抽進 State interface
2. **加新 Skill**時，這個 Skill 會不會被迫知道所有具體狀態？
   - 會 → 該抽進 State interface

`modifyDamage` 通過這個測試 → 屬於 State。

這個準則也可反過來用：如果一個方法**不需要依狀態分叉**（所有狀態行為都一樣），就不該塞進 State interface，否則是過度設計。

---

## 待續 — 尚未討論的問題

### 🔜 Q5（下次）：`modifyDamage` 的簽名怎麼設計？

Fireball 打 4 個敵人時，CheerupState 加成規則：

- 選項 1：`int modifyDamage(int baseDamage)` — 單一傷害
- 選項 2：`int modifyDamage(int baseDamage, int victimCount)` — 知道人數
- 選項 3：`int modifyDamagePerVictim(int baseDamage)` — 每位被害者分開算

哪個簽名最能同時表達需求（每位被害者 +50）且最少語意耦合？留待後續討論。

---

## 關鍵記憶點（Cheat Sheet）

| 原則 | 違反訊號 | 解法 |
|---|---|---|
| OCP | 加新狀態要改既有 class | 用 State Pattern，新狀態=新類別 |
| DIP | 高層類別 import 具體狀態類 | 只依賴 State 抽象 |
| SRP | 一個類別兼任多種職責 | 行為歸位到該狀態的類別裡 |
| 消除 code smell | `instanceof ConcreteState` | 把分叉行為抽成 State interface 的方法 |
