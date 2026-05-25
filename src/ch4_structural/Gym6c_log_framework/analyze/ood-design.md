# 道館 6c 日誌框架 — OOD 設計（對齊 Sherry 落地的 code）

> 從 Sherry 已寫好的 16 個 .java code 倒推 OOD 設計選擇，逐項解釋「為什麼這樣選」+「對應到哪一條 force / pattern」。
>
> **這份不是 OOD 規格交付物，是 Sherry 設計選擇的推理記錄** —— 對齊 5/14 Sherry critique「不能流水帳 要有思考後產出的樣子 要能讓人順著思路去走」。
>
> Last updated: 2026-05-14（按 5-folder 新結構 + code 對齊全面 redo）

---

## 1. 切入點

OOA worksheet Phase 4 抓出 6 條題目 force（F1 主考 / F2 設定繼承 / F3 種類擴充 / F4 by-name 取得 / F5 分級過濾 / F6 進階題 JSON）。這份 OOD 不重述 force 內容（看 worksheet），而是**逐項問**：

> 「這條 force，在 Sherry 已寫好的 code 裡，是用什麼設計選擇承接的？為什麼選這個不選別的？」

從 code 出發倒推設計，比從 force 出發正推規格更接近真實 OOD 過程 —— 真實 OOD 是「設計者在多個合理選項中做出 trade-off 決定」，不是「按 worksheet 填空」。

新 package 結構（CONVENTIONS.md 2026-05-14 拍板）：

- **`core/`**：Logger / LogFramework / Level / Message —— 領域核心 + 框架入口
- **`exporter/`**：Exporter + 3 子類 —— Composite Pattern 整套
- **`layout/`**：Layout + StandardLayout —— Strategy Pattern 整套
- **`json/`**：JsonFile / MiniJsonParser / Main_Json —— F6 進階題
- **`usage/`**：Main / Game / AI —— requirement Application 範例

下面每節對應一條 force 走推理。

---

## 2. F1 主考力怎麼落地 —— Composite Pattern 在 `exporter/`

### force 重述

「Logger 對外只承諾持有單一 exporter 欄位，但實際輸出目的地可能是 N 個」 —— 單一 vs N 的張力。

### code 怎麼解

`exporter/Exporter.java` 是 interface 只有一個 method：

```java
public interface Exporter {
    void export(String formatted);
}
```

`exporter/CompositeExporter.java` 同樣 implements Exporter，內部持有 `List<Exporter>`：

```java
public class CompositeExporter implements Exporter {
    private final List<Exporter> children;
    public CompositeExporter(Exporter... exporters) { ... }
    public void export(String formatted) {
        for (Exporter child : children) child.export(formatted);
    }
}
```

Logger 那邊只有 `private final Exporter exporter;` —— 一個欄位、一個型別、`exporter.export(msg)` 一行帶過。runtime 那個 exporter 可能是 leaf（Console / File）也可能是 CompositeExporter 包一棵任意深的樹。**Logger 不知道也不關心**。

### 為什麼這個結構符合 Composite Pattern canonical

對照 `我的軟設筆記/patterns/compositePattern.md` 的 canonical force：

> **F1-Self Association 遞迴關聯**：存在遞迴關聯導致高結構複雜度

CompositeExporter ─◆ Exporter 是「對抽象父類的 composition」，因為 children 裡可放另一個 CompositeExporter，runtime 因多型形成樹。requirement Main 範例的巢狀 `new CompositeExporter(new ConsoleExporter(), new CompositeExporter(...))` 直接證明這個遞迴。

### 設計選擇 1：immutable composite —— 走 constructor injection、不開 add

`CompositeExporter` 沒有 `add(child: Exporter)` method。constructor 接 variadic `Exporter...` 或 `List<Exporter>` 一次注入，`getChildren()` 還回傳 `List.copyOf()` 不可變視圖。

為什麼選 immutable？

**對照 Composite Pattern 經典 trade-off**（compositePattern.md L33-36）：

> Tradeoff F4-操作安全性 / F5-透明度

這個 trade-off 是針對 mutable composite —— 「`add(child)` 該放在 Component 介面（透明，所有 leaf 也有 add 但 throw / no-op，型別不誠實）還是只放 Composite（安全，client 要 cast 才能 add）」。

Sherry 走 immutable 直接**繞過整個 trade-off** —— 配置階段在 constructor 一次到位，runtime 沒人改得了 children。代價：runtime 不能動態加 child；收益：不用做這個取捨。

