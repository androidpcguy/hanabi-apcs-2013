package final_project;

import javax.swing.JFrame;

//shows menu before starting game for ip addresses and port number for clients
public class GameStarter extends JFrame {

	public void gameStart(int numPlayers, boolean rainbow, boolean serve,
			int port, String serverIP) {
		GameState gameState = new GameState(numPlayers, rainbow);
		if (serve) {
			Server server = new Server(gameState, port);
			server.start();
		}
		Player client = new Player(port, serverIP, gameState);
		client.start();
	}
}
