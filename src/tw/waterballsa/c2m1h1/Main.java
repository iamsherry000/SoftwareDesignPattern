package src.tw.waterballsa.c2m1h1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.nio.file.Files.write;
import static java.util.Arrays.stream;
import static java.util.Collections.shuffle;
import static src.tw.waterballsa.c2m1h1.Reversed.reversed;
import static src.tw.waterballsa.c2m1h1.Utils.intersection;
import static src.tw.waterballsa.c2m1h1.Utils.intersectionSize;

/**
 * @author - johnny850807@gmail.com (Waterball)
 */
public class Main {
    private static final String[] habits = {"打球", "跳舞", "唱歌", "看電影", "慢跑", "寫程式", "吃飯", "睡覺", "寫作", "讀書", "數學"};
    private static final Random random = new Random();
    private static final int numberOfIndividuals = 30;

    public static void main(String[] args) throws IOException {
        List<Individual> individuals = new ArrayList<>();

        for (int i = 0; i < numberOfIndividuals; i++) {
            individuals.add(new Individual(i+1,
                    random.nextBoolean() ? "MALE" : "FEMALE",
                    random.nextInt(50) + 18 ,
                    "intro", randomHabits(),
                    new Coord(random.nextInt(2000), random.nextInt(2000))));
        }

        MatchingApp app = new MatchingApp(individuals, new DistanceBasedMatching());
        Individual individual = individuals.get(0);
        app.match();
        System.out.printf("距離：%f\n", individual.getCoord().distance(individual.getMatched().getCoord()));

        app.setMatchingStrategy(new HabitBasedMatching());
        individual = individuals.get(0);
        app.match();
        System.out.printf("共同興趣：%s\n", join(", ", intersection(individual.getHabits(), individual.getMatched().getHabits())));
    }

    public static Set<String> randomHabits() {
        int size = random.nextInt(habits.length)+1;
        List<String> habitList = Arrays.asList(habits);
        shuffle(habitList);
        return new HashSet<>(habitList.subList(0, size));
    }

}
