package hero.case2;

public class Pet {
    private final String name;
    
    //@Nullable
    private Hero owner; // could be null

    public Pet(String name) {
        this.name = name;
    }

    public void eat(Fruit fruit) {
        System.out.printf("eat fruit ");
        
        if(owner != null) {
            owner.setHp(owner.getHp() + 10);
        }
        else{
            System.out.println("nothing happened.");
        }
    }

    public String getName() { // it's final, not allow setName
        return name;
    }

    public void setOwner(Hero hero) {
        this.owner = owner;
    }

    public Hero getOwner() {
        return owner;
    }

}
