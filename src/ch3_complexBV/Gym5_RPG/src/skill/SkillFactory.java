package ch3_complexBV.Gym5_RPG.src.skill;

public class SkillFactory {
    public static Skill create(String name) {
        return switch (name) {
            case "普通攻擊" -> new BasicAttack();
            case "下毒"     -> new Poison();
            case "鼓舞"     -> new Cheerup();
            case "石化"     -> new Petrochemical();
            case "水球"     -> new Waterball();
            case "火球"     -> new Fireball();
            case "自我治療" -> new SelfHealing();
            case "自爆"     -> new SelfExplosion();
            case "召喚"     -> new Summon();
            case "詛咒"     -> new Curse();
            case "一拳攻擊" -> new OnePunch();
            default -> throw new IllegalArgumentException("Unknown skill: " + name);
        };
    }
}