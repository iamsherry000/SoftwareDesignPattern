# 觀察者模式 —— Youtube 訂閱機制 難度：★☆☆☆☆

## 需求
模擬 Youtube 頻道 (Channel) 和 頻道訂閱者 (Channel Subscriber) 之間互動的程式。

## 缺點
1. 沒有用策略封裝每個響應式行為，當使用者需要多個響應式行為就會顯得複雜 => 現在重點是完成作業

## 預期結果
水球 訂閱了 水球軟體學院。

水球 訂閱了 PewDiePie。

火球 訂閱了 水球軟體學院。

火球 訂閱了 PewDiePie。

頻道 水球軟體學院 上架了一則新影片 "C1M1S2"。

水球 對影片 "C1M1S2" 按讚。

頻道 PewDiePie 上架了一則新影片 "Hello guys"。

火球 解除訂閱了 PewDiePie。

頻道 水球軟體學院 上架了一則新影片 "C1M1S3"。

火球 解除訂閱了 水球軟體學院。

頻道 PewDiePie 上架了一則新影片 "Minecraft"。

水球 對影片 "Minecraft" 按讚。