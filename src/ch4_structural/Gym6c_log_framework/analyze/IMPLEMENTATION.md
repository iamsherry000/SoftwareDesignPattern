# Gym6c 日誌框架 — 實作說明

> 給 Sherry 看「Scout 整個怎麼寫的」+ 留給未來自己回顧。
> Last updated: 2026-05-14（Scout 實作完成主需求 + 進階題）

---

## TL;DR

- **15 個 .java 檔 + 1 個 .json 配置檔** 全部跑通
- 主需求 (Main.java) 與進階題 (Main_Json.java) **輸出完全一致**（等價 Logger tree 驗證 F6）
- **僅用 Composite + Strategy 兩個 GoF pattern**，其餘 force 都是 OO 基本功承接（不超綱）
- **手寫 ~120 行 minimal JSON parser**，零外部 dependency

---

## 檔案佈局

```
Gym6c_log_framework/
├── analyze/
│   ├── requirement.md              ← 題目原文
│   ├── ooa-worksheet.md            ← OOA 推導過程（Phase 1-5）
│   └── IMPLEMENTATION.md           ← 本檔
├── OOAD/
│   ├── Gym6c.asta                  ← Astah UML 檔
│   ├── OOA_v1.png                  ← Sherry 第一版類別圖
│   ├── 參考378_forces.png          ← 別人作品參考（forces 標註）
│   └── 參考839_JsonParse.png       ← 別人作品參考（進階題 Builder 體系）
├── result/
│   ├── game.log                    ← FileExporter 輸出
│   └── game.backup.log
├── 主需求 13 個 .java 檔
│   ├── Level.java                  ← enum (TRACE/INFO/DEBUG/WARN/ERROR)
│   ├── Message.java                ← value object (level/loggerName/content/timestamp)
│   ├── Layout.java                 ← interface
│   ├── StandardLayout.java         ← Layout impl
│   ├── Exporter.java               ← interface
│   ├── ConsoleExporter.java        ← Exporter impl (System.out)
│   ├── FileExporter.java           ← Exporter impl (Files.writeString APPEND)
│   ├── CompositeExporter.java      ← Exporter impl ★ Composite Pattern
│   ├── Logger.java                 ← 核心類別 (parent / null-fallback resolve)
│   ├── LogFramework.java           ← Logger registry + JSON loader
│   ├── Game.java                   ← 應用範例
│   ├── AI.java                     ← 應用範例
│   └── Main.java                   ← in-code 配置 entry
└── 進階題 3 個檔
    ├── MiniJsonParser.java         ← 手寫 recursive-descent JSON parser
    ├── JsonFile.java               ← 外部 JSON 檔的領域實體
    ├── Main_Json.java              ← JSON 配置 entry
    └── game-config.json            ← 範例配置（同 requirement 進階題範例）
```

---

## Phase A — 主需求實作

### 設計原則

1. **OOA 紀律**：先抓 force（5 條 + 進階題 1 條 = 6 條），不主動掛 GoF 名稱
2. **不超綱**：只用 Composite + Strategy 兩個 GoF pattern（章節範圍內）
3. **F2 設定繼承用純 OO**：self-association + null-fallback lookup，不硬掛 Proxy（self-correction 詳見 `team/scout/logs/self-corrections/2026-05-14_proxy-misjudge-no-source-verify.md`）

### Force ↔ class 對應

| Force | 落在哪個 class / 結構 | 用什麼 |
|---|---|---|
| **F1 ★主考** 多輸出目的地 vs 單一欄位 | `CompositeExporter.children: List<Exporter>` + `export()` forEach | **Composite Pattern** |
| F2 設定繼承（透明委派） | `Logger.parent` + `Logger.resolveLevelThreshold/Exporter/Layout()` 三個 method | OO null-fallback（`if (self.X != null) return self.X; if (parent != null) return parent.resolveX();`） |
| F3 Exporter / Layout 種類擴充 | `Exporter` / `Layout` interface + 多個 impl class | **Strategy Pattern** |
| F4 跨檔案取得 Logger | `LogFramework.loggers: Map<String, Logger>` + `getLogger(name)` / `declareLoggers(...)` | 普通 Map registry（沒掛 Singleton 名，因為 Sherry 還沒學） |
| F5 訊息分級門檻過濾 | `Logger.log()` 開頭一個 if + `Level.isAtLeast(threshold)` | OO 內部判斷 |
| F6 進階題 配置外部化 | 見 Phase B | 手寫 JSON parser + LogFramework.loadFromJsonFile |

