package final_project;

import java.awt.Color;

public enum CardColor {

	RED(Color.RED,0),
	BLUE(Color.BLUE,1),
	GREEN(Color.GREEN,2),
	BLACK(Color.BLACK,3),
	PURPLE(new Color(208,32,144),4),
	//FIXME: rainbow color 
	RAINBOW(null,5);

	private Color color;
	
	private int colorIndex;

	private CardColor(Color color, int colorIndex) {
		this.color = color;
		this.colorIndex = colorIndex;
	}

	public Color getColor() {
		return color;
	}
	
	public int getIndex() {
		return colorIndex;
	}
	
	public boolean matchForClues (CardColor other) {
		if (other == RAINBOW || this == RAINBOW) {
			return true;
		} else {
			return this == other;
		}
	}
}
