package ch3_complexBV.ObserverPattern;

/*
* 每當火球接獲新的影片通知時，他會判斷：如果影片的長度時間 ≤ 一分鐘，那麼火球就會立刻對該頻道解除訂閱 (Unsubscribe)。
*/

public class Fireball implements YT_User {
    public String name;
    public Fireball(String name) {
        this.name = name;
    }

    @Override public String getName() { return name; }

    @Override
    public void update(YT_Channel channel, Video video) {
        if(video.length <= 60) {
            channel.unsubscribe(this);
        }
        else {
            // do nothing.
        }
    }
}
