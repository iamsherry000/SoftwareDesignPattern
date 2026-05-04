# Null Object Pattern — 為什麼 NormalState 不能用 null 取代

## 情境

Role 有一個 `state` 欄位。最初版的 `NormalState` 內容很少（只回傳名字 `"正常"`，其他狀態才有特殊行為）。很直覺地會問：

> **為什麼不直接讓 `state = null` 代表正常狀態？**
> 這樣還能少寫一個類別。

答案：**絕對不要用 null 代表「正常狀態」。**

---

## 理由 1 — 避免呼叫端長滿 null check（Null Object Pattern 的核心價值）

### 用 null 的壞處

```java
// 每個用到 state 的地方都要防禦式檢查
String stateName = (role.getState() == null)
    ? "正常"
    : role.getState().getName();

int dmg = (role.getState() == null)
    ? base
    : role.getState().modifyDamage(base);

if (role.getState() != null) {
    role.getState().onTurnStart(role);
}
```

問題：
- 每個呼叫點都要記得檢查 → **容易漏**
- 呼叫端程式碼變長、變醜
- 「正常狀態」的預設行為**散落**在各個呼叫點的 else 分支

### 用 NormalState 的好處

```java
// 呼叫端永遠可以直接呼叫，不用防禦
String stateName = role.getState().getName();
int dmg = role.getState().modifyDamage(base);
role.getState().onTurnStart(role);
```

「正常狀態 = 什麼都不做」的預設行為**集中在 NormalState 自己**，呼叫端保持乾淨。

> **Null Object Pattern 的核心價值**：
> 讓呼叫端永遠可以直接呼叫方法，不需要 `if (x != null)` 防禦。

---

## 理由 2 — 多型 vs 特例判斷（對 State Pattern 特別重要）

| 設計 | 「正常狀態」的地位 |
|---|---|
| `state = null` | **特例**，不在多型體系內。需要特殊處理 |
| `state = new NormalState()` | **一等公民**，和其他狀態平起平坐 |

### State Pattern 在狀態轉移時的差別

```java
// 用 NormalState — 乾淨、對稱
role.setState(new NormalState());
role.setState(new PoisonedState());
role.setState(new CheerupState());

// 用 null — 讀起來像 bug
role.setState(null);              // ← 看起來像忘了傳參數
role.setState(new PoisonedState());
```

狀態轉移的語意是「切換到某個具體狀態」，`null` 在語法上表達的是「沒有值 / 缺失」，**語意錯位**。

---

## 理由 3 — 擴充性

一開始 `NormalState` 可能只有 `getName()`。但未來幾乎必然會擴充：
- 需要 `modifyDamage(base) → return base`（預設行為）
- 需要 `onTurnStart(role) → do nothing`（預設行為）
- 需要 `canAct() → return true`（預設行為）

如果用 null，這些**預設行為**全都要散落在呼叫端的 if-else；用 NormalState，則是集中在一個類別裡的 override。

此外：**戰鬥中途可能從其他狀態轉回 NormalState**（例如 OnePunch 打到 CheerupState 目標時會恢復正常），這時 `new NormalState()` 就是最自然的表達。

---

## 通用結論

> 當你發現某個類別「內容很少、幾乎什麼都不做」時，不要急著刪掉它或用 null 取代。
>
> **問自己**：這個「什麼都不做」是不是代表某個領域概念的預設行為？
> 如果是 → 它就是一個 Null Object，該留下來。

### 判斷是不是 Null Object 的準則

1. 它是否參與多型體系？（別的類別也 implements 同一 interface）→ 是 = 留下
2. 呼叫端是否會頻繁存取這個欄位？→ 是 = 留下（避免 null check）
3. 它代表的是「缺失」還是「預設」？→ 「預設」= 留下

---

## 延伸到本專案的對應

| Null Object 候選 | 代表的預設行為 |
|---|---|
| `NormalState` | 狀態沒有特殊效果 |
| （未來可能）`NoSkillList` | 角色沒有技能（Slime 場合） |

Slime 在 Summon 時建立，有 `0 MP、無任何技能`。如果技能是 `List<Skill>`，空 list 已經是天然的 Null Object；不需要再設計一個類別。
