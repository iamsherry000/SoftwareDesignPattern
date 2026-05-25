# SoftwareDesignPattern — Java 專案慣例

> 給 AI（Charles / Scout / RD）的 onboarding。寫新題前必讀。
> Last updated: 2026-05-21（Sherry 釐清標準：每個 gym 頂層 **OOAD / code / analyze** 三資料夾，code/ 包住所有 Java；Gym6_prescriberSystem 為標準範例，已重整 + 重編譯 JUnit 70/70 綠 — Scout）
> 2026-05-21 晚更新（Scout）：① 命名對齊 OOD.png（程式碼對齊圖）② code/ 內 folder 收斂原則「folder 要自己賺到存在理由」+ 入口層平放（Gym6 7→5 folder，殺 system/ + usage/）— 見「Gym 內部結構」段。

---

## 來源標示（重要）

本檔有兩種規則，**權威等級不同**：

- **🟢 Sherry 拍板** — Sherry 親口決定 / 直接 critique 的內容，hard rule。文中明確標 (Sherry YYYY-MM-DD 拍板)
- **🟡 Scout 歸納** — Scout 從 sibling 專案抽出來的「**目前看起來這樣做**」的觀察，不是 hard rule。**Scout 不能拿這層內容反過來限制 Sherry 的新判斷**

當 Sherry 提出跟 🟡 歸納衝突的設計時，**以 Sherry 為準**，並更新本檔；不要拿 🟡 內容當錘子敲回 Sherry。

---

## 專案結構

```
SoftwareDesignPattern/
├── README.md
├── SoftwareDesignPattern.iml
├── .claude/CONVENTIONS.md            ← 本檔
├── src/                              ← 唯一 Java source root（package 從這裡起算）
│   ├── ch1.../ ch2.../ ch3.../
│   └── ch4_structural/
│       ├── Gym6a_relationAnalyzer/   Gym6b_employeeDb/   Gym6c_log_framework/
│       └── Gym6_prescriberSystem/    ← 🟢 結構標準範例（Sherry 2026-05-21 拍板）
└── 我的軟設筆記/                       ← Sherry 個人學習筆記，AI 不主動寫
```

**🟢 規則（Sherry 2026-05-14 拍板）**：所有 AI 產出（文件 / Java / config / 範例）**全部放 src/ 內**，不污染 project root。

每題（gym）一個資料夾，命名 `Gym{N}{letter}_{snake_case_topic}`。

### 🟢 每個 gym = 自我內聚的「一題全包」，頂層只三個資料夾（Sherry 2026-05-21 拍板）

打開任一 gym 資料夾，頂層**只看到三個資料夾**——OOAD / code / analyze：

```
Gym{N}_{topic}/
├── OOAD/        ← 類別圖（Astah .asta + 匯出 PNG）+ ref/ 參考圖（可選）
├── code/        ← 所有 Java，內部按 OOD pattern role 分子 folder（見下節）；test/、result/ 也收這裡
└── analyze/     ← 需求原文 + OOA/OOD 推理紀錄 + 與 AI 的互動紀錄（.md）
```

判準（為什麼這樣擺）：
- **頂層三資料夾、不把 role folder 散在 gym 頂層**：打開 gym 一眼分清「圖 / 程式 / 分析」，不被一堆 package 資料夾淹沒。code 的 pattern-role 子 folder 收進 `code/` 內（見下節）。
- **`code/` 納入 package 路徑**：gym 在 src/ 底下、本身是 package；加一層 `code/` → package 變 `…Gym{N}_{topic}.code.<role>`。Sherry 2026-05-21 拍板**接受這個 `.code` 段**（換「頂層乾淨三層」的內聚）。`code` 是合法 Java 識別字（不像 `code-file` 帶連字號非法），package 與資料夾仍 1:1、IntelliJ 不報錯。
- **不另包內層 `src/`**：再包 `src/` 會讓「src」混進 package 名或被迫每題註冊獨立 source root；用語意明確的 `code/` 取代。
- analyze/、OOAD/ 裝 .md / .asta，**javac 自動忽略非 .java 資料夾**，與 code/ 並存不影響編譯。
- **標準參考範例 = `Gym6_prescriberSystem/`**（已驗證：46 檔重編譯、JUnit 70/70 綠）。

