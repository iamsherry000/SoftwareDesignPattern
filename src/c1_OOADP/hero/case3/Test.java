package hero.case3;

import java.util.List;

public class Test {
    public static void main(String[] args) {
        Hero heroA = new Hero();
        Hero heroB = new Hero();
        Hero heroC = new Hero();
        Guild guild = new Guild("Software Design Pattern Class", List.of(heroA));
        System.out.printf("original number: %d\n", guild.getMembers().size());

        guild.join(heroB);
        System.out.printf("heroB joined, guild number: %d\n", guild.getMembers().size());
        guild.join(heroC);
        System.out.printf("heroC joined, guild number: %d\n", guild.getMembers().size());

        try {
            guild.join(heroA); 
            System.out.printf("heroA joined, guild number: %d\n", guild.getMembers().size());
        } catch (Exception err) {
            System.out.println("heroA cannot join again!");
        }

        guild.leave(heroA);
        System.out.printf("heroA left, guild number: %d\n", guild.getMembers().size());
        guild.leave(heroB);
        System.out.printf("heroB left, guild number: %d\n", guild.getMembers().size());

        try { // repulicated leave
            guild.leave(heroA); 
            System.out.printf("heroA left, guild number: %d\n", guild.getMembers().size());
        } catch (Exception err) {
            System.out.println("heroA cannot leave again!");
        }

        try { // the last one leave
            guild.leave(heroC); 
            System.out.printf("heroC left, guild number: %d\n", guild.getMembers().size());
        } catch (Exception err) {
            System.out.println("heroC cannot leave when hero is the last one.");
        }
    }
}
