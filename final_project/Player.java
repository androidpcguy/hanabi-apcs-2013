package final_project;

import java.io.*;
import java.net.*;

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
		gameComp = new GameComponent(gameState);
		try {
			socket = new Socket(serverIP, portNumber);
			input = new ObjectInputStream(socket.getInputStream());
			output = new ObjectOutputStream(socket.getOutputStream());
			this.playerNum = input.readInt();
		} catch (IOException ioex) {
			System.out
					.println("connection failed: wrong port number or server ip address!");
			ioex.printStackTrace();
		}
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