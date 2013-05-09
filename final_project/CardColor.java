package final_project;

import java.awt.Color;

public enum CardColor {

	RED(Color.RED),
	BLUE(Color.BLUE),
	GREEN(Color.GREEN),
	BLACK(Color.BLACK),
	PURPLE(new Color(208,32,144)),
	//FIXME: rainbow color 
	RAINBOW(null);

	private Color color;

	private CardColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
}
