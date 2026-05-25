# Gym6 處方診斷系統 — OOD 設計鎖（Phase 0 交付物）

> 寫法依 CONVENTIONS 🟢 規則：**narrative 推理風格**（題目情境 → 質疑 → 排除替代 → 收斂），不是 spec dump。
> 結尾 §8 才放「收斂 summary」當 RD 的 build spec（>5 行資訊用表格的例外）。
> 每個結論標 source：🟢 Sherry 拍板 / 🟡 Scout 歸納 / 🔵 暫定待 verify。
> Scope：**基礎題（三規則 + 診斷流程）**。進階題（JSON 載入/匯出）requirement 未貼 → 留 §9 Phase 6 TODO，不在此鎖。
> Last updated: 2026-05-21（Scout 鎖；Sherry 拍板：pattern = **Facade（入口）+ 責任鏈（規則）**、Disease 用 String、無命中回 Optional.empty）
>
> **命名對齊紀錄（2026-05-21 晚，Sherry 拍板「程式碼對齊圖」）**：類別命名已對齊 `OOAD/OOD.png`。canonical 類別清單（§8 / §9 表）已更新；§2–§7 推導 narrative 仍保留當時推導用名（歷史推理痕跡，不回改）。完整 mapping 見文末 **§11 命名對齊紀錄**。

---

## §1 題目情境 + scope boundary

requirement 第一段定調這不是「成品」而是 **framework**：

> 「請你負責設計和開發處方診斷系統中的**核心模組**……醫院中的所有技術人員會利用你的核心模組來**客製化**他們所需的處方診斷系統。」

→ 你交的是給人組裝的核心模組。這個 framing 在 §3 會變成主 force（變動性 + OCP）。跟 Gym6c 日誌框架同構（LogFramework 給人組 Logger + Exporter）。

**本檔鎖定範圍**：診斷流程（隊列 → 查病患 → 跑規則 → 通知 → 存 Case）+ 三條診斷規則。
**不鎖**：客製化點 / 進階題那段 requirement 還沒貼，**不腦補**（紀律 cross-ref `team/scout/logs/self-corrections/2026-05-14_proxy-misjudge-no-source-verify.md`）。

---

## §2 第一筆要收的設計債：子類別 vs 物件

### 22:13 OOA 圖的問題

你 5/20 的圖把三組東西長成了**繼承階層**：

- Prescription ← 清冠一號 / 青春抑制劑 / 打呼抑制劑
- Potential Disease ← COVID-19 / Attractive / SleepApneaSyndrome
- Medicine ← 清冠一號_Med / Man_Med / Tape_Med

### 質疑：子類別憑什麼成立？

判準（題庫「點的萃取 類別 vs 物件」）：**子類別要成立，得有結構或行為的不同**。

回頭看 requirement——三道處方的欄位**完全一樣**：name / potentialDisease / medicines / usage。清冠一號跟青春抑制劑沒有任何一個多出來的欄位或方法，**差的只有值**。

→ 值不同 = **物件**，不是型別。`清冠一號` 應該是

```
new Prescription("清冠一號", "新冠肺炎（COVID-19）", List.of(new Medicine("清冠一號")), "將相關藥材裝入茶包...")
```

跟「黑桃3 是 Card 的**物件**、不是 Card 的**子類別**」同一回事（cross-ref 題庫 gym1_OOAD/點的萃取_類別物件屬性判斷.md）。

### show working：假設保留子類別會撞到什麼

- **新增第四種病** → 要新增三個 class（Prescription 子類 + Disease 子類 + Medicine 子類），還要改 Prescriber 認得新型別 → 直接踩 §3 主 force
- **型別承諾不誠實** → 看到三層平行繼承樹，會以為它們有多型行為差異，實際沒有（form_vs_force：放得進去 ≠ 該放進去，cross-ref 題庫 雜項/form_vs_force_放得進去不等於該放進去.md）

**收斂 🟢**：Prescription / Medicine 都是 immutable 值物件，**不開子類別**；potentialDisease 是 Prescription 的一個 `String` 欄位（3~100），**不升 Disease 類別/enum**。

