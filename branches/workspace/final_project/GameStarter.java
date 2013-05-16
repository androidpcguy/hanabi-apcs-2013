package final_project;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;;

//shows menu before starting game for ip addresses and port number for clients
/**
 * Is a simple GUI for starting and joining games.
 */
public class GameStarter extends JFrame implements ActionListener {
	
	private static final String CMD_NEW_GAME = "n";
	private static final String CMD_JOIN_GAME = "j";
	
	private JTextField numPlayersField;
	private JCheckBox rainbowCheckBox;
	private JTextField serverIPField;
	private JTextField portNumberField;
	
	private JButton newGameButton, joinGameButton;
	
	/**
	 * Creates a new GameStarter/
	 */
	public GameStarter () {
		super("Hanabi Game Manager");
		createGUI();
		
		this.setSize(300, 200);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	private void createGUI () {
		JPanel everythingPanel = new JPanel();
		everythingPanel.setLayout(
			new BoxLayout(everythingPanel, BoxLayout.Y_AXIS));
		
		everythingPanel.add(Box.createVerticalGlue());
		
		// number of players
		JPanel numPlayersPanel = new JPanel();
		numPlayersField = new JTextField("5");
		numPlayersField.setColumns(2);
		
		numPlayersPanel.add(new JLabel("Number of players: "));
		numPlayersPanel.add(numPlayersField);
		
		everythingPanel.add(numPlayersPanel);
		everythingPanel.add(Box.createVerticalGlue());
		
		// rainbow check box
		JPanel rainbowPanel = new JPanel();
		rainbowCheckBox = new JCheckBox("Play with rainbow cards", false);
		
		rainbowPanel.add(rainbowCheckBox);
		
		everythingPanel.add(rainbowPanel);
		everythingPanel.add(Box.createVerticalGlue());
		
		// server ip field
		JPanel ipPanel = new JPanel();
		serverIPField = new JTextField();
		serverIPField.setColumns(17);
		
		ipPanel.add(new JLabel("Server IP: "));
		ipPanel.add(serverIPField);
		
		everythingPanel.add(ipPanel);
		everythingPanel.add(Box.createVerticalGlue());
		
		// port number field
		JPanel portPanel = new JPanel();
		portNumberField = new JTextField();
		portNumberField.setColumns(4);
		
		portPanel.add(new JLabel("Port number: "));
		portPanel.add(portNumberField);
		
		everythingPanel.add(portPanel);
		everythingPanel.add(Box.createVerticalGlue());
		
		// buttons
		JPanel buttonPanel = new JPanel();
		
		newGameButton = new JButton("Start Game");
		newGameButton.setActionCommand(CMD_NEW_GAME);
		newGameButton.addActionListener(this);
		
		joinGameButton = new JButton("Join Game");
		joinGameButton.setActionCommand(CMD_JOIN_GAME);
		joinGameButton.addActionListener(this);
		
		buttonPanel.add(newGameButton);
		buttonPanel.add(joinGameButton);
		
		everythingPanel.add(buttonPanel);
		everythingPanel.add(Box.createVerticalGlue());
		
		this.add(everythingPanel);
		this.pack();
	}
	
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

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new GameStarter();
			}
		});
	}

	@Override
	public void actionPerformed (ActionEvent e) {
		try {
			String cmd = e.getActionCommand();
			
			boolean startGame = CMD_NEW_GAME.equals(cmd);
			
			int numPlayers =
				limit(2, Integer.parseInt(numPlayersField.getText()), 5);
			
			boolean rainbow = rainbowCheckBox.isSelected();
			
			int port = Integer.parseInt(portNumberField.getText());
			
			String serverIP = serverIPField.getText();
			
			gameStart(numPlayers, rainbow, startGame, port, serverIP);
		} catch (Exception ex) {}
	}
	
	private int limit (int min, int val, int max) {
		return Math.min(max, Math.max(val, min));
	}
}
