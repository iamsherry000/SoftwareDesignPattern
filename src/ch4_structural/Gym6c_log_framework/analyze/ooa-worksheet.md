# 道館 6c 日誌框架 — OOA Worksheet

> 教練 Socratic 推 OOA。Scout 主問、Sherry 答。所有結論以 Sherry 親口為準，Scout 不代答。
>
> **⚠️ Override (2026-05-14, Sherry 限時)**：今天 Scout 切 power-through mode — 自己決定能決定的、只在無法決定的岔口問 Sherry。本日所有 Scout 代答內容，事後若 Sherry 不認可可隨時推翻。
>
> Last updated: 2026-05-14

---

## ⏸ Session Resume Point（2026-05-14 重啟前暫存）

**目前在**：Phase 1（點的萃取）
**已問**：Q1.1（複合輸出器 form vs force）+ Q1.2（Logger 是否單一概念）
**等 Sherry 答**：Q1.1、Q1.2 兩題
**重啟後接續**：直接看下面 Phase 1 兩題，逐題答 → Scout 接 Phase 2
**檔案路徑**：`analyze/ooa-worksheet.md`（同 requirement.md 並列；2026-05-14 從 OOAD/ 搬到 analyze/）

**規則重申**（給重啟後的 Scout 自己看）：
- canonical vocabulary 一字不改（互動 / 關鍵句型 / 自洽 / 點的萃取 / 設計的自洽 / 力）
- 從前提切上游（先掃前提，不先批結論）
- 給判準不堆術語（教練感 = 給可操作判準）
- Sherry 要最高強度檢視 — 不縮、不灌、發現邏輯漏洞點出來

---

## Phase 1 — 點的萃取（名詞掃描 + 同名異義檢測）

### Scout 問題（第一輪 — 從前提切上游）

#### Q1.1（form vs force）

Requirement 已經把「**複合輸出器 (Composite Exporter)**」寫進來，並且明確示範 `new CompositeExporter(new ConsoleExporter(), new CompositeExporter(...))` 這種巢狀用法。

對 OOA 階段而言，這個「複合輸出器」是 **form** 還是 **force**？

判準提示（不告訴妳答案，提示妳判準該怎麼下）：
- 「**form**」= 需求文字寫進來的具體解／結構
- 「**force**」= 推動這個解產生的「設計上的拉力」
- 「**form vs force 混淆**」是道館 4-6 級學員最常踩的坑（題庫雜項/form_vs_force_放得進去不等於該放進去.md）

請答：
1. 妳判 form 還是 force？
2. 判準是什麼？
3. 如果是 form，背後的 force 是什麼？（用「關鍵句型」表達 — 主詞 + 變動性 + 約束）

---

#### Q1.2（同一名詞 多重身分）

「**Logger**」這個名詞在 requirement 裡至少出現以下身分：

| 出現位置 | 被當成什麼用 |
|---|---|
| 「定義多個日誌器」 | 配置時的「規格」 |
| 「取得特定日誌器」 | runtime 可被取得的物件 |
| 「請求撰寫不同分級的日誌訊息」 | 行為主體（接收請求） |
| 「繼承父日誌器的設定」 | 設定值的繼承來源 |
| 「日誌器的分級門檻」 | 屬性的擁有者 |

請答：
1. 這幾個身分描述的**是同一個東西嗎**？
2. 如果是 → 用一句話定義 Logger 的本質是什麼
3. 如果不是 → 妳會切成幾個概念？分別是什麼？

判準提示：
- canonical 規則「**互動 = 行為中需要使用另一個實體的能力**」
- 道館一 SOP「**可存在 vs 已存在 vs 抽象分類**」
- 5/13 新規則「**裡面 vs 之間**」 — 裡面 = intrinsic 特質；之間 = inter-class 互動

---

### Scout 代答（Q1.1）— 2026-05-14 power-through

**1. 判 form 還是 force？** → **form**

**2. 判準**：requirement 已直接寫出「複合輸出器 (Composite Exporter)」這個具體類別、給出 `new CompositeExporter(...)` 巢狀範例、並把它列入「輸出器僅有以下幾種」的清單裡。這是「需求文字寫進來的具體解／結構」本身，不是推動這個解的拉力。

