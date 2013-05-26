package final_project;

/**
 * Represents a single card in the game, along with any knowledge about the
 * card.
 */
public class Card implements java.io.Serializable {

	private int number;

	private CardColor color;

	private boolean[] colorClues, numberClues;

	/**
	 * Creates a new Card.
	 * 
	 * @param number
	 *            number value (1 to 5)
	 * @param color
	 *            color (from CardColor)
	 */
	public Card(int number, CardColor color) {
		this.number = number;
		this.color = color;
		this.colorClues = new boolean[6];
		this.numberClues = new boolean[5];
		initClueArrays();
	}

	private void initClueArrays() {
		for (int i = 0; i < 6; i++)
			colorClues[i] = true;
		for (int i = 0; i < 5; i++)
			numberClues[i] = true;
	}

	/**
	 * Updates knowledge about this card's number.
	 * 
	 * @param number
	 *            the number of the clue to give
	 */
	public void giveNumberClue(int number) {
		if (this.number != number) {
			this.numberClues[number - 1] = false;
		} else {
			for (int i = 0; i < numberClues.length; i++)
				if (i != (number - 1))
					numberClues[i] = false;
		}
	}
	
	/**
	 * Updates knowledge about this card's color.
	 * 
	 * @param color
	 *            the color of the clue to give (not rainbow)
	 */
	public void giveColorClue(CardColor color) {
		if (!color.matchForClues(this.color)) {
			colorClues[color.getIndex()] = false;
			colorClues[colorClues.length - 1] = false;
		} else {
			if (!colorClues[color.getIndex()]) {
				for (int i = 0; i < 5; i++) {
					colorClues[i] = false;
				}
			} else {
				for (int i = 0; i < 5; i++) {
					if (i != color.getIndex()) {
						colorClues[i] = false;
					}
				}
			}
		}

	}

	/**
	 * Returns knowledge about the color.
	 * 
	 * @return color knowledge
	 */
	public boolean[] getColorClues() {
		return colorClues;
	}

	/**
	 * Returns knowledge about the number.
	 * 
	 * @return number knowledge
	 */
	public boolean[] getNumberClues() {
		return numberClues;
	}

	/**
	 * Returns the number of this card.
	 * 
	 * @return number
	 */
	public int getNumber() {
		return number;
	}

	/**
	 * Returns the color of this card.
	 * 
	 * @return color
	 */
	public CardColor getColor() {
		return color;
	}
	
	@Override
	public boolean equals(Object other) {
		return ((Card) other).color.equals(color)
				&& ((Card) other).number == number;
	}
	
	@Override
	public String toString() {
		return color + " " + number;
	}
}