> 🟡→🟢 Scout note（Sherry 2026-05-21 確認用 String）：參考圖 3070 把 Disease 做成 enum（COVID-19/Attractive/SleepApneaSyndrome）。enum 比子類別好，但仍把「目前三種病」焊進型別系統——新增病要改 enum，跟「客製化」force 相衝。**字串更貼 force + 對齊 requirement 原文「3~100 個字元」**。這是我跟參考圖唯一刻意的分歧，Sherry 拍板採 String。

---

## §3 變動的家：責任鏈（規則）

### force 從哪來

三條規則同骨架：「**一組條件 → 一道處方**」。requirement「目前僅能診斷出三種」的『**目前**』+ 第一段「客製化」→ canonical 關鍵句型：

> **「診斷規則的種類會持續擴充，新增規則時不應修改既有核心診斷流程。」**（變動性 + OCP）

§2 收掉子類別後，變動沒有家了。它的家就是這條 force 要的東西：**讓「一條規則」變成一個獨立物件**，Prescriber 只「持有規則、逐條問、第一個命中就回」。

### 為什麼是責任鏈、不是 if/else，也不是 Strategy

- **vs `Prescriber.match()` 內三段 if/else**：新增第四種病要改 `match()` → 踩主 force。淘汰。
- **vs Strategy**：Strategy 是「外部挑一個策略來用」；這題是「**依序問每條規則、它自己決定要不要處理、第一個處理的就回、沒人處理往下傳**」——handler 自己決定處不處理、可組裝成鏈，這是責任鏈的 intent，不是 Strategy（cross-ref 題庫 雜項/Strategy_vs_Chain_Force差異.md）。
- **form + intent 雙軌都對**（紀律 cross-ref self-correction 2026-05-14）：form = 一條鏈逐個傳遞直到有人處理；intent = 解耦「發診斷要求的 Prescriber」跟「具體哪條規則處理」，鏈可擴充。✅
- **參考圖 3070 印證**：`«interface» DiagnosisHandler`（setNext + handle）← CovidHandler / AttractiveHandler / SleepApneaHandler，next_handler 自關聯 0..1。`[source: ref/3070_OOD.png]`

> 你 gym4 BigTwo 已經做過責任鏈（BV 比對型），這不是新 pattern——CONVENTIONS「不引入未學過的 GoF」過關。

### 規則的條件異質怎麼吃

- 規則一：純症狀（噴嚏+頭痛+咳嗽）
- 規則二：讀 gender + age + 症狀（18 歲女 + 噴嚏）
- 規則三：要 BMI(>26) + 症狀（打呼）

→ 每個 handler 的 `matches(patient, symptoms)` 自己決定讀 patient 哪些資料、自己叫 `patient.getBMI()`。條件異質被各 handler 自己吸收，鏈不需要知道。

---

## §4 入口、通知、循序處理

### 通知 = callback（不是 return）

requirement：「診斷器會**通知**用戶診斷結果」+「每一次診斷耗時 **3 秒**」+「多位用戶**同時**要求」。

force：發要求的人 vs 結果送達要**解耦**——要求丟進去後不能卡著等 3 秒。

→ 解法是 **callback**：用戶發要求時帶一個 `onComplete`（拿到 Prescription 時被呼叫）。`[source: 參考圖 3070 PrescriberSystem.diagnose(symptoms, patientId, onComplete)]`

### 循序處理 = Queue + 一次一位

requirement：「將每一筆新的診斷要求**排到隊列後方**，依**先後順序**依序診斷。直到隊列中沒有要求就停止等待。」

→ `Queue`（FIFO）+ Prescriber 一次取一筆、處理完才取下一筆。

> 🟡 並行：「多位用戶同時要求」嚴格說要 thread（producer 丟、單一 consumer 跑）。**基礎題先做單執行緒、可注入延遲的版本**（見 §6 TDD），thread 的真實性留 demo 層；不為了 thread 把 core 設計複雜化。