**3. 背後的 force（關鍵句型）**：

> **輸出目的地的數量** 在不同 Logger 配置下會 **0..N 變動**，但 **Logger 對外只承諾持有單一 exporter 欄位** 的約束下，需要一個「對外看像單一 exporter、對內可承載任意數量子 exporter」的中介物。

- 主詞：輸出目的地的數量
- 變動性：0..N（題目示範了「ConsoleExporter 一個」也示範了「Console + (File + File) 三個」）
- 約束：Logger.exporter 欄位 cardinality = 1

→ 這個 force 對應 GoF Composite Pattern 的 canonical force（client 對 leaf / composite 一視同仁），但在本題的具現是「**單一 exporter 欄位 vs N 個輸出目的地**」的張力。

---

### Scout 代答（Q1.2）— 2026-05-14 power-through

**1. 是同一個東西嗎？** → **是同一個概念**

**2. Logger 本質定義**：

> Logger = **一個有唯一名稱、能依分級門檻過濾日誌請求、並委派輸出器寫出訊息的有名實體；其過濾／輸出／佈置設定可向 parent Logger 繼承**。

**3. 拆解五個身分為何都收斂到同一個概念**：

| 身分 | 為何屬於同一個 Logger |
|---|---|
| 「定義 Logger」 | Logger 的 lifecycle 之一 — instantiation phase（不是另一個 class，是 Logger 的構造階段） |
| 「取得 Logger」 | 同一個 instance 的 lookup — 透過全域 LoggerRegistry by name 取回 |
| 「請求撰寫訊息」 | Logger 的 method（行為） |
| 「繼承父 Logger 設定」 | Logger 自有 parent: Logger? 屬性，繼承是 Logger 自身的 lookup 行為 |
| 「擁有分級門檻」 | Logger 自有 levelThreshold? 屬性 |

→ 這五件事都是「**同一個 Logger instance 的不同 facet**」（lifecycle / 行為 / 屬性 / 關聯）。

**判準依據**（canonical 規則）：
- 「**裡面 vs 之間**」(5/13)：levelThreshold / exporter / layout / name 都是 Logger 的 intrinsic 特質（裡面）。parent 關係是 Logger 與 Logger 之間（之間，但仍是 Logger 自身擁有的關聯欄位，不需另切 class）。
- 「**可存在 vs 已存在 vs 抽象分類**」：「定義 Logger 規格」與「runtime 取得 Logger」是同一物件的可存在 → 已存在 transition，不構成同名異義。

**唯一可能要切的**：runtime Logger instance vs 進階題的 JSON config schema。但 JSON config 屬於進階題的 **配置語法**（不是 OOA 模型的領域物件），所以 OOA 階段不切，留給設計階段討論「ConfigParser 如何讀 JSON 並 instantiate Logger」。

---

## Phase 2 — 類別 / 物件 / 屬性 / 關聯 分類（Scout 代答 2026-05-14）

### A. 類別清單

| 類別 | 種類 | 說明 |
|---|---|---|
| **Logger** | 領域實體 | 有名實體，過濾 + 委派 |
| **Level** | 列舉 | TRACE < INFO < DEBUG < WARN < ERROR（**有序**，題目明說「越上方分級越小」） |
| **Exporter** | 抽象介面 | 輸出契約（OCP 設計需求 D） |
| **ConsoleExporter** | Exporter 子類 | leaf — 寫到 stdout |
| **FileExporter** | Exporter 子類 | leaf — append 到指定檔案 |
| **CompositeExporter** | Exporter 子類 | composite — 持有多個 child Exporter |
| **Layout** | 抽象介面 | 排版契約（OCP 設計需求 E） |
| **StandardLayout** | Layout 子類 | `yyyy-MM-dd HH:mm:ss.SSS \|-LEVEL name - msg` |
| **LoggerRegistry** | 全域註冊表 | 「在程式任意處取得某一名稱的 Logger」需要它 |

### B. 屬性

