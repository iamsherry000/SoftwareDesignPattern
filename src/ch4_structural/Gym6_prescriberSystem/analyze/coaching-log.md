# Gym6 處方診斷系統 — 教練式複盤：我不會的 / 搞懂的

> 用途：Scout 用 Socratic 方式帶 Sherry 走 Gym6 設計+code 的過程紀錄。
> 寫法：每個概念分「我原本怎麼想（卡點/陷阱）」→「為什麼那樣會出事」→「最簡判準（一句話記住）」→「怎麼自己重建」。
> 來源標示：引 requirement / ood-design.md / 題庫 時標出處，避免「Scout 憑空講」。
> Started: 2026-05-21（Scout 帶複盤）

---

## 概念 1 — 子類別 vs 物件（「點的萃取」上游判斷）

### 我原本怎麼想（5/20 圖的卡點）
把三道處方（清冠一號/青春抑制劑/打呼抑制劑）畫成 `Prescription` 的三個**子類別**，病、藥同樣各開子類別。
理由：「需求有寫出這三個，我就把它記錄成類別」+ 直覺「處方可以是任何人的、可重複用」。

### 為什麼那樣會出事
- 「需求列出三個 → 我開三個型別」= 題庫說的 **「把資料當型別」** 陷阱。需求列舉的是**三筆資料範例**，不是三個**型別**。
- 後果很具體：哪天加第四種病，子類別做法要**新增 class + 改 Prescriber 認新型別**（踩到「規則會持續擴充、不該改核心」的 force）。
- 「處方可以是任何人的、可共用」這個直覺其實**是對的**——但它支持的是「物件」（一個可共享的值），我卻拿它去推出「子類別」，方向接錯。

### 最簡判準（記這句就好）
> **子類別要成立，必須「結構不同」或「行為不同」（多/少欄位、或方法做的事不一樣）。若欄位、行為都一樣，只差欄位裡裝的「值」→ 是同一個模子印出來的不同物件，不是不同模子。**

套用：`Prescription` 四欄位 `name / potentialDisease / medicines / usage`，三道處方欄位全同、只有值不同 → **物件**。
等同「黑桃3、紅心K 是 Card 的兩個物件，不是兩個子類別」（gym1 點的萃取）。

### 收斂結論
Prescription / Medicine 都是 immutable 值物件，**不開子類別**；potentialDisease 是 `String` 欄位（不升 Disease 類別/enum）。三道具體處方是**物件**，活在組裝層（Main / setup），不在類別圖長型別。
`[source: ood-design.md §2；requirement 處方四欄位；題庫 gym1_OOAD/點的萃取_類別物件屬性判斷.md]`

### 反向套用時踩到的坑（重要：判準的第二半）
Sherry 反向想時一度又倒回「應該是子類別」，理由「都屬於 Prescription、藥物各自實作」。再追問下自己舉了 `notify()` 例：病歷重→緩慢通知 / 輕→搞笑 / 沒事→打電話，問這算不算 override。

這個例子揭露判準的**第二個條件**（光「有行為不同」不夠）：
- `notify()` 的行為是跟著**病歷輕重**變，不是跟著**哪道處方**變。
- 後果：同一道「清冠一號」開給重症要緩慢、開給輕症要搞笑 → 清冠一號自己的通知行為**不固定**。
- 子類別只能寫一個 `notify()`，裝不下「依病歷輕重而變」的需求 → **變化的軸（輕重）≠ 想切的軸（處方身分）** → 不能當子類別。
- 加碼：notify 是「通知用戶結果」= 通知者的職責，根本不是處方（一張紙）的職責（cross-ref 操作開在誰身上）。

### 最簡判準（完整兩條件版，記這個）
> **(1) 要有操作上的不同（方法內容不同，不是欄位值不同）；(2) 這個不同必須跟著「你想切的那個軸」走——想把處方切子類別，行為就得是「因為它是清冠一號，所以永遠這樣做」，跟誰拿、什麼情況無關。** 缺一不可。

### 收斂結論（已驗證）
🟢 回 requirement 確認：`Prescription` 零行為，只有四欄位 → 三道處方是**物件**定案。Sherry 這次是「找不到該被 override 的方法」自己推出來的，不是接受權威。
`[source: ood-design.md §2；requirement 處方四欄位；題庫 點的萃取 + 操作開在誰身上]`

---

## 概念 2 — 責任鏈 vs Strategy；異質條件；多重命中

