# Forces 怎麼對應 Pattern — 在這題的推理

> 從 requirement 出發，逐條問「這條力是什麼 pattern 在解？」並走完替代選項的排除。
>
> 路徑：先從題目情境切，再質疑第一直覺，再對照 `patterns/` 筆記 canonical force 原文，最後排除替代選項。
>
> Last updated: 2026-05-14

---

## 1. 題目情境先 establish

requirement.md 第 2 行直接寫「**複合模式——日誌框架**」—— 主考 GoF pattern 在標題就拍板了。

requirement A 段把概念定下來：開發者定義多個 **Logger**（日誌器），每個 Logger 可以配置 **Exporter**（輸出器）、**Layout**（訊息佈置）、**Level threshold**（分級門檻）、**parent**（父日誌器）。Exporter 有三種具現：ConsoleExporter / FileExporter / CompositeExporter。設計需求 B/C 段明寫 OCP：「在擴充 Exporter 種類時無需修改既有程式碼」+「在擴充 Layout 種類時無需修改既有程式碼」。

最後應用範例的 Main.entry() 直接 demo：

```java
exporter = new CompositeExporter(
    new ConsoleExporter(),
    new CompositeExporter(
        new FileExporter("game.log"),
        new FileExporter("game.backup.log")
    ),
)
```

這個範例提供兩個關鍵情報：(a) CompositeExporter 可以**巢狀**包另一個 CompositeExporter；(b) Logger 的 exporter 欄位**只承諾單一指派**（賦值給單一變數），但這個單一指派可以展開成一棵任意深的樹。

worksheet Phase 4 從以上情境抽出 6 條 force（F1 主考、F2 設定繼承、F3 種類擴充、F4 by-name 取得、F5 分級門檻、F6 進階題 JSON 配置）。下面要回答的問題是：**每條 force 該對應到 `patterns/` 筆記裡哪一個 pattern 的哪一條 canonical force？**

---

## 2. 第一直覺的陷阱 — 標題說「複合模式」≠ 所有 force 都歸 Composite

最容易犯的錯：看到題目標題寫複合模式，就把 worksheet 6 條 force **全部**對應到 Composite Pattern。

這樣會撞到 F3「Layout 種類擴充 OCP」這條 —— Layout 沒有「樹狀」需求（沒有 CompositeLayout 包多個 Layout），只有「種類替換」需求。把 Layout 歸到 Composite 解釋不通。

問題在哪？Composite Pattern 主要解的是「**對外看像單一、對內展開成樹**」這種結構問題；它附帶解了「擴充新的 leaf 種類」的 OCP，但**那不是它的主任務**。如果一條 force 只關心「種類擴充」、不關心「樹狀結構」，那條 force 是別的 pattern 在解的。

所以推理要先把 6 條 force 分兩類來看：
- **結構類 force**（牽涉到「組成什麼形狀」）→ 看 Composite / Adapter / Facade 等結構型 pattern
- **行為類 force**（牽涉到「替換什麼行為」）→ 看 Strategy / Template Method 等行為型 pattern

下面逐條拆。

---

## 3. F1 主考力 — 對應 Composite Pattern

worksheet F1 的力描述（節錄）：

> 輸出目的地的數量在不同 Logger 配置下會 0..N 變動，但 Logger 對外只承諾持有單一 exporter 欄位的約束下，需要一個對外看像單一 exporter、對內可承載任意數量子 exporter 的中介物。

這條 force 的核心是「**單一 vs N 個的張力**」 —— Logger 程式碼想把所有 exporter 當「一個」處理（`exporter.export(msg)` 一行帶過），但實際輸出目的地可能是 N 個。

對照 `patterns/compositePattern.md` 的 canonical force 原文：

> **F1-Self Association 遞迴關聯**：存在遞迴關聯導致高結構複雜度 (High Structural Complexity)

筆記這條的關鍵詞是「**遞迴關聯**」。在這題的具現：CompositeExporter ─◆── Exporter (1..N) 這條線 —— 從 CompositeExporter 連到 Exporter 抽象父類，因為 children 裡可能放另一個 CompositeExporter，所以 runtime 是樹狀的遞迴。requirement Main 範例的巢狀 `new CompositeExporter(..., new CompositeExporter(...))` 就是這個遞迴的直接證據。

→ **題目 F1 ↔ Composite F1-Self Association** 對應成立。

筆記的另兩條 canonical force 也同時被觸發：

