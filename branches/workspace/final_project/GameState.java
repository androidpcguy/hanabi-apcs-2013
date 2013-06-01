package final_project;

import static final_project.CardColor.*;
import java.util.*;
import java.io.Serializable;

public class GameState implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2642437239483764855L;

	private static final CardColor[] CARD_COLORS = { RED, PURPLE, BLUE, BLACK,
			GREEN, RAINBOW };

	private ArrayList<List<Card>> hands;

	private Stack<Card> deck;

	private List<Card> discardPile;

	private int[] playedCards;

	private int currPlayer;

	private static final int TOTAL_LIVES = 3, TOTAL_CLUES = 8;

	private int numClues, numLives;

	private int numCards;

	private boolean rainbow;

	private int numPlayers;

	private int cardsPerHand;

	private int numPoints;

	private int gameEndPlayer;

	private String[] lastMove = { "No moves yet.", "" };

	/**
	 * @param numPlayers
	 * @param rainbow
	 */
	public GameState(int numPlayers, boolean rainbow) {
		this.rainbow = rainbow;
		this.numPlayers = numPlayers;
		numPoints = 0;
		numCards = rainbow ? 60 : 50;
		createDeck();
		hands = new ArrayList<List<Card>>();
		dealInitialHands(numPlayers);
		numClues = TOTAL_CLUES;
		numLives = TOTAL_LIVES;
		playedCards = new int[6];

		discardPile = new ArrayList<Card>();
		currPlayer = (int) (Math.random() * numPlayers);
		gameEndPlayer = -1;
	}

	/**
	 * Creates the deck TODO
	 */
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
		for (int i = 0; i < numCards; i++) {
			deck.push(cards.remove((int) (Math.random() * cards.size())));
		}
	}

	/**
	 * Deals cards to all the players.
	 * 
	 * @param numPlayers
	 *            number of players
	 */
	public void dealInitialHands(int numPlayers) {
		for (int i = 0; i < numPlayers; i++)
			hands.add(new ArrayList<Card>());
		cardsPerHand = numPlayers < 4 ? 5 : 4;
		for (int player = 0; player < numPlayers; player++)
			for (int numCards = 0; numCards < cardsPerHand; numCards++)
				dealCard(player);
	}

	/**
	 * Deals a card to a given player given by the index.
	 * 
	 * @param playerNum
	 *            player to deal cards to
	 * @return true if deck not empty, false otherwise
	 */
	public boolean dealCard(int playerNum) {
		if (!deck.isEmpty()) {
			hands.get(playerNum).add(deck.pop());
		}
		return !deck.isEmpty();
	}

	/**
	 * Gives a clue to a player.
	 * 
	 * @param playerNum
	 *            which player receives the clue
	 * @param color
	 *            if color clue given, this is a valid color, else if number
	 *            clue is given this is <tt>null</tt>
	 * @param numOfCard
	 *            if number clue is given, 1 <= numOfCard <= 5. else, this is 0
	 */
	public void giveClue(int playerNum, CardColor color, int numOfCard) {
		lastMove[0] = "Player " + (playerNum + 1) + " received clue";
		if (color == null) {
			for (Card card : hands.get(playerNum))
				card.giveNumberClue(numOfCard);
			lastMove[1] = "\"" + numOfCard + "\"";
		} else {
			for (Card card : hands.get(playerNum))
				card.giveColorClue(color);
			lastMove[1] = "\"" + color + "\"";
		}
		numClues--;
	}

	/**
	 * Plays the given card. If successful, plays it, else added to discard pile
	 * and one life lost.
	 * 
	 * @param card
	 *            card to play
	 */
	public void playCard(Card card) {
		int index = card.getColor().getIndex();
		if (card.getNumber() == playedCards[index] + 1) {
			playedCards[index]++;
			lastMove[0] = "Successfully played";
			lastMove[1] = card.toString();
			if (card.getNumber() == 5 && numClues != 8)
				numClues++;
		} else {
			discardPile.add(card);
			numLives--;
			lastMove[0] = "Failed to play";
			lastMove[1] = card.toString();
		}
	}

	/**
	 * Discards the given card
	 * 
	 * @param card
	 *            card to discard
	 */
	public void discardCard(Card card) {
		discardPile.add(card);
		if (numClues < 8) {
			numClues++;
		}
		lastMove[0] = "Discarded";
		lastMove[1] = card.toString();
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

	public int[] getPlayedCards() {
		return playedCards;
	}

	public void updatePlayer() {
		currPlayer = (currPlayer + 1) % numPlayers;
	}

	public Stack<Card> getDeck() {
		return deck;
	}

	public boolean hasRainbow() {
		return rainbow;
	}

	public int getNumPoints() {
		return numPoints;
	}

	public String[] getLastMove() {
		return lastMove;
	}

	public int getGameEndPlayer() {
		return gameEndPlayer;
	}

	public void setGameEndPlayer(int newPlayer) {
		gameEndPlayer = newPlayer;
	}

	/**
	 * Determines if it is the end of the game.
	 * 
	 * @return true if no lives left or <tt>currPlayer == gameEndPlayer</tt>
	 */
	public boolean isEndOfGame() {
		/*
		 * if (numLives == 0) return true; if (currPlayer == gameEndPlayer)
		 * return true; return false;
		 */
		return numLives == 0 || currPlayer == gameEndPlayer;
	}
}