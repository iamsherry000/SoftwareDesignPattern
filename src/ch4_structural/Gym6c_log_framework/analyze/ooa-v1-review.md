# 道館 6c 日誌框架 — OOA v1 圖 Review 討論紀錄

> Scout 對 Sherry OOA_v1.png 的逐題討論 + Sherry 答覆 + 結論。
>
> 對應圖：`OOAD/OOA_v1.png` / 參考：`OOAD/參考378_forces.png`
>
> Last updated: 2026-05-14

---

## 議題索引

| # | 議題 | 狀態 |
|---|---|---|
| Q1 | 遞迴關聯放在 Logger 身上還是 CompositeExporter 身上？ | 🟢 討論中 |
| Q2 | 「結構變動性」標在 Logger 上錯了嗎？要拉到 CompositeExporter 嗎？ | 🟢 討論中 |

---

## Q1 — 遞迴關聯放在哪？

### Sherry 的提問

「所以遞迴關聯不在 Logger 身上而是 CompositeExporter 嗎？即使 CompositeExporter 是遞迴到父類別 Exporter？」

### 背景脈絡

Sherry v1 圖把 Logger ─◇ Exporter 標 1..\*，意圖表達「樹狀結構」。Scout 指出主考 force F1 的結構承載點在 CompositeExporter，不在 Logger。

### Scout 答覆

#### 兩種「遞迴」是不同性質，UML 上是兩條不同的線

**(a) Logger 身上的遞迴 = 真 self-association**

- `Logger.parent: Logger [0..1]`
- 同一個 class 連回自己，UML 上 Logger box 上開一條線繞回自己
- 對應 force：**F2 設定繼承**（Sherry v1 圖已畫對 ✓）

**(b) CompositeExporter 身上的遞迴 = 對抽象父類的 composition**

- `CompositeExporter.children: Exporter [1..N]`
- 從 CompositeExporter 連到 **Exporter 抽象**（不是連回 CompositeExporter 自己）
- UML 上是 CompositeExporter ─◆─▶ Exporter 這條線
- 對應 force：**F1 主考 Composite Pattern**

#### 為什麼 CompositeExporter 連的是父抽象 Exporter？

這正是 Composite Pattern 的精髓 —— **連到父抽象，runtime 才形成樹**。

CompositeExporter.children 裡面可能是：
- ConsoleExporter（leaf）
- FileExporter（leaf）
- 另一個 CompositeExporter（composite — 又包一層）

連到抽象父類 = client 一視同仁。**Logger 不用知道 children 是 leaf 還是 composite**，全當 Exporter 處理。runtime 因為多型自然形成樹，但 UML **靜態圖上不是 self-loop**。

#### 那為什麼 1..\* 不能放 Logger ─◇ Exporter？

requirement Main 範例：

```
exporter = new CompositeExporter(
    new ConsoleExporter(),
    new CompositeExporter(
        new FileExporter("game.log"),
        new FileExporter("game.backup.log")
    ),
)
```

Logger 只持有 **1 個** exporter 欄位（賦值給單一變數），這個 exporter 可能：
- 直接是 ConsoleExporter（1 個）
- 或是 CompositeExporter 包 3 個 leaf

Logger 不知道也不關心 —— 一律呼叫 `exporter.export(msg)`。如果把 1..\* 放 Logger，意思變成「Logger 持有多個 exporter」，那 CompositeExporter 整個就沒必要存在了 —— 直接讓 Logger 持有 List<Exporter> 就好。

**Composite Pattern 設計的價值就是：把「多個 vs 單一」的差異藏在 CompositeExporter 內部，讓 Logger 永遠只面對「1 個 Exporter」這個一致抽象**。

### 結論

UML 三條相關線：

| 線 | 從→到 | 重數 | 性質 | 對應 force |
|---|---|---|---|---|
| Logger self-loop | Logger → Logger (parent) | 0..1 | self-association | F2 設定繼承 |
| Logger → Exporter | Logger → Exporter | **0..1** | association（單一持有） | F2 繼承可空 |
| CompositeExporter children | CompositeExporter → Exporter | **1..N** ordered | composition（◆） | **F1 主考** |

第二條跟第三條的 1..\* / 0..1 對調，是 v1 圖最關鍵的修正點。

### Sherry 確認

**v2 圖實作狀態（2026-05-14）**：
- ✓ Logger → Exporter 重數已從 1..\* 改為 0..1
- ❌ CompositeExporter → Exporter 的 children 線**還沒畫**
- ⚠️ Exporter box 上方留下孤立的 `1..*` 標籤 — v1 的 leftover，應拿掉
- **結論**：Q1 結構主考力 F1 尚未完整答出，待補一條 CompositeExporter ─◆─▶ Exporter (1..N, ordered, role=children) 實線

---

## Q2 — 「結構變動性」標在 Logger 上錯了嗎？要拉到 CompositeExporter 嗎？

### Sherry 的提問

「OK 遞迴主要是產生出樹狀結構，那麼以此類推，我的結構變動套在 logger 上應該錯誤，也應該拉到 CompositeExporter 嗎？」

### 背景脈絡

Sherry v1 圖在 Logger 上標紅字：「Structural Variation 結構變動性 — 每次建構組合不同（新增改 Message）」。她推測這個 force 該拉到 CompositeExporter 上。

### Scout 答覆

**直覺方向部分對 —— 力的位置不在 Logger，但不是拉到 CompositeExporter，是拉到 Exporter / Layout 抽象父類**。