> **F3-擴充性**：希望在擴充新的種類至模型忠實，能有最少的擴充成本 = 良好擴充性 + OCP，而不必修改 CLI 和 Directory 程式

這條對應 requirement B 段「在擴充 Exporter 種類時無需修改既有程式碼」。具現為：未來新增 NetworkExporter / DatabaseExporter 等 leaf 種類，Logger 程式碼不動。

> **Tradeoff F4-操作安全性 / F5-透明度**

這條是 Composite Pattern 的經典 trade-off — `add(child: Exporter)` 該放在 Exporter 介面（透明）還是只放 CompositeExporter（安全）。`ood-design.md` §6 的推導已展開：**這題推 Safe**，因為 add 只發生在配置階段（Main.entry() 建構時），不在 runtime hot path；配置階段 client 本來就知道在組哪個結構，型別誠實壓過一視同仁。

---

## 4. F3 力的雙重身分 — Layout 純 Strategy，Exporter 是 Composite + Strategy 雙標

worksheet F3 描述：

> 開發者未來會新增 Exporter 種類（如 NetworkExporter）和 Layout 種類（如 JSONLayout），但不希望改動 Logger 既有程式碼。

注意這條 force 兩個主詞分開看 — Exporter 跟 Layout 是**不同的承載**。

### 4.1 Layout 純 Strategy

Layout 體系只有 StandardLayout 一個子類，沒有「CompositeLayout 包多個 Layout」這種需求。換句話說 Layout **沒有樹狀**，只有「不同 layout 對應不同 format 方式 + 未來會擴充更多 layout」。

對照 Sherry 2026-05-14 訊息舉的 Strategy canonical force：

> F1-BV: 多道行為
> F2-OCP: 擴充新 Strategy 不必修改現有 Match code

在這題的具現：
- **F1-BV 多道行為**：未來 StandardLayout / JsonLayout / XmlLayout 各自有不同 format 規則。requirement「目前僅支援以下一種排版」這句話留了擴充伏筆。
- **F2-OCP**：requirement B 段「在擴充 Layout 種類時無需修改既有程式碼」 — 直接寫明 OCP 要求。

→ **題目 F3-Layout ↔ Strategy F1-BV + F2-OCP** 對應成立。

### 4.2 Exporter 同時是 Composite Component 跟 Strategy Strategy

Exporter 體系比較有趣 —— 它**同時承載**兩種需求：
- 樹狀組合（F1 主考的核心結構）→ Composite Component 角色
- 種類擴充 + runtime 替換（F3 的 Exporter 部分）→ Strategy Strategy 角色

從 client 端看，Logger.exporter 是 runtime 可替換的「行為」（Strategy 視角）—— 開發者可以 `logger.exporter = new NetworkExporter()`。

從結構端看，Exporter 介面之下有 leaf（Console/File）跟 composite（CompositeExporter）兩種具現，後者可以遞迴包前者形成樹（Composite 視角）。

這兩個視角**不矛盾** —— 「型別 axis」（Strategy 的多態替換）跟「組合 axis」（Composite 的樹狀組合）是兩個獨立軸向。對照 `patterns/decorator-vs-proxy.md` 提過的 type axis vs composition axis 區分，**Exporter 在這題就是這兩個 axis 同時用**。

→ OOD 圖上 Exporter box 旁邊**雙標** stereotype：`«Composite» Component` + `«Strategy» Strategy`。

### 4.3 Composite F2-Structural Variation 在哪？

`patterns/compositePattern.md` 還有一條 canonical force：

> **F2-Structural Variation 結構變動性**：從 結構 = 類別 + 關係，去思考定義結構變動性 ⇒ 新增/修改"類別" or 關係改變

這條跟 Strategy 的 F2-OCP **是同一條題目 force 的兩個側面**：
- 從 Composite 角度看 = F2-Structural Variation（結構新長出 NetworkExporter 這種子類）
- 從 Strategy 角度看 = F2-OCP（client 不改）

不需要選一個，**兩個角度都在這題裡同時發生**，這正是「Exporter 同時是 Composite 跟 Strategy」的具體原因。

---

## 5. F2 / F4 / F5 為什麼不對應任何 GoF？— 排除替代選項

這三條 force 容易被誤判到 GoF pattern，但實際上它們**該用 OO 基本功承接**。逐條走過排除推理：

### 5.1 F2 設定繼承 — 為什麼不是 Decorator？

