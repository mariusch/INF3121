package hangman;

import java.util.Random;
import java.util.Scanner;


public class Game {

	private static final String[] wordForGuessing = { "computer", "programmer",
			"software", "debugger", "compiler", "developer", "algorithm",
			"array", "method", "variable" };
	private String guessWord;
	private final StringBuffer dashedWord;
	private FileReadWriter fileRW;

	public Game(boolean autoStart) {
		guessWord = getRandWord();
		dashedWord = getW(guessWord);
		fileRW = new FileReadWriter();
		if(autoStart) {
			displayMenu();
		}
	}

    private enum Command {
        restart, top, exit, help

    }

	private String getRandWord() {
		Random rand = new Random();
		return wordForGuessing[rand.nextInt(9)];
	}
	public void displayMenu() {
		System.out.println("Welcome to Hangman game. Please, try to guess my secret word.\n"
						+ "Use 'TOP' to view the top scoreboard, 'RESTART' to start a new game,"
						+ "'HELP' to cheat and 'EXIT' to quit the game.");

		findLetterAndPrintIt();
	}

	private void findLetterAndPrintIt() {
		boolean isHelpUsed = false;
		String letter;
		StringBuffer dashBuff = new StringBuffer(dashedWord);
		int mistakes = 0;

		do {
			System.out.println("The secret word is: " + printDashes(dashBuff));
			System.out.println("DEBUG " + guessWord);
			do {
				System.out.println("Enter your guess(1 letter allowed): ");
				Scanner input = new Scanner(System.in);
				letter = input.next();

				if (letter.equals(Command.help.toString())) {
					isHelpUsed = true;
					int i = 0, j = 0;
					while (j < 1) {
						if (dashBuff.charAt(i) == '_') {
							dashBuff.setCharAt(i, guessWord.charAt(i));
							++j;
						}
						++i;
					}
					System.out.println("The secret word is: "
							+ printDashes(dashBuff));
				}// end if
				menu(letter);

			} while (!letter.matches("[a-z]"));

			int counter = 0;
			for (int i = 0; i < guessWord.length(); i++) {
				String currentLetter = Character.toString(guessWord.charAt(i));
				if (letter.equals(currentLetter)) {

					{

					++counter;
					}
					dashBuff.setCharAt(i, letter.charAt(0));
				}
			}

			if (counter == 0) {
				++mistakes;
				{
					System.out.printf(
							"Sorry! There are no unrevealed letters \'%s\'. \n",
							letter);
				}
			} else {
				System.out.printf("Good job! You revealed %d letter(s).\n",
						counter);
			}

		} while (!dashBuff.toString().equals(guessWord));

		//Prints end game text
        printEndGame(isHelpUsed, mistakes, dashBuff);

		// restart the game
		new Game(true);

	}

    private void printEndGame(boolean isHelpUsed, int mistakes, StringBuffer dashBuff) {

        if (!isHelpUsed) {
            System.out.println("You won with " + mistakes + " mistake(s).");
            System.out.println("The secret word is: " + printDashes(dashBuff));

            System.out
                    .println("Please enter your name for the top scoreboard:");
            Scanner input = new Scanner(System.in);
            String playerName = input.next();

            fileRW.openFileToWrite();
            fileRW.addRecords(mistakes, playerName);
            fileRW.closeFileFromWriting();
            fileRW.openFileToRead();
            fileRW.readRecords();
            fileRW.closeFileFromReading();
            fileRW.printAndSortScoreBoard();

        } else {
            System.out.println("You won with "
                            + mistakes
                            + " mistake(s). but you have cheated. You are not allowed to enter into the scoreboard.");
            System.out.println("The secret word is: " + printDashes(dashBuff));
        }

    }


	private void menu(String letter) {

		if (letter.equals(Command.restart.toString())) {
			new Game(true);

		} else if ( letter.equals( Command.top.toString() ) ) {

				fileRW.openFileToRead();
				fileRW.readRecords();
				fileRW.closeFileFromReading();
				fileRW.printAndSortScoreBoard();
				new Game(true);

        } else if (letter.equals(Command.exit.toString())) {

            System.exit(1);
		}
	}

	private StringBuffer getW(String word) {
		StringBuffer dashes = new StringBuffer("");

		for (int i = 0; i < word.length(); i++) {
			dashes.append("_");
		}
		return dashes;
	}


	private String printDashes(StringBuffer word) {
		String toDashes = "";


		for (int i = 0; i < word.length(); i++) {
			toDashes += (" " + word.charAt(i));
		}

		return toDashes;
	}

}
