package ch3_complexBV.ObserverPattern;

import java.util.ArrayList;
import java.util.List;

public class YT_Channel {
    public String name;
    private List<YT_User> subscribers = new ArrayList<>();
    public YT_Channel(String name) {
        this.name = name;
    }

    public void subscribe(YT_User user) {
        // Logic to subscribe to the channel
        System.out.println(user.getName() + " subscribed to " + name);
        subscribers.add(user);
    }

    public void unsubscribe(YT_User user) {
        // Logic to unsubscribe from the channel
        System.out.println(user.getName() + " unsubscribed from " + name);
        subscribers.remove(user);
    }

    public void uploadVideo(Video video) {
        System.out.println("Channel "+ name + "uploaded a new video: " + video.title);
        notifySubscribers(video);
    }

    private void notifySubscribers(Video video) {
        for (YT_User user : subscribers) {
            user.update(video);
        }
    }
}
