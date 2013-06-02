package junit_tests;

import static final_project.CardColor.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class CardColorTest {

	@Test
	public void test_matchForClues() {
		assertTrue(RED.matchForClues(RED));
		assertFalse(RED.matchForClues(BLUE));
		assertTrue(RAINBOW.matchForClues(RED));
		assertTrue(RED.matchForClues(RAINBOW));
	}

	@Test
	public void testToString() {
		assertEquals(RED.toString(), "Red");
		assertEquals(BLUE.toString(), "Blue");
		assertEquals(BLACK.toString(), "Black");
		assertEquals(GREEN.toString(), "Green");
		assertEquals(PURPLE.toString(), "Purple");
		assertEquals(RAINBOW.toString(), "Rainbow");
	}

}
