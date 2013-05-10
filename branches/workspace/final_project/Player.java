package final_project;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class Player {
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	
	private ArrayList<Card> cards;

	private GameComponent gameComp;

	private int playerNum;

	public int getPlayerNum() {
		return playerNum;
	}
}