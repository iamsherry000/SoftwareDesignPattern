# Gym6 處方診斷系統 — OOA Worksheet

> Scout 推 OOA，Sherry 主導判斷。**每結論明標 source**（🟢 Sherry 拍板 / 🟡 Scout 觀察 / 🔵 暫定待後續 requirement verify）。
> Last updated: 2026-05-20（A 段診斷流程 + 診斷規則段 + 整體流程拍板 + 兩個 clean lever）

---

## Session Resume Point（給開機後的 Sherry 自己看）

**目前在**：Phase 1 點的萃取已大致完成 → 準備進 Phase 2 互動分析
**今天（5/20）做到哪**：
- A 段「診斷流程」+「處方診斷規則（三種）」需求都已貼進 requirement.md
- 萃取出 Prescriber / PrescriptionDemand / Prescription / Symptom / Medicine / **DiagnosisRule**
- 整體流程已對齊（見下方 Phase 1.6「整體流程拍板」）
- 兩個 clean lever 已釐清：**demand 別吞病患資料**、**規則別焊死在 match()**

**開機後待續**：
1. 圖補完：加 `DiagnosisRule`（持有條件 + 綁定一道 Prescription）、Case 內部結構（Symptom / Prescription / caseTime）、demand 瘦身只留 id+symptoms
2. **進 Phase 2 互動**：demand → 查 Patient → 規則逐條 `matches()` → 命中吐處方 → notify → user 存 Case
3. **兩個需求疑義要先釐清**（見 Phase 1.6 §疑義）：多規則同時命中怎麼辦 / 都沒命中回什麼
4. 客製化點 / 進階題那段 requirement 還沒貼

**檔案路徑**：`src/ch4_structural/Gym6_prescriberSystem/analyze/ooa-worksheet.md`

---

## Phase 1.1 — 題目情境（從 requirement 切入）

requirement 第一段就丟出關鍵 framing：

> 「請你負責設計和開發處方診斷系統中的**核心模組**。開發完畢之後，醫院中的所有技術人員會利用你的核心模組來**客製化**他們所需的處方診斷系統。」

**🟡 Scout 觀察**：你做的不是「成品」，是 framework / library。醫院 IT 用你寫的東西組他們自己的系統。這跟 Gym6c 日誌框架的 framing 同構（LogFramework 給人組 Logger + Exporter）。

→ 「**可擴充 / 客製化**」這個訊號先記著，OOA 階段**不掛 form / pattern**。後面 requirement 出現「行為」段時，這層會變成 force（變動性 + OCP 約束）。

**紀律提醒**（cross-ref `team/scout/logs/self-corrections/2026-05-14_proxy-misjudge-no-source-verify.md`）：
- OOA 階段紀律 — 只抓 force，不主動掛 form
- 沒對應到 GoF 名稱很正常，硬塞 form 反而走偏

---

## Phase 1.2 — 點的萃取（名詞先抓出）

從 requirement A 段第一部分直接讀到的名詞（不腦補）：

- **Prescriber System** — 系統範圍 boundary，**不是類別**
- **核心模組** — 你開發的 framework 本體，**不是 domain 類別**
- **病患資料庫 (PatientDatabase / PatientDB)** — 存放病患的容器
- **病患 (Patient)** — 主角
- **看診案例 (Case)** — 被 Patient 含有

cross-ref：[題庫 gym1_OOAD/點的萃取_類別物件屬性判斷.md](../../../../../../workspaces/water-ball/教練與學員/題庫/0_所有問題log/gym1_OOAD/點的萃取_類別物件屬性判斷.md)

### 收斂（暫定，等後面 requirement verify）

**🔵 暫定為類別**：`Patient`、`Case`、`PatientDB`
**🟡 Scout 排除為 boundary**：Prescriber System、核心模組

#### Open Q（待後面 requirement 補）

**Q1.a：PatientDB 算 domain 類別還是只是「Patient 的集合」？**

- 如果後面 requirement 給 PatientDB 真正的**行為**（例如「找出所有 65 歲以上的病患」「依 id 查詢」）→ 領域類別
- 如果只是 pure storage，所有查詢都從外部 loop → 可能只是 `List<Patient>` 不用畫

→ 等行為段落出來再 commit。

