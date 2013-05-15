package final_project;

import javax.swing.*;

//this will be a jframe one per player
public class GameComponent extends JFrame {

	private GameState gameState;

	public GameComponent(GameState gameState) {
		this.gameState = gameState;
		redraw();
	}

	public GameState getGameState() {
		return gameState;
	}

	public void updateGame(GameState gameState) {
		this.gameState = gameState;
		// TODO: redraw new gameState
	}

	public void play() {
		// TODO: let the character do something and updates gameState, also need
		// to redraw after, remember to gameState.currPlayer++
	}

	public void redraw() {
		// TODO: draw screen with info from gameState
	}
}