package ch3_complexBV.ObserverPattern;

public class Waterball implements YT_User{
    public String name;
    public Waterball(String name) {
        this.name = name;
    }

    @Override public String getName() { return name; }

    @Override
    public void update(Video video) {
        System.out.println("Waterball received a new video: " + video.title);
    }
}
