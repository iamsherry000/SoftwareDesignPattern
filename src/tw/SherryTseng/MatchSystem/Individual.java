package src.tw.SherryTseng.MatchSystem;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class Individual {
    private int id;
    private boolean gender;
    private int age;
    private String intro;
    private Set<String> habits;
    private Coordinate coord;
    private Individual matched;

    public Individual(int id, boolean gender, int age, Set<String> habits, String intro, Coordinate coord) {
        this.id = id;
        this.gender = gender;
        this.age = age;
        this.intro = intro;
        this.habits = habits;
        this.coord = coord;
    }

    // Getters and setters
    public int getId() {
		return id;
	}

	public boolean getGender() {
		return gender;
	}

	public int getAge() {
		return age;
	}

	public Set<String> getHabits() {
		return habits;
	}

	public String getIntro() {
		return intro;
	}

	public Coordinate getCoord() {
        return coord;
	}

	public Individual getMatched() { //被別人match 
		return matched;
	}

    public void match(Individual matched) { //match到別人
		if (matched == this) {
			throw new IllegalStateException("Must not match one to himself.");
		}
		this.matched = matched;
	}
}
