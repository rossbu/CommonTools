package com.example.jdk5;

import java.util.Random;
import java.util.Scanner;

/*
 * Define an enumeration called Sign, with 3 elements, referred to as:
 * HandSign.SCISSOR, HandSign.PAPER, HandSign.STONE.
 */
enum HandSign {
    SCISSOR, PAPER, STONE
}


/**
 * A card's suit can only be spade, diamond, club or heart. In other words, it has a limited set of values.
 * Before the introduction of enum type in JDK 1.5, we usually have to use an int variable to hold these values. For example,
 * The drawbacks are:
 *
    It is not type-safe. You can assign any int value (e.g., 88) into the int variable suit.
    No namespace: You must prefix the constants by the class name CardSuit.
    Brittleness: new constants will break the existing codes.
    Printed values are uninformative: printed value of 0, 1, 2 and 3 are not very meaningful.
 */
class CardSuitBeforeJDK15{
    public static final int SPADE = 0;
    public static final int DIAMOND = 1;
    public static final int CLUB = 2;
    public static final int HEART = 3;
}
class Card {
    int suit;   // CardSuit.SPADE, CardSuit.DIAMOND, CardSuit.CLUB, CardSuit.HEART  , not type safe, this can be assigned other values as well
}
enum CardSuitAfterJDK15 { SPADE, DIAMOND, CLUB, HEART }

enum Tlight {

    RED(1){
        @Override
        public Tlight showTheLight(){
            return RED;
        }
    },

    GREEN(2){
        @Override
        public Tlight showTheLight(){
            return GREEN;
        }
    },

    YELLOW(3) {
        @Override
        public Tlight showTheLight(){
            return YELLOW;
        }
    };

    Tlight(int i) {
        seconds = i;
    }
    private final int seconds;     // Private variable

    public abstract Tlight showTheLight();

}

/*
 * A game of scissor-paper-stone.
 */
public class EnumScissorPaperRock {
    public static void main(String[] args) {
        Random random = new Random();   // Create a random number generator
        boolean gameOver = false;
        HandSign playerMove = HandSign.SCISSOR;
        HandSign computerMove;
        int numTrials = 0;
        int numComputerWon = 0;
        int numPlayerWon = 0;
        int numTie = 0;

        Scanner in = new Scanner(System.in);
        System.out.println("Let us begin...");

        while (!gameOver) {
            System.out.printf("%nScissor-Paper-Stone");

            // Player move
            // Use a do-while loop to handle invalid input
            boolean validInput;
            do {
                System.out.print("   Your turn (Enter s for scissor, p for paper, t for stone, q to quit): ");
                char inChar = in.next().toLowerCase().charAt(0); // Convert to lowercase and extract first char
                validInput = true;
                if (inChar == 'q') {
                    gameOver = true;
                } else if (inChar == 's') {
                    playerMove = HandSign.SCISSOR;
                } else if (inChar == 'p') {
                    playerMove = HandSign.PAPER;
                } else if (inChar == 't') {
                    playerMove = HandSign.STONE;
                } else {
                    System.out.println("   Invalid input, try again...");
                    validInput = false;
                }
            } while (!validInput);

            if (!gameOver) {
                // Computer Move
                int aRandomNumber = random.nextInt(3); // random int between 0 (inclusive) and 3 (exclusive)
                if (aRandomNumber == 0) {
                    computerMove = HandSign.SCISSOR;
                    System.out.println("   My turn: SCISSOR");
                } else if (aRandomNumber == 0) {
                    computerMove = HandSign.PAPER;
                    System.out.println("   My turn: PAPER");
                } else {
                    computerMove = HandSign.STONE;
                    System.out.println("   My turn: STONE");
                }

                // Check result
                if (computerMove == playerMove) {
                    System.out.println("   Tie!");
                    ++numTie;
                } else if (computerMove == HandSign.SCISSOR && playerMove == HandSign.PAPER) {
                    System.out.println("   Scissor cuts paper, I won!");
                    ++numComputerWon;
                } else if (computerMove == HandSign.PAPER && playerMove == HandSign.STONE) {
                    System.out.println("   Paper wraps stone, I won!");
                    ++numComputerWon;
                } else if (computerMove == HandSign.STONE && playerMove == HandSign.SCISSOR) {
                    System.out.println("   Stone breaks scissor, I won!");
                    ++numComputerWon;
                } else {
                    System.out.println("   You won!");
                    ++numPlayerWon;
                }
                ++numTrials;
            }
        }

        // Print statistics
        System.out.printf("%nNumber of trials: " + numTrials);
        System.out.printf("I won %d(%.2f%%). You won %d(%.2f%%).%n", numComputerWon,
                100.0*numComputerWon/numTrials, numPlayerWon, 100.0*numPlayerWon/numTrials);
        System.out.println("Bye! ");
    }
}