**Logger**：
- `name: String` — 唯一，Root 必為 "Root"
- `parent: Logger?` — Root 為 null，其餘必有
- `levelThreshold: Level?` — Root 必填，子可 nullable（null = 繼承父）
- `exporter: Exporter?` — 同上
- `layout: Layout?` — 同上

**FileExporter**：
- `fileName: String`

**CompositeExporter**：
- `children: List<Exporter>` — 1..N，依序 export

### C. 關聯（之間 — inter-class）

| 來源 | 目標 | 重數 | 性質 |
|---|---|---|---|
| Logger | Logger (parent) | 0..1 | 繼承樹（Root 0、其餘 1） |
| Logger | Exporter | 0..1 | 持有（null 則繼承父） |
| Logger | Layout | 0..1 | 持有（null 則繼承父） |
| CompositeExporter | Exporter | 1..N | 組合（Composite Pattern） |
| LoggerRegistry | Logger | 1..N | 全域 Map<name, Logger> |

### D. 關鍵觀察

1. **Logger 的繼承是「設定 lookup chain」不是 OOP 類別繼承** — parent 欄位是 instance-level relation，不是 `extends`。
2. **Level 是 enum 不是 class** — 行為極弱（只有比較大小），用 enum 即可。
3. **LoggerRegistry 的存在是被需求逼出來的** — 「在程式任意處取得某一名稱的日誌器」這句話 = 全域 by-name lookup = registry。

---

## Phase 3 — 互動 / 操作開在誰身上（Scout 代答 2026-05-14）

### 互動清單（who calls whom）

| 主動方 | 被動方 | 行為 | 觸發條件 |
|---|---|---|---|
| Developer | LoggerRegistry | `getLogger(name): Logger` | 任意位置要取得 Logger |
| Developer | Logger | `log(level, msg)` / `info()` / `debug()` / `trace()` / `warn()` / `error()` | 寫日誌 |
| Logger (self) | Logger (self) | `resolveLevelThreshold(): Level` | 過濾前查門檻（向 parent 遞迴） |
| Logger (self) | Logger (self) | `resolveExporter(): Exporter` | 通過門檻後查輸出器 |
| Logger (self) | Logger (self) | `resolveLayout(): Layout` | 同上查佈置 |
| Logger | Layout | `format(level, loggerName, msg): String` | 取得排版好的字串 |
| Logger | Exporter | `export(formattedMsg)` | 把字串送出去 |
| CompositeExporter | child Exporter | `export(formattedMsg)` | forEach child |

### 操作開在誰身上 — 判準

- **過濾邏輯開在 Logger** — 因為門檻屬於 Logger 自身屬性（裡面）。
- **resolve\* 系列開在 Logger** — 繼承 lookup 是 Logger 對 parent 的關係（之間，但 query 動作是 Logger 主動），所以 method 開在 Logger 身上。
- **format 開在 Layout** — Layout 是 Strategy，「如何排版」屬於 Layout 自身知識（裡面）。
- **export 開在 Exporter** — 同 Strategy 思路。CompositeExporter override export 為 forEach child.export(msg)。
- **getLogger 開在 LoggerRegistry** — 全域 by-name lookup 不該開在 Logger 自身（Logger 不該知道兄弟姊妹），開在 Registry 才符合單一職責。

---

## Phase 4 — Forces 盤點（Scout 代答 2026-05-14，**純 OOA 階段**）

> **OOA 階段紀律**：只列 force（從需求抽出來的「拉力」），**不掛 form / 不寫 GoF pattern 名稱**。form 對應留給 OOD 階段做。
>
> 預告（不算 OOA 結論，只給 Sherry 心裡有底）：Sherry 提示「主考 Proxy + 之前學過的都可以用」。OOA 階段先把 force 抓乾淨，OOD 才會把這些 force 對應到 Proxy / Composite / Strategy 等 form。

---

### Force 1 — 多輸出目的地 vs 單一持有欄位 ★ 主考 force