### Sherry 的答（高強度問爆模式，三題直球）
- 應用對應對：責任鏈→診斷規則（DiseaseHandler）、Strategy→匯出 CSV/JSON。✅
- 異質條件：「父類別架構、子類別各自實作各自職責」。✅
- 多重命中：「責任鏈有順序，命中就解掉，不會有多重命中問題？」（用問號，不確定）。

### 卡點 / 要磨利的
1. **intent 講鈍了**：只講到「有 order、每個去查閱」，沒講到責任鏈的魂。
2. **用詞錯**：說「責任鏈用在 Prescription」——錯，Prescription 是死資料，責任鏈是套在**規則**（DiseaseHandler）上。
3. 多命中答對但**沒把握**（問號）。

### 最簡判準（記這些）
> **責任鏈 vs Strategy 的分界刀 = 「誰決定用哪一個？」**
> Strategy = 呼叫端**事先挑定一個**（選 CSV 就 CSV，清楚知道選了誰）。
> 責任鏈 = **沒人事先挑**，丟進鏈裡，每個 handler 自己舉手「要不要接」，第一個接的回，呼叫端**不知道是誰處理的**。
> `[source: 題庫 雜項/Strategy_vs_Chain_Force差異.md；ood-design.md §3]`

> **多重命中**：鏈是「第一個 match 就 return，後面不被問到」→ 多命中**根本沒機會發生**（不是被解決，是結構讓它不發生）。鏈序 = 優先序；要改優先序只改組裝順序，核心不動。`[source: ood-design.md §5]`

### ⚡ 跨概念對照（Sherry 自己踩到，很關鍵）
**同一題裡：Prescription 不准開子類別、DiseaseHandler 應該開子類別。**
分界 = 有沒有「不同的行為要被 override」：
- `Covid19.matches()` vs `Attractive.matches()` = 真的不同 code（讀不同欄位、不同判斷）→ 行為不同 → 子類別成立。
- 三道處方只差欄位的值、零行為 → 物件。
這正是概念 1 判準的正反兩面，Sherry 這格通了判準。
`[source: 概念 1；code/diagnosis/*.java；ood-design.md §2+§3]`

---

## 概念 3 — Facade / 通知用 callback / 無命中回 Optional.empty

### Facade（Q4，過）
✅ Sherry：門面 = 簡化 client、藏實作、給簡單入口操作整包子系統。
精準版：**form** = 單一 class `PrescriberSystem` 包住 `PatientDatabase + Queue + Prescriber + 責任鏈`；**intent** = 給核心模組統一高層入口，醫院 IT 不碰子系統內部就能客製化。沒 Facade 用戶得自己 new Queue / new Prescriber / 組鏈。
`[source: ood-design.md §4；code/PrescriberSystem.java]`

### 通知 = callback（Q5，方向對、講鈍）
卡點：Sherry 說「延時 return」——用詞打架（只要 return 就得站著等，不可能延時）。
🔪 判準：**return ＝「我站著等到有值」；callback ＝「我丟了就走，好了你打給我」。**
force = 診斷一筆 3 秒 + 排隊一次一位 → 排第三個會卡 9 秒乾等 → 不能 return，改 callback。
`requestDiagnosis(id, symptoms, onComplete)` 丟完立刻 return void；`onComplete` = 留的電話，診斷器處理完 `onComplete.accept(處方)` 回呼。
`[source: requirement「3 秒/同時多位/通知」；ood-design.md §4；code/PrescriberSystem.java + Prescriber.java]`

### 無命中 → Optional.empty（Q6，**Sherry 原本不會**，已教）
gap：「為什麼不塞兜底處方」答不出來。
最簡解：`Prescription` 的 invariant = `medicines` 至少 1 顆藥（1..*）。兜底處方湊不出合法藥 → 要嘛塞假藥（不誠實）、要嘛留空（違反 invariant、new 不出來）。兩條都爛。
🔪 判準：**「查無」是『沒有』，不是『一種有』。不要為了型別好看捏一個自相矛盾的物件 → 這叫「自洽」。**
乾淨解：回 `Optional.empty()`、通知「查無對應處方」、**不建 Case**（Case 必綁處方 1..1）。`Optional` = Java 誠實表達「可能沒值」，比 null 安全。
`[source: ood-design.md §5；requirement 處方 medicines「多個」；code/PrescriberSystem.diagnoseAndExport / saveCase]`

---

## 概念 4 — 規則註冊該住哪：核心模組純度（Sherry 主導的重構，2026-05-24）

### Sherry 的設計主張
「**不應該有內建**。全部靠 client 透過 `PrescriberSystem` 去 register，讀 `SupportDiseases.in` 後才組鏈 → register 包在 Facade 裡。」

