package hero.case3;

import java.util.ArrayList;
import java.util.Collection;
import static java.util.Collections.unmodifiableList;
import java.util.List;

public class Guild {
    private final String name;
    private final List<Hero> members = new ArrayList<>();
    // private final Set<Hero> members;

    public Guild(String name, List<Hero> members) {
        this.name = name;
        if(members.size() < 1 || members.size() > 10) {
            throw new IllegalStateException("must from 1 to 10.");
        }
        this.members.addAll(members); // 雙向
        /* 
        for(Hero hero: members) {
            hero.addGuild(this);
        }
        */
    }

    public void join(Hero member) {
        if(members.size() == 10) {
            throw new IllegalStateException("More than 10, cannot join.");
        }
        if(members.contains(member)) {
            throw new IllegalStateException("Already exist, cannot join.");
        }

        members.add(member);
        //member.addGuild(this); // passive add
    }

    public void leave(Hero member) {
        if(members.size() == 1) {
            throw new IllegalStateException("The only member cannot leave.");
        }

        if(!members.contains(member)) {
            throw new IllegalStateException("Not in the guild.");
        }

        members.remove(member);
        //member.removeGuild(this);
        //System.out.printf("%s left.\n", member);
    }

    public String getName() {
        return name;
    }

    /* 
    public List<Hero> getMembers() {
        return Collections.unmodifiableList(members); // cannot directly add a new member after
    }
    */

    public Collection<Hero> getMembers() {
       //return unmodifiableSet(members);
       return unmodifiableList(members);
    }
}
