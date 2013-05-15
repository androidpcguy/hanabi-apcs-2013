package final_project;

public class Card {

	private int number;

	private CardColor color;

	public Card(int number, CardColor color) {
		this.number = number;
		this.color = color;
	}

	public int getNumber() {
		return number;
	}

	public CardColor getColor() {
		return color;
	}

	public String toString() {
		return "Number: " + number + " Color: " + color;
	}
}