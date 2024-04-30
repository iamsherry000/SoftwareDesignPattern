package src.tw.SherryTseng;

import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class Main {
    private static final String[] habits = {"dancing", "music", "boardgame", "online games", "coding", "movie", "KTV","swimming","hiking"};
    private static final int numbOfIndividuals = 10; 

    public static void main(String[] args) {
        List<Individual> individualsList = new ArrayList<>();

        for(int i = 0; i < numbOfIndividuals; i++){
            individualsList.add(new Individual(i+1, 
                                getRandomBoolean(), 
                                getRandomInt(),
                                getRandomHabits(habits),
                                getIntro(),                                
                                getCoordinate()
                                ));
        }
    }

    // generate random gender with 0 for male, 1 for female.
    public static boolean getRandomBoolean() {
        return Math.random() < 0.5;
    }

    // generate random age between maxAge and minAge.
    public static int getRandomInt() { 
        final int maxAge = 80;
        final int minAge = 18;
        return (int)(Math.random() * (maxAge - minAge + 1)) + minAge; 
    }

    // generate random habits 
    public static Set<String> getRandomHabits(String[] habits) {
        Set<String> selectedHabits = new HashSet<>();
        int numOfHabits = (int)(Math.random() * habits.length) + 1; 
        List<String> habitList = Arrays.asList(habits);
        Collections.shuffle(habitList); 

        for (int i = 0; i < numOfHabits; i++) {
            //Add the first numOfHabits shuffled interests from the shuffled interests list to the set.
            selectedHabits.add(habitList.get(i)); 
        }

        return selectedHabits;
    }

    // generate intro
    private static final String[] intros = {
        "Test1 - Hi, I'm a fun-loving person who enjoys exploring new places.",
        "Test2 - Hey there! I'm passionate about music and love attending concerts.",
        "Test3 - Hello! I'm an avid reader and always looking for a good book recommendation.",
        "Test4 - ",
        "Test5 - "
    };
    public static String getIntro() {
        int index = (int)(Math.random() * intros.length);
        return intros[index];
    }

    // generate the coordinate
    public static Coordinate getCoordinate() {
        int x = (int)(Math.random() * 100); // x: 0-100
        int y = (int)(Math.random() * 100); // y: 0-100
        return new Coordinate(x, y);
    }
} 