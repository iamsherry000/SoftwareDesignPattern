# Gym6c 日誌框架 — Code Walkthrough（narrative 推理紀錄）

> 給 Sherry 看「Scout 寫每個檔當下心裡在想什麼、為什麼這樣不那樣」，依 CONVENTIONS.md 新規（2026-05-14）narrative 風格。
> 16 個檔（13 主需求 + 3 進階題 + 1 config）逐一推理，已存在的 .java code 為主軸，不是憑 OOA worksheet 自己想規格。
> Last updated: 2026-05-14

---

## 起手 — 為什麼整題長這樣

題目第 2 行寫「複合模式——日誌框架」，所以主考是 Composite Pattern；requirement 設計需求 B + C 明文要 OCP，自然引出 Strategy；F2「子日誌器只有在要覆寫父日誌器的設定時才需要設定…沒有覆寫的參數則繼承父日誌器的設定」（requirement 第 21 行）這條最容易誤判成 Proxy — 但其實是 OO 基本的 self-association + null-fallback，沒對應任何 GoF 名稱（這條我曾經誤判，self-correction 詳見 `team/scout/logs/self-corrections/2026-05-14_proxy-misjudge-no-source-verify.md`）。

整題 6 條 force 收斂結果：F1 主考 → Composite、F3 → Strategy、F2 / F4 / F5 / F6 → 純 OO 不掛 GoF（Sherry 還沒學 Builder / Singleton）。Force ↔ 結構對應表在 `ooa-worksheet.md` Phase 4。

16 個檔我會按 OOA bottom-up 順序講，先看沒依賴的 enum / value object，再看抽象介面 + impl，最後 Logger 串起來 + 應用範例 + 進階題。

---

## ① Level.java

```java
public enum Level {
    TRACE, INFO, DEBUG, WARN, ERROR;

    public boolean isAtLeast(Level threshold) {
        return this.ordinal() >= threshold.ordinal();
    }
}
```

需求說「分級越上方越小」（requirement 第 14 行）— TRACE 最小、ERROR 最大。我第一直覺是寫 int constant（`static final int TRACE = 0`），但這走 C 風格、type-unsafe — `logger.log(99, "msg")` 編譯通過但語意爛掉。於是改用 enum：固定 5 個值、不會增加、Java 編譯期 type-safe，這是 enum 的標準場景。

順序剛好對應 Java 自動分配的 `ordinal()`（0..4），所以 F5「請求 level 是否大於等於 threshold」可以直接用 `ordinal()` 比較，不用自己另寫 numeric mapping。

`isAtLeast` method 開在 Level 自己身上 — 不開在 Logger。為什麼？OOA worksheet Phase 3 我用「裡面 vs 之間」判準（5/13 canonical）：level 之間的大小比較是 Level 自身的 intrinsic 特質，屬於「裡面」。如果寫在 Logger 裡（`requestLevel.ordinal() < threshold.ordinal()`），就把「level 怎麼比大小」這個知識洩漏給 Logger — 未來規則改（例如要支援 OFF 全關）就要動 Logger。封裝在 Level 內就只改 Level。

---

## ② Message.java

```java
public class Message {
    private final Level level;
    private final String loggerName;
    private final String content;
    private final LocalDateTime timestamp;

    public Message(Level level, String loggerName, String content) {
        this(level, loggerName, content, LocalDateTime.now());
    }

    public Message(Level level, String loggerName, String content, LocalDateTime timestamp) {
        // assignments
    }
    // getters only
}
```

Message 是我 OOA Phase 5 一開始**漏掉**的 class — 當時判它是「不限長度的字串」（requirement 第 13 行），是參數型別不是領域實體。看完參考378_forces.png 後修正：Message 確實是一筆 log 的「**事件物**」，含 level + content（+ 排版需要的 loggerName + timestamp）— 抽成 value object 比一路傳四個獨立參數乾淨。

兩個建構子：三參數版自動填 `LocalDateTime.now()`（99% 場合是「現在」），四參數版顯式給 timestamp（給 testing / replay 場景留窗口）。三參數版用 `this(...)` 鏈到四參數版 — 建構責任永遠落在四參數那個，沒重複 assignment。

為什麼欄位用 `loggerName: String` 不是 `logger: Logger`？因為 Layout 排版只要名字字串就夠（requirement 第 31 行的格式 `|-LEVEL name - content`）。如果 Message 持有 Logger reference，就讓「一筆 log」知道 Logger 的內部設定（threshold / parent / exporter / layout），形成不必要耦合 — value object 越精簡越好。

