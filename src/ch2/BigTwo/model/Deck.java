package BigTwo.model;

import java.util.ArrayList;
import java.util.List;

public class Deck {
    private List<Card> cards = new ArrayList<>();

    public Deck() {
        for(int suit = 0; suit < 4; suit++) {
            for (int rank = 0; rank < 13; rank++) {
                cards.add(new Card(suit, rank));
            }
        }
    }

//    private List<Card> cards;
//
//    public Deck(List<Card> cards) {
//        this.cards = cards;
//    }

    public void shuffle() {
        java.util.Collections.shuffle(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public Card draw() {
        if (isEmpty()) {
            throw new IllegalStateException("Deck is empty");
        }
        return cards.remove(cards.size() - 1);
    }

    //Test
    public Card getCard(int index) {
        if (index < 0 || index >= cards.size()) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + cards.size());
        }
        return cards.get(index);
    }

    public int getSize() {
        return cards.size();
    }

    public static int parseSuit(char ch) {
        return switch (ch) {
            case 'S' -> 0; // Spades
            case 'H' -> 1; // Hearts
            case 'D' -> 2; // Diamonds
            case 'C' -> 3; // Clubs
            default -> throw new IllegalArgumentException("Invalid suit: " + ch);
        };
    }

    public static int parseRank(String s) {
        return switch (s) {
            case "A" -> 0;
            case "2" -> 1;
            case "3" -> 2;
            case "4" -> 3;
            case "5" -> 4;
            case "6" -> 5;
            case "7" -> 6;
            case "8" -> 7;
            case "9" -> 8;
            case "10" -> 9;
            case "J" -> 10;
            case "Q" -> 11;
            case "K" -> 12;
            default -> throw new IllegalArgumentException("Invalid rank: " + s);
        };
    }

}