> 🟢 **Sherry 2026-05-21 拍板（流程複盤後）**：採**非同步**互動 = queue + callback、**單執行緒**（不開 thread）、**不用 observer**（callback 最輕）。`requestDiagnosis` 丟進 queue 後立刻返回（呼叫端不等 3 秒），結果在診斷器 drain 到該筆時透過 callback 通知。此為與原 SD.png（同步 `getPrescription` 回傳）的刻意分歧——**SD.png 待 Sherry 改成 async 版**（submit 立刻返回 + Queue 參與者 + notifyUser 移到處理完成後）。基礎 `Main` 已改：走 `fromFiles`（= provideData 設定階段，不再手 new Patient/DB/鏈）→ 四階段 Phase 0→1→2→3 完整 demo（驗證綠：70/70 + Main 端到端「共存入病例數 3」）。方法名維持現狀（Sherry：不用改命名）。

### 入口 = PrescriberSystem（Facade）

🟢 **Sherry 2026-05-21 拍板**：學過 Facade → 入口正式標 **Facade 角色**。

用戶不直接碰 PatientDB / Queue / 鏈，而是透過 `PrescriberSystem` 這個 **Facade** 入口（發要求、存 Case）。
- **form**：單一入口類別包住子系統（PatientDB + Queue + Prescriber + 責任鏈）。
- **intent**：給核心模組一個統一的高層介面，醫院 IT 不必碰子系統內部就能客製化。
- form + intent 雙軌都對 ✅（紀律 cross-ref self-correction 2026-05-14：不只看 form 像不像）。

---

## §5 兩個需求疑義鎖定

### 多規則同時命中 → 鏈序 first-match-wins

例：18 歲女 + BMI>26 + 噴嚏 + 打呼 → 同時中規則二（青春抑制劑）跟規則三（打呼抑制劑）。

責任鏈天然解：**鏈的順序 = 優先序，第一個 match 的回，後面不跑**。

🟢 鎖定鏈序 = **requirement 原順序（規則一 Covid → 規則二 Attractive → 規則三 Snore）**。
> 若你心裡有別的優先序（例如重症優先），改鏈的組裝順序即可，core 不動——這正是責任鏈的好處。先按原序鎖。

### 一條都沒命中 → 回 `Optional.empty()`，不造假處方

worksheet 你之前傾向「default 兜底」。**我這裡改了，理由要講清楚 `[A2: 我主動偏離你前一個傾向]`**：

Prescription 的 invariant 是 `medicines` **1..\***（至少一個藥）。一個「查無對應處方」的兜底 Prescription **湊不出合法的藥** → 要嘛塞假藥（資料不誠實）、要嘛違反 invariant（設計不**自洽**）。

→ 乾淨解：鏈跑完沒人 match 就回 `Optional.empty()`；PrescriberSystem 通知用戶「查無對應處方」，**不建 Case**（Case 的 prescription 是 1..1，沒處方就沒 Case）。

> 🟢 **Sherry 2026-05-21 討論後確認**：採 `Optional.empty()`。「查無對應」在這個系統裡是**『沒有結果』**（什麼都不發、不存），不是要存的一種結果——所以不需要 DiagnosisResult，也不造假兜底處方。default 兜底會撞 `medicines` ≥1 invariant → 不自洽。

---

## §6 操作開在誰身上（cross-ref 題庫 操作開在誰身上.md）

- `patient.getBMI()` — BMI = 體重 ÷ 身高²，全是 Patient 自己的資料 → 開在 Patient，**不另開 BMICalculator helper**（同 Gym6c `Level.isAtLeast` 開在 enum 的紀律）。
  - ⚠️ RD 注意：height 存 cm（1~500），BMI 公式要換算 m：`weight / (height/100.0)²`。圖上 `{BMI = weight ÷ height²}` 沒換算，是 bug，test 要抓。
- `patientDB.findById(id)` — 開在 DB。
- `handler.matches(patient, symptoms)` — 每條規則自己判，自己決定讀 patient 哪些欄位。
- `patient.addCase(case)` — Case 掛在 Patient 自己身上（§2 已收掉「Cases 關聯類別」誤判，用 0..* 多重性表達）。

---

## §7 設計自洽檢查