`timestamp` 為什麼進 Message 不是 Logger 排版時當場拿？因為 timestamp 跟 level / content 一樣是「這筆 log 發生時的事實」，邏輯上屬於 Message 的一部分。Layout 拿到的應該是「完整的一筆 log 資料束」，timestamp 從哪來不該洩漏給 Layout。

選 class 不選 Java 14+ record — 跟 sibling 風格一致（Gym6b 的 Employee 是 class）；且 record 不能有兩個建構子，要做「自動填 now()」需要 compact constructor + helper，繞。

---

## ③ Layout.java + StandardLayout.java

```java
public interface Layout {
    String format(Message message);
}

public class StandardLayout implements Layout {
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    @Override
    public String format(Message message) {
        return FORMATTER.format(message.getTimestamp())
                + " |-" + message.getLevel().name()
                + " " + message.getLoggerName()
                + " - " + message.getContent();
    }
}
```

把「**訊息排版**」做成介面 + impl 兩層是因為 requirement 設計需求 C（第 39 行）明寫「擴充訊息佈置種類時無需修改既有程式碼，遵守 OCP」 — F3 force 的承接結構就是 Strategy Pattern：行為抽介面、runtime 由 Logger 持有抽象的 reference。

Layout 介面只有一個 method — strategy 介面該只暴露「strategy 唯一負責的責任」。排版的責任 = 「給我 Message，回排版好的字串」一句話描述完。加多餘 method（`setColor()` / `getMaxWidth()`）會變成介面汙染 — 不是每種 layout 都需要。未來 JSONLayout 要 colorize 選項，是 JSONLayout 自己 constructor 接收的 config，不該爬到介面層。

`DateTimeFormatter` 設成 `static` 而非 instance — Java 8+ 的 `DateTimeFormatter` 是 thread-safe + immutable，全程式共用一個 instance 即可，每個 StandardLayout 都 new 一個浪費記憶體。注意這跟 Java 8 前的 `SimpleDateFormat` 不同 — 那個不是 thread-safe，多 thread 共用會出錯。

字串拼接我用直接 `+`，沒選 `String.format` 或 `StringBuilder`：format 的 `%s %s %s %s` 沒可讀標記、debug 時看不出哪個 %s 對到哪個變數；StringBuilder 行數多且現代 Java 編譯器自動把 + 轉成 StringBuilder，效能沒差。直接 + 最直觀，每段 literal 接哪個 field 一眼看得出。

為什麼 Layout 不負責輸出？因為「字串送哪去」是 Exporter 的職責 — 單一職責原則。Layout 管 how to render、Exporter 管 where to send，兩個職責分開，可以正交組合（M × N → M + N）。如果混在一個 class（例如 `StandardConsoleHandler`），未來新增就組合爆炸。

---

## ④ Exporter.java + 三個 impl ★ 主考登場

```java
public interface Exporter {
    void export(String formatted);
}

public class ConsoleExporter implements Exporter {
    @Override public void export(String formatted) {
        System.out.println(formatted);
    }
}

public class FileExporter implements Exporter {
    private final String fileName;
    public FileExporter(String fileName) { this.fileName = fileName; }

    @Override public void export(String formatted) {
        try {
            Files.writeString(Path.of(fileName), formatted + System.lineSeparator(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException("Failed to append to " + fileName, e);
        }
    }
}

public class CompositeExporter implements Exporter {
    private final List<Exporter> children;

    public CompositeExporter(Exporter... exporters) {
        this.children = new ArrayList<>(Arrays.asList(exporters));
    }

    @Override public void export(String formatted) {
        for (Exporter child : children) {
            child.export(formatted);
        }
    }
}
```

這個檔組是整題的高潮 — **Strategy 跟 Composite 同時出場**。Strategy 是因為 F3（Exporter 種類擴充）：Exporter 介面 + 多個 impl class，Logger 持有抽象 Exporter reference。Composite 是 ★ 主考（requirement 標題第 2 行「複合模式——日誌框架」），承接 F1「輸出目的地數量 0..N，但 Logger.exporter cardinality = 1」。

### ConsoleExporter

最簡單的 leaf — `System.out.println` 一行收尾。沒持有任何 state，純行為。所有 ConsoleExporter instance 行為都一樣，理論上可以做成 Singleton，但教學專案不繞這層。

### FileExporter

差別在 leaf 但持有 `fileName: String` state。export 用 `Files.writeString` 配 `CREATE + APPEND` 兩個 OpenOption：CREATE 表示「沒檔就建」、APPEND 表示「有檔就附加在後面」（不覆蓋）。對應 requirement 第 27 行「檔案輸出器會將訊息添加（Append）至某個檔案中」。

