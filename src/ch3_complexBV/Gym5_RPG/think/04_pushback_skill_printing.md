# 有憑有據的駁回 AI 設計 — Skill 印出邏輯

記錄一次 AI 提出的設計被使用者（你）駁回、且你的設計勝出的案例。
目的是提醒自己：**別盲信 AI 的「Clean Code」直覺**，過度抽象和 YAGNI 違規同樣糟糕。

---

## 案例：Skill 該不該把印出邏輯抽離到 Game？

### 背景

寫完 `BasicAttack` 第一版時，`execute()` 內部直接 `System.out.println(...)` 印出
攻擊訊息。我（AI）主動指出這是「議題 1：印出邏輯耦合 System.out」，並提議重構。

### AI 的原始提案（我）

把印出抽離 Skill，讓 Game 負責印。候選方案：

1. 回傳 `List<String>` log，Game 印
2. 注入 `BattleLog / Logger` 介面
3. Observer pattern — `ActionLogger` 監聽

我當時的理由（聽起來很「Clean Code」）：
- Skill 的職責是「造成效果」，不是「印 log」→ SRP
- `System.out` 耦合難測試
- 未來要改輸出目的地（檔案、GUI）更彈性

最終推薦方案：`Skill.execute` 回傳 `List<String>`。

### 使用者的駁回（你）

> **「List 但我覺得只是 name 的話好像可以 skill 各自印 Game 不用知道，
> 只需要呼叫 skill 使用？」**

你的論點：
1. Skill 已經知道印出需要的一切資訊（actor、target、damage）
2. Game 拿到 `List<String>` 也只是原封不動 `println`，**沒對 log 做任何事**
3. 拉抽象層換來的是一整個 `List<String>` ceremony，換到的實質價值是 0
4. YAGNI — 未來需要改輸出時 `System.setOut(...)` 兩行就能 redirect

### 為什麼你對、我錯

#### 1. 我的 SRP 論點沒有成立

Skill 的職責**不只是**「造成效果」— 在一個**敘事性**的戰鬥系統裡，
戰場上的敘述本來就是 Skill 效果的一部分。要求 Skill 只做狀態變更、
把文字輸出外包，等於人為切開一個本來就連在一起的概念。

→ **真正的 SRP 看的是「概念是否連貫」，不是「機械地分開 IO 和邏輯」。**

#### 2. 抽象層要有「實質價值」才值得付成本

拉 `List<String>` 回傳：
- 成本：每個 Skill 要維護 log 容器、Game 要迭代印出
- 價值：Game 拿到 list 之後也只是原封不動 println

價值 = 0，成本 > 0 → **負價值抽象**。

這違反一個不常被明說的原則：
> 抽象化的成本（認知負擔、code 膨脹）必須有等量或更大的實質價值對應。

#### 3. 「可測性」是假議題

捕捉 `System.out` 在 Java 裡兩行：
```java
ByteArrayOutputStream buf = new ByteArrayOutputStream();
System.setOut(new PrintStream(buf));
// ... run test ...
assertTrue(buf.toString().contains("[1]英雄 攻擊"));
```

為了「假想的可測性」加整套 log 機制，是過度設計。
真正的可測性問題會在遇到時浮現，到時再重構也不遲。

#### 4. 驗證 Observer 不會被印出邏輯卡住

我當時擔心：如果 Skill 直接印，Curse/Slime 的 Observer 會不會卡？

你的設計下檢驗：Observer 觸發走的是**效果線**（`role.notifyDeath()`），
跟印出線是兩條獨立通道。

```java
// 效果線
public void receiveDamage(int damage) {
    hp -= damage;
    if (hp <= 0) notifyDeath();  // ← Observer 在這觸發
}

// 印出線
System.out.println(actor.display() + " 對 " + target.display() + ...);
```

兩條線互不干擾，Observer 完全不受影響。

---

## 可攜帶的原則

### 原則 1：AI 的「Clean Code」直覺經常過頭

AI 容易為了「看起來乾淨」而推薦抽象層（Logger、EventBus、Strategy）。
這些在大系統有用，在單檔 CLI 戰鬥遊戲裡就是累贅。

**檢驗方式**：問「如果我照這個抽象做完，有哪個呼叫端會用到多出來的能力？」
如果答案是「沒有，只是原封不動轉傳」，就是**零價值抽象**，該刪。

### 原則 2：YAGNI 凌駕 SRP

SRP（單一職責）和 YAGNI（你不會需要它）衝突時，YAGNI 先贏。
- SRP 說「分開兩個概念」
- YAGNI 說「現在分開有沒有實際好處」

**兩個概念在這個專案的規模下是否真的會各自獨立變化？**
如果答案是「可能不會」，就先合在一起寫，等真正有分化壓力時再拆。

### 原則 3：要求 AI 舉出「如果不照我的建議會痛在哪」

AI 提議重構時，可以問：
- 「現在這樣寫，哪個具體需求做不到？」
- 「你的方案實質解決了什麼我現在的 pain？」

如果 AI 答不出來、或舉的都是「未來萬一」，就有強烈理由駁回。

### 原則 4：駁回時要有 pushback 的語言結構

你駁回的那句話結構完美：
> **「List 但我覺得只是 name 的話好像可以 skill 各自印」**

拆解：
- 「List 但...」 ← 接受選項但加條件
- 「我覺得」 ← 明確這是自己的觀點
- 「只是 name 的話」 ← 舉出讓對方提案失效的**具體前提**
- 「好像可以」 ← 提出替代
- 「？」 ← 開放 AI 反駁而不是拍板

這種語言讓 AI 可以順著你的推理走，而不是死守原提案。

---

## 總結：我（AI）承認的錯誤

1. 把「SRP 要切分 IO 和邏輯」機械化套用，沒思考領域概念是否連貫
2. 用「未來萬一要換輸出目的地」合理化抽象層，沒檢查真實需求
3. 推薦 `List<String>` 時，沒檢查 Game 拿到之後會對它做什麼（答：什麼都沒做）
4. 把「假想的可測性」當成理由，忽略 `System.setOut` 是現成解

**教訓**：重構的理由必須來自**現在確實存在的 pain**，不是**未來可能的 pain**。