- demand 只裝 `patientId + symptoms`（clean lever 1：別吞病患資料；BMI 等比對當下從查出的 Patient 算）。
- 規則不焊死在 `Prescriber`（clean lever 2：每條規則獨立物件）。
- 沒命中不造假（§5）。
- 三道具體處方/藥是**物件**，活在組裝層（demo / setup），不在類別圖長型別（§2）。

---

## §8 收斂 summary（RD 的 build spec）

> 這節是給 RD 照著寫的。folder 按 CONVENTIONS pattern role 分。

### 類別清單 + 職責

| package | class | 職責 + 關鍵 invariant |
|---|---|---|
| `core` | `Patient` | private final id/name/gender/age/height/weight + `List<Case>`；constructor 驗 invariant；`getBMI():double`；`addCase(Case)` |
| `core` | `Gender` | enum { MALE, FEMALE } |
| `core` | `Symptom` | enum { SNEEZE, HEADACHE, COUGH, SNORE } |
| `core` | `Medicine` | immutable，`String name`，constructor 驗長度（**非空 ≤30**，下限放寬，見 §10 requirement 瑕疵）|
| `core` | `Prescription` | immutable，name(4~30) / potentialDisease(String 3~100) / `List<Medicine>`(1..*) / usage(0~1000)；constructor 驗 |
| `core` | `Case` | immutable，`List<Symptom>` + `Prescription`(1) + `caseTime` |
| `core` | `PatientDatabase` | 持有病患；`findById(String):Optional<Patient>` |
| `diagnosis` | `DiseaseHandler` | abstract：`setNext(DiseaseHandler)`；`handle(Patient,List<Symptom>):Optional<Prescription>`（template：matches→回綁定處方，否則傳 next）；abstract `matches(...)` + 綁定的 `Prescription` |
| `diagnosis` | `Covid19` | matches：症狀含 SNEEZE+HEADACHE+COUGH → 清冠一號（對齊 OOD.png 子類 "Covid-19"，連字號非法 → `Covid19`）|
| `diagnosis` | `Attractive` | matches：gender==FEMALE && age==18 && 含 SNEEZE → 青春抑制劑 |
| `diagnosis` | `SleepApneaSyndrome` | matches：getBMI()>26 && 含 SNORE → 打呼抑制劑 |
| `prescriber` | `PrescriptionDemand` | immutable，`patientId` + `List<Symptom>`（**只裝輸入**） |
| `prescriber` | `Queue` | FIFO；`enqueue` / `dequeue` / `isEmpty` |
| `prescriber` | `Prescriber` | 持有 鏈 head + PatientDatabase + Queue；`submit(demand)`；`processNext()`：dequeue→findById→鏈 handle→onComplete callback；3 秒延遲（**可注入**） |
| `code`（根，入口層）| `PrescriberSystem` | Facade 入口（單一 class，不另開 folder）：`requestDiagnosis(patientId, symptoms, onComplete)`；`saveCase(patientId, symptoms, prescription, caseTime)` |
| `code`（根，入口層）| `Main` | 組裝鏈（規則一→二→三）+ sample patients + demo |

### 每類別的 first test（TDD 紅燈起點）

- `Patient`：非法 id（無大寫開頭/非9位數字）throw；age 0 / 181 throw；getBMI 已知值（如 100kg/200cm → 25.0）。
- `Medicine` / `Prescription`：欄位長度越界 throw；medicines 空 list throw。
- `Covid19`：給齊三症狀 → 回清冠一號；缺一症狀 → empty（往下傳）。
- `Attractive`：18 歲女+噴嚏 → 青春抑制劑；17 歲 / 男 → 不中。
- `SleepApneaSyndrome`：BMI>26+打呼 → 打呼抑制劑；BMI=26 邊界 → 不中（>26 嚴格）。
- 鏈：多命中回鏈序第一個；全不中回 `Optional.empty()`。
- `PatientDatabase`：findById 命中/查無。
- `Prescriber`：submit 多筆 → FIFO 順序；callback 帶正確處方觸發；3 秒用注入的 fake delay，test 不真等。
- `PrescriberSystem`：requestDiagnosis 後可 saveCase，Case 進 patient.cases；查無處方 → 不建 Case。