cross-ref：[題庫 雜項/OOA顆粒度_PoEAA介入時機與第三方依賴標註.md](../../../../../../workspaces/water-ball/教練與學員/題庫/0_所有問題log/雜項/OOA顆粒度_PoEAA介入時機與第三方依賴標註.md)

---

## Phase 1.3 — Patient 屬性 + 不變條件 (invariant)

requirement 對每個欄位都標了範圍 / 格式 invariant，這是 OOD 時 enforce 的訊號（constructor 驗證）：

- `id: String` — 開頭大寫英文字母 + 9 位數字
- `name: String` — 長度 1~30 英文字母
- `gender` — male / female
- `age: int` — 1~180
- `height: float` — 1~500（cm）
- `weight: float` — 1~500（kg）
- `cases: 多個 Case`

### Sherry 的設計選擇（從圖看）

**🟢 Sherry 拍板（2026-05-19 圖）**：`gender` 切 enum `Gender { Male, Female }`，不留 String。
- 判準：cross-ref [題庫 雜項/Enum是什麼.md](../../../../../../workspaces/water-ball/教練與學員/題庫/0_所有問題log/雜項/Enum是什麼.md)
- 兩值固定不擴充 → enum 合理

---

## Phase 1.4 — Sherry Astah 圖初版 + Scout 質疑

### Sherry 畫的（2026-05-19 圖）

```
PrescriberSystem        [可擴充/客製化標籤]
       │
   PatientDB ──── 1 : 0..* ──── Patient
                    ╎                    ◇──── Gender (enum)
                    ╎
                  Cases (association class)
```

**Sherry 的 raise（原話）**：
> 「PatientDB 跟 Patient 是複合關係 擁有且不獨立存在 但我覺得 cases 應該是他們的關聯類別儲存所有 case 那就不是複合關係?」

### Scout 攻擊點 1：Cases 不是關聯類別

**判準（題庫 canonical）**：
- 關聯類別出現的條件是「**兩個類別之間的關聯本身有屬性 / 行為**」
- 例如：玩家 ↔ 玩家「交換手牌」這個關係本身有「交換時間、什麼牌被交換」這些屬於**關係**的資料

cross-ref：
- [題庫 gym1_OOAD/關聯類別使用時機.md](../../../../../../workspaces/water-ball/教練與學員/題庫/0_所有問題log/gym1_OOAD/關聯類別使用時機.md)
- [題庫 gym1_OOAD/交換手牌關聯與關聯類別.md](../../../../../../workspaces/water-ball/教練與學員/題庫/0_所有問題log/gym1_OOAD/交換手牌關聯與關聯類別.md)

**回到 requirement 原文 verify**：

> 「每一筆病患資料中都包含了 ... 多項病例：為病患**在醫院中**的所有看診案例紀錄」

requirement 明寫「**病患**包含多項病例」，**不是**「PatientDB 跟 Patient 的關係包含病例」。Cases 是 Patient 自己的看診紀錄，沒有描述「DB 跟 Patient 之間發生了什麼」 → **不符合關聯類別語意**。

**Sherry 掉進的根因**：把「Cases 是一群 Case 的容器 / 儲存所有 case」當成「Cases 描述了 DB 跟 Patient 的關係」。**「容器」≠「關係 metadata」** — 容器是 `List<Case>` / 自然的多重性 `0..*`，不需要造一個關聯類別。

旁敲：[題庫 gym1_OOAD/抽牌發牌與手牌類別關係.md](../../../../../../workspaces/water-ball/教練與學員/題庫/0_所有問題log/gym1_OOAD/抽牌發牌與手牌類別關係.md) — Hand 是 Player 自己擁有的集合，不是 Player ↔ Game 的關聯類別，同類 framing。

### 修法（Scout 建議，等 Sherry 起床確認）

直接把 Case 掛在 **Patient 自己身上**，用多重性表達「多項」：

```
Patient ◆── 0..* ──> Case
        (or ◇)
```

不需要 Cases 容器類別，也不需要關聯類別。**0..\* 已經表達「多個」**。

---

### Scout 攻擊點 2：Patient ↔ Case 是複合還是聚合？（**留 open**）

**🔵 Sherry 暫畫複合，但要 verify**：

