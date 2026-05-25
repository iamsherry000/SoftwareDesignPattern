# Gym6b employeeDb — AI 攻防戰紀錄

> 紀錄人類 ↔ AI 在 OOA / forces / OOD / OOP 階段的辯論。
> Schema 雙向 — 可能 AI 說服我、可能我說服 AI。Case-by-case 紀錄，不是「我贏了 AI」流水帳。
> 為什麼要紀錄：AI 不會記得當初為什麼這樣選，這是補償 AI memory 的 anti-corruption layer，也是未來 PM portfolio case study 的素材源。

---

## Schema（每筆紀錄套這個格式）

```
## 攻防 #N — YYYY-MM-DD — {phase: 需求/OOA/forces/OOD/OOP}

- **AI 提案**：[AI 想 propose 什麼]
- **Sherry 立場**：[同意 / push back / 部分接受]
- **雙方根據**：
  - 我的知識點：[基於什麼判斷]
  - AI 的知識點：[基於什麼判斷]
- **結果**：誰被說服、為什麼
- **保留為決策**：[最終採用 + 簡短 rationale]
- **Open / 待驗證**：[沒解決的部分 — null 也填 null]
```

---

## 攻防 #1 — 2026-05-07 ~ 2026-05-09（精靈 review 前準備期間）— OOA → OOD 邊界

- **AI 提案**：[AI 在 OOAD review 階段直接 propose「設計成什麼樣子」]
- **Sherry 立場**：push back —「先檢視需求 + OOAD 完，後面才會擬出一版可以設計的樣子」
- **雙方根據**：
  - 我的知識點：[待 Sherry 補 — 具體 forces / 哪個需求點還沒收斂]
  - AI 的知識點：[待 Sherry 補 — AI 提的 design 是基於什麼假設]
- **結果**：Sherry 說服 AI（守住 OOA 紀律 — problem 沒收斂前不進 solution）
- **保留為決策**：先完成需求檢視 + OOA → 才進 OOD
- **Open / 待驗證**：5/9 早上精靈 review 是否 confirm 這個 OOA 已經足夠成熟可進 OOD？6.C / 6 最終要再驗證一次

---
