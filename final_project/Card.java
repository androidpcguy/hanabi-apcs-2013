package final_project;

public class Card {
	private int number;
	private CardColor color;
	
	// array for storing what player knows about this card.
	// clues[0] for number, clues[1] for color. null used to show user
	// doesn't know something about this card
	private String[] clues;
}