標準範例 `code/` 內部實際長相（2026-05-21 收斂後，27 個 production class / 5 folder + 入口層）：

```
code/
├── PrescriberSystem.java        ← Facade 入口（單一 class，不開 folder）
├── Main.java                    ← 單一 demo（讀 resource/ 測資、輸出寫 result/）
├── core/        domain model（Patient / Prescription / Case / Medicine / Symptom / Gender / PatientDatabase）
├── diagnosis/   責任鏈（DiseaseHandler + Covid19 / Attractive / SleepApneaSyndrome + DiagnosisRuleRegistry）
├── prescriber/  引擎（Prescriber / PrescriptionDemand / Queue / Sleeper）
├── io/          讀檔（MiniJsonParser / PatientJsonLoader / DiseaseFileLoader）
├── export/      Strategy（SaveStrategy + SaveStrategy_JSON / SaveStrategy_CSV + ExportFormat + DiagnosisRecord）
├── test/        各 package 對應 JUnit5
├── resource/    測資輸入（AllPatients.json / SupportDiseases.in / PrescribePatient.in）
└── result/      Main 匯出輸出（patientN.csv / patientN.json）
```

> 命名對齊 `OOAD/OOD.png`（Sherry 2026-05-21「程式碼對齊圖」）；資料夾收斂見下節「folder 要自己賺到存在理由」原則。

---

## Gym 內部結構 — 按 OOD pattern role 分子 folder

**🟢 規則（Sherry 2026-05-14 拍板）**：gym 內部 **不平層堆所有 .java**，要**按 OOD pattern role / responsibility 分子 folder + sub-package**，保持乾淨整潔（clean code）。

### 🟢 收斂原則：folder 要「自己賺到存在理由」（Sherry 2026-05-21 拍板，精修 5-14 規則）

不是「每種角色都開 folder」——分太細也是雜訊。一個 folder 要存在，必須是下列**能被單獨理解的邊界**之一：

1. **一個 pattern 的多角色群**（pattern 視覺邊界）—— 例：`diagnosis/`（責任鏈：抽象+各 handler+組鏈 registry）、`export/`（Strategy：介面+各實作+選擇器）。
2. **domain model**（整個系統在講的那些名詞，被各處引用）—— 例：`core/`。
3. **能單獨理解的責任群 / 基礎設施**（非 pattern，但內聚成一個可獨立推理的單元）—— 例：`prescriber/`（排隊+循序診斷引擎）、`io/`（外部檔案讀取）。

**不開 folder 的情況**：
- **單一 class 的 pattern**（最典型是 **Facade**）——它的重點就是「單一入口」，沒有多角色可框，**1-class folder 等於沒框到東西** → 放 `code/` 根當入口，不開 folder。
- **入口層 / demo**（`Main` / `Main_Advanced` / Client 端）—— 平放 `code/` 根。
- 換句話說：**`code/` 根目錄 = 入口層（Facade + demo）；子資料夾 = 內部實作**。

> ⚠️ 反例提醒：Gym6 一度開到 7 個 folder（含只裝 1 個 PrescriberSystem 的 `system/` + 只裝 Main 的 `usage/`）。2026-05-21 Sherry 質疑「folder 太多」→ 收斂成 5 folder + 入口層平放。**「分了 folder」≠「乾淨」；folder 沒賺到存在理由就是過度分包**。

### 推薦分組維度

按「class 在 OOD 中扮演的角色」分，不按字母 / 字數。常見維度：

- **核心領域物**（Logger / Framework / 領域基本實體）
- **某個 GoF Pattern 的整套體系**（每個 pattern 一個 folder — Pattern 視覺邊界）
- **進階題 / 變體**（跟主流程平行的延伸實作）
- **使用範例 / Demo**（Main / Client 端 sample code，跟 framework 本體分離）

### 範例（按 pattern role 分 — 以 Gym6c 結構示意）

> ⚠️ 下圖是**理想 layout**。磁碟上實際的 Gym6c code 目前包在 `code-file/` 子資料夾裡（package 對不上、帶連字號），**屬待對齊的偏離**（見「歷史」段）。已對齊標準的範例是 `Gym6_prescriberSystem/`。