### 各 class 職責逐項

**Level.java**（enum）
- 五個 level 按 ordinal 順序：TRACE(0) < INFO(1) < DEBUG(2) < WARN(3) < ERROR(4)
- 提供 `isAtLeast(threshold)` 判斷大小

**Message.java**（value object）
- 持有一筆 log 的 (level, loggerName, content, timestamp)
- 兩個建構子：一個自動填 `LocalDateTime.now()`，一個顯式給 timestamp（給 Layout 排版用）

**Layout.java + StandardLayout.java**
- Layout 介面只有一個 method：`String format(Message)`
- StandardLayout 用 `DateTimeFormatter` 產出題目要求格式：`yyyy-MM-dd HH:mm:ss.SSS |-LEVEL name - content`
- 未來新增 JSON Layout / XML Layout 只需新類別 implements Layout（OCP / F3）

**Exporter.java + 三個 impl**
- Exporter 介面：`void export(String formatted)` — 接收已排版好的字串
- ConsoleExporter：`System.out.println`
- FileExporter：`Files.writeString` 用 `StandardOpenOption.CREATE + APPEND`
- **CompositeExporter** ★主考：持有 `List<Exporter> children`，`export()` 內 forEach child.export — Composite Pattern 教科書級實作

**Logger.java**（核心）
- 5 個 final field：`name / parent / levelThreshold / exporter / layout`，後三者可 null
- 三個 `resolveX()` method 用 OO null-fallback（自己有就回自己；沒有就向 parent 遞迴查；parent 也沒有就 throw — 因為 root 必須完整）
- `log(level, content)`：先過濾（F5）→ 建 Message → resolveLayout().format → resolveExporter().export
- 五個 convenience method：`trace/info/debug/warn/error`，內部呼叫 `log(Level.X, content)`

**LogFramework.java**（registry）
- 持有 `Map<String, Logger>`
- `declareLoggers(Logger...)` 一次註冊多個（對應 requirement 應用範例的 `declareLoggers(root, gameLogger, aiLogger)`）
- `getLogger(name)` by-name 取回，找不到就 throw
- 進階題加 `loadFromJsonFile(JsonFile)` — 見 Phase B

**Game.java + AI.java + Main.java**
- 完全照 requirement 應用範例的 Java 等價物
- Game 持有 4 個 AI 玩家，遍歷讓他們 makeDecision
- AI.makeDecision 跑 4 條 log（trace / warn / error / trace）
- Main 建構 root + app.game + app.game.ai 三個 Logger，串好 parent，註冊到 LogFramework，然後跑 Game

### 跑法（主需求）

```bash
cd SoftwareDesignPattern
javac -encoding UTF-8 -d out src/ch4_structural/Gym6c_log_framework/*.java
java -cp out ch4_structural.Gym6c_log_framework.Main
```

輸出 17 行（與 requirement.md 第 133-150 行的應用範例輸出**完全一致**，僅時間戳不同）。同時 `result/game.log` 與 `result/game.backup.log` 也會被寫入相同內容（Composite 三路同步）。

---

## Phase B — 進階題實作（JSON 配置）

### 為什麼手寫 JSON parser

**約束**：純 Java IntelliJ 專案，沒設 Maven / Gradle dependency 管理。Java 標準庫**沒有 built-in JSON parser**（Jakarta JSON API 是外部 spec）。

**選項**：
- ❌ 引入 Gson / Jackson dependency — 違反專案慣例
- ❌ 用 properties / XML 格式繞開 — 違反題目「JSON 格式」要求
- ✅ 手寫 minimal recursive-descent JSON parser ~120 行

### MiniJsonParser 設計

**recursive-descent 結構**：
- `parseValue()` — 看第一個非空字元 dispatch 到 object / array / string / number / true / false / null
- `parseObject()` — `{ "key": value, ... }` → `LinkedHashMap<String, Object>`（保留順序，遞迴 logger tree 要 deterministic）
- `parseArray()` — `[ value, ... ]` → `ArrayList<Object>`
- `parseString()` — `"..."` 含 escape 處理（\n \t \r \" \\ \/）
- `parseNumber()` — int / decimal，統一回 Long 或 Double
- `skipWhitespace() / peek() / expect(c)` — 三個 token-level helper

**型別對應**：
| JSON | Java |
|---|---|
| object | `LinkedHashMap<String, Object>` |
| array | `ArrayList<Object>` |
| string | `String` |
| number | `Long` or `Double` |
| true / false | `Boolean` |
| null | `null` |