### Folder
```
Gym6_prescriberSystem/          ← 一題全包，頂層只三個資料夾（2026-05-21 Sherry 重整）
├── OOAD/       Astah 圖（OOA.png / .asta）+ ref/ 參考圖（3070 / 839）
├── analyze/    requirement.md / ooa-worksheet.md / ood-design.md
└── code/       所有 Java + result/；【根目錄 = 入口層】
    ├── PrescriberSystem.java   ← Facade 入口（單一 class，不另開 folder）
    ├── Main.java  Main_Advanced.java   ← demo（usage）
    ├── core/       Patient Gender Symptom Medicine Prescription Case PatientDatabase   (domain model)
    ├── diagnosis/  DiseaseHandler Covid19/Attractive/SleepApneaSyndrome + DiagnosisRuleRegistry   (責任鏈)
    ├── prescriber/ Prescriber PrescriptionDemand Queue Sleeper   (引擎：排隊+循序診斷)
    ├── io/         MiniJsonParser PatientJsonLoader DiseaseFileLoader   (讀檔；進階題 C-1/C-2)
    ├── export/     SaveStrategy SaveStrategy_JSON/SaveStrategy_CSV ExportFormat DiagnosisRecord   (Strategy；進階題)
    ├── test/       對應各 package 的 JUnit5
    └── result/     patients.json diseases.txt + demo 匯出檔
```
package：入口層三個 class（PrescriberSystem / Main / Main_Advanced）直接在 `ch4_structural.Gym6_prescriberSystem.code`；子體系在 `…code.<sub>`（core/diagnosis/prescriber/io/export），跟 folder 1:1。
**資料夾收斂原則（🟢 Sherry 2026-05-21 拍板）**：folder 要「自己賺到存在理由」——pattern 邊界（diagnosis 責任鏈 / export Strategy）、domain model（core）、或能單獨理解的責任群（prescriber 引擎 / io 讀檔）才開 folder；入口層（Facade + Main）平放根、**不開 1-class folder**（原 `system/` 殺掉、`usage/` 攤平）。7 folder → 5 folder，重編譯 JUnit 70/70 綠。

---

## §9 進階題（Phase 6）— 門面模式 + 外部客製化

requirement B/C 段（2026-05-21 貼齊）。題目標題明寫「★★ 門面模式」→ **Facade 是主角**。如預判，這層只「加 IO/序列化 + 外部組裝」，不推翻 §2–§6 責任鏈核心。

### requirement → pattern 對應

- **B-1**：封裝 `PatientDatabase`（查病患 / 加病例）+ `Prescriber`（要求診斷）兩個維護者介面。
  - ⚠️ requirement 指定名 `PatientDatabase`，§1–§8 我命名 `PatientDB` → **rename `PatientDB` → `PatientDatabase`**（requirement 是 ground truth）。
- **B-2**：外部決定「收到結果觸發什麼」不改內部 → 我們的 **onComplete callback**（§4）已滿足。
- **B-3**：外部擴充新規則不改內部 → **責任鏈（§3）+ 規則註冊表**。新規則 = 寫一個 `DiagnosisHandler` 子類 + 註冊它的學名，核心不動。
- **C-1**：JSON 檔決定病患資料庫 → `MiniJsonParser`（仿 Gym6c，recursive-descent，無外部 lib）+ `PatientJsonLoader`。
- **C-2**：純文字檔（每行一個疾病**專業學名**）決定診斷器支援哪些疾病 → 註冊表按學名挑 handler、依序組鏈。
- **C-3**：**門面（Facade）** 讓用戶 1~3 行跑完整流程 + 匯出結果（JSON / CSV）→ Facade + **匯出 Strategy**。

### 關鍵決定（🟡 Scout 定，Sherry 可推翻）

1. **病患 JSON schema**（requirement 沒給格式 → 我定）：物件陣列，欄位對齊 A-1。
   ```json
   [{"id":"A123456789","name":"Alice","gender":"female","age":30,"height":165,"weight":55}]
   ```
   `cases` 載入時預設空（病例是診斷後才產生）。給一份 sample `result/patients.json`。