```
Gym6c_log_framework/
├── analyze/   OOAD/   result/      (學習 + 圖 + 執行產出)
│
├── core/                            ← Logger 主體 + framework 入口
│   ├── Logger.java                    package ...Gym6c_log_framework.core
│   ├── LogFramework.java
│   ├── Level.java
│   └── Message.java
│
├── exporter/                        ← Composite Pattern 整套
│   ├── Exporter.java                  package ...Gym6c_log_framework.exporter
│   ├── ConsoleExporter.java
│   ├── FileExporter.java
│   └── CompositeExporter.java
│
├── layout/                          ← Strategy Pattern 整套
│   ├── Layout.java                    package ...Gym6c_log_framework.layout
│   └── StandardLayout.java
│
├── json/                            ← 進階題（requirement C 段）
│   ├── JsonFile.java                  package ...Gym6c_log_framework.json
│   ├── MiniJsonParser.java
│   └── Main_Json.java
│
└── usage/                           ← 應用範例（基礎題 demo）
    ├── Main.java                      package ...Gym6c_log_framework.usage
    ├── Game.java
    └── AI.java
```

### 分組調整彈性

- **class 數量少**（例如 < 6 個）的簡單 gym，平層也 OK，不強迫分
- **複雜題（≥ 10 個 class）建議按 pattern role 分 folder**（Gym6c 就是這層）
- **iteration 版本**（v1 / v2）可作為「再上一層的 folder」，子 folder 在版本內繼續分（Gym6a 已有 v1/v2 版本資料夾的 precedent）

### Package 必同步動

Java package declaration 跟資料夾結構 1:1 對應。標準（code/ wrapper）下 role folder 在 `code/` 內，package 含 `.code` 段：

```java
// 標準範例 — File: Gym6_prescriberSystem/code/diagnosis/Covid19.java
package ch4_structural.Gym6_prescriberSystem.code.diagnosis;

import ch4_structural.Gym6_prescriberSystem.code.core.Patient;  // 跨 package
```

（舊題若無 `code/` wrapper，package 就少 `.code` 段，例如 Gym6c 的 `ch4_structural.Gym6c_log_framework.exporter`。）

---

## 檔案命名

**🟡 Scout 從 sibling 歸納**：

| 角色 | 命名範例 |
|---|---|
| 抽象介面 | `Database.java` / `Logger.java` / `Exporter.java` |
| 實作 / 真實主體 | `RealDatabase.java` / `ConsoleExporter.java` |
| 模式角色 | `<Pattern><Subject>` 形式：`PasswordProtectionProxy.java` / `CompositeExporter.java` |
| 應用主程式 | `Main.java`（基礎題） / `Main_Json.java`（進階題或變體） |
| 使用情境驅動 | `Client.java` / `Game.java` / `AI.java` |
| 資料檔 | `employees.txt` / `*.json` |

命名語言：class / method / variable 用英文；註解中英混用 OK。

---

## Java 風格

**🟢 Sherry 拍板（從 code 行為觀察）**：

- **`private final` 欄位 + constructor injection**（不用 setter）
- **immutable composite**（如 CompositeExporter）— 一次傳入 children，沒有 add/remove method
- **Optional<T>** 用於可能不存在的回傳
- **var** 在 local 變數可用（Java 10+）
- **enum 方法封裝**（如 Level.isAtLeast）— 比較邏輯放 enum 自己，不切 helper class

**🟡 Scout 觀察**：

- Main 用 `framework.declareLoggers(...)` / `client.run(...)` 一行收尾
- 不用 javadoc 大段落
- 不用 Spring / DI 框架

### 不做的事

- ❌ 不用 setter（建構後 immutable）
- ❌ 不引入 Maven / Gradle 之外的框架
- ❌ 不引入未學過的 GoF pattern（要先確認 Sherry 學到哪一章）
- ❌ 不擅自寫 `我的軟設筆記/`（Sherry 個人筆記）

---

## Sherry 的學習慣例

