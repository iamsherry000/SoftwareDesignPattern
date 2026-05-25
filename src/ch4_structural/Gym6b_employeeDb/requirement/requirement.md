代理人模式——【延遲載入】員工資料表存取
難度：★☆☆☆☆

A. 需求
你有一張員工資料表檔案，你需要為此資料表設計一個資料存取工具 (姑且稱它為 Database)。

員工資料表中的第一列為 Header。

此表格有 4 個欄位，依序為：id, name, age, subordinateIds 。意思分為別員工的 ID，姓名、年紀以及此員工的直接下屬們的 IDs （員工之間有上下級關係）。
Header 之後的每一列為一筆員工資料，由多個欄位的值構成，欄位之間以空白隔開。id 和 age 為正整數、name 和 subordinateIds 為不包含空白字元的字串。subordinateIds 包含多個數字，數字之間以單一逗號隔開。如果該員工不具備下屬，則該欄位為空。
這張表格保證第 
i
i 列必為 Id 為 
i
−
1
i−1 的員工。例如：第 2 列為 Id=1 的員工、第 3 列為 Id=2 的員工。
以下為一張員工資料表範例：

id name age subordinateIds
1 waterball 25 
2 fixiabis 15 1,3
3 fong 7 1
4 cc 18 1,2,3
5 peterchen 3 1,4
6 handsomeboy 22 1
...

Database 只有一個操作：

輸入：員工 Id
輸出：如果該員工存在，回傳該筆員工的完整資料，否則用某種方式表達該員工不存在。
B. 設計需求
類別圖的初始設計必須為下圖所示：有 Database 和 Employee 兩個介面，Database 介面表達著查詢員工的意圖，RealDatabase 為實際實現意圖之類別、Employee 介面表達著單一員工及該員工上下級關聯等完整資料，而 RealEmployee 為實際乘載員工資料之類別。Client 類別為 Database 的使用端，可隨意撰寫 Database 的使用情境。

c4g11-1-0

Out Of Memory Prevention：由於員工資料表中可能存在上千萬筆資料，為了防止大量的記憶體消耗，RealDatabase 不能一次將所有員工資料載入至記憶體當中，Client 要讀哪一筆員工資料，就直接跳到該員工所屬列去讀取那一位員工資料即可。

延遲載入 Lazy Initialization：希望在讀取員工資料時，能延遲載入該員工的「直接下屬」，以下範例程式來說明：

Employee employee = database.getEmployeeById(1)

// ...

// 直到呼叫 employee.getSubordinates() 時才真正從資料庫中去載入該位員工的直接下屬物件們
List<Employee> subordinates = employee.getSubordinates()

//...

密碼保護 (Password Protection)：在存取 RealDatabase 的 getEmployeeId 操作時，必須在實際存取前先檢查執行環境變數中 PASSWORD 變數的值，確定該值必須為 1qaz2wsx 後才得以繼續，否則中斷此存取。
必須在不修改既有 B1 設計的前提下，擴充設計元素來滿足設計需求 B3, B4。