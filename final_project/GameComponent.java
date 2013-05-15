package final_project;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;

//this will be a jframe one per player
public class GameComponent extends JComponent implements MouseListener {

	private GameState gameState;

	private int playerNum;
	
	private boolean myTurn = false;

	public GameComponent(int playerNum, GameState gameState) {
		this.gameState = gameState;
		this.playerNum = playerNum;
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
	public void paint (Graphics g) {
		//TODO paint
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