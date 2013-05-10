package final_project;

public class Card {
	private int number;
	private CardColor color;

	// array for storing what player knows about this card.
	// clues[0] for number, clues[1] for color. null used to show user
	// doesn't know something about this card
	private String[] clues;

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
	
	public String[] getClues()
	{
		return clues;
	}
}