`Files.writeString` 會 throw checked `IOException`，但 Exporter 介面的 `export` 沒宣告 throws — 為了保持介面契約簡潔。所以在 FileExporter 內部 try-catch 包成 RuntimeException 上拋。這是教學專案的折衷；production 該設計專屬 ExporterException（checked or unchecked）讓 caller 明確處理。

`+ System.lineSeparator()` 確保每筆 log 一行，跨平台正確（Windows 是 `\r\n`、Unix 是 `\n`）。

### CompositeExporter — Composite Pattern 教科書級實作

這是整題核心。三個結構特徵全中（對照 `decorator-vs-proxy.md` 的 Composite 識別準則）：

1. **同型別 wrapping**：CompositeExporter implements Exporter，內部又持有 `List<Exporter>` — 同型別包同型別
2. **委派**：`export()` 內 forEach child.export — 把工作分派給每個 child
3. **可遞迴巢狀**：child 也可以是另一個 CompositeExporter（requirement 第 29 行「複合輸出器中依序指定多個輸出器」+ 應用範例的 `new CompositeExporter(new ConsoleExporter(), new CompositeExporter(...))` 巢狀用法）

Constructor 用 varargs（`Exporter...`）— 對應 requirement 應用範例的 `new CompositeExporter(new ConsoleExporter(), new CompositeExporter(...))` 多參數風格。同時提供 `List<Exporter>` overload 給程式化建構場景（進階題 JSON parser 用 stream 收集後傳 array）。

`new ArrayList<>(Arrays.asList(exporters))` — defensive copy。如果直接 `Arrays.asList(exporters)` 然後存進 children，caller 修改原 array 會影響 Composite 內部狀態，破壞 immutability。雖然這個專案 caller 都不會這樣搞，但 CompositeExporter 的契約是 immutable，物件建構後不變，所以 defensive copy 是正確做法。

`getChildren()` 回 `List.copyOf(children)` 也是相同邏輯 — 防止外部拿到 internal list 後修改。

`export` 用 forEach 而非 stream — Sherry 慣例（CONVENTIONS）沒明文，但 forEach 對 side-effect 操作（IO）語意更清楚（stream 偏 functional / transformation 風格）。

CompositeExporter 沒有 add / remove method — immutable composite。一次傳入就決定 children，之後不能改。對應 CONVENTIONS 寫的 Sherry 拍板「immutable composite — 一次傳入 children，沒有 add/remove method」（line 130）。如果需要動態增刪，就重建 CompositeExporter。

---

## ⑤ Logger.java（核心）

```java
public class Logger {
    private final String name;
    private final Logger parent;
    private final Level levelThreshold;
    private final Exporter exporter;
    private final Layout layout;

    public Logger(String name, Logger parent, Level levelThreshold,
                  Exporter exporter, Layout layout) {
        // assignments
    }

    public Level resolveLevelThreshold() {
        if (levelThreshold != null) return levelThreshold;
        if (parent != null) return parent.resolveLevelThreshold();
        throw new IllegalStateException("Root logger must define levelThreshold");
    }

    public Exporter resolveExporter() { /* 同樣 pattern */ }
    public Layout resolveLayout() { /* 同樣 pattern */ }

    public void log(Level level, String content) {
        if (!level.isAtLeast(resolveLevelThreshold())) return;
        Message message = new Message(level, name, content);
        String formatted = resolveLayout().format(message);
        resolveExporter().export(formatted);
    }

    public void trace(String content) { log(Level.TRACE, content); }
    // info / debug / warn / error 同 pattern
}
```

Logger 是所有 force 交會的地方。OOA Q1.2 我答 Logger 是同一個概念（5 個身分都是同一個 Logger 的不同 facet），所以這裡是一個 concrete class，不切 LoggerInterface + LoggerImpl 雙層。

### 五個 final 欄位

`name` 必填、`parent` 在 Root 為 null 其餘必填、後三者（threshold / exporter / layout）都 nullable — 對應 OOA Phase 5 「Logger 三個設定欄位都標 [0..1]」（requirement 第 21 行「子日誌器只有在要覆寫父日誌器的設定時才需要設定…沒有覆寫的參數則繼承父日誌器的設定」）。nullable 是 F2 設定繼承 force 的具體 Java 表現。

全部 final + constructor injection — 跟 CONVENTIONS 拍板的 Java 風格一致。

### `resolveX()` 三個 method — F2 的核心承接

這是我曾經誤判成 Proxy 的地方。實際上純 OO null-fallback：

```java
if (levelThreshold != null) return levelThreshold;  // 自己有就回自己
if (parent != null) return parent.resolveLevelThreshold();  // 自己沒就遞迴查 parent
throw new IllegalStateException(...);  // 到 Root 都還沒就 throw（root 必須完整）
```

