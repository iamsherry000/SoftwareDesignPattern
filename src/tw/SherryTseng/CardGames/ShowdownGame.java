package src.tw.SherryTseng.CardGames;

import java.util.ArrayList;
import java.util.List;

import src.tw.SherryTseng.CardGames.Card.Rank;
import src.tw.SherryTseng.CardGames.Card.Suit;

public class ShowdownGame extends Game{
    private Deck deck;

    @Override
    public void initialDeck() {
        //System.out.println("ShowdownGame start with 52 cards");       
        List<Card> cardList = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cardList.add(new Card(suit, rank));
            }
        }
        deck = new Deck(cardList);
        System.out.println("Total cards in deck: " + deck.getCardList().size());
        deck.shuffle();
    }

    // @Override
    // public void drawCards() {
    //     int numCards = 13;
    //     System.out.println("Drawing " + numCards + " cards for each player");
    //     for (Player player : playerList) {
    //         for (int i = 0; i < numCards; i++) {
    //             Card card = deck.drawCard();
    //             player.addCardToHand(card);
    //         }
    //     }
    // }

    @Override
    public void drawCards() {
        int numCards = 13;
        List<List<Card>> initialHands = new ArrayList<>();

        System.out.println("Drawing " + numCards + " cards for each player");
        for (Player player : playerList) {
            List<Card> hand = new ArrayList<>();
            for (int i = 0; i < numCards; i++) {
                Card card = deck.drawCard();
                hand.add(card);
                player.addCardToHand(card);
            }
            initialHands.add(hand);
        }

        // Print initial hands
        System.out.println("Initial hands:");
        for (int i = 0; i < playerList.size(); i++) {
            Player player = playerList.get(i);
            List<Card> initialHand = initialHands.get(i);
            System.out.print(player.getName() + "'s hand: ");
            for (Card card : initialHand) {
                System.out.print(card.getSuit() + " " + card.getRank() + ", ");
            }
            System.out.println();
        }
    }
}