**不支援**：unicode escape (`\uXXXX`)、numeric exponent (`1e10`)、巢狀 escape — 對 logger config 用途夠了。

### LogFramework.loadFromJsonFile 流程

```
JsonFile.content (String)
   ↓
MiniJsonParser.parse → LinkedHashMap<String, Object>
   ↓
取出 root["loggers"] (LinkedHashMap)
   ↓
parseLogger("Root", loggerSpec, parent=null)  ← 遞迴入口
   ↓ (recurse for each non-reserved key)
parseLogger("app.game", subSpec, parent=rootLogger)
   ↓ (recurse)
parseLogger("app.game.ai", subSubSpec, parent=appGameLogger)
```

### parseLogger 三步驟

```java
private void parseLogger(String name, Map<String, Object> spec, Logger parent) {
    // 1. 取出自己的設定欄位（null 表示繼承）
    Level levelThreshold = parseLevel(spec.get("levelThreshold"));
    Exporter exporter = parseExporter(spec.get("exporter"));
    Layout layout = parseLayout(spec.get("layout"));

    // 2. 建構 Logger，註冊到 framework
    Logger logger = new Logger(name, parent, levelThreshold, exporter, layout);
    loggers.put(name, logger);

    // 3. 遞迴處理子 logger（任何 key 不是 reserved 欄位的就是子 logger 名稱）
    for (Map.Entry<String, Object> entry : spec.entrySet()) {
        if (RESERVED_KEYS.contains(entry.getKey())) continue;
        parseLogger(entry.getKey(), (Map<String, Object>) entry.getValue(), logger);
    }
}
```

**RESERVED_KEYS = {"levelThreshold", "exporter", "layout"}** — 這三個 key 是 logger 自身欄位，其他 key 都當作子 logger 名稱。

### parseExporter dispatch

```java
return switch (type) {
    case "console" -> new ConsoleExporter();
    case "file" -> new FileExporter((String) spec.get("fileName"));
    case "composite" -> {
        // 遞迴 — children 也是 exporter spec 陣列
        Exporter[] children = childSpecs.stream()
                .map(this::parseExporter)
                .toArray(Exporter[]::new);
        yield new CompositeExporter(children);
    }
    default -> throw new IllegalArgumentException("Unknown exporter type: " + type);
};
```

**沒用 Builder Pattern** — 為什麼：
- Builder 是 GoF Creational Pattern，水球還沒教 → 不超綱原則
- 替代做法：簡單 if-else / switch dispatch，把 type 字串映射到對應 class
- 缺點：未來新增 Exporter 種類要改 LogFramework.parseExporter（違反 OCP for parser extension）
- 但 OOA F3 OCP 只要求「**新增 Exporter 種類不改 Logger**」，**沒要求「不改 parser」** — 嚴格說沒違反需求 OCP
- 真要遵守 parser OCP → 引入 Builder 體系（參考 `OOAD/參考839_JsonParse.png`），但那是 Sherry 學完 Builder 後再升級

### Main_Json.java

```java
LogFramework framework = new LogFramework();
framework.loadFromJsonFile(new JsonFile(configPath));
Game game = new Game(framework);
game.start();
```

**只差兩行** — `loadFromJsonFile` 取代了 Main.java 中 13 行的 `new Logger(...) + declareLoggers(...)` — 證明 F6 force「兩種建構來源 → 等價 Logger tree」設計達成。

### game-config.json

完全照 requirement.md 第 156-193 行的進階題範例 JSON，僅 fileName 加了相對路徑前綴讓 log 落在 `result/` 資料夾。

### 跑法（進階題）

```bash
cd SoftwareDesignPattern
java -cp out ch4_structural.Gym6c_log_framework.Main_Json
# 或自訂配置檔：
java -cp out ch4_structural.Gym6c_log_framework.Main_Json /path/to/your-config.json
```

輸出**逐字元相同於主需求版本**（時間戳除外）。

---

## 取捨記錄（為什麼這樣寫，不那樣寫）

### 取捨 1：F2 設定繼承不用 Proxy
- **誤判過程**：Scout 一開始照 Sherry 口頭提示「主考代理人模式」誤判 F2 對應 Proxy
- **修正**：requirement.md 標題明寫「複合模式——日誌框架」，Composite 才是主考；F2 就是 OO 基本的 self-association + null-fallback，**不需掛 GoF 名**
- **詳情**：`team/scout/logs/self-corrections/2026-05-14_proxy-misjudge-no-source-verify.md`

