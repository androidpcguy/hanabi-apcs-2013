package final_project;

import java.util.ArrayList;
import static final_project.CardColor.*;
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

	ArrayList<boolean[]> cardClues;

	private boolean rainbow;

	private int numPlayers;

	public GameState(int numPlayers, boolean rainbow) {
		this.rainbow = rainbow;
		this.numPlayers = numPlayers;
		cardClues = new ArrayList<boolean[]>();
		for (int i = 0, cards = rainbow ? 60 : 50; i < cards; i++) {
			cardClues.add(new boolean[11]);
		}
		createDeck();
		hands = new ArrayList<List<Card>>();
		numClues = TOTAL_CLUES;
		numLives = TOTAL_LIVES;

		discardPile = new ArrayList<Card>();
		currPlayer = (int) (Math.random() * numPlayers);
	}

	private void createDeck() {
		CardColor[] colors = { RED, PURPLE, BLUE, BLACK, GREEN, RAINBOW };
		ArrayList<Card> cards = new ArrayList<Card>(cardClues.size());
		for (int color = 0; color < cardClues.size() / 10; color++) {
			cards.add(new Card(1, colors[color]));
			cards.add(new Card(1, colors[color]));
			cards.add(new Card(1, colors[color]));
			cards.add(new Card(2, colors[color]));
			cards.add(new Card(2, colors[color]));
			cards.add(new Card(3, colors[color]));
			cards.add(new Card(3, colors[color]));
			cards.add(new Card(4, colors[color]));
			cards.add(new Card(4, colors[color]));
			cards.add(new Card(5, colors[color]));
		}
		deck = new Stack<Card>();
		for (int i = 0; i < cards.size(); i++)
			deck.push(cards.get((int) (Math.random() * cards.size())));
	}

	public void dealCard(int playerNum) {
		hands.get(playerNum).add(deck.pop());
	}

	/**
	 * @param to
	 * @param cards
	 * @param color
	 * @param numOfCard
	 */
	public void giveClue(Player to, List<Card> cards, CardColor color,
			int numOfCard) {
		int playerNum = to.getPlayerNum();
		//TODO: think about this. don't edit yet
		if (color != null)
			for (Card card : cards) {
			}
	}

	public int getCurrPlayer() {
		return currPlayer;
	}

	public int getNumLives() {
		return numLives;
	}

	public int getNumClues() {
		return numClues;
	}

	public List<Card> getHand(int numPlayer) {
		return hands.get(numPlayer);
	}

	public static void main(String... args) {
		GameState g = new GameState(3, false);
		for (Card c : g.deck)
			System.out.println(c);
	}
}