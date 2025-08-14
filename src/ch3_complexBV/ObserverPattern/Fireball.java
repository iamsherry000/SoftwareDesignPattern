package ch3_complexBV.ObserverPattern;

public class Fireball implements YT_User {
    public String name;
    public Fireball(String name) {
        this.name = name;
    }

    @Override public String getName() { return name; }

    @Override
    public void update(Video video) {
        System.out.println("Fireball received a new video: " + video.title);
    }
}
