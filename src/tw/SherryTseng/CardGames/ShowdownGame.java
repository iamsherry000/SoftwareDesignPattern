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

    @Override
    public void takeTurn() {
        // in the next 13 rounds 
        for (int round = 1; round <= 13; round++) {
            System.out.println("Round " + round + " begins:");
            List<Card> playedCards = new ArrayList<>();

            for (Player player : playerList) {
                Card playedCard = player.playCard();
                System.out.println(player.getName() + " plays: " + playedCard.getSuit() + " " + playedCard.getRank() + " in round:" + round);
                playedCards.add(playedCard);
            }
    
            Player roundWinner = determineRoundWinner(playedCards);
            System.out.println("Round " + round + " winner: " + roundWinner.getName());
            roundWinner.addPoint();        
        }

        Player overallWinner = determineOverallWinner();
        System.out.println("Final Winner " + overallWinner.getName());
    }

    private Player determineRoundWinner(List<Card> playedCards) {
        Player roundWinner = null;
        Card winningCard = null;
    
        for (int i = 0; i < playedCards.size(); i++) {
            Card playedCard = playedCards.get(i);
            if (winningCard == null || isCardHigher(playedCard, winningCard)) {
                winningCard = playedCard;
                roundWinner = playerList.get(i); 
            }
        }
    
        return roundWinner;
    }
    

    private boolean isCardHigher(Card card1, Card card2) {
        // 比較牌的階級
        int rankComparison = card1.getRank().compareTo(card2.getRank());
        if (rankComparison > 0) {
            return true; // card1的階級比較高
        } else if (rankComparison < 0) {
            return false; // card2的階級比較高
        } else {
            // 階級相同，比較花色
            int suitComparison = card1.getSuit().compareTo(card2.getSuit());
            return suitComparison > 0; // 如果花色比較大，則card1勝出
        }
    }

    private Player determineOverallWinner() {
        Player overallWinner = null;
        int maxPoints = Integer.MIN_VALUE;
    
        for (Player player : playerList) {
            int playerPoints = player.getTotalPoints();
            if (playerPoints > maxPoints) {
                maxPoints = playerPoints;
                overallWinner = player;
            }
        }
    
        return overallWinner;
    }
}
