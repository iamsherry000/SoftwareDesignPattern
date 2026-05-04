# Interface vs Abstract Class — Skill 的抉擇

## 情境

寫 `Skill` 時要決定：做成 `interface` 還是 `abstract class`？

直覺反應：**「介面當入口」** → 選 `interface`。

這個直覺不完全錯，但不夠深。以下是完整思考過程。

---

## 第一輪：interface 的問題浮現

假設選 `interface Skill`：

```java
public interface Skill {
    String getName();
    int getMpCost();
    int getTargetAmount();
    void execute(Role actor, List<Role> targets);
}
```

然後實作 11 個 Skill（BasicAttack、Fireball、Waterball、Poison、Summon、
SelfExplosion、Cheerup、Curse、OnePunch、SelfHealing、Petrochemical）：

```java
public class BasicAttack implements Skill {
    private final String name = "普通攻擊";
    private final int mpCost = 0;
    private final int targetAmount = 1;
    public String getName() { return name; }
    public int getMpCost() { return mpCost; }
    public int getTargetAmount() { return targetAmount; }
    public void execute(Role actor, List<Role> targets) { ... }
}

public class Fireball implements Skill {
    private final String name = "火球";        // ← 又寫一次
    private final int mpCost = 20;              // ← 又寫一次
    private final int targetAmount = -1;        // ← 又寫一次
    public String getName() { return name; }   // ← getter 又寫一次
    public int getMpCost() { return mpCost; }  // ← getter 又寫一次
    public int getTargetAmount() { return targetAmount; }
    public void execute(Role actor, List<Role> targets) { ... }
}
```

**問題**：11 個 Skill × 3 個欄位 + 3 個 getter = **33 行以上的重複**。

明顯違反 **DRY (Don't Repeat Yourself)**。

**根本原因**：Java interface 不能有實例欄位（只能有 `public static final` 常數），
所以共用狀態無處可放。

---

## 第二輪：abstract class 解決了什麼？

```java
public abstract class Skill {
    private final String name;
    private final int mpCost;
    private final int targetAmount;

    protected Skill(String name, int mpCost, int targetAmount) {
        this.name = name;
        this.mpCost = mpCost;
        this.targetAmount = targetAmount;
    }

    public String getName()       { return name; }
    public int getMpCost()        { return mpCost; }
    public int getTargetAmount()  { return targetAmount; }

    public abstract void execute(Role actor, List<Role> targets);
}

// 子類超乾淨：
public class BasicAttack extends Skill {
    public BasicAttack() { super("普通攻擊", 0, 1); }

    @Override
    public void execute(Role actor, List<Role> targets) {
        Role target = targets.get(0);
        target.receiveDamage(actor.getStrength());
    }
}
```

**消除了什麼？**
- 3 個欄位只宣告一次 ✅
- 3 個 getter 只寫一次 ✅
- 子類只剩「自己獨特的部分」— 建構子傳值 + execute 實作 ✅

---

## 第三輪：那「介面當入口」的直覺要不要放棄？

**不用放棄，因為這個直覺其實被誤解了。**

「介面當入口」的真正意涵是：
> 外部程式碼應該依賴**抽象**（契約），不是依賴**具體實作**。

例如：
```java
Skill skill = new BasicAttack();   // ← 變數型別是 Skill，而不是 BasicAttack
skill.execute(actor, targets);
```

**關鍵洞察**：`abstract class Skill` **一樣是抽象**！你不能 `new Skill()`。
它和 `interface Skill` 一樣滿足「依賴抽象」的原則，只是多了共用實作。

所以：
- ❌ 誤解：「入口必須是 `interface` 關鍵字」
- ✅ 正解：「入口必須是**抽象型別**」— 兩者都算

---

## 第四輪：interface vs abstract class 的真正差別

| 面向 | interface | abstract class |
|---|---|---|
| 能放實例欄位 | ❌ 只能放常數 | ✅ |
| 能有建構子 | ❌ | ✅ |
| 能繼承多個 | ✅（可以 implements 多個） | ❌（Java 單一繼承） |
| 能有實作（method body） | ⚠️ 只能 default method | ✅ |
| 表達「is-a」 vs 「can-do」 | 偏 can-do（能力） | 偏 is-a（本質） |

**真正的判斷準則**：
1. **需要共用狀態（欄位）嗎？** 需要 → abstract class
2. **子類需要同時繼承別的 class 嗎？** 需要 → interface（保留 extends 位置）
3. **這是「能力」還是「分類」？** 能力 → interface；分類 → abstract class

---

## 第五輪：混合路線（interface + abstract base）

有一種中間解：

```java
public interface Skill { ... }                              // 純契約
public abstract class AbstractSkill implements Skill { ... } // 共用實作
public class BasicAttack extends AbstractSkill { ... }
```

**何時該用？**
- 同時存在**多個不相容的 Skill 家族**，它們無法共用同一份欄位
- 需要允許外部類別「不繼承 AbstractSkill 也能實作 Skill」（例如 mock、
  decorator、proxy）

**本題需要嗎？** 不需要。
- 11 個 Skill 共用同一組欄位（name / mpCost / targetAmount）
- 沒有外部類別需要繞過 AbstractSkill 直接實作 Skill

→ 用雙層反而是**過度設計**，違反 YAGNI。

---

## 結論：本題選 `abstract class Skill`

**判斷依據**：
1. ✅ 需要共用欄位（name / mpCost / targetAmount）→ 需要 abstract class
2. ✅ Skill 不會繼承別的 class → 沒有必要保留 extends 位置給別人
3. ✅ 11 個子類家族單純 → 不需要雙層結構
4. ✅ 「是一種技能」是分類，而不是「能執行技能」的能力 → is-a 語意

---

## 可攜帶到其他題目的思考框架

面對 interface / abstract class 抉擇時，依序問：

1. **有沒有共用欄位或實作要給子類？**
   - 沒有 → interface
   - 有 → 往下
2. **子類需不需要同時繼承別的 class？**
   - 要 → interface（配 default method 放共用實作；或改用 composition）
   - 不用 → 往下
3. **是不是要同時支持多組不相容的子類家族？**
   - 是 → interface + 多個 abstract base
   - 否 → **abstract class**（最直接的解）

---

## 一個常見的誤區

> 「Clean Code 說要優先用 interface」

這句話常被斷章取義。Robert C. Martin 真正的意思是：
> 「外部依賴應該指向**抽象型別**」。

抽象型別包含 interface **和** abstract class。選哪個要看上面的判斷準則，
不是盲目選 interface。

盲目選 interface 最常見的後果就是 **DRY 災難**（33 行重複 getter），
以及為了繞過 Java interface 限制而發明奇怪的 default method hack。
