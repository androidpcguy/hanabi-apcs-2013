package final_project;

import java.net.*;
import java.io.*;

public class Player {
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;

	private GameComponent gameComp;

	private int playerNum;

	public int getPlayerNum() {
		return playerNum;
	}
}