關鍵：v1 圖的「結構變動性」紅字其實混了**三個不同的力**，要拆分到三個位置。

#### 三個容易混淆的力

**力 A — 建構變動性（Construction Variation）**

- 內容：「同一個 Logger 物件，多個屬性，每次建構時組合會不同」
- 位置：**Logger** ✓
- 依據：Logger 構造時可選填 levelThreshold / exporter / layout，或留 null 繼承父。Root 全填、Child 可能只填 1-2 個
- 對應參考圖 1 標註：「Force-建構變動性 — 物件有多個屬性，但每次建構時，組合會不同」

**力 B — 結構變動性 / 種類擴充（Structural Variation）**

- 內容：「Exporter / Layout 的種類未來會新增或變動」
- 位置：**Exporter 旁、Layout 旁**（抽象父類）
- 依據：今天有 Console/File/Composite，未來可能加 Network/Database。requirement B 段明寫 OCP
- 對應參考圖 839 標註：「Force-Structural Variation: Exporter 結構有可能會變動或者新增、刪除」

**力 C — 高結構複雜度（High Structural Complexity）**

- 內容：「樹狀結構造成的高複雜度」
- 位置：**CompositeExporter** ✓
- 依據：CompositeExporter ─◆─▶ Exporter 造成樹（深度任意、leaf 數任意）
- 對應參考圖 839 標註：「Force-High Structural Complexity: 存在遞迴關聯並導致樹狀結構而形成高結構複雜度」

#### v1 混淆診斷

| v1 標註內容 | 真正屬於哪個力 | 該放哪 |
|---|---|---|
| 「每次建構組合不同」 | 力 A 建構變動性 | Logger ✓ 保留 |
| 「新增改 Message」（用詞模糊，推測指種類擴充） | 力 B 結構變動性 | Exporter / Layout 旁 |
| 「Structural Variation」這個英文標題 | 用詞像力 B，內容卻寫力 A — 用詞跟內容對不上 | 拆開 |

v1 圖把 A + B 全壓在 Logger 一個 note，且力 C 完全沒體現（因為 Q1 主考結構漏掉了，修正後才會出現）。

### 結論

三個力的位置：

| 力 | 位置 | 內容摘要 |
|---|---|---|
| A 建構變動性 | Logger | 四欄位每次組合不同（含可繼承） |
| B 結構變動性（種類擴充） | Exporter 旁、Layout 旁 | OCP — 未來種類會新增 |
| C 高結構複雜度 | CompositeExporter | 樹狀結構造成的複雜度 |

不是「拉到 CompositeExporter」就解決 —— 是 v1 一個 note 要拆成三個 note 放三個位置。

### Sherry 確認

**v2 圖實作狀態（2026-05-14）**：
- ✓ 力 B「結構變動性 / OCP」已拉到 Exporter 旁 +「在擴充 [2d-exporter] 輸出器種類時，無需修改既有程式碼，遵守 OCP」
- ✓ 力 B「在擴充 [2e-layout] 訊息佈置的種類時...OCP」拉到 Layout 旁
- ✓ 力 C「Self Association 遞迴關聯 + High Structural Complexity」拉到 CompositeExporter 區
- ⚠️ 力 A「建構變動性」v1 在 Logger 上，v2 紅字消失了 — 該補回 Logger 旁
- **結論**：Q2 主結論成立，三個力位置都拉對，唯力 A 補回 Logger 即可

---

## Q3 — Logger ─◇ Message (0..*) composition 該改 «create» dependency 嗎？

**狀態：🟡 撤回（Scout 看錯）**

Scout 誤判 Sherry v2 圖上 Logger → Message 是 composition aggregation（菱形 ◇），實際 Sherry 本來就是 dashed arrow（dependency）。Sherry 2026-05-14 14:xx 糾正：「這個你應該看錯 我本來就是->」。

### 結論

Sherry v2 圖 Logger ─ ─ ► Message 已是 dependency 連法 ✓，不需改動。Q3 議題撤回。

### Scout 自我紀錄

- 觀圖時把 dashed line 跟實心菱形混淆，沒仔細看清線型
- 之後讀圖前應該先 zoom in 看線型細節再下結論
- 對齊 2026-05-14 self-correction `proxy-misjudge-no-source-verify` 的「先 verify 再答」規則 — 這次應該先 verify 線型再下 review

---

## Q4 — 跳階段：OOD 設計（套完 Strategy + Composite）

### Sherry 的需求

「現在應該告訴我套完 strategy 跟 composite 應該是怎麼樣」

### Scout 產出

獨立檔 `analyze/ood-design.md`，內含：

- §1 Forces → Forms 對應表（F1 → Composite ★主考 / F3 → Strategy ×2 / 其餘無 GoF）
- §2 Composite Pattern on Exporter（三角色 + class 規格 + 樹遞迴）
- §3 Strategy Pattern on Exporter + Layout（Logger 是 Context，持有兩個 Strategy reference）
- §4 writeMessage collaboration（Composite + Strategy 接縫）+ resolveExporter null-fallback
- §5 OOD vs OOA 圖差異 — 落圖 checklist（stereotype / realization / method signatures / GoF 標註）
- §6 透明 vs 安全 trade-off（推薦 Safe — add 只開在 CompositeExporter）
- §7 完整 OOD class diagram 落圖順序建議
- §8 不入 OOD 圖的東西

### Sherry 確認

_待 Sherry 看 ood-design.md 後確認 / 開始落圖_
