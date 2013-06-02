package junit_tests;

import static org.junit.Assert.*;
import final_project.*;
import org.junit.*;

public class JUnitNetworkTest {

	private static Server test_server;

	private static final int PORT = 1000;

	@Test
	public void test_constructor() {
		test_server = new Server(null, PORT);
		assertEquals(PORT, test_server.getPort());
		assertNull(test_server.getGamestate());
		assertEquals(test_server.getClients().size(), 0);

	}

}