> **輸出目的地的數量** 在不同 Logger 配置下會 **0..N 變動**（題目示範：「ConsoleExporter 一個」也示範「Console + (File + File) 三個」），但 **Logger 對外只承諾持有單一 exporter 欄位** 的約束下，需要一個能「對外看像單一輸出器、對內承載任意數量子輸出器」的中介物。

- 主詞：輸出目的地的數量
- 變動性：0..N
- 約束：Logger.exporter cardinality = 1
- 需求依據：A「複合輸出器」+ 應用範例的巢狀 `CompositeExporter(...)` 配置 + **requirement.md 標題第 2 行「複合模式——日誌框架」明示主考**

---

### Force 2 — 設定繼承（透明委派）

> 子 Logger **多數情況沿用父設定，少數情況覆寫**；client（Game / AI）拿到子 Logger 後不該知道也不該關心「這個設定到底是子自己有的還是繼承自父」。

- 主詞：levelThreshold / exporter / layout 的設定來源
- 變動性：自有 vs 繼承自 parent（每個欄位獨立決定）
- 約束：對 client 透明 — 子 Logger 與 root Logger 對外行為一致（同樣能 log()），client 一律以 Logger 的角色操作之
- 需求依據：A「子日誌器只有在要覆寫父日誌器的設定時才需要設定…沒有覆寫的參數則繼承父日誌器的設定」+ 應用範例（Game 拿 app.game、AI 拿 app.game.ai 都直接呼叫 log.info / log.trace）

> **❌ 注意**：Scout 2026-05-14 曾誤判 F2 對應 Proxy Pattern，已在 self-correction log 修正。F2 **不對應任何 GoF pattern** — 就是 OO 基本的 self-association + null-fallback lookup（parent reference + 自己沒設就向上查父）。詳見 `team/scout/logs/self-corrections/2026-05-14_proxy-misjudge-no-source-verify.md`

---

### Force 3 — Exporter / Layout 種類未來會擴充

> 開發者未來會新增 Exporter 種類（例如 NetworkExporter、DatabaseExporter）和 Layout 種類（例如 JSONLayout），但**不希望改動 Logger 既有程式碼**。

- 主詞：Exporter / Layout 的種類數量
- 變動性：未來會增加未知種類
- 約束：OCP — 對擴充開放、對既有程式碼修改封閉
- 需求依據：設計需求 B + C 明文 OCP 要求

---

### Force 4 — 跨檔案取得同一個 Logger

> Logger 在 Main 中定義配置，但會在 Game / AI 等任意檔案中**以名稱取回同一個 Logger**。創建位置與使用位置在程式中分離。

- 主詞：Logger 的取得位置
- 變動性：可能在程式任意處
- 約束：必須以名稱（unique name）取回到「同一個」Logger，不是新建一個
- 需求依據：A「使用日誌器：開發者能在程式任意處取得某一名稱的日誌器」+ 應用範例 `getLogger("app.game")`

---

### Force 5 — 訊息分級門檻過濾

> 開發者請求撰寫不同分級的訊息，但 Logger 須**根據自身 level threshold 決定要不要受理** — 高過門檻的訊息才往下走（formatting + export）。

- 主詞：日誌訊息能否通過門檻
- 變動性：依 (請求 level, Logger threshold) 兩者比較結果
- 約束：threshold 是 Logger 的內在屬性，過濾發生在 Logger 自身、在 export 之前
- 需求依據：A「日誌器會根據自身的分級門檻評估要不要受理這項請求」

---

### Force 6 — 配置外部化 / 建構來源可替換（進階題 2026-05-14 加入）

> 進階題允許開發者用 **JSON 檔案** 描述整棵 Logger tree（取代 in-code `new Logger(...)`）。同一個 LogFramework 可選擇「in-code 直接建構」或「從 JSON 檔讀入建構」，兩種來源必須產出**語義等價的 Logger tree**（同樣的 hierarchy / 設定 / by-name 取得能力）。

- 主詞：Logger tree 的建構來源
- 變動性：in-code 寫死 vs 外部 JSON 檔
- 約束：建構來源切換不影響 Logger / Exporter / Layout 對外行為；不改 code 就能調 Logger 設定（部署 / 維運分離 code 與 config）
- 需求依據：C「進階挑戰題 — 允許開發者撰寫一份 JSON 格式的檔案來定義和配置所有日誌器」

