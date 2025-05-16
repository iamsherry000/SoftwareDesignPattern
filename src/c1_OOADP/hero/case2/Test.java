package hero.case2;

public class Test {
    public static void main(String[] args) {
        Hero hero = new Hero();
        Pet cat = new Pet("Catty");
        
        System.out.printf("Hero hp: %d\n", hero.getHp());
        
        //System.out.printf("Hero's pet name: %s \n", hero.getPet().getName());
        if (hero.getPet() != null) {
            System.out.printf("Hero's pet name: %s \n", hero.getPet().getName());
        } else {
            System.out.println("Hero has no pet.");
        }

        for(int i = 0 ; i < 5 ; i++) {
            cat.eat(new Fruit());
        }
    }
}