三步：自己有 → 自己沒但 parent 有 → 連 root 都沒（不可能合法）。這個 pattern 在三個 method 完全相同，重複是因為三個欄位型別不同（Level / Exporter / Layout）— Java 8 之後可以用 Function reference 抽出 helper（`resolve(field, Logger::resolveX)`），但教學專案保留重複版本，可讀性比抽象更重要。

為什麼最後一條是 throw 不是 return null？因為 requirement 第 21 行明寫「根日誌器必須要設定清楚分級門檻、輸出器和訊息佈置」 — root 必填是契約。如果 root 沒設又有人 log，是設定錯誤，throw 比 silent null 更早發現問題（fail-fast）。

### `log(Level, String)` — F5 + 三個 force 的串接點

```java
if (!level.isAtLeast(resolveLevelThreshold())) return;   // F5 過濾
Message message = new Message(level, name, content);
String formatted = resolveLayout().format(message);       // F3 strategy: Layout
resolveExporter().export(formatted);                      // F1 + F3: Composite + Strategy
```

四行流程跑完整個責任鏈：F5（過濾）→ 建 Message → F3（Layout strategy）→ F1（Composite Exporter — 可能是 leaf 也可能遞迴展開）。

第一行 `level.isAtLeast(...)` — 比較邏輯不寫在 Logger 而是寫在 Level 自己身上，這就是 ① 講過的「行為跟資料放在一起」。Logger 不需要知道 level 怎麼比大小。

Convenience method（`trace / info / debug / warn / error`）只是 `log(Level.X, content)` 的 syntactic sugar — 對應 requirement 應用範例的 `log.info(...)` / `log.trace(...)` 簡寫風格。

---

## ⑥ LogFramework.java（registry）

```java
public class LogFramework {
    private final Map<String, Logger> loggers = new HashMap<>();

    public void declareLoggers(Logger... loggers) {
        for (Logger logger : loggers) {
            this.loggers.put(logger.getName(), logger);
        }
    }

    public Logger getLogger(String name) {
        Logger logger = loggers.get(name);
        if (logger == null) {
            throw new IllegalArgumentException("No logger registered with name: " + name);
        }
        return logger;
    }

    // ── 進階題：loadFromJsonFile + parseLogger / parseExporter / parseLayout ──
}
```

LogFramework 承接 F4「Logger 在 Main 定義，但 Game / AI 任意檔案要以名稱取回同一個 instance」。最簡單做法就是一個 `Map<String, Logger>` + getter + setter。

`declareLoggers(Logger...)` 用 varargs — 對應 requirement 應用範例的 `declareLoggers(root, gameLogger, aiLogger)` 三個一起傳。內部 forEach put 進 Map。

`getLogger(name)` 找不到時 throw `IllegalArgumentException`，不 return null — fail-fast 邏輯：如果 Game / AI 寫了 `framework.getLogger("typo")` 但實際註冊的是 "app.game"，throw 立刻顯示問題；return null 會在後續 `log.info(...)` NPE，stack trace 跟根因隔好幾層。

### 為什麼沒做 Singleton？

LogFramework 全域唯一其實是 F4 的天然需求 — 一個程式只該有一個 logger registry。但 Sherry 還沒學 Singleton（CONVENTIONS 不超綱規則），所以沒寫 `private constructor + static getInstance()`。改用 dependency injection：Main 自己 new 一個 LogFramework，傳給 Game constructor，Game 傳給 AI。實際效果跟 Singleton 一樣（只有一個 instance），但寫法是 explicit DI 不是 implicit 全域。

未來 Sherry 學完 Singleton 可以 5 行內升級。

---

## ⑦ Game.java + ⑧ AI.java（應用範例領域物）

```java
public class Game {
    private final Logger log;
    private final AI[] players;

    public Game(LogFramework framework) {
        this.log = framework.getLogger("app.game");
        this.players = new AI[] {
                new AI("AI 1", framework),
                new AI("AI 2", framework),
                new AI("AI 3", framework),
                new AI("AI 4", framework)
        };
    }

    public void start() {
        log.info("The game begins.");
        for (AI ai : players) {
            log.trace("The player *" + ai.getName() + "* begins his turn.");
            ai.makeDecision();
            log.trace("The player *" + ai.getName() + "* finishes his turn.");
        }
        log.debug("Game ends.");
    }
}

public class AI {
    private final Logger log;
    private final String name;

    public AI(String name, LogFramework framework) {
        this.name = name;
        this.log = framework.getLogger("app.game.ai");
    }

    public void makeDecision() {
        log.trace(name + " starts making decisions...");
        log.warn(name + " decides to give up.");
        log.error("Something goes wrong when AI gives up.");
        log.trace(name + " completes its decision.");
    }
}
```

