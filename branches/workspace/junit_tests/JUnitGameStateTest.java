package junit_tests;

import static org.junit.Assert.*;


import static final_project.CardColor.*;

import java.util.List;
import java.util.Stack;

import org.junit.*;
import final_project.*;
public class JUnitGameStateTest {

	private GameState testGameState;

	private int testNumPlayers;

	private boolean rainbow;

	@Test
	public void test_constructor() {
		testNumPlayers = 5;
		rainbow = false;
		testGameState = new GameState(testNumPlayers, rainbow);
		assertTrue(enoughCards());
		rainbow = true;
		testGameState = new GameState(testNumPlayers, rainbow);
		assertTrue(enoughCards());
	}

	@Test
	public void test_play_card() {
		testNumPlayers = 2;
		testGameState = new GameState(testNumPlayers, rainbow);
		testGameState.playCard(new Card(1, CardColor.BLACK));
		assertEquals(
				testGameState.getPlayedCards()[CardColor.BLACK.getIndex()], 1);
		testGameState = new GameState(testNumPlayers, rainbow);
		testGameState.playCard(new Card(5, CardColor.PURPLE));
		assertEquals(testGameState.getDiscardPile().size(), 1);
		assertEquals(testGameState.getNumClues(), 8);
	}

	@Test
	public void test_give_color_clue() {
		testNumPlayers = 2;
		testGameState = new GameState(testNumPlayers, rainbow);
		testGameState.giveClue(0, CardColor.BLACK, 0);
		List<Card> cards = testGameState.getHand(0);
		for (Card c : cards) {
			if (c.getColor() == CardColor.BLACK)
				assertTrue(c.getColorClues()[CardColor.BLACK.getIndex()]);
			else {
				boolean[] clues = c.getColorClues();
				for (int i = 0; i < clues.length - 1; i++) {
					if (i != BLACK.getIndex()) {
						System.out.println(clues[i]);
						assertTrue(clues[i]);
					} else
						assertFalse(clues[i]);

				}
				assertFalse(c.getColorClues()[CardColor.BLACK.getIndex()]);
			}
		}
	}

	@Test
	public void test_discard_card() {
		testGameState = new GameState(2, false);
		Card discard = new Card(1, BLUE);
		testGameState.discardCard(discard);
		assertEquals(testGameState.getDiscardPile().size(), 1);
		assertEquals(testGameState.getDiscardPile().get(0), discard);
	}

	@Test
	public void test_end_game() {
		testGameState = new GameState(2, rainbow);
		for (int i = 0; i < 3; i++)
			testGameState.playCard(new Card(2, BLUE));
		
		assertTrue(testGameState.isEndOfGame());
		testGameState = new GameState(2, rainbow);
		for (int i = 0; i < 40; i++)
			testGameState.discardCard(testGameState.getDeck().pop());
		assertEquals(testGameState.getDeck().size(),0);
		System.out.println(testGameState.getCurrPlayer());
		testGameState.setGameEndPlayer(testGameState.getCurrPlayer());
		testGameState.updatePlayer();
		testGameState.updatePlayer();
		assertTrue(testGameState.isEndOfGame());
	}

	@Test
	public void test_give_number_clue() {
		testNumPlayers = 2;
		testGameState = new GameState(testNumPlayers, rainbow);
		testGameState.giveClue(0, null, 1);
		List<Card> cards = testGameState.getHand(0);
		for (Card c : cards) {
			// System.out.println(c.getNumber());
			if (c.getNumber() == 1)
				assertTrue(c.getNumberClues()[0]);
			else
				assertFalse(c.getNumberClues()[0]);
		}
	}

	private boolean emptyArray(int[] array) {
		for (int s : array)
			if (s != 0)
				return false;
		return true;
	}

	private boolean enoughCards() {
		int[][] colors = new int[testGameState.isRainbow() ? 6 : 5][5];
		for (int x = 0; x < colors.length; x++) {
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
				if (y != 0)
					return false;
			}
		}
		return true;
	}
}
