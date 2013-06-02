package final_project;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JButton;
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

	private int port;

	private String serverIP;

	private boolean runThread;

	private boolean debug;

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
		this.port = portNumber;
		this.serverIP = serverIP;
		this.runThread = true;
	}

	public void connect() {
		try {
			socket = new Socket(serverIP, port);
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			this.playerNum = input.readInt();
			System.out.println(playerNum);
		} catch (IOException ioex) {
			showErrorPanel();
			System.err
					.println("connection failed: wrong port number or server ip address!");
			ioex.printStackTrace();
		}
	}

	public void startGUI() {
		gameComp = new GameComponent(playerNum, gameState);

		JFrame gameFrame = new JFrame("Hanabi: Player " + (playerNum + 1));
		gameFrame.add(gameComp);
		gameFrame.pack();
		if (!debug)
			gameFrame.setVisible(true);
		gameFrame.setResizable(false);
		gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	private void showErrorPanel() {
		final JFrame frame = new JFrame();
		JOptionPane connFailed = new JOptionPane(
				"connection failed: wrong port number or server ip address!",
				JOptionPane.ERROR_MESSAGE);
		frame.add(connFailed);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(500, 150);
		((JButton) (((Container) connFailed.getComponent(1)).getComponent(0)))
			.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					frame.dispose();
				}
			});

	}

	@Override
	public void run() {
		connect();
		startGUI();
		while (socket != null && socket.isConnected() && runThread) {
			try {
				gameState = (GameState) input.readObject();
				gameComp.updateGame(gameState);
				if (gameState.isEndOfGame()) {
					showGameEnd();
					break;
				} else if (gameState.getCurrPlayer() == playerNum) {
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
		frame.setSize(new Dimension(150, 75));
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

	void endThread() {
		this.runThread = false;
	}

	public boolean connected() {
		return socket.isConnected();
	}
}