這兩個 class 完全照 requirement 應用範例（第 82-129 行）的 Java 等價物。需求範例是虛擬碼，Java 化幾乎 1:1 對應。

設計重點：**Logger 透過 LogFramework 取得，不是直接 new**。對應 requirement 第 36 行「開發者能在程式任意處取得某一名稱的日誌器」— Game 與 AI 都用 `framework.getLogger("...")` 拿 Logger，不會也不該自己 `new Logger(...)`。這個約束是 F4 force 的具體承接。

`AI[] players = new AI[]{...}` 用 array 不用 ArrayList — requirement 範例就是用 array (`var players = {new AI("AI 1"), ...}`)，4 個固定值不會動態增減，array 比 list 更貼切。

AI 持有 `name` 是因為 requirement 範例需要 `ai.getName()`。

---

## ⑨ Main.java（in-code 配置入口）

```java
public class Main {
    public static void main(String[] args) {
        LogFramework framework = new LogFramework();

        Logger root = new Logger("Root", null,
                Level.DEBUG, new ConsoleExporter(), new StandardLayout());

        Logger gameLogger = new Logger("app.game", root,
                Level.INFO,
                new CompositeExporter(
                        new ConsoleExporter(),
                        new CompositeExporter(
                                new FileExporter("game.log"),
                                new FileExporter("game.backup.log"))),
                null);  // layout 繼承 root

        Logger aiLogger = new Logger("app.game.ai", gameLogger,
                Level.TRACE,
                null,  // exporter 繼承 gameLogger
                null);  // layout 繼承 gameLogger → root

        framework.declareLoggers(root, gameLogger, aiLogger);

        Game game = new Game(framework);
        game.start();
    }
}
```

完全照 requirement 第 49-80 行的應用範例配置 Logger tree：
- root 完整設定（DEBUG threshold + ConsoleExporter + StandardLayout）
- app.game 覆寫 threshold + exporter（巢狀 CompositeExporter — 3 個目的地：console + 2 files），layout 用 null 繼承 root
- app.game.ai 只覆寫 threshold（TRACE），exporter 跟 layout 都 null 繼承 app.game → root

`null` 在 OOA 紀律上對應「設定欄位 [0..1]」的下界 0 — 表達「我不設、由 parent 提供」。Java code 寫 `null` 是最直接的表達。

三個 Logger 一起 `declareLoggers(root, gameLogger, aiLogger)` 註冊，這時候 Game 才能 getLogger by name 找到。

Main 跑出來：console + game.log + game.backup.log 三路輸出內容完全一致（Composite 三路同步） — 對應 requirement 第 132 行「控制台輸出 / game.log 檔案內容 / game.backup.log 檔案內容皆為以下」。

### 為什麼用 args 而不寫 default config path？

Main 沒接受 args（簡化版）— 整個配置寫死在 code 裡。進階題版本 `Main_Json` 才會接收 args 給 config 檔路徑。Main 主要展示「**in-code 配置**」這個 use case，沒必要 parametrize。

---

## ⑩ JsonFile.java（進階題 — 外部 JSON 配置領域實體）

```java
public class JsonFile {
    private final String path;
    private final String content;

    public JsonFile(String path) {
        this.path = path;
        try {
            this.content = Files.readString(Path.of(path));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + path, e);
        }
    }
    // getters
}
```

進階題 F6「配置外部化」對應的 OOA 概念實體。requirement 第 154 行「允許開發者撰寫一份 JSON 格式的檔案來定義和配置所有日誌器」— JSON 檔案本身就是領域中存在的外部物件，OOA 收進 Phase 5 是合理的。

設計問題：constructor 要不要在建構時就讀檔，還是延遲到 caller 主動 `load()`？

我選**建構時讀**：JsonFile instance 一旦存在就保證 content 已 ready，consumer 不用做 lifecycle 管理（沒有 `isLoaded()` 之類的多餘 API）。代價是 constructor 會 throw（IO 失敗時）— 但既然 JsonFile 的存在意義就是「指向一個 JSON 檔」，找不到檔本來就是錯誤狀態，建構失敗就 throw 是合理的。

IO 部分跟 FileExporter 一樣：checked `IOException` 用 RuntimeException 包起來，避免汙染領域 API 介面。

JsonFile 本身**不參與 parsing** — 它只是「外部檔案 + 內容字串」的代表。Parsing 是 LogFramework 的責任（透過 MiniJsonParser）。這是單一職責切割。

---

## ⑪ MiniJsonParser.java（手寫 recursive-descent JSON parser）