- **OOA worksheet**：`<gym>/analyze/ooa-worksheet.md`（Scout 主導推 OOA）
- **OOAD class diagram**：`<gym>/OOAD/`（Astah `.asta` + 匯出 PNG）
- **題目原文**：`<gym>/analyze/requirement.md`
- **想法 / 推理紀錄**：`<gym>/analyze/` 內依主題分檔（review / design / mapping）
- **執行結果**：`<gym>/result/`（log 檔等）
- **think/**：Sherry 思考過程草稿（可選）

### analyze/ 內 design 檔的寫作風格

**🟢 Sherry 拍板（2026-05-14）**：design 檔（review / OOD spec / pattern mapping 等）必須用 **narrative 推理風格**，不用 spec / table / checklist 結構：

1. 從題目情境切入（引 requirement 原句）
2. show 思考路徑 — 第一直覺 / 質疑 / 排除替代 / 收斂
3. 每個結論至少一個有憑有據的 cross-ref（requirement 第 N 段 / 筆記 canonical 原文 / 圖某處）
4. 主軸永遠是當前題目，GoF 通論是工具不是主角
5. 表格只在「同樣資訊用段落超過 5 行」時用，當收斂 summary

詳見 `team/scout/logs/self-corrections/2026-05-14_analyze-spec-not-narrative.md`。

---

## AI 工作流（Scout / RD / Charles）

1. **進新題前**：讀本檔 + 該題 `analyze/requirement.md` + `analyze/ooa-worksheet.md`（如果有）
2. **OOA 階段**：Scout 推 worksheet，不寫 code
3. **OOD / 實作階段**：RD 寫 .java，按本檔慣例**先分 folder 再寫**
4. **每題實作完成後**：Sherry 自己跑 + verify，AI 不主動跑 javac

### 寫 OOD spec 前必做

**🟢 Sherry 拍板（2026-05-14）**：寫任何 OOD design 檔之前，**先看 Sherry 已存在的 .java code**。如果 code 已實作，design 檔要對齊 code 的實際設計選擇，不要憑 OOA worksheet 自己想規格。

---

## 主程式範例

```java
package ch4_structural.Gym6X_xxx.usage;   // 注意 sub-package

public class Main {
    public static void main(String[] args) {
        // 1. 建構 framework
        Framework framework = new Framework();

        // 2. 配置（pattern 應用）
        var component = new SomeComposite(
                new SomeLeaf(),
                new SomeLeaf()
        );

        // 3. 給 Client 使用
        Client client = new Client(framework);
        client.run();
    }
}
```

---

## 歷史 / 既有題目對齊狀態（2026-05-21 盤點）

各題實際結構彼此不一致：

- `Gym6_prescriberSystem`：✅ **對齊標準**（頂層 OOAD/ + code/ + analyze/ 三資料夾，code/ wrapper、package 含 .code、JUnit 70/70 綠）— 標準參考範例。
- `Gym6c_log_framework`：code 包在 `code-file/` wrapper（連字號非法、package 對不上），role 分組對但 wrapper 名錯，**偏離標準**。
- `Gym6b_employeeDb`：code 平層直放 gym（沒按 pattern role 分、無 code/），有 requirement/、無 analyze/。
- `Gym6a_relationAnalyzer`：用 v1/v2 版本資料夾，無 analyze/、無 code/。

**新題一律照標準**（頂層 OOAD / code / analyze 三資料夾）。既有 6a/6b/6c 是否 retro-fit 到三資料夾標準 = **待 Sherry 決定**（早先「forward-only」是在舊的誤解標準下講的，需重新確認）。

---

## 推翻紀錄

- 2026-05-14：原 CONVENTIONS 寫「平層結構 — 不開子 package」，是 Scout 從 6a / 6b 歸納的觀察（🟡），不是 Sherry 拍板。Sherry 2026-05-14 直接 raise「應該按 pattern role 分 folder」，新慣例覆蓋舊歸納。Scout 寫 self-correction codify「歸納不能反過來限制 Sherry 新判斷」rule。
- 2026-05-21：原本（同日早晨）Scout 把 Sherry 的結構指示 codify 成「role folder 直接散在 gym 頂層、**不用 code/ wrapper**」。當天稍晚 Sherry 釐清：她要的標準是「頂層 **OOAD / code / analyze** 三資料夾，code/ 包住所有 Java」（即現在的 Gym6）。根因同 `team/scout/logs/self-corrections/2026-05-21_structural-directive-means-restructure.md`（結構性指示誤讀成流程而非目標結構）。已把 Gym6 重整為三資料夾 + 重寫本檔結構段。
