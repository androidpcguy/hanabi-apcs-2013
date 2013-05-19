package final_project;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.*;

/**
 * Knows how to draw a GameState. Each client has one of these.
 */
public class GameComponent extends JComponent implements MouseListener,
                                                         MouseMotionListener {

	private static final Color HAND_BG = new Color(0x0000ff);
	private static final Color THIS_HAND_BG = new Color(0xff0000);
	
	private static final Color NORMAL_BG = new Color(0xffffff);
	private static final Color HIGHLIGHT_BG = new Color(0xd0d000);
	
	private static final int ZONE_NONE = -1;
	private static final int ZONE_HAND1 = 0;
	private static final int ZONE_HAND2 = 1;
	private static final int ZONE_HAND3 = 2;
	private static final int ZONE_HAND4 = 3;
	private static final int ZONE_HAND5 = 4;
	private static final int ZONE_CLUE = 5;
	private static final int ZONE_PLAY = 6;
	private static final int ZONE_DISCARD = 7;
	
	private static final Rectangle BOUNDS_HAND1 = new Rectangle(0, 0, 400, 120);
	private static final Rectangle BOUNDS_HAND2 = new Rectangle(0, 120, 400, 120);
	private static final Rectangle BOUNDS_HAND3 = new Rectangle(0, 240, 400, 120);
	private static final Rectangle BOUNDS_HAND4 = new Rectangle(0, 360, 400, 120);
	private static final Rectangle BOUNDS_HAND5 = new Rectangle(0, 480, 400, 120);
	private static final Rectangle BOUNDS_CLUE = new Rectangle(400, 0, 400, 150);
	private static final Rectangle BOUNDS_PLAY = new Rectangle(400, 150, 400, 75);
	private static final Rectangle BOUNDS_DISCARD = new Rectangle(400, 225, 400, 350);

	private GameState gameState;

	private int playerNum;

	private boolean myTurn = false;
	
	private int mouseZone = ZONE_NONE;
	private int mouseIndex = 0;

	/**
	 * Creates a new GameComponent.
	 * 
	 * @param playerNum
	 *            which player this component is for
	 * @param gameState
	 *            the default game state; this can be null
	 */
	public GameComponent(int playerNum, GameState gameState) {
		this.gameState = gameState;
		this.playerNum = playerNum;
		this.setPreferredSize(new Dimension(800, 600));
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
	}

	/**
	 * Returns the current GameState.
	 * 
	 * @return the game state
	 */
	public GameState getGameState() {
		return gameState;
	}

	/**
	 * Updates the current GameState.
	 * 
	 * @param gameState the new game state
	 */
	public void updateGame(GameState gameState) {
		this.gameState = gameState;
		repaint();
	}

	/**
	 * Tells this component that it is this player's turn.
	 */
	public void play() {
		myTurn = true;
		gameState.updatePlayer();
	}

	/**
	 * Returns whether or not this player has finished his or her turn.
	 * 
	 * @return true if done, false if still playing
	 */
	public boolean doneWithTurn() {
		return !myTurn;
	}

	@Override
	public void paint(Graphics g) {
		// TODO paint
		if (gameState == null) {
			return;
		}

		paintHands(g, BOUNDS_HAND1.x, BOUNDS_HAND1.y);
		paintClueArea(g, BOUNDS_CLUE.x, BOUNDS_CLUE.y);
		paintPlayArea(g, BOUNDS_PLAY.x, BOUNDS_PLAY.y);
	}

	private void paintHands(Graphics g, int x, int y) {
		for (int i = 0; i < gameState.getNumPlayers(); i++) {
			// paint the hand
			paintHand(g, gameState.getHand(i), x, y + i * 120, i != playerNum, i);
		}
	}

	private void paintHand(Graphics g, List<Card> hand, int x, int y,
			boolean known, int index) {
		// fill rectangle with hand background color
		if (index == playerNum) {
			g.setColor(THIS_HAND_BG);
		} else if (index == mouseZone - ZONE_HAND1) {
			g.setColor(HIGHLIGHT_BG);
		} else {
			g.setColor(HAND_BG);
		}
		g.fillRect(x, y, BOUNDS_HAND1.width, BOUNDS_HAND1.height);
		
		for (int i = 0; i < hand.size(); i++) {
			if (index == playerNum && i == mouseIndex &&
					index == mouseZone - ZONE_HAND1) {
				g.setColor(HIGHLIGHT_BG);
				g.fillRect(x + i*80, y, 80, 120);
			}
			paintCard(g, hand.get(i), x + i * 80, y, known);
		}
	}

	private void paintCard(Graphics g, Card card, int x, int y, boolean known) {
		Image cardImages = ImageLoader.getImage(ImageLoader.CARD_IMAGES);
		Image clueImages = ImageLoader.getImage(ImageLoader.CLUE_IMAGES);

		Dimension cardSize = ImageLoader.getTileSize(ImageLoader.CARD_IMAGES);

		// draw the card background and big number
		int tilex, tiley;
		if (known) {
			tilex = card.getColor().getIndex();
			tiley = card.getNumber() - 1;
		} else {
			tilex = 6;
			tiley = 0;
		}

		g.drawImage(cardImages,
			x, y, x + cardSize.width, y + cardSize.height,
			tilex * cardSize.width, tiley * cardSize.height,
			(tilex + 1) * cardSize.width, (tiley + 1) * cardSize.height,
			null);

		// draw the number clues
		boolean[] numberClues = card.getNumberClues();
		tiley = 1;

		for (int i = 0; i < numberClues.length; i++) {
			if (numberClues[i]) {
				g.drawImage(clueImages,
					x, y, x + cardSize.width, y + cardSize.height,
					i * cardSize.width, tiley * cardSize.height,
					(i + 1) * cardSize.width, (tiley + 1) * cardSize.height,
					null);
			}
		}

		// draw the color clues
		boolean[] colorClues = card.getColorClues();
		boolean drawn = false;
		tiley = 0;

		for (int i = 0; i < colorClues.length - 1; i++) {
			if (colorClues[i]) {
				drawn = true;
				g.drawImage(clueImages,
					x, y, x + cardSize.width, y + cardSize.height,
					i * cardSize.width, tiley * cardSize.height,
					(i + 1) * cardSize.width, (tiley + 1) * cardSize.height,
					null);
			}
		}
		if (!drawn) { // then known rainbow
			g.drawImage(clueImages,
				x, y, x + cardSize.width, y + cardSize.height,
				5 * cardSize.width, tiley * cardSize.height,
				6 * cardSize.width, (tiley + 1) * cardSize.height,
				null);
		}
	}
	
	private void paintClueArea (Graphics g, int x, int y) {
		g.setColor(NORMAL_BG);
		g.fillRect(x, y, BOUNDS_CLUE.width, BOUNDS_CLUE.height);
		
		if (mouseZone == ZONE_CLUE) {
			int tilex = mouseIndex % 5;
			int tiley = mouseIndex / 5;
			g.setColor(HIGHLIGHT_BG);
			g.fillRect(x + tilex*80, y + tiley*75, 80, 75);
		}
		
		Image clueImage = ImageLoader.getImage(ImageLoader.CLUE_AREA);
		Dimension size = ImageLoader.getTileSize(ImageLoader.CLUE_AREA);
		g.drawImage(clueImage,
			x, y, x + size.width, y + size.height,
			0, 0, size.width, size.height,
			null);
	}
	
	private void paintPlayArea (Graphics g, int x, int y) {
		//TODO play area
		Image playImages = ImageLoader.getImage(ImageLoader.PLAY_AREA_IMAGES);
		Dimension tiles = ImageLoader.getTileCount(ImageLoader.PLAY_AREA_IMAGES);
		Dimension tilesize = ImageLoader.getTileSize(ImageLoader.PLAY_AREA_IMAGES);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		//TOOD mouse clicked
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseDragged (MouseEvent e) {}

	@Override
	public void mouseMoved (MouseEvent e) {
		int x = e.getX();
		int y = e.getY();
		
		boolean repaint = false;
		
		if (isInRect(x, y, BOUNDS_HAND1)) {
			if (mouseZone != ZONE_HAND1) {
				mouseZone = ZONE_HAND1;
				repaint = true;
			}
			int newMouseIndex = x / 80;
			if (mouseIndex != newMouseIndex) {
				mouseIndex = newMouseIndex;
				repaint = true;
			}
		} else if (isInRect(x, y, BOUNDS_HAND2)) {
			if (mouseZone != ZONE_HAND2) {
				mouseZone = ZONE_HAND2;
				repaint = true;
			}
			int newMouseIndex = x / 80;
			if (mouseIndex != newMouseIndex) {
				mouseIndex = newMouseIndex;
				repaint = true;
			}
		} else if (isInRect(x, y, BOUNDS_HAND3)) {
			if (mouseZone != ZONE_HAND3) {
				mouseZone = ZONE_HAND3;
				repaint = true;
			}
			int newMouseIndex = x / 80;
			if (mouseIndex != newMouseIndex) {
				mouseIndex = newMouseIndex;
				repaint = true;
			}
		} else if (isInRect(x, y, BOUNDS_HAND4)) {
			if (mouseZone != ZONE_HAND4) {
				mouseZone = ZONE_HAND4;
				repaint = true;
			}
			int newMouseIndex = x / 80;
			if (mouseIndex != newMouseIndex) {
				mouseIndex = newMouseIndex;
				repaint = true;
			}
		} else if (isInRect(x, y, BOUNDS_HAND5)) {
			if (mouseZone != ZONE_HAND5) {
				mouseZone = ZONE_HAND5;
				repaint = true;
			}
			int newMouseIndex = x / 80;
			if (mouseIndex != newMouseIndex) {
				mouseIndex = newMouseIndex;
				repaint = true;
			}
		} else if (isInRect(x, y, BOUNDS_CLUE)) {
			if (mouseZone != ZONE_CLUE) {
				mouseZone = ZONE_CLUE;
				repaint = true;
			}
			int newMouseIndex = 5 * (y/75) + (x-400) / 80;
			if (mouseIndex != newMouseIndex) {
				mouseIndex = newMouseIndex;
				repaint = true;
			}
		} else if (isInRect(x, y, BOUNDS_PLAY)) {
			if (mouseZone != ZONE_PLAY) {
				mouseZone = ZONE_PLAY;
				repaint = true;
			}
		} else if (isInRect(x, y, BOUNDS_DISCARD)) {
			if (mouseZone != ZONE_DISCARD) {
				mouseZone = ZONE_DISCARD;
				repaint = true;
			}
		} else {
			if (mouseZone != ZONE_NONE) {
				mouseZone = ZONE_NONE;
				repaint = true;
			}
		}
		
		if (repaint) {
			repaint();
		}
	}
	
	private boolean isInRect (int x, int y, Rectangle rect) {
		return x >= rect.x && x < rect.x + rect.width &&
		       y >= rect.y && y < rect.y + rect.height;
	}
}