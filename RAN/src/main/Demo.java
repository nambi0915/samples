package main;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Demo implements ActionListener {
	JFrame frame;
	JTextField userInput;
	JLabel textContents;
	String word = "horse";
	int correctGuesses;
	int incorrectGuesses;
	String guessesLeft;
	boolean lose = false;
	StringBuilder wordsGuessedCorrectly;
	JLabel letters;

	Demo() {
		incorrectGuesses = 0;
		correctGuesses = 0;
		guessesLeft = "You have " + (6 - incorrectGuesses) + " chances left to guess a " + word.length()
				+ " letter word";
		wordsGuessedCorrectly = new StringBuilder();

		frame = new JFrame("A Hangman Game");
		frame.setSize(300, 125);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(3, 1));

		userInput = new JTextField();
		userInput.addActionListener(this);
		frame.add(userInput);

		textContents = new JLabel();
		textContents.setText(guessesLeft);
		textContents.setHorizontalAlignment(JLabel.CENTER);
		textContents.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		frame.add(textContents);

		letters = new JLabel("The letters you guess correctly go down here!");
		letters.setHorizontalAlignment(JLabel.CENTER);
		letters.setBorder(BorderFactory.createLineBorder(Color.RED));
		frame.add(letters);

		frame.setVisible(true);

	}

	String seperateLetter(String a, int x, int y) {
		return a.substring(x, y);
	}

	void testLetter(String a) {
		if (word.contains(a) && wordsGuessedCorrectly.toString().contains(a) == false
				&& correctGuesses != (word.length() - 1)) {
			correctGuesses++;
			wordsGuessedCorrectly.append(a);
			letters.setText(wordsGuessedCorrectly.toString());
			textContents.setText("Correct Guess!");
		} else if (word.contains(a) && wordsGuessedCorrectly.toString().contains(a)) {
			textContents.setText("You've already guessed this letter!");
		} else if (word.contains(a) && wordsGuessedCorrectly.toString().contains(a) == false
				&& correctGuesses == (word.length() - 1)) {
			lose = true;
			textContents.setText("You Win!");
			wordsGuessedCorrectly.append(a);
			letters.setText(wordsGuessedCorrectly.toString());
		} else if (word.contains(a) == false && incorrectGuesses == 5) {
			textContents.setText("You lose!");
			lose = true;
		} else {
			incorrectGuesses++;
			textContents.setText("Incorrect Guess! You have " + (6 - incorrectGuesses) + " left.");
		}
	}

	public void actionPerformed(ActionEvent ae) {
		if (lose == false) {
			testLetter(seperateLetter(userInput.getText(), 0, 1));
			userInput.setText("");
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new Demo();
			}
		});
	}
}