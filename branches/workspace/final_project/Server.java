package final_project;

import java.net.*;
import java.io.*;
import java.util.*;

/**
 * This is the server for the game. Sends and receives data to and from clients.
 * 
 * @author Akshara B, Albert H, Henry W
 * @version 1.0
 * 
 */
public class Server extends Thread {

	private ArrayList<Socket> clients;

	private ServerSocket server;

	private boolean debug;

	private GameState gameState;

	private ArrayList<ObjectOutputStream> output;

	private ArrayList<ObjectInputStream> input;

	
	/**
	 * Constructs a new <tt>Server</tt> and instantiates all fields to inital
	 * states. Also starts the <tt>ServerSocket</tt> to listen to incoming
	 * connections.
	 * 
	 * @param gameState
	 *            initial <tt>GameState</tt>
	 * @param port
	 *            port number to open server on
	 * @param debug
	 *            true for debug mode, false otherwise (only for testing)
	 */
	public Server(GameState gameState, int port, boolean debug) {
		this.gameState = gameState;
		this.debug = debug;
		this.clients = new ArrayList<Socket>();
		this.output = new ArrayList<ObjectOutputStream>();
		this.input = new ArrayList<ObjectInputStream>();
		try {
			server = new ServerSocket(port);
		} catch (IOException ioex) {
			System.out
					.println("connection failed: wrong port number or server ip address!");
			ioex.printStackTrace();
		}
	}

	@Override
	public void run() {
		if (!debug) {
			connect(gameState.getNumPlayers());
			while (allAreConnected()) {
				sendNewGameState();
				getNewGameState();
			}
		}
	}

	/**
	 * Sends updated game state to each Player.
	 */
	public void sendNewGameState() {
		try {
			for (int x = 0; x < input.size(); x++) {
				output.get(x).writeObject(gameState);
			}
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}

	/**
	 * Receives new game state from player whose turn finished last.
	 */
	public void getNewGameState() {
		try {
			gameState = (GameState) input.get(gameState.getCurrPlayer())
					.readObject();
		} catch (IOException ioex) {
			ioex.printStackTrace();
		} catch (ClassNotFoundException cnfex) {
			cnfex.printStackTrace();
		}
	}

	/**
	 * Waits for all players to join and connect to server and adds output and
	 * input streams for communication.
	 * 
	 * @param numConnections
	 *            number of players
	 */
	public void connect(int numConnections) {
		try {
			for (int x = 0; x < numConnections; x++) {
				Socket client = server.accept();
				clients.add(client);
				output.add(new ObjectOutputStream(client.getOutputStream()));
				input.add(new ObjectInputStream(client.getInputStream()));
				output.get(x).writeInt(x);
			}
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}

	/**
	 * Determines whether all players have connected to the server.
	 * 
	 * @return true if all players connected, false if otherwise
	 */
	public boolean allAreConnected() {
		for (int x = 0; x < clients.size(); x++) {
			if (!clients.get(x).isConnected()) {
				return false;
			}
		}
		return true;
	}

	// methods for testing

	public ArrayList<ObjectOutputStream> getOutput() {
		return output;
	}

	public ArrayList<ObjectInputStream> getInput() {
		return input;
	}

	public int getPort() {
		return server.getLocalPort();
	}

	public ServerSocket getServer() {
		return server;
	}

	public ArrayList<Socket> getClients() {
		return clients;
	}

	public GameState getGamestate() {
		return gameState;
	}
}