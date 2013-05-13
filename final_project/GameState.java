package final_project;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GameState {

	private ArrayList<List<Card>> hands;

	private Stack<Card> deck;

	private List<Card> discardPile;

	private int currPlayer;

	private static final int TOTAL_LIVES = 3;

	private static final int TOTAL_CLUES = 8;

	private int numClues, numLives;

	private List<boolean[]> cardClues;

	private boolean rainbow;

	private int numPlayers;

	public GameState(int numPlayers, boolean rainbow) {
		this.rainbow = rainbow;
		this.numPlayers = numPlayers;
		cardClues = new ArrayList<boolean[]>();
		for (int i = 0, cards = (rainbow ? 60 : 50); i < cards; i++)
			cardClues.add(new boolean[11]);
		numClues = TOTAL_CLUES;
		numLives = TOTAL_LIVES;
		discardPile = new ArrayList<Card>();
		currPlayer = (int) (Math.random() * numPlayers);
	}

	public void dealCard(int playerNum) {
		hands.get(playerNum).add(deck.pop());
	}

	public void giveClue()
	{
		//TODO
	}
	public int getCurrPlayer() {
		return currPlayer;
	}
}