package final_project;

public enum CardColor {
	
	BLUE("Blue",0),
	RED("Red",1),
	GREEN("Green",2),
	BLACK("Black",3),
	PURPLE("Purple",4),
	RAINBOW("Rainbow",5);
	
	private int colorIndex;
	
	private String name;

	private CardColor(String name, int colorIndex) {
		this.name = name;
		this.colorIndex = colorIndex;
	}

	public String getName() {
		return name;
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
	
	@Override
	public String toString () {
		return name;
	}
}
