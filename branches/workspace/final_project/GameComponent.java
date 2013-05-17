package final_project;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.*;

public class GameComponent extends JComponent implements MouseListener {

	private static final Color HAND_BG = new Color(0xffff80);

	private static final Color THIS_HAND_BG = new Color(0x6060ff);

	private GameState gameState;

	private int playerNum;

	private boolean myTurn = false;

	public GameComponent(int playerNum, GameState gameState) {
		this.gameState = gameState;
		this.playerNum = playerNum;
		this.setPreferredSize(new Dimension(800, 600));
	}

	public GameState getGameState() {
		return gameState;
	}

	public void updateGame(GameState gameState) {
		this.gameState = gameState;
		repaint();
	}

	public void play() {
		myTurn = true;
		// TODO: let the character do something and updates gameState, also need
		// to redraw after, remember to gameState.currPlayer++
		gameState.updatePlayer();
	}

	public boolean doneWithTurn() {
		return !myTurn;
	}

	@Override
	public void paint(Graphics g) {
		// TODO paint
		if (gameState == null) {
			return;
		}

		paintHands(g, 0, 0);
	}

	private void paintHands(Graphics g, int x, int y) {
		for (int i = 0; i < gameState.getNumPlayers(); i++) {
			// fill rectangle with hand background color
			g.setColor(i == playerNum ? THIS_HAND_BG : HAND_BG);
			g.fillRect(x, y + i * 120, 400, 120);

			// paint the hand
			paintHand(g, gameState.getHand(i), x, y + i * 120, i != playerNum);

			// put a border around it
			g.setColor(Color.BLACK);
			g.drawRect(x, y + i * 120, 400, 120);
		}
	}

	private void paintHand(Graphics g, List<Card> hand, int x, int y,
			boolean known) {
		for (int i = 0; i < hand.size(); i++) {
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

		g.drawImage(cardImages, x, y, x + cardSize.width, y + cardSize.height,
				tilex * cardSize.width, tiley * cardSize.height, (tilex + 1)
						* cardSize.width, (tiley + 1) * cardSize.height, null);

		// draw the number clues
		boolean[] numberClues = card.getNumberClues();
		tiley = 1;

		for (int i = 0; i < numberClues.length; i++) {
			if (numberClues[i]) {
				g.drawImage(clueImages, x, y, x + cardSize.width, y
						+ cardSize.height, i * cardSize.width, tiley
						* cardSize.height, (i + 1) * cardSize.width,
						(tiley + 1) * cardSize.height, null);
			}
		}

		// draw the color clues
		boolean[] colorClues = card.getColorClues();
		boolean drawn = false;
		tiley = 0;

		for (int i = 0; i < colorClues.length - 1; i++) {
			if (colorClues[i]) {
				drawn = true;
				g.drawImage(clueImages, x, y, x + cardSize.width, y
						+ cardSize.height, i * cardSize.width, tiley
						* cardSize.height, (i + 1) * cardSize.width,
						(tiley + 1) * cardSize.height, null);
			}
		}
		if (!drawn) { // then known rainbow
			g.drawImage(clueImages, x, y, x + cardSize.width, y
					+ cardSize.height, 5 * cardSize.width, tiley
					* cardSize.height, 6 * cardSize.width, (tiley + 1)
					* cardSize.height, null);
		}
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {}

	@Override
	public void mouseEntered(MouseEvent arg0) {}

	@Override
	public void mouseExited(MouseEvent arg0) {}

	@Override
	public void mousePressed(MouseEvent arg0) {}

	@Override
	public void mouseReleased(MouseEvent arg0) {}
}