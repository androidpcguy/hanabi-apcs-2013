package final_project;

import static final_project.CardColor.*;
import java.util.*;
import java.io.Serializable;

public class GameState implements Serializable {

	private static final CardColor[] CARD_COLORS = { RED, PURPLE, BLUE, BLACK,
			GREEN, RAINBOW };

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
		dealInitialHands(numPlayers);
		numClues = TOTAL_CLUES;
		numLives = TOTAL_LIVES;

		discardPile = new ArrayList<Card>();
		currPlayer = (int) (Math.random() * numPlayers);
	}

	private void createDeck() {
		ArrayList<Card> cards = new ArrayList<Card>(numCards);
		for (int color = 0; color < numCards / 10; color++) {
			cards.add(new Card(1, CARD_COLORS[color]));
			cards.add(new Card(1, CARD_COLORS[color]));
			cards.add(new Card(1, CARD_COLORS[color]));
			cards.add(new Card(2, CARD_COLORS[color]));
			cards.add(new Card(2, CARD_COLORS[color]));
			cards.add(new Card(3, CARD_COLORS[color]));
			cards.add(new Card(3, CARD_COLORS[color]));
			cards.add(new Card(4, CARD_COLORS[color]));
			cards.add(new Card(4, CARD_COLORS[color]));
			cards.add(new Card(5, CARD_COLORS[color]));
		}
		deck = new Stack<Card>();
		for (int i = 0; i < numCards; i++){
			deck.push(cards.remove((int) (Math.random() * cards.size())));
		}
	}

	public void dealCard(int playerNum) {
		hands.get(playerNum).add(deck.pop());
	}

	public void dealInitialHands(int numPlayers) {
		for (int i = 0; i < numPlayers; i++)
			hands.add(new ArrayList<Card>());
		if (numPlayers < 4) {
			for (int player = 0; player < numPlayers; player++)
				for (int numCards = 0; numCards < 5; numCards++)
					dealCard(player);
		} else
			for (int player = 0; player < numPlayers; player++)
				for (int numCards = 0; numCards < 4; numCards++)
					dealCard(player);
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
		currPlayer = (currPlayer + 1)  % numPlayers;
	}
	
	public Stack<Card> getDeck() {
		return deck;
	}
	
	public boolean hasRainbow () {
		return rainbow;
	}
}