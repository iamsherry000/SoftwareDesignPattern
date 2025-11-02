package ch3_complexBV.ObserverPattern;

/*
* 每當水球接獲新的影片通知時，他會判斷：如果影片的長度時間 ≥ 三分鐘，那麼水球就會對其影片按讚 (Like)，否則置之不理。
 */

public class Waterball implements YT_User{
    public String name;
    public Waterball(String name) {
        this.name = name;
    }

    @Override public String getName() { return name; }

    @Override
    public void update(YT_Channel channel, Video video) {
        // System.out.println("Waterball received a new video: " + video.title);
        if(video.length >= 180) {
            System.out.println(name + " liked the video \"" + video.title + "\"");
        } else {
            // channel.unsubscribe(this);
            // System.out.println(name + " unsubscribed \"" + channel.name + "\"");
        }
    }
}
