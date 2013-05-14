package final_project;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server {

	private ArrayList<Socket> clients;

	private GameState gameState;

	private ObjectOutputStream output;

	private ObjectInputStream input;

	public static void main(String[] args) {}

	private class ClientHandler extends Thread {

		private Socket client;

		public ClientHandler(Socket s) {
			client = s;
		}

		@Override
		public void run() {
			// TODO: do stuff here.
		}
	}
}