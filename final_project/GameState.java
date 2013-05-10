package final_project;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class GameState {
	private ArrayList<List<Card>> hands;
	private ArrayList<Card> deck;
	private List<Card> discardPile;
	private int currPlayer;
	private int numClues, numLives;
	private List<boolean[]> cardClues;
}