### 三個版本的演化（重點：核心模組認不認識具體的病）
1. **注入版（5/21 舊）**：`fromFiles(..., registry, sleeper)`，registry 從外面傳進來。
2. **內建版（5/24 改前）**：registry 包在 Facade 裡，但 **constructor 焊死三條** `register("COVID-19"...)` → 核心 import 了 Covid19/Attractive/SleepApnea，**認識三種特定的病**。
3. **Sherry 版（5/24 改後，現行）**：registry 包在 Facade 裡、**出廠空的**；client 用 `system.registerRule(...)` 註冊；`provideData` 讀清單才組鏈。

### 為什麼版本 3 對（判準）
> requirement 定位這是「**核心模組**，給醫院技術人員**客製化**」。那三種特定的病（COVID/Attractive/SleepApnea）是**客製化內容**，該住在 client，不該焊進核心模組。
> **肉眼可驗**：改後 `PrescriberSystem` 的 import **零個具體 handler**；`import Covid19` 搬到 `Main`。核心對具體規則零依賴。

### 為什麼「包在 Facade 裡（空）」比「外面 new registry 注入」更好
client 從頭到尾**不需要看到 `DiagnosisRuleRegistry` 型別**，只跟 Facade 講話（`system.registerRule(...)`）。子系統完全藏在門面後 = Facade 精神（子系統一個都不外露）。注入版會把 registry 型別漏給 client，門面較鬆。

### 代價（要知道的契約）
client **必須先 `registerRule` 再 `provideData`**；反了 → `buildChain` throw「No rule registered」（fail-fast）。隱性順序是此設計的成本。

### 改了什麼 + 驗證
- `PrescriberSystem`：移除 constructor 三條內建 register + 移除 Covid19/Attractive/SleepApnea import。
- `Main` / `PrescriberSystemTest` / `PrescriberSystemAdvancedTest`：補 `registerRule` 三條 + import。
- ✅ Scout 親跑：javac 45 檔 exit 0、JUnit **70/70 綠**。
`[source: code/PrescriberSystem.java + Main.java + test/*；ood-design.md §1/§9 B-3；概念 1（具體規則活在組裝層）]`

---

## 概念 5 — Facade 的 force「高結構複雜度」怎麼在 OOA 呈現（Sherry 主動問 force 來源）

### Sherry 的問題
「高結構複雜度應該抓到更多便條紙/類別圖才夠複雜。根據需求我應該補什麼？套上 FacadeSystem 是因為高結構複雜度，這 force 在哪呈現？」

### Facade 的 force（精準版）
不是單一句需求，是一個**張力**：**子系統結構複雜（一堆協作類別）⟷ 客戶端想要簡單（1~3 行跑完）**。Facade 站中間收掉複雜。

### force 在哪呈現（三處）
1. **類別圖**：`PrescriberSystem` 往子系統的**關聯扇出**——PatientDatabase、Prescriber(→Queue/責任鏈/Sleeper)、registry、io(PatientJsonLoader/MiniJsonParser/DiseaseFileLoader)、export(SaveStrategy+2實作+ExportFormat+DiagnosisRecord) ≈ **15+ class**。關聯線越密 = 複雜度越顯性。
2. **便條紙**：寫一張 force 紙「客戶端跑一次診斷+匯出要串接 ~15 類別且有強制先後順序」。
3. **requirement 原句**：第一段「技術人員不足…客製化」+ C-3「1~3 行」= 客戶端要低使用複雜度（resulting context）。

### ⚠️ 關鍵陷阱（Sherry 問法裡的，已點）
「為了夠複雜 → 該補什麼類別」是**倒果為因 = 過度設計**（題庫「放得進去 ≠ 該放進去」）。
- 複雜度**本來就存在**，是別的需求逼出來的：排隊+3秒(Queue/Prescriber/Sleeper)、規則擴充(責任鏈+registry)、JSON載入(io)、多格式匯出(export Strategy)。
- 「補什麼」正解 = **補「畫」不補「類別」**：把既有 collaborator 的關聯線畫齊 + 補 force 便條紙，讓既有複雜度被如實呈現。

### 自我檢查的尺
誠實畫完若門面後沒幾個類別 → Facade 就是過度設計該拿掉。本題畫完 15+ class + 強制順序 → force 紮實。題目標題寫「★★門面模式」給了 form，**但讓它合法的是 force 不是標題**。
`[source: 題庫 雜項/form_vs_force_放得進去不等於該放進去.md；requirement 第一段+C-3；code 全部 io/export/prescriber/diagnosis collaborator；ood-design.md §4]`
