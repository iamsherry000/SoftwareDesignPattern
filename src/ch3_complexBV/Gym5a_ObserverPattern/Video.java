package ch3_complexBV.ObserverPattern;

public class Video {
    public String title;
    public String description;
    public int length; // in seconds

    public Video(String title, String description, int length) {
        this.title = title;
        this.description = description;
        this.length = length;
    }
}
