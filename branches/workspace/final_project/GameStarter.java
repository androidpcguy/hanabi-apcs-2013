package final_project;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

//shows menu before starting game for ip addresses and port number for clients
public class GameStarter extends JFrame {

	public void gameStart(int numPlayers, boolean rainbow, boolean startGame,
			int port, String serverIP) {
		GameState gameState = new GameState(numPlayers, rainbow);
		if (startGame) {
			Server server = new Server(gameState, port);
			server.start();
		}
		Player client = new Player(port, serverIP, gameState);
		client.start();
	}

	// TODO: need gui

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new GameStarter();
			}
		});
	}
}
