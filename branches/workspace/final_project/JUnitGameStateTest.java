package final_project;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.junit.*;

public class JUnitGameStateTest {

	private GameState testGameState;
	private int testNumPlayers;
	private boolean rainbow;
	
	@Test
	public void startUpTest() {
		testNumPlayers = 5;
		rainbow = false;
		testGameState = new GameState(testNumPlayers, rainbow);
		assertTrue(enoughCards());
		rainbow = true;
		testGameState = new GameState(testNumPlayers, rainbow);
		assertTrue(enoughCards());
	}

	public boolean enoughCards() {
		int[][] colors = new int[testGameState.hasRainbow()?6:5][5];
		for ( int x = 0; x < colors.length; x++ ) {
			colors[x][0] = 3;
			colors[x][1] = 2;
			colors[x][2] = 2;
			colors[x][3] = 2;
			colors[x][4] = 1;
		}
		for (int x = 0; x < testGameState.getNumPlayers(); x++) {
			List<Card> playerHand = testGameState.getHand(x);
			for (int y = 0; y < playerHand.size(); y++) {
				Card curCard = playerHand.get(y);
				colors[curCard.getColor().getIndex()][curCard.getNumber() - 1]--;
			}
		}
		Stack<Card> testDeck = testGameState.getDeck();
		while (!testDeck.isEmpty()) {
			Card a = testDeck.pop();
			colors[a.getColor().getIndex()][a.getNumber() - 1]--;
		}
		for (int[] x : colors) {
			for (int y : x) {
				if ( y != 0 )
					return false;
			}
		}
		return true;
	}
}
