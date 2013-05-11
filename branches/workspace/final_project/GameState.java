package final_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameState {

	private ArrayList<List<Card>> hands;

	private Stack<Card> deck;

	private List<Card> discardPile;

	private int currPlayer;

	private int numClues, numLives;

	private List<boolean[]> cardClues;

	public void dealCard(int playerNum) {
		hands.get(playerNum).add(deck.pop());
	}
}