### 取捨 2：LogFramework 不掛 Singleton
- 目前 LogFramework 由 Main 自己 `new` 一個 instance
- 沒做 private constructor + static getInstance() — Sherry 還沒學 Singleton
- 跨檔案需要時透過 dependency injection（`Game(framework)`、`AI(name, framework)`）傳遞
- 未來學完 Singleton 可升級

### 取捨 3：JSON parsing 不用 Builder Pattern
- 用 switch-case 字串 dispatch
- 缺點：parser OCP — 新增 Exporter 種類要改 parseExporter
- 但 requirement 的 OCP 只針對 Logger 不改，沒針對 parser → 沒違反需求
- 未來學完 Builder 可重構

### 取捨 4：Logger 是 class 不是 interface
- OOA 階段 Q1.2 已答 Logger 是同一個概念（各種身分都是同一個 Logger 的不同 facet）
- 沒切 LoggerInterface + LoggerImpl 雙層
- Logger 直接是 concrete class with public methods

### 取捨 5：Message 抽成 value object
- 一開始 OOA Phase 5 我沒收 Message（判它是 String 參數）
- 對照參考378 後修正：抽成 class 比較乾淨（Layout.format 只接受一個 Message 而不是三個參數）
- 屬性：level / loggerName / content / timestamp，後者由建構子預設填 `now()`

### 取捨 6：FileExporter 用 try-catch 包 IOException
- Java 的 `Files.writeString` 會 throw `IOException`（checked）
- Exporter 介面的 `export(String)` 沒宣告 throws — 為了保持介面簡潔
- 在 FileExporter 內部把 IOException 包成 RuntimeException 上拋
- 這是教學專案 OK，production 應該設計專屬 ExporterException

### 取捨 7：手寫 JSON parser 不用 Gson
- 純 Java 專案無 dependency 管理
- ~120 行 recursive descent 自包含
- 教學意義：學員看得懂解析過程

---

## OOA → 實作對照表（給未來自己看）

| OOA 圖元素 | 對應 Java 檔 | 對應 method / field |
|---|---|---|
| Logger（class） | `Logger.java` | 全 |
| Logger.parent (0..1) | `Logger.parent` field | `getParent()` |
| Logger.levelThreshold [0..1] | `Logger.levelThreshold` field | `resolveLevelThreshold()` |
| Logger.exporter [0..1] | `Logger.exporter` field | `resolveExporter()` |
| Logger.layout [0..1] | `Logger.layout` field | `resolveLayout()` |
| Level (enum) | `Level.java` | `isAtLeast(threshold)` |
| Exporter (abstract) | `Exporter.java` interface | `export(String)` |
| ConsoleExporter | `ConsoleExporter.java` | `System.out.println` |
| FileExporter (fileName) | `FileExporter.java` | `Files.writeString APPEND` |
| CompositeExporter ★ | `CompositeExporter.java` | `children: List<Exporter>` + forEach |
| Layout (abstract) | `Layout.java` interface | `format(Message)` |
| StandardLayout | `StandardLayout.java` | `DateTimeFormatter` 格式 |
| Message (value object) | `Message.java` | level / loggerName / content / timestamp |
| LogFramework | `LogFramework.java` | `loggers: Map` + `getLogger / declareLoggers` |
| LogFramework.source 0..1 → JsonFile（進階）| `LogFramework.loadFromJsonFile` | 接收 JsonFile，遞迴 parseLogger |
| JsonFile | `JsonFile.java` | path + content |

---

## 已知 caveat

1. **Main_Json 跑時 working directory 必須是 SoftwareDesignPattern/** — 因為 `game-config.json` 用相對路徑，且 config 內 fileName 也是相對於 working directory
2. **MiniJsonParser 不支援 unicode escape (`\uXXXX`) 與 scientific notation (`1e10`)** — logger config 用不到
3. **CompositeExporter 的 `getChildren()` 回 `List.copyOf()`** — defensive copy，client 不能改 children
4. **沒寫 unit test** — Sherry 教學專案慣例似乎不用 JUnit；要的話可以後補

---

## 相關文件

- 題目原文：`analyze/requirement.md`
- OOA 推導：`analyze/ooa-worksheet.md`（Phase 1-5 完整紀錄，含我自己的 Proxy 誤判與修正）
- UML 圖：`OOAD/Gym6c.asta` + Sherry 的 `OOA_v1.png`
- Scout self-correction：`team/scout/logs/self-corrections/2026-05-14_proxy-misjudge-no-source-verify.md`
- 專案慣例：`SoftwareDesignPattern/CONVENTIONS.md`
