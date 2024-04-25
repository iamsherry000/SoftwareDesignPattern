import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final String[] habits = {"dancing", "music", "boardgame", "online games", "coding", "movie", "KTV","swimming","hiking"};
    private static final int numbOfIndividuals = 10; 

    public static void main(String[] args) {
        List<Individual> individualsList = new ArrayList<>();

        for(int i = 0; i < numbOfIndividuals; i++){
            individualsList.add(new Individual(i+1, 
                                getRandomBoolean(), 
                                getRandomInt(),

                                ))
        }
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
    int age = 0;
    return age = (int)(Math.random() * (maxAge - minAge)) + minAge;
}

// generate random habits 
public static Set<String> randomHabits() {
    // int totalHabitsNum = habits.length + 1;
    // int size = (int)(Math.random() * (habits.length - 1))
    int size = (int)(Math.random() * habits.length);
    
}