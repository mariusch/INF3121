package hangman;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileReadWriter {
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private final ArrayList<Players> playerArr = new ArrayList<>();

	/**
	 * Opens file to be written
	 */
	public void openFileToWrite() {
		try {
			output = new ObjectOutputStream(new FileOutputStream("players.ser", true));
		} catch (IOException ioException) {
			System.err.println("Error opening file.");
		}
	}

	/**
	 * Adds the records to file
	 * @param  scores count of tries
	 * @param  name name of the player
	 */
	public void addRecords(int scores, String name) {

		Players players = new Players(name, scores);

		try {
			output.writeObject(players);
		} catch (IOException ioException) {
			System.err.println("Error writing to file.");
		}
	}

	/**
	 * Closes the opened file
	 */
	public void closeFileFromWriting() {
		try {
			if (output != null){
				output.close();
			}
		} catch (IOException ioException) {
			System.err.println("Error closing file.");
			System.exit(1);
		}
	}

	/**
	 * Opens file to be read
	 */
	public void openFileToRead() {
		try {
			input = new ObjectInputStream(new FileInputStream("players.ser"));

		} catch (IOException ioException) {
			System.err.println("Error opening file.");
		}
	}

	/**
	 * Reads records from file
	 * Broken method: Fails if one player exist on the scoreboard
	 */
	public void readRecords() {
		Players records;

		// input the values from the file
		try {
			Object obj = null;

			while (!(obj = input.readObject()).equals(null)) {
				records = (Players) obj;{
					playerArr.add(records);
					System.out.printf("DEBUG: %-10d%-12s\n", records.getScores(),
							records.getName());
				}
			}

		} catch (EOFException endOfFileException) {
			System.err.println("Error: endOfFileException.");
		} catch (ClassNotFoundException classNotFoundException) {
			System.err.println("Unable to create object.");
		} catch (IOException ioException) {
			System.err.println("Error during reading from file.");
		}
	}

	/**
	 * Prints the scoreboard
	 */
	public void printAndSortScoreBoard() {

		Players tempP;

		int pSize = playerArr.size();
		for (int pass = 1; pass < pSize; pass++) {

			for (int i = 0; i < pSize - pass; i++) {
				if (playerArr.get(i).getScores() > playerArr.get(i + 1).getScores()) {

					tempP = playerArr.get(i);
					playerArr.set(i, playerArr.get(i + 1));
					playerArr.set(i + 1, tempP);
				}
			}
		}

		System.out.println("Scoreboard:");
		for (int i = 0; i < pSize; i++) {
			Players player = playerArr.get(i);

			System.out.printf("%d. %s ----> %d", i, player.getName(),
					player.getScores());
		}

	}

	/**
	 * Closes file to be read
	 */
	public void closeFileFromReading() {
		try {
			if (input != null){
				input.close();
			}

			// exit
			System.exit(0);

		} catch (IOException ioException) {
			System.err.println("Error closing file.");
			System.exit(1);
		}
	}

}
