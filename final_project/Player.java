package final_project;

import java.io.*;
import java.net.*;

public class Player {

	private ObjectInputStream inputStream;

	private ObjectOutputStream outputStream;

	private Socket socket;

	private GameComponent gameComp;

	private int playerNum;

	public Player(int playerNum, int portNumber, String serverIP) {
		try {
			socket = new Socket(serverIP, portNumber);
		} catch (IOException ioex) {
			System.out.println("connection failed: wrong port number or server ip address!");
			ioex.printStackTrace();
		}
	}

	public int getPlayerNum() {
		return playerNum;
	}
}