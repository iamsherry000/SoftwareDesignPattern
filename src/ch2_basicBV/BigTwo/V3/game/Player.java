package ch2_basicBV.BigTwo.V3.game;

import ch2_basicBV.BigTwo.V3.card.Hand;
import ch2_basicBV.BigTwo.V3.card.Card;
import ch2_basicBV.BigTwo.V3.card.Suit;
import ch2_basicBV.BigTwo.V3.card.Rank;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Player {
    protected String name;
    protected final Hand hand = new Hand();

    public Player(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public boolean hasCard(Card card) {
        return hand.contains(List.of(card));
    }

    public void addHandCard(Card card) {
        hand.add(List.of(card));
    }

    public void removeHandCard(Card card) {
        hand.remove(List.of(card));
    }

    public void playHandCard(List<Card> cards) {
        if (!hand.contains(cards)) {
            throw new IllegalArgumentException("Card not found in hand: " + cards);
        }
        hand.remove(cards);
    }

    public String getHand() {
        return hand.toString();
    }

    public List<Card> getHandCards() {
        return hand.getCards();
    }

    public int getHandSize() {
        return hand.size();
    }

    public boolean isEmpty() {
        return hand.isEmpty();
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Check if player has Club 3 (梅花3)
     */
    public boolean hasClub3() {
        Card club3 = new Card(Suit.CLUB, Rank.THREE);
        return hasCard(club3);
    }

    /**
     * Check if player has no hand cards (won the game)
     */
    public boolean hasNoHandCards() {
        return isEmpty();
    }

    /**
     * Remove cards from hand (when playing cards)
     */
    public void loseHandCards(List<Card> cards) {
        playHandCard(cards);
    }

    /**
     * Get player's play action through user input
     */
    public PlayAction play() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\n--- " + getName() + "'s Turn ---");
        System.out.println("Hand: " + getHand());
        System.out.println("or Pass(p)");
        System.out.println("Cards left: " + getHandSize());
        // Get cards to play
        System.out.print("Enter cards: ");
        
        String input = scanner.nextLine().trim();
        
        if ("p".equals(input) || "P".equals(input) || "pass".equalsIgnoreCase(input)) {
            return PlayAction.pass();
        }
        
        try {
            List<Card> selectedCards = parseCards(input);
            
            // Check if player has these cards
            if (!getHandCards().containsAll(selectedCards)) {
                System.out.println("❌ You don't have these cards!");
                return play(); // Try again recursively
            }
            
            return PlayAction.playCards(selectedCards);
            
        } catch (Exception e) {
            System.out.println("❌ Invalid input format! " + e.getMessage());
            return play(); // Try again recursively
        }
    }

    /**
     * Parse input card string
     */
    private List<Card> parseCards(String input) {
        List<Card> cards = new ArrayList<>();
        String[] cardStrings = input.split("\\s+");
        
        for (String cardStr : cardStrings) {
            if (cardStr.isEmpty()) continue;
            
            char suitChar = cardStr.charAt(0);
            String rankStr = cardStr.substring(1);
            
            Suit suit = parseSuit(suitChar);
            Rank rank = parseRank(rankStr);
            
            cards.add(new Card(suit, rank));
        }
        
        return cards;
    }
    
    /**
     * Parse suit character
     */
    private Suit parseSuit(char suitChar) {
        switch (Character.toUpperCase(suitChar)) {
            case 'S': return Suit.SPADE;
            case 'H': return Suit.HEART;
            case 'D': return Suit.DIAMOND;
            case 'C': return Suit.CLUB;
            default: throw new IllegalArgumentException("Invalid suit: " + suitChar);
        }
    }
    
    /**
     * Parse rank string
     */
    private Rank parseRank(String rankStr) {
        switch (rankStr.toUpperCase()) {
            case "3": return Rank.THREE;
            case "4": return Rank.FOUR;
            case "5": return Rank.FIVE;
            case "6": return Rank.SIX;
            case "7": return Rank.SEVEN;
            case "8": return Rank.EIGHT;
            case "9": return Rank.NINE;
            case "10": return Rank.TEN;
            case "J": return Rank.JACK;
            case "Q": return Rank.QUEEN;
            case "K": return Rank.KING;
            case "A": return Rank.ACE;
            case "2": return Rank.TWO;
            default: throw new IllegalArgumentException("Invalid rank: " + rankStr);
        }
    }
}