```java
public class MiniJsonParser {
    private final String text;
    private int pos = 0;

    public static Object parse(String text) {
        MiniJsonParser parser = new MiniJsonParser(text);
        Object result = parser.parseValue();
        parser.skipWhitespace();
        if (parser.pos != text.length()) {
            throw new IllegalArgumentException("Unexpected trailing content at " + parser.pos);
        }
        return result;
    }

    private Object parseValue() {
        // dispatch by 第一個非空字元：{ → object / [ → array / " → string / digit → number / ...
    }

    private Map<String, Object> parseObject() { /* ... */ }
    private List<Object> parseArray() { /* ... */ }
    private String parseString() { /* ... */ }
    private Number parseNumber() { /* ... */ }
}
```

這個檔是進階題最大的工程量 — ~120 行。為什麼自己手寫不引入 Gson / Jackson？

**約束**：Sherry 的專案是純 Java IntelliJ project，沒設 Maven / Gradle，沒 dependency 管理。Java 標準庫**沒有 built-in JSON parser**（Jakarta JSON API 是外部 spec）。

**選項評估**：
- 引入 Gson / Jackson → 違反 CONVENTIONS「不引入框架」（line 144）
- 用 properties / XML 繞開 → 違反 requirement「JSON 格式」明文要求
- 自己手寫 minimal recursive-descent JSON parser → 純 Java、無 dependency、~120 行可控

選第三個。教學意義：學員看得懂 parsing 流程，不是 black box。

### Recursive-descent 結構

`parseValue()` 是 dispatcher — 看下一個非空字元決定走哪條：
- `{` → `parseObject()`（呼叫 parseString 取 key + 遞迴 parseValue 取 value）
- `[` → `parseArray()`（遞迴 parseValue 多次）
- `"` → `parseString()`（含 escape 處理：`\n \t \r \" \\ \/`）
- `-` 或數字 → `parseNumber()`
- `true / false / null` → 對應 Boolean / null

每個 parser method 維護 shared state `pos`（當前解析位置），遞迴呼叫間自動推進。

`peek()` 看當前字元、`expect(c)` 驗證並推進、`skipWhitespace()` 跳過空白 — 三個 token-level helper 構成解析骨幹。

### 型別對應

```
JSON              Java
object       →    LinkedHashMap<String, Object>
array        →    ArrayList<Object>
string       →    String
number       →    Long 或 Double（看有沒有小數點）
true/false   →    Boolean
null         →    null
```

用 `LinkedHashMap` 而不是 `HashMap` — 保留 JSON 的 key 順序。雖然 JSON spec 說 object 是 unordered，但對 logger tree 結構，子 logger 遍歷順序若隨機會讓 debug 變難。`LinkedHashMap` 給出 deterministic 行為。

`parseNumber` 用 `String.contains(".")` 簡判 — 沒小數點 `Long.parseLong`、有小數點 `Double.parseDouble`。Logger config 用不到大數值或精度問題。

### 不支援的範圍

- Unicode escape `\uXXXX`
- Scientific notation `1e10`
- Nested escape sequences

對 logger config 用途夠了。production-grade JSON parser 處理這些需要再加 ~50 行，教學專案不必。

---

## ⑫ LogFramework.loadFromJsonFile（進階題 — JSON → Logger tree）

```java
public void loadFromJsonFile(JsonFile jsonFile) {
    Object parsed = MiniJsonParser.parse(jsonFile.getContent());
    if (!(parsed instanceof Map)) throw new IllegalArgumentException(...);
    Map<String, Object> root = (Map<String, Object>) parsed;
    Object loggersNode = root.get("loggers");
    if (!(loggersNode instanceof Map)) throw new IllegalArgumentException(...);
    parseLogger("Root", (Map<String, Object>) loggersNode, null);
}

private void parseLogger(String name, Map<String, Object> spec, Logger parent) {
    Level levelThreshold = parseLevel(spec.get("levelThreshold"));
    Exporter exporter = parseExporter(spec.get("exporter"));
    Layout layout = parseLayout(spec.get("layout"));

    Logger logger = new Logger(name, parent, levelThreshold, exporter, layout);
    loggers.put(name, logger);

    for (Map.Entry<String, Object> entry : spec.entrySet()) {
        if (RESERVED_KEYS.contains(entry.getKey())) continue;
        parseLogger(entry.getKey(), (Map<String, Object>) entry.getValue(), logger);
    }
}
```

把 JSON 字串 → Logger tree 的轉換責任放在 LogFramework 而不是新開一個 JsonParser class（簡化版，不引入 Builder Pattern）。F6 force 的承接結構就是這個 method。

### 三步驟模式