2. **C-3「病患的名稱」vs A「身分證字號」**：requirement A 診斷流程精確說輸入 **id**，C-3 寫「名稱」。判 C-3 是寬鬆說法 → **Facade 仍以 id 為查詢鍵**（findById 是唯一識別）。🟡 flag wording 不一致，採 id。
3. **匯出 = Strategy**：`SaveStrategy` 介面 + `SaveStrategy_JSON` / `SaveStrategy_CSV`，`ExportFormat { JSON, CSV }` enum 當選擇器。匯出內容 = 該次診斷結果（patientId + symptoms + prescription + caseTime）。（命名對齊 OOD.png，§11）
4. **規則註冊表**：`DiagnosisRuleRegistry` 持 `學名 → Supplier<DiagnosisHandler>`。維護者 `register("COVID-19", CovidHandler::new)`（B-3）；C-2 loader 讀文字檔、按學名取 handler、依檔案行序組鏈。學名 keys = `COVID-19` / `Attractive` / `SleepApneaSyndrome`（對齊處方 potentialDisease 的學名）。

### Facade API（C-3，目標 ≤3 行）

```java
// line 1：C-1 載病患 + C-2 載疾病組鏈（維護者已先 register 三條規則）
PrescriberSystem system = PrescriberSystem.fromFiles("patients.json", "diseases.txt", registry, sleeper);
// line 2：診斷 + 匯出（C-3 自由決定 病患 id / 症狀 / 匯出檔 / 格式）
system.diagnoseAndExport("A123456789", List.of(SNEEZE, HEADACHE, COUGH), "result.json", ExportFormat.JSON);
```
- 內部仍走 Prescriber 的 queue + 3 秒 + callback（reuse §4 核心，不另開路徑）。
- 同時保留 §4 的低階 `requestDiagnosis(id, symptoms, onComplete)`（給 B-2 自訂行為）。

### 收斂 spec（RD 建這些，phase 6）

| package | class | 職責 |
|---|---|---|
| `core` | `PatientDatabase`（rename 自 `PatientDB`）| 同 §8，名稱對齊 requirement B-1 |
| `io` | `MiniJsonParser` | recursive-descent JSON → Map/List/String/Number（仿 Gym6c，本 gym 自己一份）|
| `io` | `PatientJsonLoader` | 讀 JSON 檔 → `List<Patient>` → PatientDatabase（C-1）|
| `io` | `DiseaseFileLoader` | 讀純文字檔（每行學名）+ registry → 組好的鏈 head（C-2）|
| `diagnosis` | `DiagnosisRuleRegistry` | `register(學名, Supplier<DiagnosisHandler>)` + `buildChain(List<學名>)`（B-3）|
| `export` | `SaveStrategy`（介面）| `export(DiagnosisRecord): String`（命名對齊 OOD.png）|
| `export` | `SaveStrategy_JSON` / `SaveStrategy_CSV` | Strategy 兩格式（C-3）|
| `export` | `ExportFormat`（enum JSON/CSV）+ `DiagnosisRecord` | 格式選擇器 + 匯出資料載體 |
| `code`（根，入口層）| `PrescriberSystem`（擴充）| Facade：`fromFiles(...)` + `diagnoseAndExport(...)` + 保留 `requestDiagnosis` |
| `code`（根，入口層）| `Main_Advanced` | C-3 demo：1~3 行完整流程；附 sample patients.json + diseases.txt |
| `test` | 對應各新 class | parser round-trip、loader、registry 組鏈、兩 exporter 格式、Facade 端到端 |

### 不變的紀律
- 維護者 register 三條規則放**組裝層**（registry setup / Main_Advanced），不在類別圖長型別（延續 §2）。
- 無命中仍 `Optional.empty()`（§5）；匯出時若查無 → 不寫結果檔或寫空，待 §10 風格決定（傾向不寫 + 通知）。
- 匯出 Strategy 用 enum 選 impl，不把序列化塞進 enum 本體（避免 enum 肥大）。

---

## §10 requirement 瑕疵紀錄（Medicine 名稱長度）

RD TDD 實作時抓到：requirement **自相矛盾**。

