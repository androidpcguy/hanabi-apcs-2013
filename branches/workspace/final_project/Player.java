package final_project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.WindowConstants;

public class Player extends Thread {

	private ObjectInputStream input;

	private ObjectOutputStream output;

	private Socket socket;

	private GameComponent gameComp;

	private int playerNum;

	private GameState gameState;

	/**
	 * Constructs a new <tt>Player</tt> and connects it to the server using the
	 * specified <tt>serverIP</tt> and <tt>portNumber</tt>
	 * 
	 * @param portNumber
	 *            port number of server
	 * @param serverIP
	 *            IP Address of server
	 * @param gameState
	 *            the game state
	 */
	public Player(int portNumber, String serverIP, GameState gameState) {
		this.gameState = gameState;
		try {
			socket = new Socket(serverIP, portNumber);
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			this.playerNum = input.readInt();

			gameComp = new GameComponent(playerNum, gameState);

			JFrame gameFrame = new JFrame("Hanabi: Player " + (playerNum + 1));
			gameFrame.add(gameComp);
			gameFrame.pack();
			gameFrame.setVisible(true);
			gameFrame.setResizable(false);
			gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		} catch (IOException ioex) {
			JOptionPane connFailed = new JOptionPane(
					"connection failed: wrong port number or server ip address!",
					JOptionPane.ERROR_MESSAGE);
			connFailed.setVisible(true);
			System.err
					.println("connection failed: wrong port number or server ip address!");
			ioex.printStackTrace();
		}

	}

	@Override
	public void run() {
		while (socket.isConnected()) {
			try {
				gameState = (GameState) input.readObject();
				gameComp.updateGame(gameState);
				if (gameState.getCurrPlayer() == playerNum) {
					gameComp.play();
					try {
						while (!gameComp.doneWithTurn())
							sleep(10);
					} catch (InterruptedException iex) {
						iex.printStackTrace();
					}
					this.gameState = gameComp.getGameState();
					output.writeObject(gameState);
				}
				if (gameState.isEndOfGame()) {
					showGameEnd();
					break;
				}
			} catch (ClassNotFoundException cnfex) {
				cnfex.printStackTrace();
			} catch (IOException ioex) {
				ioex.printStackTrace();
			}
		}
	}

	private void showGameEnd() {
		JFrame frame = new JFrame();
		JLabel label = new JLabel("Score: " + getScore());
		frame.setLayout(new BorderLayout());
		frame.add(label, BorderLayout.CENTER);
		frame.setSize(new Dimension(100, 50));
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
	}

	private int getScore() {
		int[] playedCards = gameState.getPlayedCards();
		int score = 0;
		for (int i : playedCards)
			score += i;
		return score;
	}

	/**
	 * Accessor method for index of player.
	 * 
	 * @return this <tt>Player</tt>'s index
	 */
	public int getPlayerNum() {
		return playerNum;
	}
}