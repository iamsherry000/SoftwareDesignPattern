package src.tw.SherryTseng.BigTwo;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    List<Player> playerList = new ArrayList();
    int humanNum;
    Deck deck = new Deck();

    public Game() {
        this.playerList = new ArrayList<>(); 
    }

    public void start() {
        getHumanNum();
        initPlayers();
        deckShuffle();
        dealToPlayer();
        //showHands();

    }

    public void getHumanNum() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter number of human players: ");
        this.humanNum = scanner.nextInt();
    }

    public void initPlayers() {
        for(int i = 0; i < 4; i++) {
            if (i < humanNum) {
                HumanPlayer humanPlayer = new HumanPlayer();
                humanPlayer.nameHimself();
                playerList.add(humanPlayer);
            } else {
                AIPlayer aiPlayer = new AIPlayer();
                aiPlayer.nameHimself();
                playerList.add(aiPlayer);
            }
            //System.out.println("Player " + (i + 1) + ": " + playerList.get(i).getName()); 
        }

        // Make sure the name in the playerList
        // for(int i=0; i<playerList.size();i++){
        //     System.out.println("Player " + (i + 1) + ": " + playerList.get(i).getName()); 
        // }
    }

    public void deckShuffle() {
        // System.out.println("Before shuffle:");
        // deck.showTopCards(4);
        deck.shuffle();
        // System.out.println("After shuffle:");
        // deck.showTopCards(4);
    }

    public void dealToPlayer() {
        deck.deal(playerList);
    }

    // Show everyone's hand 
    public void showHands() {
        for (Player player : playerList) {
            System.out.println(player.getName() + "'s hand: " + player.getHand());
        }
    }
    
    
}