worksheet F2 描述：子 Logger 多數沿用父設定、少數覆寫；client 拿到子 Logger 後不關心設定是子自有還是繼承自父。

第一直覺可能想：「向 parent 委派」聽起來像 Decorator —— 「自己不（完全）做事 → delegate 給內部同型別物件 → 對結果加工」。

但檢驗 `patterns/decorator-vs-proxy.md` 的 Decorator 三個結構訊號：
1. 同 interface（Logger implements Logger 自己？— 不適用，Logger 不是 interface）
2. 持有同型別物件當 field（Logger.parent: Logger ✓ 結構像）
3. **內部 delegate 給該物件 + 對結果加工 / 加功能**（❌ 不適用 —— 子 Logger 沒「加工 / 加功能」，是「**自己沒設就向上問**」）

子 Logger 不是 wrapping parent 然後 enhance 行為，它是 **fallback lookup** —— 自己 exporter 是 null 時才向 parent 問，問到就直接用 parent 的 exporter，不是包裝 parent 的 exporter。

這個結構是 **OO 基本的 self-association + null-fallback resolution**，不是 GoF pattern。

→ F2 ↔ 無 GoF，圖上不掛 stereotype。

### 5.2 F4 by-name 取得 Logger — 為什麼不是 Singleton？

worksheet F4 描述：Logger 在 Main 中定義配置，但會在 Game / AI 等任意檔案中以名稱取回同一個 Logger。

第一直覺：「全域取得」聽起來像 Singleton。

但 Singleton 的 canonical 意圖是「**確保某 class 只有一個 instance**」 —— 在這題明顯不適用，Logger 有 N 個 instance（Root / app.game / app.game.ai），不是只有一個。

實際的力是「**by name 取回已建好的某個 instance**」 —— 這是 Registry / Lookup table 模式，但 Sherry 還沒學 Registry pattern（也不在 GoF 23 個經典裡）。

具現方式：LogFramework 持有 `Map<String, Logger>` + `getLogger(name): Logger`。這是 OO 基本的「集合 + key 查詢」。

→ F4 ↔ 無 GoF，圖上不掛 stereotype。

### 5.3 F5 分級門檻過濾 — 為什麼不是 Strategy？

worksheet F5 描述：Logger 根據自身 level threshold 決定要不要受理請求。

第一直覺：「不同 threshold 不同行為」聽起來像 Strategy。

但 Strategy F1-BV 的關鍵是「**多道行為 — 不同 ConcreteStrategy 各自實作不同 algorithm**」。F5 不是這個結構 —— 過濾邏輯只有一條（`level >= threshold`），threshold 是**參數值**不是 algorithm。要替換的是「值」不是「算法」。

具現方式：Logger 內部 `if (resolveLevelThreshold().isPass(level) == false) return;` 一行 if 判斷。如果未來真的出現「不同 threshold 比較邏輯」（例如 RangeThreshold 要求 level 在某區間），那時候才升級到 Strategy。

→ F5 ↔ 無 GoF，圖上不掛 stereotype。

---

## 6. F6 進階題 — Sherry code 走 switch + 直接 new（不用 Builder + Chain）

worksheet F6 是 requirement C 段的進階挑戰：用 JSON 檔取代 in-code `new Logger(...)`。

### 教練參考圖的路線（沒採用）

`OOAD/參考839_JsonParse.png` 教練圖走兩個 pattern：
- **Builder**：ConsoleExporterBuilder / FileExporterBuilder / CompositeExporterBuilder 各自負責解析自己 type 的 JSON
- **Chain of Responsibility**：canParse → doParse 鏈式判斷，下一個 builder 接手

### Sherry code 實際路線 — switch + 直接 new

`core/LogFramework.java` 內建 parser，沒拉出 Builder / Chain 體系：

```java
private Exporter parseExporter(Object obj) {
    String type = (String) spec.get("type");
    return switch (type) {
        case "console" -> new ConsoleExporter();
        case "file" -> new FileExporter((String) spec.get("fileName"));
        case "composite" -> {
            // recursive parseExporter children
            ...
            yield new CompositeExporter(children);
        }
    };
}
```

switch 17 行就解決 + recursive `parseExporter` 處理巢狀 composite。`json/` folder 只有 3 個 class（JsonFile / MiniJsonParser / Main_Json），整個進階題沒引入 Builder 體系。

### 為什麼簡單路線是好選擇