**為什麼這題能接受這個代價？** 看 `usage/Main.java`：所有 CompositeExporter 都是 main() 啟動時一次建好，遊戲跑起來之後再也不需要改。**配置 phase 跟 runtime phase 在這題天然分離** —— immutable 完全 fit。

如果這題改成「runtime 動態切換輸出目的地」（例如 server 收 admin command 加 NetworkExporter），就會被迫考慮 mutable + safe add 那條路。**immutable 是基於這題 scope 的判斷，不是 universal best practice**。

### 設計選擇 2：CompositeExporter 走 generic 而不分 subFileExporters / subConsoleExporters

OOA v1 圖 Sherry 早期版本曾分 `subFileExporters` 跟 `subConsoleExporters` 兩個 list —— 那是 over-design（也是 v1 被改名 `OOA_v1_overDesign` 的原因之一）。code 統一用 `List<Exporter>` 一個 list 裝所有子類，**透過多型一視同仁**。

這個選擇對應 Composite Pattern 的核心 intent：**client 不應該知道 leaf vs composite 差異，更不應該知道 leaf 有什麼子類**。分兩個 list = 引入 client 對 leaf 子類的知識 = 違反 Composite intent。

---

## 3. F3 怎麼落地 —— Strategy Pattern 在 `layout/`，雙重身分在 `exporter/`

### force 重述

「Exporter / Layout 種類未來會擴充，但不希望改動 Logger」 —— OCP。

### 注意這條 force 兩個主詞分開

Exporter 跟 Layout 是不同的承載：
- **Layout 只有種類擴充需求**（StandardLayout 一個子類，未來加 JsonLayout / XmlLayout） —— 純 Strategy
- **Exporter 同時有種類擴充 + 樹狀組合需求** —— Strategy + Composite 雙重身分

### Layout = 純 Strategy 在 `layout/` folder

`layout/Layout.java` interface + `layout/StandardLayout.java` implements。Logger 持 `private final Layout layout`，runtime 可替換（雖然這題 immutable 沒換，未來可以）。

對照 Sherry 訊息舉的 Strategy canonical force：

> F1-BV: 多道行為
> F2-OCP: 擴充新 Strategy 不必修改現有 Match code

具現：未來加 `JsonLayout implements Layout` 不必動 Logger / Main / Game / AI 任何一行。Layout 種類擴充的成本只在 layout/ 一個 folder 內。

### 設計選擇 3：Layout.format 收 Message 物件，不拆三個原始參數

`Layout.format(Message message)` 收完整 Message 一個型別 —— 不是 `format(Level, String name, String msg, LocalDateTime time)` 拆四個。

為什麼？因為 Message 未來可能加屬性（thread name / correlation id / hostname...）。每加一個屬性，format 簽名拆參數版本就要全 layout implementations 都改 —— 違反 OCP。**Message 抽 first-class object** 把擴充封鎖在 Message 內，Layout 介面跟所有 implementations 永遠只面對一個型別。

這對應 `compositePattern.md` 沒明寫但隱含的設計判斷 —— **介面方法的參數該抽 value object，不要散原始型別**。

### Exporter = Strategy + Composite 雙重身分

從 client 端看，Logger.exporter 是 runtime 可替換的「行為」 —— Strategy Strategy 角色。從結構端看，Exporter 介面下有 leaf + composite —— Composite Component 角色。

對照 `patterns/decorator-vs-proxy.md` 提的「type axis vs composition axis」區分：

| Axis | 在 Exporter 上的意義 |
|---|---|
| Type axis（型別軸） | Exporter 是抽象型別，多種具現 leaf / composite 可替換 → Strategy |
| Composition axis（組合軸） | CompositeExporter 內含 List<Exporter> 形成樹 → Composite |

**這兩個 axis 不衝突**，Exporter 同時承擔兩個角色，這也是為什麼 `exporter/` folder 裝整套 Composite 體系時，**整套就同時也是 Strategy 體系**。

---

## 4. F2 設定繼承怎麼落地 —— 無 GoF 在 `core/Logger`

### force 重述

「子 Logger 多數沿用父設定、少數覆寫；client 對子 / 父透明」。

### code 怎麼解

`core/Logger.java` 三個 resolve method 同模式：

