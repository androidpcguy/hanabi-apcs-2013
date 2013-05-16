package final_project;

public class Card {

	private int number;

	private CardColor color;

	private int cardIndex;

	private boolean[] colorClues, numberClues;

	public Card(int number, CardColor color, int cardIndex) {
		this.number = number;
		this.color = color;
		this.cardIndex = cardIndex;
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


	public void giveNumberClue(int number) {
		if (this.number != number) {
			this.numberClues[number - 1] = false;
		} else {
			for (int i = 0; i < numberClues.length; i++)
				if (i != (number - 1))
					numberClues[i] = false;
		}
	}

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

	public boolean[] getColorClues() {
		return colorClues;
	}

	public boolean[] getNumberClues() {
		return numberClues;
	}
	

	public int getNumber() {
		return number;
	}

	public CardColor getColor() {
		return color;
	}

	public int getCardIndex() {
		return cardIndex;
	}

	public boolean equals(Object other) {
		return ((Card) other).color.equals(color)
				&& ((Card) other).number == number;
	}

	public String toString() {
		return "Number: " + number + " Color: " + color;
	}
}