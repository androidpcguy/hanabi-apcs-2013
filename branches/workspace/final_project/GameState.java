package final_project;

import static final_project.CardColor.*;
import java.util.*;

public class GameState {

	private static final CardColor[] CARD_COLORS = { RED, PURPLE, BLUE, BLACK,
			GREEN, RAINBOW };

	private static final ArrayList<CardColor> COLORS = new ArrayList<CardColor>(
			Arrays.asList(CARD_COLORS));

	private ArrayList<List<Card>> hands;

	private Stack<Card> deck;

	private List<Card> discardPile;

	private int currPlayer;

	private static final int TOTAL_LIVES = 3, TOTAL_CLUES = 8;

	private int numClues, numLives;

	private int numCards;

	private boolean rainbow;

	private int numPlayers;

	public GameState(int numPlayers, boolean rainbow) {
		this.rainbow = rainbow;
		this.numPlayers = numPlayers;
		numCards = rainbow ? 60 : 50;
		createDeck();
		hands = new ArrayList<List<Card>>();
		numClues = TOTAL_CLUES;
		numLives = TOTAL_LIVES;

		discardPile = new ArrayList<Card>();
		currPlayer = (int) (Math.random() * numPlayers);

	}

	private void createDeck() {
		ArrayList<Card> cards = new ArrayList<Card>(numCards);
		for (int color = 0, i = 0; color < numCards / 10; color++) {
			cards.add(new Card(1, CARD_COLORS[color], i++));
			cards.add(new Card(1, CARD_COLORS[color], i++));
			cards.add(new Card(1, CARD_COLORS[color], i++));
			cards.add(new Card(2, CARD_COLORS[color], i++));
			cards.add(new Card(2, CARD_COLORS[color], i++));
			cards.add(new Card(3, CARD_COLORS[color], i++));
			cards.add(new Card(3, CARD_COLORS[color], i++));
			cards.add(new Card(4, CARD_COLORS[color], i++));
			cards.add(new Card(4, CARD_COLORS[color], i++));
			cards.add(new Card(5, CARD_COLORS[color], i++));
		}
		deck = new Stack<Card>();
		for (int i = 0; i < cards.size(); i++)
			deck.push(cards.get((int) (Math.random() * cards.size())));
	}

	public void dealCard(int playerNum) {
		hands.get(playerNum).add(deck.pop());
	}

	/**
	 * @param playerNum
	 * @param color
	 *            if color clue given, this is a valid color, else if number
	 *            clue is given this is <tt>null</tt>
	 * @param numOfCard
	 *            if number clue is given, 1 <= numOfCard <= 5. else, this is 0
	 */
	public void giveClue(int playerNum, CardColor color, int numOfCard) {
		if (color == null) {
			for (Card card : hands.get(playerNum))
				card.giveNumberClue(numOfCard);
		} else {
			for (Card card : hands.get(playerNum))
				card.giveColorClue(color);
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

	public int getNumPlayers() {
		return this.numPlayers;
	}

	public List<Card> getDiscardPile() {
		return discardPile;
	}

	public void updatePlayer() {
		currPlayer++;
	}
}