判準（題庫 canonical [gym1_OOAD/聚合vs複合_獨立存在性判準.md](../../../../../../workspaces/water-ball/教練與學員/題庫/0_所有問題log/gym1_OOAD/聚合vs複合_獨立存在性判準.md)）：
- Case 在 system 內，**離開 Patient 還能單獨存在嗎**？
- 看診紀錄離開病患 → 沒語意 → 直覺**複合**
- 但要等後面 requirement 看 Case 有沒有被別的類別「拿出來看」（例如「醫師查詢自己處理過的 case」）→ 如果有外部 reference 抓 Case，可能要重判

→ 暫畫複合（◆），等後面 requirement 給互動再 verify。

---

### Scout 攻擊點 3：PatientDB ↔ Patient 是複合嗎？（**raise，不是 commit**）

**🟡 Scout 觀察 raise**（**Sherry 還沒拍板，不要當她的設計**）：

Sherry 圖上判複合，但這值得再 verify：

判準：Patient 離開 PatientDB 還能存在嗎？
- **現實 framing**：病人是現實的人，DB 砍了病人還是病人 → 直覺聚合
- **system framing**：可能 Patient 物件 lifecycle 真的綁 DB → 複合

**真正要看的訊號**（後面 requirement 才會給）：
- 如果有「醫師拿 Patient 跑診斷」「外部模組查 Patient」這類 → Patient 會被外部抓 reference → **聚合**較合理
- 如果 Patient 只活在 DB 裡面，從來不離開 → 複合 OK

→ Sherry 可以選擇先按目前判斷畫複合，腦中記著 open question，等後面行為段補完再 verify。

cross-ref：[題庫 gym1_OOAD/實體vs物件vs類別_可存在vs已存在.md](../../../../../../workspaces/water-ball/教練與學員/題庫/0_所有問題log/gym1_OOAD/實體vs物件vs類別_可存在vs已存在.md)

---

## Phase 1.5 — 診斷流程萃取（5/20 新貼需求）

requirement「診斷流程」段冒出的名詞，照「可獨立存在 / 有行為」收斂：

- **Prescriber 診斷器** — 🟡 真正有行為的領域類別（收 id+症狀、排隊、依序診斷、通知）。這段主角。
- **PrescriptionDemand 診斷要求** — 🟡 請求物件，**只裝 病患 id + 多項症狀**（使用者唯一輸入）。有自己身分（要排隊被一筆筆處理）→ 類別。
- **Prescription 處方** — 🟡 診斷結果類別。屬性 + invariant：name(4~30)、potentialDisease(3~100)、medicines(多個，1..*)、usage(0~1000)。
- **Symptom 症狀** — 🔵 暫定薄類別 / 也可能 String，等行為再定。
- **Medicine 藥物** — 🔵 Sherry 升成類別（name 3~30）。可成立（集中 invariant）也可只是受約束字串，**待 Sherry self-justify scope**（typo：圖上 `Medincine` → `Medicine`）。
- **Case 結構解開**：requirement「一項病例含多項症狀、處方、診斷時間」→ `Case ◆ 0..* Symptom`、`Case ──> 1 Prescription`、`Case.caseTime`。補上昨天的 open Q。
- **模組用戶 / 用戶** — 🟡 boundary（用 framework 的醫院 IT），不是領域類別。

### 此段藏的 force（OOA 只記力、不掛 form）
- F-A：隊列 + 一次一位 + 先後順序 + 沒要求就等 → 「循序處理請求」的力
- F-B：「診斷器通知用戶結果」+「每次耗時 3 秒」→ 「發請求者 vs 結果送達 解耦」的力

### 升級昨天 open Q
- 用戶輸入 **id** 要求診斷 → 系統要「依 id 查 Patient」→ 這行為落在 PatientDB 上 → **PatientDB 是有行為的領域類別（findById），不是 List<Patient>**。🟡 觀察升級，最終 Sherry 拍。

---

## Phase 1.6 — 處方診斷規則 + 整體流程拍板（5/20 核心）

### 規則段把「客製化」force 變具體

三條規則同骨架：「**一組條件 → 一道處方**」。→ 萃取出一級概念 **`DiagnosisRule`**（持有它的條件 + 綁定它對應的那道 Prescription）。Prescription 的線從 DiagnosisRule 接出，不是 Prescriber 直接生。

