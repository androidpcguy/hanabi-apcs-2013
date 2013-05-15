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

	private static final int TOTAL_LIVES = 3, TOTAL_CLUES = 8,
			NUMBER_OF_POSSIBLE_CLUES = 11;

	private int numClues, numLives;

	/**
	 * List holding all the clues for the cards. Boolean array: indices 0-4:
	 * number clues, 5-10: color clues
	 */
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
		ArrayList<Card> cards = new ArrayList<Card>(cardClues.size());
		for (int color = 0, i = 0; color < cardClues.size() / 10; color++) {
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
	 * @param to
	 * @param cards
	 * @param color
	 *            if color clue given, this is a valid color, else if number
	 *            clue is given this is <tt>null</tt>
	 * @param numOfCard
	 *            if number clue is given, 1 <= numOfCard <= 5. else, this is 0
	 */
	public void giveClue(Player to, List<Card> cards, CardColor color,
			int numOfCard) {
		int playerNum = to.getPlayerNum();
		//TODO: work on this
		if (color != null)
			for (Card card : cards) {
				int cardIndex = card.getCardIndex();
				if (rainbow) {
					boolean isRainbowCard = false;
					for (int i = 5; i < NUMBER_OF_POSSIBLE_CLUES; i++) {
						if (cardClues.get(cardIndex)[i]) {
							isRainbowCard = true;
						} else {
							if (i == COLORS.indexOf(color))
								cardClues.get(cardIndex)[i] = true;
						}
					}
					if (isRainbowCard) {
						for (int i = 5; i < NUMBER_OF_POSSIBLE_CLUES - 1; i++)
							cardClues.get(cardIndex)[i] = false;
						cardClues.get(cardIndex)[10] = true;
					}
				}// end if
				else {

				}
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

	public void updatePlayer() {
		currPlayer++;
	}

	// TESTING: DELETE AFTER EVERYTHING WORKS
	public static void main(String... args) {
		GameState g = new GameState(3, false);
		for (Card c : g.deck)
			System.out.println(c);
		System.out.println(g.deck.size());
	}

}