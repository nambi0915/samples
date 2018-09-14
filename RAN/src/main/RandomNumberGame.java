package main;

import java.util.Random;
import java.util.Scanner;

public class RandomNumberGame {
	public static void main(String args[]) {
		Random rand = new Random();
		int max = 100;
		Scanner input = new Scanner(System.in);
		int guess;
		boolean play = true;
		int totalGames = 0;
		int totalGuesses = 0;
		int bestGame = Integer.MAX_VALUE;

		System.out.println("Can you guess the number?");
		System.out.println("I am sure you cannot guess!");
		System.out.println("OK.. Go ahead and try!");
		System.out.println();

		while (play) {

			System.out.println("I'm thinking of a number between 1 and " + max + "...");
			int numberToGuess = rand.nextInt(max) + 1;
			int numberOfTries = 0;

			boolean win = false;
			while (!win) {

				System.out.print("Your guess? ");
				guess = input.nextInt();
				numberOfTries++;

				if (guess == numberToGuess) {
					win = true;
				} else if (guess > numberToGuess) {
					System.out.println("It's lower.");
				} else if (guess < numberToGuess) {
					System.out.println("It's higher.");
				}
				input.nextLine();
			}

			if (numberOfTries == 1) {
				System.out.println("Correct the number is " + numberToGuess);
				System.out.println("You got it right in " + numberOfTries + " guess!");
			} else {
				System.out.println("Correct the number is " + numberToGuess);
				System.out.println("You got it right in " + numberOfTries + " guesses!");
			}

			totalGames++;
			totalGuesses += numberOfTries;
			System.out.print("Do you want to play again? ");

			String answer = input.nextLine();
			char firstLetter = answer.charAt(0);
			if (firstLetter == 'y' || firstLetter == 'Y') {
				play = true;
			} else {
				play = false;
			}

			bestGame = Math.min(bestGame, numberOfTries);
			System.out.println();
		}
		input.close();
		double avgGuess = totalGuesses / (double) totalGames;
		if (avgGuess <= 5)
			System.err.println("Wow you're really smart \n");
		else if (avgGuess <= 7 && avgGuess > 5)
			System.err.println("You're a Good thinker \n");
		else if (avgGuess > 7 && avgGuess <= 10)
			System.err.println("It's somewhat a bad score \n");
		else
			System.err.println("Thank you for Quitting the Game !!! \n");

		System.out.println("Overall results:");
		System.out.println("Total games   = " + totalGames);
		System.out.println("Total guesses = " + totalGuesses);
		System.out.printf("Average Guesses per game  = %.1f \n", totalGuesses / (double) totalGames);
		System.out.println("Best game     = " + bestGame);
	}
}
