package src.tw.waterballsa.c2m1h1;

import java.util.Set;

public class Individual {
	private final int id;
	private final String gender;
	private final int age;
	private final String intro;
	private final Set<String> habits;
	private final Coord coord;
	private Individual matched;

	public Individual(int id, String gender, int age, String intro, Set<String> habits, Coord coord) {
		this.id = id;
		this.gender = gender;
		this.age = age;
		this.intro = intro;
		this.habits = habits;
		this.coord = coord;
	}

	public int getId() {
		return id;
	}

	public String getGender() {
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

	public Coord getCoord() {
		return coord;
	}

	public Individual getMatched() {
		return matched;
	}

	public void match(Individual matched) {
		if (matched == this) {
			throw new IllegalStateException("Must not match one to himself.");
		}
		this.matched = matched;
	}
}
