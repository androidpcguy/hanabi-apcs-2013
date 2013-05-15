package final_project;

public class Card {

	private int number;

	private CardColor color;

	private int cardIndex;

	public Card(int number, CardColor color, int cardIndex) {
		this.number = number;
		this.color = color;
		this.cardIndex = cardIndex;
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