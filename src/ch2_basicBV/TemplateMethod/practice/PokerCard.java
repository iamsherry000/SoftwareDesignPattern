package TemplateMethod.practice;

public class PokerCard extends Card implements Comparable<PokerCard>{
    private final Rank rank; // 2~10, J, Q, K, A
    private final Suit suit; // Club, Diamond, Heart, Spade

    public PokerCard(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank + " of " + suit;
    }

    // ENUM => 固定且有限的值集合
//    public int getRankValue() {
//        return switch (rank) {
//            case "2" -> 2;
//            case "3" -> 3;
//            case "4" -> 4;
//            case "5" -> 5;
//            case "6" -> 6;
//            case "7" -> 7;
//            case "8" -> 8;
//            case "9" -> 9;
//            case "10" -> 10;
//            case "J" -> 11;
//            case "Q" -> 12;
//            case "K" -> 13;
//            case "A" -> 14;
//            default -> -1;
//        };
//    }
//
//    public int getSuitValue() {
//        return switch (suit) {
//            case "Club" -> 1;
//            case "Diamond" -> 2;
//            case "Heart" -> 3;
//            case "Spade" -> 4;
//            default -> -1;
//        };
//    }

    @Override
    public boolean canPlayOn(Card topCard) {
        return false; // 撲克牌比大小，不是依照這個邏輯出牌
    }

    public int compareTo(PokerCard other) {
        if (this.rank.getValue() != other.rank.getValue()) {
            return Integer.compare(this.rank.getValue(), other.rank.getValue());
        }
        return this.suit.ordinal() - other.suit.ordinal();
    }
}
