package hero.case2;

public class Hero {
    private int level = 1;
    private int totalExp = 0;
    private int hp = 100;
    
    //@Nullable
    private Pet pet; // pet can be null

    public Hero() { // constructor
        // use default level, totalExp and hp.
    }

    public Hero(int level, int totalExp, int hp) {
        this.level = level;
        setTotatlExp(totalExp);
        setHp(hp);
    }

    public void setPet(Pet pet) {
        if(this.pet != null){
            this.pet.setOwner(null); // Abandoned the pet
        }
        
        this.pet = pet;

        if(pet!= null) {
            pet.setOwner(this); // New pet
        }

    }

    public Pet getPet() {
        return pet;
    }

    private void setTotatlExp(int totalExp) {
        if(totalExp < 0) {
            throw new IllegalArgumentException("totalExp >= 0.");
        }
        this.totalExp = totalExp;
    }
    
    public void setHp(int hp) {
        if(hp < 0) {
            throw new IllegalArgumentException("Hp must >= 0.");
        }
        this.hp = hp;

        System.out.println("new hp: %d " + hp);
    }

    private void setLevel(int level) {
        if(level < 0) {
            throw new IllegalArgumentException("Level must >= 0.");
        }
        this.level = level;
    }

    public void gainExp(int exp, LevelSheet levelsheet) {
        if(exp < 0) {
            throw new IllegalArgumentException("Exp must >= 0.");
        }

        
        //totalExp += exp;
        setTotatlExp(totalExp + exp);
        setLevel(levelsheet.queryLevel(totalExp));

        System.out.printf("Current level %d, gain %d EXP, totalEXP %d \n", level, exp, totalExp);
    }

    public int getLevel() {
        return level;
    }

    public int getTotalExp() {
        return totalExp;
    }

    public int getHp() {
        return hp;
    }
}
