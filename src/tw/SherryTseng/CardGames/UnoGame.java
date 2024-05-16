package src.tw.SherryTseng.CardGames;

import java.util.ArrayList;
import java.util.List;

import src.tw.SherryTseng.CardGames.Card.Color;

public class UnoGame extends Game {
    private Deck deck;

    @Override
    public void initialDeck() {
        //System.out.println("UnoGame start with 40 cards");
        List<Card> cardList = new ArrayList<>();
        for (Color color : Color.values()) {
            for (int number = 0; number <= 9; number++) {
                cardList.add(new Card(color, number));
            }
        }
        deck = new Deck(cardList);
        System.out.println("Total cards in deck: " + deck.getCardList().size());
        deck.shuffle();
    }

    // @Override
    // public void drawCards() {
    //     int numCards = 5;
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
        int numCards = 5;
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
            System.out.print(player.getName() + "'s initial hand: ");
            for (Card card : initialHand) {
                System.out.print(card.getColor() + "" + card.getNumber() + " ");
            }
            System.out.println();
        }
    }

    @Override
    public void takeTurn() {
        // Card tableCard = tableCardList.remove(0);
        // p1 - p4 take turn, 
            // if player.hand have no same color with tableCard.getColor, drawOneCard;
        // getFinalWinner if player.hand = empty 
    }

}
