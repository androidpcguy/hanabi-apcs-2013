package final_project;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server extends Thread {

	private ArrayList<Socket> clients;

	private ServerSocket server;

	private GameState gameState;

	private ArrayList<ObjectOutputStream> output;

	private ArrayList<ObjectInputStream> input;

	public Server(GameState gameState, int port) {
		this.gameState = gameState;
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
		connect(gameState.getNumPlayers());
		while (allAreConnected()) {
			sendNewGameState();
			getNewGameState();
		}
	}

	public void sendNewGameState() {
		try {
			for (int x = 0; x < input.size(); x++) {
				output.get(x).writeObject(gameState);
			}
		} catch (IOException ioex) {
			ioex.printStackTrace();
		}
	}

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

	public boolean allAreConnected() {
		for (int x = 0; x < clients.size(); x++) {
			if (!clients.get(x).isConnected()) {
				System.out.println("false");
				return false;
			}
		}
		System.out.println("true");
		return true;
	}
}