- requirement A 段：「食用藥物 (medicines) — ……每個藥物名稱包含 **3~30** 個字元」
- requirement 規則二：食用藥物「假鬢角、**臭味**」——「臭味」只有 **2 字元**，違反上面的 3~30。

**Scout 2026-05-21 繞過決定 `[source: requirement 自相矛盾，非實作 bug]`**：規則二是三大核心規則之一，它綁定的藥**必須能被建構**，否則 AttractiveHandler / 鏈 / 規則二測試全炸。**example data（臭味）勝過描述性範圍（3~30）** → Medicine 下限放寬為「非空 ≤30」。

- ✅ reversible：純 constructor 邊界，要改回嚴格 3~30 隨時可。
- 🟡 待 Sherry：若你知道此 gym 官方解怎麼處理（或想當「需求釐清」練習自己拍），可推翻本決定。其他可能解：把「假鬢角、臭味」當**單一**藥名（6 字合法，但語意上是兩個藥，不建議）。

---

## §11 命名對齊紀錄（2026-05-21 晚，🟢 Sherry 拍板「程式碼對齊圖」）

Sherry 用 `requirement.md + OOA_v1.png + OOD.png` 重新核對程式碼後，發現 OOD.png 的命名跟程式碼不一致，**拍板「程式碼對齊圖」**。Scout 放大精讀 OOD.png 確認精確拼寫後，把程式碼 rename 對齊。mapping：

| 原程式碼名 | 對齊後（= OOD.png）| 備註 |
|---|---|---|
| `DiagnosisHandler`（abstract）| `DiseaseHandler` | |
| `CovidHandler` | `Covid19` | 圖上是 `Covid-19`，連字號是非法 Java 識別字 → `Covid19`（唯一被迫偏離）|
| `AttractiveHandler` | `Attractive` | |
| `SleepApneaHandler` | `SleepApneaSyndrome` | |
| `ResultExporter`（介面）| `SaveStrategy` | |
| `JsonResultExporter` | `SaveStrategy_JSON` | 保留底線（貼圖名）|
| `CsvResultExporter` | `SaveStrategy_CSV` | 保留底線（貼圖名）|

**例外（圖跟需求衝突，程式碼站需求）**：`PatientDB`（圖）維持 `PatientDatabase`（程式碼），因 requirement B-1 明文指定 `PatientDatabase`。

**未動（圖沒畫、屬實作演化）**：`DiagnosisRuleRegistry` / `DiagnosisRecord` / `ExportFormat` / `Queue` / `Sleeper` 等進階題支援類別圖上沒有，不在對齊範圍。

**仍存的結構分歧（待 Sherry 另外決定，未自動處理）**：OOD.png 畫了 `Client` 類別（`provideData` / `getPrescription` / `savePrescription` 等），程式碼沒有獨立 Client，由 `usage/Main` 扮演 client 角色。是否要長出一個 `Client` 類別 = 結構決定不是 rename，留給 Sherry。

**驗證**：rename 後 46 檔重編譯 exit 0、JUnit **70/70 綠**、Main + Main_Advanced 端到端行為與 rename 前完全一致（Scout 2026-05-21 親跑）。

對應 test class 也同步 rename：`Covid19Test` / `AttractiveTest` / `SleepApneaSyndromeTest` / `SaveStrategy_CSVTest` / `SaveStrategy_JSONTest`。

---

## §12 真實測資整合（2026-05-21 晚，Sherry 給官方風格測資 + 參考 Main）

Sherry 把實際測資放 `code/resource/`：`AllPatients.json`（病患庫）、`SupportDiseases.in`（支援疾病學名）、`PrescribePatient.in`（診斷要求，三行一組：id / 症狀 / 匯出路徑）。先前驗證是對 Scout 自編 schema，**對真實測資原本跑不起來**——以下相容性修正後可跑：

