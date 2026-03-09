package ch3_complexBV.ObserverPattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

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
        System.out.println("Channel "+ name + " uploaded a new video \"" + video.title + "\".");
        notifySubscribers(video);
    }

    private void notifySubscribers(Video video) {
        for (YT_User user : new ArrayList<>(subscribers)) {
            user.update(this, video);
        }
//        Iterator<YT_User> iterator = subscribers.iterator();
//        while (iterator.hasNext()) {
//            YT_User user = iterator.next();
//            user.update(this, video);
//            if (!user.isInterested(video)) { // Assuming `isInterested` checks if the user wants to unsubscribe
//                iterator.remove(); // Safely remove the user
//                System.out.println(user.getName() + " unsubscribed from " + this.name);
//            }
//        }
    }
}