---

### OOA 階段 Forces ↔ OOA 圖結構對應表（總表）

6 條 forces 都來自 requirement 字面，每條對應到圖上哪個結構：

| # | Force | 主詞 / 變動性 | OOA 圖承接結構 |
|---|---|---|---|
| **F1 ★主考** | **多輸出目的地 vs 單一欄位** | **數量 0..N / Logger.exporter cardinality=1** | **CompositeExporter ──◆ `children` 1..N ──▶ Exporter**（self-recursive 到抽象） |
| F2 | 設定繼承（透明委派） | 設定來源變動 / 對 client 透明 | Logger self-association：`parent` 0..1 ↔ `children` 0..\* + Logger 的 levelThreshold/exporter/layout 三欄位都標 [0..1] |
| F3 | Exporter / Layout 種類擴充 | 種類數量增 / OCP | Exporter / Layout 抽象 + generalization 三角箭頭（CompositeExporter / ConsoleExporter / FileExporter ▷ Exporter；StandardLayout ▷ Layout） |
| F4 | 跨檔案取得 Logger | 取得位置任意 / by-name | LogFramework ──1 ↔ 0..\*── Logger + LogFramework 提供 `getLogger(name): Logger` |
| F5 | 訊息分級門檻過濾 | 訊息能否通過 | Logger.levelThreshold: Level [0..1] + `writeMessage(level, msg)` 行為 |
| F6 進階 | 配置外部化 | in-code vs JSON 檔 | LogFramework ──`source` 0..1 ──▶ JsonFile（新加 box + 新加 line） |

→ **OOD 階段 form 對應**（按 Sherry 已學範圍 — Builder / Singleton 都還沒學，故剔除）：
- **F1 → Composite ★主考**（CompositeExporter implements Exporter + children）
- **F3 → Strategy**（Exporter / Layout 抽象介面 + runtime 替換）
- F2 → 無 GoF（純 OO self-association + null-fallback）
- F4 → 無 GoF（LogFramework 持有 Logger 集合 + by-name lookup，不掛 Singleton 名）
- F5 → 無 GoF（Logger 內部 if 判斷）
- F6 → 無 GoF（LogFramework 直接讀 JsonFile，不引入 Builder 體系）

**結論：這題能用的 form 就 Composite + Strategy 兩個**，其餘 forces 都是 OO 基本功承接，不掛 GoF 名稱。

---

## Phase 5 — OOA 類別圖（純需求版，2026-05-14）

> **OOA 紀律**：只放需求字面寫到的概念類別與關係。不放 OOD 才需要的 helper（如 LoggerRegistry）。不寫 «interface» / «abstract» stereotype（OOA 不分 interface vs class，用「抽象分類 + generalization 三角」表達）。**不標 pattern 名稱**。

### A. 概念類別（7 個）

| # | 名稱 | 屬性 | 備註 |
|---|---|---|---|
| 1 | **Logger** | `name: String`（unique）<br>`levelThreshold: Level [0..1]`（可繼承）<br>`exporter: Exporter [0..1]`（可繼承）<br>`layout: Layout [0..1]`（可繼承） | 領域核心實體 |
| 2 | **Level** | enum: TRACE, INFO, DEBUG, WARN, ERROR（**有序**） | 列舉 |
| 3 | **Exporter** | （抽象分類，無屬性） | 抽象 — 子類有 3 個 |
| 4 | **ConsoleExporter** | （無） | Exporter 子類 |
| 5 | **FileExporter** | `fileName: String` | Exporter 子類 |
| 6 | **CompositeExporter** | （透過關聯持有子 Exporter） | Exporter 子類 |
| 7 | **Layout** | （抽象分類，無屬性） | 抽象 — 子類有 1 個 |
| 8 | **StandardLayout** | （無） | Layout 子類 |

