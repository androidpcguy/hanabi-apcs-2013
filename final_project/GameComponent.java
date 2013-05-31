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
	private static final Color CLICK_BG = new Color(0x40ff40);
	
	private static final Color PLAYED_COLOR = new Color(0x008000);
	private static final Color DISCARDED_COLOR = new Color(0xff0000);
	private static final Color UNSEEN_COLOR = new Color(0x404040);
	
	private static final Color MY_TURN_COLOR = new Color(0x008000);
	private static final Color OTHER_TURN_COLOR = new Color(0x000000);
	
	private static final Color REMAINING_COLOR = new Color(0x000000);
	private static final Color NO_MORE_COLOR = new Color(0xff0000);
	
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
	private static final Rectangle BOUNDS_DISCARD = new Rectangle(400, 225, 400, 300);
	
	private static final Font BIG_FONT = new Font(Font.MONOSPACED, Font.BOLD, 48);
	private static final Font SMALL_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 20);

	private GameState gameState;

	private int playerNum;

	private boolean myTurn = false;
	private static final Object TURN_LOCK = new Object();
	
	private int mouseZone = ZONE_NONE;
	private int mouseIndex = 0;
	
	private int clickZone;
	private int clickIndex;

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
		
		resetClick();
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
		Toolkit.getDefaultToolkit().beep();
	}

	/**
	 * Returns whether or not this player has finished his or her turn.
	 * 
	 * @return true if done, false if still playing
	 */
	public boolean doneWithTurn() {
		synchronized (TURN_LOCK) {
			if(!myTurn)
				gameState.updatePlayer();
			return !myTurn;
		}
	}

	@Override
	public void paint(Graphics g) {
		if (gameState == null) {
			return;
		}

		paintHands(g, BOUNDS_HAND1.x, BOUNDS_HAND1.y);
		paintClueArea(g, BOUNDS_CLUE.x, BOUNDS_CLUE.y);
		paintPlayArea(g, BOUNDS_PLAY.x, BOUNDS_PLAY.y);
		paintDiscardArea(g, BOUNDS_DISCARD.x, BOUNDS_DISCARD.y);
		paintInfoArea(g, 400, 525);
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
		} else if (index == clickZone - ZONE_HAND1) {
			g.setColor(CLICK_BG);
		} else {
			g.setColor(HAND_BG);
		}
		g.fillRect(x, y, BOUNDS_HAND1.width, BOUNDS_HAND1.height);
		
		for (int i = 0; i < hand.size(); i++) {
			if (index == playerNum && i == mouseIndex &&
					index == mouseZone - ZONE_HAND1) {
				g.setColor(HIGHLIGHT_BG);
				g.fillRect(x + i*80, y, 80, 120);
			} else if (index == playerNum && i == clickIndex &&
					index == clickZone - ZONE_HAND1) {
				g.setColor(CLICK_BG);
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
		
		if (clickZone == ZONE_CLUE) {
			int tilex = clickIndex % 5;
			int tiley = clickIndex / 5;
			g.setColor(CLICK_BG);
			g.fillRect(x + tilex*80, y + tiley*75, 80, 75);
		}
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
		Image playImages = ImageLoader.getImage(ImageLoader.PLAY_AREA_IMAGES);
		Dimension tilesize = ImageLoader.getTileSize(ImageLoader.PLAY_AREA_IMAGES);
		
		int[] playedCards = gameState.getPlayedCards();
		
		if (mouseZone == ZONE_PLAY) {
			g.setColor(HIGHLIGHT_BG);
		} else if (clickZone == ZONE_PLAY) {
			g.setColor(CLICK_BG);
		} else {
			g.setColor(NORMAL_BG);
		}
		g.fillRect(x, y, BOUNDS_PLAY.width, BOUNDS_PLAY.height);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, BOUNDS_PLAY.width, BOUNDS_PLAY.height);
		
		for (int tilex = 0; tilex < playedCards.length; tilex++) {
			if (playedCards[tilex] > 0) {
				int tiley = playedCards[tilex] - 1;
				g.drawImage(playImages,
					x + tilex*tilesize.width, y,
					x + (tilex+1)*tilesize.width, y + tilesize.height,
					tilex*tilesize.width, tiley*tilesize.height,
					(tilex+1)*tilesize.width, (tiley+1)*tilesize.height,
					null);
			}
		}
	}
	
	private void paintDiscardArea (Graphics g, int x, int y) {
		Image discardImage = ImageLoader.getImage(ImageLoader.DISCARD_AREA);
		
		if (mouseZone == ZONE_DISCARD) {
			g.setColor(HIGHLIGHT_BG);
		} else if (clickZone == ZONE_DISCARD) {
			g.setColor(CLICK_BG);
		} else {
			g.setColor(NORMAL_BG);
		}
		g.fillRect(x, y, BOUNDS_DISCARD.width, BOUNDS_DISCARD.height);
		g.setColor(Color.BLACK);
		g.drawRect(x, y, BOUNDS_DISCARD.width, BOUNDS_DISCARD.height);
		
		g.drawImage(discardImage,
			x, y, x + BOUNDS_DISCARD.width, y + BOUNDS_DISCARD.height,
			0, 0, 400, 300,
			null);
		
		// fill in each square
		boolean[] seen = new boolean[60]; // all false
		
		// played cards
		g.setColor(PLAYED_COLOR);
		int[] playedCards = gameState.getPlayedCards();
		for (int i = 0; i < playedCards.length; i++) {
			for (int j = 1; j <= playedCards[i]; j++) {
				int index = getIndexForCard(seen, CardColor.values()[i], j);
				paintSquare(g, x, y, index);
			}
		}
		
		// discarded cards
		g.setColor(DISCARDED_COLOR);
		List<Card> discardPile = gameState.getDiscardPile();
		for (Card c : discardPile) {
			int index = getIndexForCard(seen, c.getColor(), c.getNumber());
			paintSquare(g, x, y, index);
		}
		
		// all other cards
		g.setColor(UNSEEN_COLOR);
		for (int i = 0; i < seen.length; i++) {
			if (!seen[i]) {
				paintSquare(g, x, y, i);
			}
		}
	}
	
	private void paintInfoArea (Graphics g, int x, int y) {
		Image infoImage = ImageLoader.getImage(ImageLoader.INFO_AREA);
		Dimension imageSize = ImageLoader.getTileSize(ImageLoader.INFO_AREA);
		
		g.drawImage(infoImage,
			x, y, x + imageSize.width, y + imageSize.height,
			0, 0, imageSize.width, imageSize.height,
			null);
		
		g.setFont(BIG_FONT);
		
		// current player
		int currPlayer = gameState.getCurrPlayer();
		g.setColor(currPlayer == playerNum ? MY_TURN_COLOR : OTHER_TURN_COLOR);
		g.drawString("" + (currPlayer+1), x + 10, y + 65);
		
		// clues
		int clues = gameState.getNumClues();
		g.setColor(clues == 0 ? NO_MORE_COLOR : REMAINING_COLOR);
		g.drawString("" + clues, x + 55, y + 65);
		
		// lives
		int lives = gameState.getNumLives();
		g.setColor(lives == 0 ? NO_MORE_COLOR : REMAINING_COLOR);
		g.drawString("" + lives, x + 100, y + 65);
		
		// last move
		String[] lastMove = gameState.getLastMove();
		g.setColor(Color.BLACK);
		g.setFont(SMALL_FONT);
		g.drawString(lastMove[0], x + 145, y + 35);
		g.drawString(lastMove[1], x + 145, y + 65);
	}
	
	private int getIndexForCard (boolean[] seen, CardColor color, int number) {
		int ans = color.getIndex() * 10;
		switch (number) {
			case 1:
				ans += 0;
				break;
			case 2:
				ans += 3;
				break;
			case 3:
				ans += 5;
				break;
			case 4:
				ans += 7;
				break;
			case 5:
				ans += 9;
				break;
		}
		
		while (seen[ans]) {
			ans++;
		}
		
		seen[ans] = true;
		return ans;
	}
	
	private void paintSquare (Graphics g, int bx, int by, int i) {
		int tilex = i % 10;
		int tiley = i / 10;
		g.fillRect(bx + 40 + 9 + 36*tilex, by + 30 + 13 + 45*tiley, 18, 19);
	}
	
	@Override
	public void mousePressed (MouseEvent e) {
		if (!myTurn) {
			return;
		}
		
		int x = e.getX();
		int y = e.getY();
		
		if (isInRect(x, y, BOUNDS_HAND1)) {
			if (clickZone == ZONE_CLUE) {
				giveClue(clickZone, clickIndex, ZONE_HAND1, x/80);
			} else {
				clickZone = ZONE_HAND1;
				clickIndex = x / 80;
			}
		} else if (isInRect(x, y, BOUNDS_HAND2)) {
			if (clickZone == ZONE_CLUE) {
				giveClue(clickZone, clickIndex, ZONE_HAND2, x/80);
			} else {
				clickZone = ZONE_HAND2;
				clickIndex = x / 80;
			}
		} else if (isInRect(x, y, BOUNDS_HAND3)) {
			if (clickZone == ZONE_CLUE) {
				giveClue(clickZone, clickIndex, ZONE_HAND3, x/80);
			} else {
				clickZone = ZONE_HAND3;
				clickIndex = x / 80;
			}
		} else if (isInRect(x, y, BOUNDS_HAND4)) {
			if (clickZone == ZONE_CLUE) {
				giveClue(clickZone, clickIndex, ZONE_HAND4, x/80);
			} else {
				clickZone = ZONE_HAND4;
				clickIndex = x / 80;
			}
		} else if (isInRect(x, y, BOUNDS_HAND5)) {
			if (clickZone == ZONE_CLUE) {
				giveClue(clickZone, clickIndex, ZONE_HAND5, x/80);
			} else {
				clickZone = ZONE_HAND5;
				clickIndex = x / 80;
			}
		} else if (isInRect(x, y, BOUNDS_CLUE)) {
			if ((clickZone == ZONE_HAND1 ||
			        clickZone == ZONE_HAND2 ||
			        clickZone == ZONE_HAND3 ||
			        clickZone == ZONE_HAND4 ||
			        clickZone == ZONE_HAND5) &&
			        playerNum != clickZone - ZONE_HAND1) {
				giveClue(clickZone, clickIndex, ZONE_CLUE, 5*(y/75) + (x-400)/80);
			} else {
				clickZone = ZONE_CLUE;
				clickIndex = 5 * (y/75) + (x-400) / 80;
			}
		} else if (isInRect(x, y, BOUNDS_PLAY)) {
			if (playerNum == clickZone - ZONE_HAND1) {
				synchronized (TURN_LOCK) {
					gameState.playCard(
						gameState.getHand(playerNum).remove(clickIndex));
					gameState.dealCard(playerNum);
					
					myTurn = false;
					resetClick();
				}
			} else {
				clickZone = ZONE_PLAY;
			}
		} else if (isInRect(x, y, BOUNDS_DISCARD)) {
			if (playerNum == clickZone - ZONE_HAND1) {
				synchronized (TURN_LOCK) {
					gameState.discardCard(
						gameState.getHand(playerNum).remove(clickIndex));
					if(!gameState.dealCard(playerNum) && gameState.getGameEndPlayer()==-1)
						gameState.setGameEndPlayer(playerNum);
					
					myTurn = false;
					resetClick();
				}
			} else {
				clickZone = ZONE_DISCARD;
			}
		} else {
			resetClick();
		}
	}
	
	private void giveClue (int z1, int i1, int z2, int i2) { 
		synchronized (TURN_LOCK) {
			if(gameState.getNumClues()==0)
				return;
			myTurn = false;
			
			int handIndex;
			int clueIndex;
			
			// find the right indices
			if (z1 == ZONE_CLUE) {
				clueIndex = i1;
				handIndex = z2 - ZONE_HAND1;
			} else {
				handIndex = z1 - ZONE_HAND1;
				clueIndex = i2;
			}
			
			// don't give clues to yourself
			if (handIndex == playerNum) {
				myTurn = true;
				return;
			}
			
			if (clueIndex < 5) {
				// color clue
				gameState.giveClue(handIndex, CardColor.values()[clueIndex], 0);
			} else {
				// number clue
				gameState.giveClue(handIndex, null, clueIndex - 4);
			}
			
			resetClick();
		}
	}
	
	@Override
	public void mouseEntered (MouseEvent e) {}

	@Override
	public void mouseExited (MouseEvent e) {}

	@Override
	public void mouseClicked (MouseEvent e) {}

	@Override
	public void mouseReleased (MouseEvent e) {}

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
	
	private void resetClick () {
		clickZone = ZONE_NONE;
		clickIndex = 0;
	}
}