```java
public Exporter resolveExporter() {
    if (exporter != null) return exporter;
    if (parent != null)   return parent.resolveExporter();
    throw new IllegalStateException("Root logger must define exporter");
}
```

自己有就用、自己 null 向 parent 問、parent 也 null throw —— Root 必填三項，跟 requirement 直接對齊：

> 根日誌器必須要設定清楚分級門檻、輸出器和訊息佈置，而子日誌器只有在要覆寫父日誌器的設定時才需要設定…

### 為什麼不掛 GoF —— 排除 Decorator 的推理

第一直覺可能想：「向 parent 委派」聽起來像 Decorator —— 「自己不（完全）做事 → delegate 給內部同型別物件 → 對結果加工」。

但檢驗 `patterns/decorator-vs-proxy.md` Decorator 的三個結構訊號：
1. 同 interface ❌（Logger 不是 interface）
2. 持有同型別 field ✓（Logger.parent: Logger）
3. **內部 delegate + 對結果加工 / 加功能** ❌（子 Logger 沒「加工」，是「自己沒設就向上問」 —— fallback 不是 wrap）

子 Logger 不是 wrapping parent 然後 enhance 行為，是 **fallback lookup**。結構像 Decorator 第 2 點，但 intent 不同 —— 對應 5/14 Scout 自己 codify 的「**form 同 ≠ force 同**」規則。

→ F2 ↔ 無 GoF。**OO 基本的 self-association + null-fallback resolution**，不需要 stereotype。

---

## 5. F4 by-name 取得怎麼落地 —— 無 GoF 在 `core/LogFramework`

### force 重述

「Logger 在 Main 中定義配置，但會在 Game / AI 等任意檔案中以名稱取回同一個 Logger」。

### code 怎麼解

`core/LogFramework.java`：

```java
private final Map<String, Logger> loggers = new HashMap<>();

public void declareLoggers(Logger... loggers) {
    for (Logger logger : loggers) this.loggers.put(logger.getName(), logger);
}

public Logger getLogger(String name) {
    Logger logger = loggers.get(name);
    if (logger == null) throw new IllegalArgumentException(...);
    return logger;
}
```

`usage/Game.java` constructor `this.log = framework.getLogger("app.game")`、`usage/AI.java` 同 `framework.getLogger("app.game.ai")` —— 對齊 requirement 範例。

### 為什麼不是 Singleton

第一直覺：「全域取得」聽起來像 Singleton。但 Singleton 的 canonical intent 是「**確保某 class 只有一個 instance**」 —— 在這題明顯不適用，Logger 有 N 個 instance（Root / app.game / app.game.ai）。

實際的力是「**by name 取回已建好的某個 instance**」 —— Registry / Lookup table 模式（不在 GoF 23 個經典裡）。具現用 `Map<String, Logger>` + getLogger by key —— **OO 基本的集合 + key 查詢**。

→ F4 ↔ 無 GoF。

---

## 6. F5 過濾怎麼落地 —— 無 GoF 在 `core/Logger.log()`

### force 重述

「Logger 根據自身 level threshold 決定要不要受理請求」。

### code 怎麼解

`core/Logger.java` log() method 開頭一行：

```java
public void log(Level level, String content) {
    if (!level.isAtLeast(resolveLevelThreshold())) {
        return;
    }
    // ... format + export ...
}
```

過濾邏輯先跑、過了才 format + export。

### 設計選擇 4：isAtLeast 開在 Level enum 上，不切 LevelThreshold class

`core/Level.java`：

```java
public enum Level {
    TRACE, INFO, DEBUG, WARN, ERROR;
    public boolean isAtLeast(Level threshold) {
        return this.ordinal() >= threshold.ordinal();
    }
}
```

對照教練的參考圖 1 —— 把 isPass 邏輯抽到 `LevelThreshold` class 上，多開一層 class。Sherry **沒切 LevelThreshold**，直接放 Level enum 上 ——「**操作開在資料身上**」OO 基本原則 + OOA 紀律「不開不必要的抽象 helper」。

這個選擇也跟 OOA worksheet Phase 2 結論一致（Phase 2 把 LevelThreshold 排除在 OOA 圖外）—— code 完全對齊 OOA 紀律。

### 為什麼不是 Strategy