> **不收進 OOA 圖的東西**：Map（OOD helper，但 LogFramework 這個概念實體可以收 — 它對應「能在任意處取得 Logger」這個需求字面能力）、Message（題目把 message 描述為「不限長度的字串」— 是參數型別不是領域實體）、Developer（是 actor 不是 class，可放 use case 圖不放 class 圖）、ConfigParser / Builder（OOD helper，OOA 只放外部資料來源 JsonFile 本身）。

> **Sherry v1 已加 LogFramework 是合理選擇** — 我之前 Phase 5 規格擋掉 Registry 太嚴苛；getLogger 是需求字面要求（A 末段「使用日誌器：開發者能在程式任意處取得某一名稱的日誌器」），LogFramework 作為「持有 Logger 集合 + 提供 getLogger」的概念實體該收進 OOA 圖。

### A2. 進階題新增的概念類別（2026-05-14 加入）

| # | 名稱 | 屬性 | 備註 |
|---|---|---|---|
| 9 | **JsonFile** | `path: String`<br>`format: String`（"json"，未來可擴充 yaml/toml） | 外部配置檔 — 領域中存在的「外部資料來源」實體，不是 Parser |

### B. 關係（7 條）

| # | 關係 | 重數 | 備註 |
|---|---|---|---|
| 1 | Logger ──`parent`──▶ Logger | 0..1 | self-reference；Root 為 0、其餘為 1 |
| 2 | Logger ──`levelThreshold`──▶ Level | 0..1 | 可繼承父，故可空 |
| 3 | Logger ──`exporter`──▶ Exporter | 0..1 | 可繼承父，故可空 |
| 4 | Logger ──`layout`──▶ Layout | 0..1 | 可繼承父，故可空 |
| 5 | CompositeExporter ──`children`──◆ Exporter | 1..N（**有序**） | composition；題目寫「依序指定多個輸出器」 |
| 6 | ConsoleExporter / FileExporter / CompositeExporter ──△── Exporter | — | generalization（三角箭頭） |
| 7 | StandardLayout ──△── Layout | — | generalization |
| 8 | LogFramework ──`loggers` 1 ↔ 0..*── Logger | 1 / 0..* | LogFramework 持有 Logger 集合，提供 by-name 取得（F4） |
| 9 | LogFramework ──`source` 0..1── JsonFile | 0..1 | 進階題：可選的外部配置檔來源（F6） |

### C. Astah 落圖 checklist（純 OOA 版）

**畫 box（8 個）**：
- [ ] Logger（4 個屬性，含 0..1 的可選性）
- [ ] Level（«enumeration»: TRACE / INFO / DEBUG / WARN / ERROR）
- [ ] Exporter（抽象 — class name 用 italic）
- [ ] ConsoleExporter
- [ ] FileExporter（屬性 fileName）
- [ ] CompositeExporter
- [ ] Layout（抽象 — class name 用 italic）
- [ ] StandardLayout

**連線（7 條）**：
- [ ] Logger → Logger（self-loop, role=`parent`, 0..1）
- [ ] Logger → Level（role=`levelThreshold`, 0..1）
- [ ] Logger → Exporter（role=`exporter`, 0..1）
- [ ] Logger → Layout（role=`layout`, 0..1）
- [ ] CompositeExporter ─◆ Exporter（composition, role=`children`, 1..N, ordered）
- [ ] generalization triangle: ConsoleExporter, FileExporter, CompositeExporter → Exporter
- [ ] generalization triangle: StandardLayout → Layout

**不要畫進 OOA 圖**：
- ❌ LoggerRegistry / Map / lookup helper
- ❌ Message class（是 String 參數）
- ❌ pattern 標註（Proxy / Composite / Strategy stick notes 留 OOD）
- ❌ method signature（OOA 階段不開 method，留 OOD；如果要寫，最多寫 `log(level, msg)` 與 `export(msg)`、`format(...)` 這種需求字面行為）

---

## Phase 3 — 互動 / 操作開在誰身上

_待 Phase 2 完成_

---

## Phase 4 — Forces 盤點

_待 Phase 3 完成_

---

## Phase 5 — OOA 類別圖整合

_待前 4 步完成_