- `PatientJsonLoader`：gender 接受 `M`/`F`（原只認 male/female）；病患的 `cases` 歷史**不載入**（§② 從簡，diagnosis 不需要歷史）。
- `DiseaseFileLoader`：學名分隔改 `[,\r\n]+`（原只換行）——吃 `COVID-19,` 這種帶逗號格式。
- `Symptom.parse(String)`：小寫逗號字串 → enum（`sneeze,headache,cough` → SNEEZE/HEADACHE/COUGH）。
- `ExportFormat.fromFileName(String)`：副檔名推格式（`.csv`/`.json`）。
- demand id 尾端空白 trim。

**單一 Main**（`Main_Advanced` 移 .trash）：讀三個 resource 檔 → 逐筆 `diagnoseAndExport`（reuse async 核心：enqueue→processAll→callback 匯出）→ 輸出寫到 **`code/result/`**（🟢 Sherry：丟到 result；用 demand 路徑的檔名）。

**匯出形狀 🟢 Sherry 拍板「照 requirement」**：含 symptoms、potentialDisease 用全名（`新冠肺炎（COVID-19）`）、caseTime 用 datetime、只匯出這次結果。**這跟 `AllPatients.json` 內既有 case 的形狀不同**（那邊無 symptoms、只學名、純日期 `2025-12-30`）——是測資 sample 比 requirement 寬鬆，Sherry 選以 requirement 為準。

**驗證**：45 檔編譯 exit 0、JUnit 70/70 綠、Main 對真實測資跑出 patient1.csv(A→清冠一號) / patient2.json(B→青春抑制劑) / patient3.csv(Z→打呼抑制劑) 三筆診斷正確。

**程式風格 🟢 Sherry**：全 code 最簡、註解只在超必要時（如 Medicine §10 繞過、Covid19 連字號、Patient BMI cm→m）。production 檔已做註解精簡 sweep。

---

## §13 Facade 封裝重構（2026-05-24，Sherry 五點 OOD review）

Sherry 審 OOD 後拍五點，落地兩件 code 改動（#3 圖複雜度 / #4 PotentialDisease 基數 1 / #5 Prescriber→DiseaseHandler 委派 = 圖或既有，code 不動）：

- **#1 Registry 包進 Facade**：原本 Main 自己 new registry + register 三次 + 傳進 `fromFiles`（子系統漏給 Client）。改成 **`PrescriberSystem` 內部持有 registry**、建構子預先註冊三條規則、開 `registerRule(學名, factory)` 當**外部擴充縫**（縫開在 Facade 上，不是丟裸 registry 給 Client）。
- **#2 解析放 Facade 內部**：新增 `provideData(patientsJson, diseases)`——Client 把資料**內容字串**交給 System，System 內部用 `PatientJsonLoader` / `DiseaseFileLoader` + 自己的 registry 解析建 DB + 鏈。Client 不碰 loader。

**建構改分階段**（對齊參考 Main `new PrescriberSystem(...)` → `importPatients` → 用）：`new PrescriberSystem(sleeper)`（內建三規則）→ 可選 `registerRule(...)`（外部擴充）→ `provideData(...)`（解析建構）→ `diagnoseAndExport(...)`。移除舊的 `(PatientDatabase, DiseaseHandler, Sleeper)` 建構子 + 靜態 `fromFiles`（靜態工廠沒有外部 register 的時機窗，與 #1 衝突）。`requestDiagnosis`/`processAll`/`diagnoseAndExport`/`saveCase` 名稱不變（🟢 Sherry：不改命名）。

> 取捨：分階段 → `patientDB`/`prescriber` 非 final（provideData 才設），輕度違反「全 final」慣例。但這是 SD.png（provideData 獨立步驟）+ 參考 Main（staged）的設計，owner 設計優先。用 `requireReady()` 守未 provideData 就診斷。

**Main 簡化**：`new PrescriberSystem(sleeper)` → `provideData(讀兩檔內容)` → loop `diagnoseAndExport`。不再 import/碰 registry、handler、loader——這就是 Facade 該有的樣子（Client 只看 Facade）。

**驗證**：45 檔編譯 exit 0、JUnit **70/70 綠**（PrescriberSystemTest / AdvancedTest 改用 `provideData(字串)` 建構，數目維持）、Main 對真實測資跑出 result/patient1.csv / patient2.json / patient3.csv 三筆診斷正確。