`parseLogger` 遞迴每一層 logger node：
1. **取出自己的設定**（threshold / exporter / layout） — null 表示 JSON 沒給，自動繼承
2. **建構 Logger + 註冊**到 framework
3. **遞迴處理子 logger** — JSON 的 nested key 如果不在 RESERVED_KEYS（`{"levelThreshold", "exporter", "layout"}`）內，就當作子 logger 名稱

`RESERVED_KEYS` 是個 hardcoded constant — JSON 規格設計選擇把「設定欄位」跟「子 logger」混在同一個 JSON object 內，用「保留字」區分。這個設計有風險（如果未來新增 Logger 欄位要記得更新 RESERVED_KEYS），但對應 requirement 第 162 行的 JSON 範例就是這個結構，照需求做。

### parseExporter — type 字串 dispatch

```java
return switch (type) {
    case "console"   -> new ConsoleExporter();
    case "file"      -> new FileExporter((String) spec.get("fileName"));
    case "composite" -> {
        List<Object> childSpecs = (List<Object>) spec.get("children");
        Exporter[] children = childSpecs.stream()
                .map(this::parseExporter)
                .toArray(Exporter[]::new);
        yield new CompositeExporter(children);
    }
    default -> throw new IllegalArgumentException("Unknown exporter type: " + type);
};
```

Java 14+ 的 enhanced switch — 比舊的 `switch (...) { case ...: break; }` 簡潔。每個 case 直接回 Exporter，最後一條 default throw。

`"composite"` 的 case 是遞迴 — children 是 JSON array of exporter specs，map 每個 child 遞迴 `parseExporter`，最後 yield 新的 CompositeExporter。

**沒用 Builder Pattern** — Sherry 還沒學。如果用 Builder 體系（參考 `OOAD/參考839_JsonParse.png`），會多 5+ 個 class（ExporterBuilder 抽象 + 3 個 Concrete + LayoutBuilder + 1 個 Concrete + Director），對教學範圍而言 over-engineered。

**parser OCP 缺點**：未來新增 Exporter 種類（例如 NetworkExporter），要改 `parseExporter` 的 switch — 違反 OCP for parser extension。但 requirement 設計需求 D 的 OCP 只針對 Logger / 既有 code，沒明文要求 parser 也 OCP。**嚴格說沒違反需求**。Sherry 學完 Builder 後可重構升級。

`@SuppressWarnings("unchecked")` — 因為 `(Map<String, Object>) obj` cast 會有 unchecked warning（runtime type erasure），但 JSON parser 的型別契約我自己定義清楚，cast 是安全的，suppress 掉 noise。

### parseLayout — 簡化版

```java
return switch (name) {
    case "standard" -> new StandardLayout();
    default -> throw new IllegalArgumentException("Unknown layout: " + name);
};
```

目前只有一種 layout，switch 一個 case。未來 JSONLayout / XMLLayout 加進來再多 case。

---

## ⑬ Main_Json.java（進階題入口）

```java
public class Main_Json {
    public static void main(String[] args) {
        String configPath = args.length > 0
                ? args[0]
                : "src/ch4_structural/Gym6c_log_framework/game-config.json";

        LogFramework framework = new LogFramework();
        framework.loadFromJsonFile(new JsonFile(configPath));

        Game game = new Game(framework);
        game.start();
    }
}
```

**只差兩行** vs `Main.java` — `loadFromJsonFile(new JsonFile(...))` 取代了 Main 中 13 行的 `new Logger(...) × 3 + declareLoggers(...)` 配置。

這正是 F6 force「**in-code vs JSON 兩種來源產出語義等價 Logger tree**」的具體驗證：建構方式換了，但下游 Game / AI 完全不用改，因為它們只依賴 LogFramework.getLogger 這個 by-name 取 Logger 的介面。

`args.length > 0 ? args[0] : default` 對應 CONVENTIONS 的 sibling 慣例（Gym6b 的 Main 也這樣寫，line 119-120）— 接收 runtime 參數但有 default fallback，IntelliJ 直接跑也能 work。

跑出來的 console 輸出跟 Main 版本**逐字元相同**（時間戳除外），證明等價建構成立。

---

## ⑭ game-config.json（範例配置檔）

完全照 requirement 第 156-193 行的進階題 JSON 範例，唯一差別是 fileName 加了相對路徑前綴：

```json
"fileName": "src/ch4_structural/Gym6c_log_framework/result/game.log"
```

requirement 範例只寫 `"game.log"`，但實際跑時這個相對路徑會落在 working directory（IntelliJ 跑時是 project root），不在 `result/` 子資料夾。所以這裡加長路徑讓輸出落在乾淨位置。

如果 Sherry 想保持跟 requirement 一模一樣的 `"game.log"`，可以改回去 — 只是 log 會出現在 SoftwareDesignPattern/ 根目錄。

---