第一直覺：「不同 threshold 不同行為」可能想到 Strategy。但 Strategy F1-BV 的關鍵是「**多道行為 — 不同 ConcreteStrategy 各自實作不同 algorithm**」。F5 不是這個結構 —— 過濾邏輯只有一條（`level >= threshold`），threshold 是**參數值**不是 algorithm。要替換的是「值」不是「算法」。

→ F5 ↔ 無 GoF。Logger 內 if 判斷一行就解。

---

## 7. F6 進階題怎麼落地 —— LogFramework 內建 switch parser 在 `json/`

### force 重述

「JSON 配置檔取代 in-code `new Logger(...)`；兩種建構來源產生語義等價的 Logger tree」。

### code 怎麼解

`core/LogFramework.java` 內建 parser：

```java
public void loadFromJsonFile(JsonFile jsonFile) {
    Object parsed = MiniJsonParser.parse(jsonFile.getContent());
    // ... recurse parseLogger ...
}

private Exporter parseExporter(Object obj) {
    String type = (String) spec.get("type");
    return switch (type) {
        case "console" -> new ConsoleExporter();
        case "file" -> new FileExporter((String) spec.get("fileName"));
        case "composite" -> { /* recursive parseExporter children */ }
    };
}
```

`json/JsonFile.java` 包外部檔案 → `json/MiniJsonParser.java` 解 JSON 成 Map / List → `LogFramework.parseExporter` switch type + 直接 new。`json/Main_Json.java` 是進階題 entry point —— `framework.loadFromJsonFile(...)` 一行完成 + 跑同樣的 Game 範例。

### 設計選擇 5：switch + 直接 new，不用 Builder + Chain of Responsibility

教練的參考839 圖（`OOAD/參考839_JsonParse.png`）走 Builder + Chain of Responsibility 三層體系 —— `ConsoleExporterBuilder` / `FileExporterBuilder` / `CompositeExporterBuilder` 各自負責解析自己 type，鏈式 canParse → doParse。

Sherry 走更簡單路線：switch 17 行就解決 + recursive parseExporter 處理巢狀 composite。

**為什麼簡單路線更好**？

Builder + Chain 體系的價值是「**新加 Exporter 種類時不改既有程式碼**」（嚴格 OCP）—— 寫 `NetworkExporterBuilder` 加到 chain 後面即可。代價：3 個 class 起跳，理解門檻較高。

switch 路線的代價是「新加 Exporter 種類要改 parseExporter 的 switch」—— 違反嚴格 OCP。但這題 scope 內 Exporter 種類只有 3 種（console / file / composite），requirement 沒有「未來會頻繁加 Exporter 種類」的明示訊號，**OCP 嚴格度跟複雜度交換** —— 簡潔贏。

**設計判斷的根據**：requirement scope 是「日誌框架基礎」+「JSON 配置擴展」，沒提「外掛系統 / runtime 加 Exporter 種類 / 第三方擴充」這類更高彈性需求。Builder + Chain 是「未來確定會頻繁擴充 Exporter 種類」才該引入的成本，這題沒到那層。

→ F6 ↔ 無 Builder + Chain，**switch + 直接 new + 遞迴**。OO 基本。

---

## 8. force / pattern / package 對應總表

把上面 7 個設計選擇收成一張映射（這張表是收斂用，不取代論述）：

| Force | Pattern | Code 承載位置 | 關鍵設計選擇 |
|---|---|---|---|
| F1 主考 | Composite | `exporter/` 整個 folder | 選 1 immutable / 選 2 generic List<Exporter> |
| F3 (Layout) | Strategy | `layout/` 整個 folder | 選 3 Message 抽 first-class object |
| F3 (Exporter) | Strategy + Composite 雙重 | `exporter/`（跟 F1 共用） | 同選 1 + 選 2 |
| F2 設定繼承 | 無 GoF | `core/Logger.resolveX()` | self-association + null-fallback |
| F4 by-name 取得 | 無 GoF | `core/LogFramework.getLogger()` | Map + key 查詢 |
| F5 分級過濾 | 無 GoF | `core/Logger.log()` if + `core/Level.isAtLeast()` | 選 4 isAtLeast 開在 enum 上 |
| F6 JSON 配置 | 無 Builder + Chain | `core/LogFramework.parseExporter()` switch + `json/` 整個 folder | 選 5 switch 簡潔贏嚴格 OCP |

---

## 9. 跟 OOA_v2 圖的對齊狀態

OOA_v2 圖跟 code 高度對齊（前面 review 已逐項檢查），唯一**圖待補**的點是：