Builder + Chain 的價值是「**新加 Exporter 種類時不改既有程式碼**」（嚴格 OCP）—— 寫 `NetworkExporterBuilder` 加到 chain 後面即可。代價：3 個 class 起跳，理解門檻較高。

switch 路線的代價是「新加 Exporter 種類要改 parseExporter 的 switch」—— 違反嚴格 OCP。但這題 scope 內 Exporter 種類只有 3 種（console / file / composite），requirement 沒有「未來會頻繁加 Exporter 種類」的明示訊號 —— **OCP 嚴格度跟複雜度交換時，簡潔贏**。

Builder + Chain 是「未來確定會頻繁擴充 + 第三方 plugin 機制」才該引入的成本，這題沒到那層。

→ **F6 ↔ 無 GoF**（不是 Builder + Chain，是 OO 基本 switch + 直接 new + 遞迴）。詳見 `ood-design.md` §7「F6 進階題怎麼落地」段。

---

## 7. 收斂 — 三類對應總圖

把上面的推理收成一張對應結論（**這張表是收斂用，不取代論述**）：

| Gym6c worksheet 力 | 對應 GoF Pattern | 在 pattern 內是哪幾條 canonical force | 圖上 stereotype |
|---|---|---|---|
| F1 ★主考 多輸出目的地 vs 單一持有欄位 | Composite | F1-Self Association + F3-擴充性 + Tradeoff F4/F5 | Exporter «Component» / CompositeExporter «Composite» / Console+File «Leaf» |
| F3-Layout 種類擴充 | Strategy | F1-BV + F2-OCP | Layout «Strategy» / StandardLayout «ConcreteStrategy» |
| F3-Exporter 種類擴充 | Composite F2-Structural Variation **同時也是** Strategy F2-OCP（雙重身分） | — | Exporter 額外加 «Strategy» / CompositeExporter+Console+File 額外加 «ConcreteStrategy» |
| F2 設定繼承 | 無 GoF（OO 基本：self-association + null-fallback） | — | 不掛 |
| F4 by-name 取得 Logger | 無 GoF（OO 基本：Map + getLogger） | — | 不掛 |
| F5 分級門檻過濾 | 無 GoF（OO 基本：if 判斷） | — | 不掛 |
| F6 進階題 JSON 配置 | 無 GoF（OO 基本：LogFramework switch + recursive new） | — | 不掛 |

→ 圖上**只在 Exporter 體系 + Layout 體系掛 stereotype**，Logger / Level / LogFramework / JsonFile 不掛。

---

## 8. 下一步 — 怎麼把這份對應落到 OOD 圖

1. **先確認 Q1 結構補完** — CompositeExporter ─◆── Exporter (1..N, ordered) 那條 children 線必須畫上去，否則 F1 主考的結構承載點空缺，所有 Composite stereotype 都不成立
2. **再在 Exporter 體系雙標 stereotype**：Exporter「«Strategy» + «Composite» Component」；CompositeExporter「«Composite» + «ConcreteStrategy»」；Console/File「«Leaf» + «ConcreteStrategy»」
3. **Layout 體系單標**：Layout「«Strategy»」；StandardLayout「«ConcreteStrategy»」
4. **F2 / F4 / F5 對應結構保留但不加 GoF 標註** — 主圖乾淨，OO 基本功不需要 stereotype

---

## 參考來源

- `analyze/requirement.md` — 標題第 2 行（複合模式）+ A 段（Logger / Exporter / Layout / Level）+ B 段（OCP 兩處）+ Main entry() 應用範例
- `analyze/ooa-worksheet.md` Phase 4 — 6 條 worksheet force 原文
- `analyze/ood-design.md` — class 規格 + collaboration + §6 透明 vs 安全 trade-off 推導
- `analyze/ooa-v1-review.md` Q1 — CompositeExporter → Exporter children 線必補
- `我的軟設筆記/patterns/compositePattern.md` — F1-Self Association / F2-Structural Variation / F3-擴充性 / Tradeoff F4-F5
- `我的軟設筆記/patterns/decorator-vs-proxy.md` — type axis vs composition axis（用來解釋 Exporter 為何能雙重身分）
- Sherry 2026-05-14 訊息 — 「F1-BV: 多道行為 / F2-OCP: 擴充新 Strategy 不必修改現有 Match code」（Strategy canonical 命名）
- `OOAD/參考378_forces.png` / `OOAD/參考839_JsonParse.png` — 教練 forces 圖示
