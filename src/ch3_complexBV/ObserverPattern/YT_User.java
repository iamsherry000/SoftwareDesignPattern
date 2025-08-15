package ch3_complexBV.ObserverPattern;

public interface YT_User {
    String getName();
    void update(YT_Channel channel, Video video); // 若要更多資訊，可用 Notification event
}
