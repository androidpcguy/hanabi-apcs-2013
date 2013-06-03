package final_project;

/**
 * A Java enumeration containing all the color values for cards.
 * 
 * @author Akshara B.
 * @version 1.0
 */
public enum CardColor {

	BLUE("Blue", 0), 
	RED("Red", 1), 
	GREEN("Green", 2), 
	BLACK("Black", 3), 
	PURPLE("Purple", 4), 
	RAINBOW("Rainbow", 5);

	private int colorIndex;

	private String name;

	private CardColor(String name, int colorIndex) {
		this.name = name;
		this.colorIndex = colorIndex;
	}

	/**
	 * Accessor for <tt>name</tt>.
	 * 
	 * @return name of this CardColor
	 */
	public String getName() {
		return name;
	}

	/**
	 * Accessor for <tt>colorIndex</tt>.
	 * 
	 * @return universal index of this CardColor
	 */
	public int getIndex() {
		return colorIndex;
	}

	/**
	 * A function to determine whether colors match other colors.
	 * 
	 * @param other
	 *            a <tt>CardColor</tt> to compare against
	 * @return true if colors match, false otherwise
	 */
	public boolean matchForClues(CardColor other) {
		if (other == RAINBOW || this == RAINBOW) {
			return true;
		} else {
			return this == other;
		}
	}

	@Override
	public String toString() {
		return name;
	}
}