- CompositeExporter → Exporter 的 children 線**還沒在圖上畫出來** —— code 上 `List<Exporter> children` 結構已實作完整，但 OOA_v2 圖只標 `1..*` 標籤沒對應實線。圖補上這條線就會跟 code 完全對齊。

圖上保留的「Layout 旁策略模式」「CompositeExporter 旁透明 vs 安全」兩個 OOD 註解，嚴格 OOA 紀律不該掛 form 名 —— 留著當教學 note 也行，正式 OOA 交付物建議移除。

---

## 10. 進階：為什麼 5-folder 結構比平層好

CONVENTIONS.md 2026-05-14 新慣例（按 OOD pattern role 分 folder）對這題的具體價值：

**對應到 OOD pattern visual boundary**：

- 翻 `exporter/` folder 就看到「整個 Composite Pattern 範圍」 —— Component 介面 + 兩個 Leaf + 一個 Composite，4 個 class 一覽
- 翻 `layout/` folder 就看到「整個 Strategy Pattern 範圍」 —— Strategy 介面 + 一個 ConcreteStrategy
- 翻 `core/` folder 看到「不歸屬任何 Pattern 但是領域核心」 —— Logger (Context) / LogFramework (facade) / Level + Message (value object)
- 翻 `json/` folder 看到「進階題 F6 全部」 —— 跟主流程隔離
- 翻 `usage/` folder 看到「sample / demo」 —— 跟 framework 本體分離

**對應到 Java package 一致性**：

每個 folder 對應一個 sub-package，cross-package 必須 explicit import —— **import 行清楚表達「哪個 class 依賴哪個 pattern 體系」**。例如 `core/Logger.java` 開頭：

```java
import ch4_structural.Gym6c_log_framework.exporter.Exporter;
import ch4_structural.Gym6c_log_framework.layout.Layout;
```

兩行 import 直接告訴讀者：「Logger 是 Context，依賴 Composite (exporter) + Strategy (layout) 兩個 pattern」。平層結構這個依賴關係要靠記憶。

---

## 11. 不適用 / 撤回的舊內容（2026-05-14 redo 過程記下）

之前版本（spec 風格 + 沒對齊 code）寫過但這次撤回：

- ❌ 「CompositeExporter 加 `+ add(child: Exporter): void` method」 —— Sherry 走 immutable，沒這 method
- ❌ 「透明 vs 安全 trade-off 推 Safe」 —— Sherry 走 immutable 繞過整個 trade-off
- ❌ 「F6 對應 Builder + Chain of Responsibility」 —— Sherry 用 switch + 直接 new
- ❌ 「Layout.format(Level, String, String): String」拆三參數簽名 —— Sherry 用 `format(Message)`
- ❌ 「Logger.writeMessage(level, msg)」命名 —— Sherry 用 `log(level, content)` + 5 個 convenience method 對齊 requirement 應用範例

撤回根因：之前的 spec 版本是憑 OOA worksheet 自己想規格，沒看 Sherry 已存在的 .java code。違反 CONVENTIONS.md L116「寫 OOD spec 前必做 — 先看 Sherry 已存在的 .java code」（這條 rule 本身就是 2026-05-14 從這次失誤 codify 出來的）。

---

## 參考來源

- `analyze/requirement.md` —— 標題第 2 行（複合模式）+ A 段（領域物 + 設定繼承） + B 段（OCP 兩處） + C 段（JSON 配置範例）
- `analyze/ooa-worksheet.md` Phase 4 —— 6 條 worksheet force 原文
- `analyze/forces-pattern-mapping.md` —— 每條 force 對應 GoF 的推理
- `analyze/ooa-v1-review.md` —— v1 → v2 圖演進 + Q1 children 線待補
- `OOAD/OOA_v2.png` —— 主圖（v2，修正後）
- `OOAD/OOA_v1_overDesign.png` —— 歷史（已被 Sherry 標 over-design）
- `我的軟設筆記/patterns/compositePattern.md` —— Composite canonical force (F1-F5)
- `我的軟設筆記/patterns/decorator-vs-proxy.md` —— type axis vs composition axis（解釋 Exporter 雙重身分）
- Sherry 2026-05-14 訊息 —— Strategy canonical「F1-BV / F2-OCP」
- Code（16 個 .java，5 folder）—— `core/` / `exporter/` / `layout/` / `json/` / `usage/`
