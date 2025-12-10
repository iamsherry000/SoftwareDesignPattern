
import card.Card;
import card.Rank;
import card.Suit;
import cardPatternHandlers.*;
import cardPatterns.CardPattern;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Card club3 = new Card(Suit.CLUB, Rank.THREE);
        Player[] players = new Player[4];
        int round = 1;
        
        // Build card pattern handler chain (Chain of Responsibility)
        CardPatternHandler handlerChain = buildHandlerChain();

        try (Scanner scanner = new Scanner(System.in)) {
            // ===== 1. Game Initialization: Player Naming =====
            System.out.println("=== Big Two Game Start ===");
            for(int i = 0; i < 4; i++) {
                System.out.print("Player " + i + " ");
                players[i] = playerNaming(scanner);
            }
            System.out.println();

            // ===== 2. Deal Cards: Shuffle and Deal to Four Players =====
            Deck deck = new Deck();
            deck.shuffle();
            dealCardsToPlayers(deck, players);
            
            // Display each player's hand cards
            for(int i = 0; i < 4; i++) {
                System.out.println(players[i].getName() + "'s hand (" + players[i].getHandSize() + " cards):");
                displayPlayerCards(players[i]);
                System.out.println();
            }

            // ===== 3. Find the Player with Club 3 (First Player) =====
            int firstPlayerWithClub3 = findFirstPlayer(players, club3);
            int currentPlayerIndex = firstPlayerWithClub3;
            System.out.println("Player with Club 3: " + players[firstPlayerWithClub3].getName());
            System.out.println();

            // ===== Main Game Loop =====
            int topPlayerIndex = currentPlayerIndex; // Top player
            List<Card> topPlay = null; // Top play cards on table
            CardPattern topPattern = null; // Top play pattern on table
            int passCount = 0; // Consecutive pass count
            boolean gameOver = false;

            while (!gameOver) {
                System.out.println("===== Round " + round + " Start =====");
                
                // Special handling at round start
                if (round > 1) {
                    // New round starts, clear the table
                    topPlay = null;
                    topPattern = null;
                    passCount = 0;
                    currentPlayerIndex = topPlayerIndex;
                    System.out.println("Top player from last round " + players[topPlayerIndex].getName() + " starts");
                }

                // Players take turns
                while (passCount < 3) {
                    Player currentPlayer = players[currentPlayerIndex];
                    System.out.println("\n" + currentPlayer.getName() + "'s turn (remaining " + currentPlayer.getHandSize() + " cards)");
                    
                    // Display table status
                    if (topPlay != null) {
                        System.out.println("Current top play: " + topPlay + " (played by " + players[topPlayerIndex].getName() + ")");
                    } else {
                        System.out.println("Table is empty");
                    }

                    // Display player's hand
                    System.out.println(currentPlayer.getName() + "'s hand:");
                    displayPlayerCards(currentPlayer);

                    // Player action
                    boolean mustPlay = (round == 1 && currentPlayerIndex == firstPlayerWithClub3 && topPlay == null)
                                    || (topPlay == null && currentPlayerIndex == topPlayerIndex);
                    
                    if (mustPlay) {
                        System.out.println("(Must play, cannot PASS)");
                    }

                    // Read player action
                    String action = scanner.nextLine().trim();
                    
                    if (action.equalsIgnoreCase("PASS") && !mustPlay) {
                        System.out.println(currentPlayer.getName() + " calls PASS");
                        passCount++;
                    } else {
                        // Player plays cards
                        List<Card> playedCards = parseCardsInput(action, currentPlayer);
                        
                        if (playedCards == null || playedCards.isEmpty()) {
                            System.out.println("Invalid input, please try again");
                            continue;
                        }

                        // Validate pattern and rules
                        if (round == 1 && currentPlayerIndex == firstPlayerWithClub3 && topPlay == null) {
                            // First player in first round must include Club 3
                            if (!containsCard(playedCards, club3)) {
                                System.out.println("First play must contain Club 3!");
                                continue;
                            }
                        }

                        // Validate pattern legality (using CardPatternHandler chain)
                        CardPattern playedPattern = handlerChain.handle(playedCards);
                        if (playedPattern == null) {
                            System.out.println("Invalid pattern! Please try again");
                            continue;
                        }

                        // Validate if greater than top play
                        if (topPattern != null) {
                            // Check if same pattern type
                            if (!isSamePatternType(topPattern, playedPattern)) {
                                System.out.println("Different pattern type! Please play the same type of pattern");
                                continue;
                            }
                            
                            // Compare size
                            if (!isGreaterThan(playedPattern, topPattern)) {
                                System.out.println("Your cards are not big enough! Please play bigger cards");
                                continue;
                            }
                        }

                        // Play the cards
                        for (Card card : playedCards) {
                            currentPlayer.removeCard(card);
                        }
                        
                        topPlay = playedCards;
                        topPattern = playedPattern;
                        topPlayerIndex = currentPlayerIndex;
                        passCount = 0;
                        
                        System.out.println(currentPlayer.getName() + " plays: " + playedCards);

                        // ===== 5. Check game end condition =====
                        if (currentPlayer.isEmpty()) {
                            System.out.println("\n=== Game Over ===");
                            System.out.println("Winner is: " + currentPlayer.getName() + "!");
                            gameOver = true;
                            break;
                        }
                    }

                    // Next player
                    currentPlayerIndex = (currentPlayerIndex + 1) % 4;
                }

                // Round end
                if (!gameOver) {
                    System.out.println("\n===== Round " + round + " End =====");
                    System.out.println("Three consecutive PASSes, round ended");
                    System.out.println("Top player: " + players[topPlayerIndex].getName());
                    round++;
                    passCount = 0;
                }
            }

            System.out.println("\nThank you for playing Big Two!");
        }
    }

    /**
     * Player naming input
     */
    public static Player playerNaming(Scanner scanner) {
        System.out.print("Enter player name: ");
        String name = scanner.nextLine();
        return new Player(name);
    }

    /**
     * Deal cards to four players
     */
    private static void dealCardsToPlayers(Deck deck, Player[] players) {
        int playerIndex = 0;
        while (!deck.isEmpty()) {
            Card card = deck.deal();
            players[playerIndex].addCard(card);
            playerIndex = (playerIndex + 1) % 4;
        }
    }

    /**
     * Find the player who has a specific card (used to find player with Club 3)
     */
    private static int findFirstPlayer(Player[] players, Card targetCard) {
        for (int i = 0; i < players.length; i++) {
            // Manually check each card since Card class doesn't override equals()
            for (Card card : players[i].getHandCards()) {
                if (card.getSuit() == targetCard.getSuit() && 
                    card.getRank() == targetCard.getRank()) {
                    return i;
                }
            }
        }
        throw new IllegalStateException("No player has " + targetCard);
    }

    /**
     * Check if a card list contains a specific card
     * (Manual check since Card class doesn't override equals())
     */
    private static boolean containsCard(List<Card> cards, Card targetCard) {
        for (Card card : cards) {
            if (card.getSuit() == targetCard.getSuit() && 
                card.getRank() == targetCard.getRank()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Display player's hand cards
     */
    private static void displayPlayerCards(Player player) {
        List<Card> cards = player.getHandCards();
        for (int i = 0; i < cards.size(); i++) {
            System.out.println("  [" + i + "] " + cards.get(i));
        }
    }

    /**
     * Parse player's card input (simplified version: assumes input format "0 1 2" as indices)
     */
    private static List<Card> parseCardsInput(String input, Player player) {
        try {
            String[] indices = input.split("\\s+");
            List<Card> cards = new ArrayList<>();
            List<Card> handCards = player.getHandCards();
            
            for (String indexStr : indices) {
                int index = Integer.parseInt(indexStr);
                if (index >= 0 && index < handCards.size()) {
                    cards.add(handCards.get(index));
                } else {
                    return null;
                }
            }
            return cards;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Build card pattern handler chain
     * Order: Single -> Pair -> Straight -> FullHouse
     */
    private static CardPatternHandler buildHandlerChain() {
        CardPatternHandler fullHouseHandler = new FullHouseHandler(null);
        CardPatternHandler straightHandler = new StraightHandler(fullHouseHandler);
        CardPatternHandler pairHandler = new PairHandler(straightHandler);
        CardPatternHandler singleHandler = new SingleHandler(pairHandler);
        return singleHandler;
    }

    /**
     * Check if two patterns are the same type
     */
    private static boolean isSamePatternType(CardPattern pattern1, CardPattern pattern2) {
        return pattern1.getClass().equals(pattern2.getClass());
    }

    /**
     * Compare two patterns' sizes
     * @param played Player's played pattern
     * @param top Top pattern on table
     * @return true if played > top
     */
    private static boolean isGreaterThan(CardPattern played, CardPattern top) {
        // Get the maximum card from both patterns
        Card playedMaxCard = findMaxCard(played.getCards());
        Card topMaxCard = findMaxCard(top.getCards());
        
        return compareCards(playedMaxCard, topMaxCard) > 0;
    }

    /**
     * Find the maximum card from a list of cards
     * According to Big Two rules: compare rank first, then suit
     */
    private static Card findMaxCard(List<Card> cards) {
        return cards.stream()
            .max(Comparator.comparingInt((Card c) -> getBigTwoRankValue(c.getRank()))
                          .thenComparingInt(c -> c.getSuit().ordinal()))
            .orElseThrow(() -> new IllegalArgumentException("Card list cannot be empty"));
    }

    /**
     * Compare two cards' sizes
     * @return Positive if card1 > card2, negative if card1 < card2, 0 if equal
     */
    private static int compareCards(Card card1, Card card2) {
        // Compare rank first
        int rankCompare = Integer.compare(
            getBigTwoRankValue(card1.getRank()),
            getBigTwoRankValue(card2.getRank())
        );
        
        if (rankCompare != 0) {
            return rankCompare;
        }
        
        // If rank is the same, compare suit
        return Integer.compare(
            card1.getSuit().ordinal(),
            card2.getSuit().ordinal()
        );
    }

    /**
     * Get the rank value according to Big Two rules
     * Rule: 3 < 4 < 5 < 6 < 7 < 8 < 9 < 10 < J < Q < K < A < 2
     */
    private static int getBigTwoRankValue(Rank rank) {
        switch (rank) {
            case THREE: return 3;
            case FOUR: return 4;
            case FIVE: return 5;
            case SIX: return 6;
            case SEVEN: return 7;
            case EIGHT: return 8;
            case NINE: return 9;
            case TEN: return 10;
            case JACK: return 11;
            case QUEEN: return 12;
            case KING: return 13;
            case ACE: return 14;  // A is greater than K
            case TWO: return 15;  // 2 is the greatest
            default: throw new IllegalArgumentException("Unknown rank: " + rank);
        }
    }
}