## ⑮ 跑法 + 驗證

```bash
cd SoftwareDesignPattern
javac -encoding UTF-8 -d out src/ch4_structural/Gym6c_log_framework/*.java

# 主需求
java -cp out ch4_structural.Gym6c_log_framework.Main

# 進階題（JSON 配置）
java -cp out ch4_structural.Gym6c_log_framework.Main_Json

# 進階題 + 自訂配置
java -cp out ch4_structural.Gym6c_log_framework.Main_Json /path/to/config.json
```

`-encoding UTF-8` 必加 — 不然 Logger.java 等含中文註解的檔在 Windows 預設 cp1252 編碼下會 compile fail。

驗證：兩個 Main 跑出來的 console 輸出**完全相同**（時間戳除外）— 證明 F6 等價建構達成。同時 `result/game.log` + `result/game.backup.log` 兩檔內容也相同（Composite 三路同步驗證）。

---

## ⑯ 取捨記錄總表（為什麼這樣寫不那樣寫）

| # | 取捨 | 為什麼這樣 | 為什麼不那樣 |
|---|---|---|---|
| 1 | F2 不用 Proxy | OO null-fallback 直接表達意圖 | Proxy 的 intent 是 access 控制，不符 F2 force（self-correction 詳見 2026-05-14_proxy-misjudge-no-source-verify.md）|
| 2 | LogFramework 不掛 Singleton 名 | Sherry 還沒學 Singleton（不超綱）| 用 DI 傳遞，效果相同 |
| 3 | JSON parsing 不用 Builder | Builder 還沒學 | 用 switch dispatch，違反 parser OCP 但未違反需求 OCP |
| 4 | Logger 是 class 不切 Interface + Impl | Q1.2 答 Logger 是同一個概念 | 不過度抽象 |
| 5 | Message 抽 value object | 4 個欄位（level + name + content + timestamp）邏輯上一束 | 不一路傳 4 個獨立參數 |
| 6 | FileExporter 用 RuntimeException 包 IOException | 介面契約簡潔，沒有 checked throws | 教學專案 OK，production 該設 ExporterException |
| 7 | MiniJsonParser 自己手寫 | 純 Java、無 dependency、教學意義 | 不引 Gson / Jackson |
| 8 | enhanced switch（Java 14+）| 簡潔可讀 | 不用舊版 break |
| 9 | DateTimeFormatter 設 static | thread-safe + immutable，全程式共用 | 不每 instance new |
| 10 | CompositeExporter immutable | 一次傳入 children，沒 add/remove | 跟 CONVENTIONS 拍板的 immutable composite 一致 |

---

## OOA → 實作對照（給未來自己看）

| OOA 圖元素 | 對應 Java | 對應 method / field |
|---|---|---|
| Logger | `Logger.java` | 全 |
| Logger.parent (0..1) | `parent` field | `getParent()` |
| Logger.levelThreshold [0..1] | `levelThreshold` field | `resolveLevelThreshold()` |
| Logger.exporter [0..1] | `exporter` field | `resolveExporter()` |
| Logger.layout [0..1] | `layout` field | `resolveLayout()` |
| Level (enum) | `Level.java` | `isAtLeast(threshold)` |
| Exporter (abstract) | `Exporter.java` interface | `export(String)` |
| ConsoleExporter | `ConsoleExporter.java` | `System.out.println` |
| FileExporter (fileName) | `FileExporter.java` | `Files.writeString APPEND` |
| CompositeExporter ★ | `CompositeExporter.java` | `children: List<Exporter>` + forEach |
| Layout (abstract) | `Layout.java` interface | `format(Message)` |
| StandardLayout | `StandardLayout.java` | `DateTimeFormatter` 排版 |
| Message (value object) | `Message.java` | 4 個 final 欄位 |
| LogFramework | `LogFramework.java` | `loggers: Map` + `getLogger` / `declareLoggers` |
| LogFramework.source 0..1 → JsonFile（進階）| `loadFromJsonFile` | 遞迴 parseLogger |
| JsonFile | `JsonFile.java` | path + content |

---

## 相關文件

- 題目原文：`analyze/requirement.md`
- OOA 推導：`analyze/ooa-worksheet.md`（Phase 1-5 完整紀錄）
- 初版實作摘要：`analyze/IMPLEMENTATION.md`（spec 風格，被本檔的 narrative 風格取代；保留作為簡短 reference）
- UML 圖：`OOAD/Gym6c.asta` + `OOA_v1.png` + 兩張參考
- Scout self-correction（Proxy 誤判）：`team/scout/logs/self-corrections/2026-05-14_proxy-misjudge-no-source-verify.md`
- 專案慣例：`src/CONVENTIONS.md`
