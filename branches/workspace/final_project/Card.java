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

	public int getNumber() {
		return number;
	}

	public CardColor getColor() {
		return color;
	}

	public int getCardIndex() {
		return cardIndex;
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
		int oneTrue = oneColor();
		if (oneTrue == -1) {
			if (this.color != color)
				this.colorClues[color.getIndex()] = false;
			else
				for (int i = 0; i < colorClues.length - 1; i++)
					if (i != color.getIndex())
						colorClues[i] = false;
		} else {
			// TODO: finish this
		}

	}

	private int oneColor() {
		// change name this later
		int i = -1;
		for (int j = 0; j < colorClues.length - 1; j++)
			if (colorClues[j] && i != -1)
				return -1;
			else if (colorClues[j])
				i = j;
		return i;

	}

	public boolean equals(Object other) {
		return ((Card) other).color.equals(color)
				&& ((Card) other).number == number;
	}

	public String toString() {
		return "Number: " + number + " Color: " + color;
	}
}