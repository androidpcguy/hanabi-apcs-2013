package final_project;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;
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
	 */
	public Player(int portNumber, String serverIP, GameState gameState) {
		this.gameState = gameState;
		gameComp = new GameComponent(playerNum, gameState);
		
		try {
			socket = new Socket(serverIP, portNumber);
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			this.playerNum = input.readInt();
			System.out.println("Player num " +  playerNum);
		} catch (IOException ioex) {
			System.out
					.println("connection failed: wrong port number or server ip address!");
			ioex.printStackTrace();
		}
		JFrame gameFrame = new JFrame("Hanabi: Player " + playerNum);
		gameFrame.add(gameComp);
		gameFrame.pack();
		gameFrame.setVisible(true);
		gameFrame.setResizable(false);
		gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
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
			} catch (ClassNotFoundException cnfex) {
				cnfex.printStackTrace();
			} catch (IOException ioex) {
				ioex.printStackTrace();
			}
		}
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