- **「目前僅三種」的『目前』= 主 force**：🟡 客製化 = 醫院 IT 自己新增診斷規則（新疾病/新條件/新處方），**不更動核心模組**。canonical 關鍵句型：「**診斷規則的種類會持續擴充，新增規則時不應修改既有核心診斷流程**」（變動性 + OCP）。
- **條件異質**：規則一純症狀；規則二讀 gender+age+症狀；規則三要 BMI(>26)+症狀。→ 第二軸 force：「規則內條件種類異質、以組合湊出一條規則」。
- **操作開在誰身上**（cross-ref [題庫 操作開在誰身上.md](../../../../../../workspaces/water-ball/教練與學員/題庫/0_所有問題log/gym1_OOAD/操作開在誰身上.md)）：
  - `Patient.getBMI()`（= 體重/身高²，全是自己的資料）→ 開在 Patient，不另開 BMICalculator helper（同 Gym6c isAtLeast 開在 enum 的紀律）
  - `PatientDB.findById(id)` → 開在 DB

### 整體流程拍板（5/20 Sherry 提 + Scout 修正）

使用者透過 PrescriberSystem（入口）發 **PrescriptionDemand**（**只有 id + 多項症狀**）→ Prescriber 用 id `findById` 取 Patient → Prescriber 持有一組 DiagnosisRule，逐條 `rule.matches(patient, symptoms)`（規則自己決定讀 patient 哪些資料 / 自己叫 getBMI）→ 第一條命中吐綁定的 Prescription，沒命中走兜底規則回 default → Prescriber notify 使用者 → 使用者透過 PrescriberSystem 把結果存成 Patient 的一筆 Case（症狀+處方+caseTime）。

### 兩個 clean lever（Sherry 問「能不能更 clean」的答案）

1. **demand 別吞病患資料**：Sherry 初版讓 demand 包 gender/age/height/weight + 算 BMI。根因 = 把「使用者輸入」跟「系統存的病患資料」混成一包。**標準：demand 只裝輸入（id+症狀）；病患屬性永遠從查出的 Patient 讀，BMI 比對當下才叫 `patient.getBMI()`**。否則 demand 變複製病患全欄位的 god object。
2. **規則別焊死在 match()**：Sherry 初版 `Prescriber.match()` 內「三種 if/else + default()」→ 新增第四種疾病就要改 Prescriber，違反主 force。**標準：每條規則是獨立物件（自己 matches + 自己綁處方）；Prescriber 只「持有規則集合、逐條問、第一命中就回」。新增規則 = new 一個丟進集合，Prescriber 不動。default 不做特例 method，當「永遠回 true 的最後一條規則」排隊尾，邏輯統一**。
   - ⚠️ OOA 階段**不掛 form 名**（cross-ref self-correction 2026-05-14）。責任切對、force 寫精準，OOD 收斂時 pattern 自然浮出。

### 需求疑義（🟡 raise，OOD 前要先有答案，別自己腦補）
- **多規則同時命中**怎麼辦？（例：18 歲女+BMI>26+又打噴嚏又打呼 → 可能同中規則二三）回哪個 / 全部 / 有優先序？
- **一條都沒命中**回什麼？（null / 空處方 / 例外 / default 處方）— Sherry 已傾向 default 兜底
- 🔵 暫定別過度設計：potentialDisease「新冠肺炎（COVID-19）」看似顯示名+學名兩欄，但 requirement 定義是單一字串(3~100)，**先留一個字串**，別升 Disease 類別（form-first）。

---

## Phase 2 — 下一步（開機後接續）

1. **圖補完**：加 DiagnosisRule（條件 + 綁定 Prescription）、Case 內部結構、demand 瘦身、清雙重表達（`cases:list<Case>` 屬性 + `medicines` 屬性刪掉，資訊留線上）、補多重性、修 Medincine typo
2. **進 Phase 2 互動分析**：誰 call 誰（demand → 查 Patient → 規則逐條評估 → 命中吐處方 → notify → 存 Case）
3. **先釐清兩個需求疑義**（多命中 / 無命中）再排規則評估順序
4. 客製化點 / 進階題段 requirement 還沒貼

---

## 紀律 reminder（給接下來的 Scout 自己看）

- canonical vocabulary 一字不改（互動 / 關鍵句型 / 自洽 / 點的萃取 / 力）
- 主軸永遠是當前題目（處方診斷系統），GoF 通論是工具不是主角
- OOA 階段只抓 force，不主動掛 form
- 每結論明標 source（🟢 / 🟡 / 🔵）
- 引 requirement / 題庫 canonical 原文，不憑空講
