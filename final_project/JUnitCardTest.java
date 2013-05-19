package final_project;

import static org.junit.Assert.*;

import org.junit.*;
import org.junit.Test;

public class JUnitCardTest {

	private Card testCard;
	private CardColor testColor;
	private int testNumber;

	@Before
	public void before() {
		testColor = CardColor.RED;
		testNumber = 1;
		testCard = new Card(testNumber, testColor);
	}

	@Test
	public void constructorTest() {
		assertEquals(testCard.getColor(), testColor);
		assertEquals(testCard.getNumber(), testNumber);
		for (boolean a : testCard.getColorClues())
			assertTrue(a);
		for (boolean a : testCard.getNumberClues())
			assertTrue(a);
	}

	@Test
	public void giveNumberClueTest() {
		testCard.giveNumberClue(3);
		assertFalse(testCard.getNumberClues()[2]);
		testCard.giveNumberClue(1);
		assertTrue(testCard.getNumberClues()[0]);
		boolean[] a = testCard.getNumberClues();
		for (int x = 1; x < a.length; x++) {
			assertFalse(a[x]);
		}
	}

	@Test
	public void giveColorClueTest() {
		testCard.giveColorClue(CardColor.BLUE);
		assertFalse(testCard.getColorClues()[CardColor.BLUE.getIndex()]);
		testCard.giveColorClue(testColor);
		for (int x = 0; x < 6; x++) {
			assertTrue(x == testColor.getIndex() ? testCard.getColorClues()[x]
					: !testCard.getColorClues()[x]);
		}
		testCard = new Card(1, CardColor.RAINBOW);
		testCard.giveColorClue(testColor);
		assertTrue(testCard.getColorClues()[testColor.getIndex()]);
		testCard.giveColorClue(CardColor.BLUE);
		assertTrue(testCard.getColorClues()[CardColor.RAINBOW.getIndex()]);
		for (int x = 0; x < 5; x++) {
			assertFalse(testCard.getColorClues()[x]);
		}
	}

	@Test
	public void equalsTest() {
		assertTrue(testCard.equals(new Card(1, CardColor.RED)));
		assertFalse(testCard.equals(new Card(1, CardColor.BLUE)));
		assertFalse(testCard.equals(new Card(2, CardColor.RED)));
		assertFalse(testCard.equals(new Card(2, CardColor.BLUE)));
	}

}