# 好友關係分析器 ★
轉接器模式——好友關係分析器
難度：★☆☆☆☆

## A. 需求
在現代網路社群平台上，人們透過社群平台的好友功能，可以與他人互相結為好友。現在你要開發一個簡易的好友關係分析器，我們首先使用此分析器制定的格式來撰寫一部分人們的好友關係腳本 (Script)（i.e., 使用一個特殊的語法格式來描述每個人分別有哪些好友），然後將此腳本餵入分析器，接著你就能夠讓分析器分析一些關係組成。

在初版的需求中，我們不需要太多複雜的關係分析功能，再將腳本餵入分析器之後，接著只希望能夠藉由輸入兩個名字 (name) ，來請分析器查詢兩人之間的所有共同好友 (Mutual Friends)。
共同好友的定義如下：
C 為 A 和 B 的共同好友，若且唯若 A 和 C 是好友，且 C 和 B 也是好友。

B. 設計需求
你和同事小華共同參與這項專案的開發，小華在理解需求之後又自顧自地決定他要負責開發「好友關係分析器的具體實作」類，然後接著便揚長而去，獨留你一人。
在小華開發好好友關係分析器的具體實作類別之前，你又必須先以介面來表達「分析好友關係」的意圖了。
於是你設計的第一版物件導向分析圖如下：

c4m7-1

RelationshipAnalyzer 介面中的 parse 操作會接收關係腳本字串，並且此腳本字串必須遵守以下格式。每一行的開頭為 <某人的 name>: ，代表此行敘述的是此某人的好友清單。冒號後面會空一個空白，接著為連續 0 ~ * 個 name，代表此某人的每一位好友名稱。

A: B C D
B: A D E
C: A E G K M
D: A B K P
E: B C J K L
F: Z
...

在 RelationshipAnalyzer parse 完關係腳本之後，接著可以呼叫他的 getMutualFriends 操作：傳入 name1 和 name2，取得所有 name1 和 name2 兩者之間的共同好友 （順序不影響）。

Client 的行為如 Client 類別下方便條紙所述：首先先從某個純文字檔案中讀取關係腳本的完整字串，接著餵進 RelationshipAnalyzer 的 parse 操作，然後再呼叫 RelationshipAnalyzer 的 getMutualFriends 並印出共同好友列表。

C. 設計需求（整合小華類別）
在開發好了依賴於 RelationshipAnalyzer 的 Client 程式之後，小華總算將他負責的好友關係分析器具體實作類開發完畢。

加入小華的類別之後，類別圖如下：
小華導入了新的類別 SuperRelationshipAnalyzer。

c4m7-2

SuperRelationshipAnalyzer 介面中的 init 操作會接收關係腳本字串，但此腳本字串遵守的格式與需求 B-2-B 不同，格式如以下所示。每一行的格式為 <name1> -- <name2> ，代表 name1 和 name2 兩人之間為好友關係。

A -- B
A -- C
A -- D
B -- D
B -- E
C -- E
C -- G
C -- K
C -- M
D -- K
D -- P
E -- J
E -- K
E -- L
F -- Z
...

在 SuperRelationshipAnalyzer init 完關係腳本之後，接著可以呼叫他的 isMutualFriend 操作：傳入 targetName、name1 和 name2，得知 targetName 是否為 name1 和 name2 兩者之間的共同好友（回傳 true，代表是；回傳 false 代表不是）。

請在完全不修改 Client、RelationshipAnalyzer 和 SuperRelationshipAnalyzer 任何一行程式碼的前提下整合 SuperRelationshipAnalyzer 來實現 Client 的意圖。

進階挑戰題
A2. 進階需求
延續 A 章節的需求，但是新增一項功能：將腳本餵入分析器之後，接著希望能夠藉由輸入兩個名字 (name) ，來詢問分析器，此兩人是否存在連結 (Connection)。
連結的定義如下：我們說 A 和 B 兩者之間存在連結，若且唯若存在 
A
,
x
1
,
x
2
,
…
,
x
n
,
B
A,x 
1
​
 ,x 
2
​
 ,…,x 
n
​
 ,B，其中 
A
A 和 
x
1
x 
1
​
  是好友、
x
n
x 
n
​
  和 
B
B 是好友，並且所有 
x
i
x 
i
​
  和 
x
i
+
1
x 
i+1
​
  都是好友 (
i
=
1
,
2
,
.
.
.
,
n
−
1
i=1,2,...,n−1)。
舉例來說：如果小明和小美是朋友、小美和小玉是好友，而小玉和小華是朋友，則可以說小明和小華之間存在連結。也能說小華和小明之間存在連結（好友關係是雙向的）。以此類推，可以說小明和小美、小玉以及小華都存在連結（反向亦然）。
B2. 進階設計需求
你仍然必須先以介面來表達「分析好友關係」的意圖，但是由於進階挑戰題要分析兩人之間的連結性 (Connectivity)，你認為要將自 Client 意圖出發所表達的介面的使用方式改變一下，才能夠應付如此複雜的需求以及未來可能會再新增的更多其餘需求。
於是你設計的第一版物件導向分析圖如下：

c4m7-3

RelationshipAnalyzer 介面中的 parse 操作會接收關係腳本字串，並且此腳本字串遵守的格式完全與需求 B-2-B 中描述的格式相同。而在進階設計需求中，parse的使用方式變得更方便了，parse 完關係腳本之後會直接回傳 RelationshipGraph。

RelationshipGraph 中只有一個操作 hasConnection ，傳入 name1 和 name2 ，此操作會回傳 name1 和 name2 是否有連結（回傳 true，代表有；回傳 false 代表沒有）。

Client 的行為如 Client 類別下方便條紙所述：首先先從某個純文字檔案中讀取關係腳本的完整字串，接著餵進 RelationshipAnalyzer 的 parse 操作，獲得 RelationshipGraph 之後再呼叫 RelationshipGraph 的 hasConnection 來印出某兩人之間存在連結。

請在完全不修改 Client、RelationshipAnalyzer 和 RelationshipGraph 任何一行程式碼的前提下整合第三方 Graph 套件來實現 Client 的意圖。

功能性需求 A2-1-A 涉及了 圖論領域中的 connectivity analysis，請你尋找一下你所使用的程式語言是否有第三方的 Graph 套件，能夠支援 connectivity analysis 等需求，並將其整合到程式碼之中。