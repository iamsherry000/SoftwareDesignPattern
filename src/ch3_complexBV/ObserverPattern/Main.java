package ch3_complexBV.ObserverPattern;

public class Main {
    public static void main(String[] args){
        YT_Channel channel1 = new YT_Channel("水球軟體學院");
        YT_Channel channel2 = new YT_Channel("PewDiePie");
        Waterball waterball = new Waterball("水球");
        Fireball fireball = new Fireball("火球");
        channel1.subscribe(waterball);
        channel2.subscribe(waterball);
        channel1.subscribe(fireball);
        channel2.subscribe(fireball);
        Video video1 = new Video("C1M1S2", "這個世界正是物件導向的呢！", 240);
        Video video2 = new Video("Hello guys","Clickbait",30);
        Video video3 = new Video("C1M1S3", "物件 vs. 類別", 60);
        Video video4 = new Video("Minecraft", "Let’s play Minecraft", 1800);

    }
}
