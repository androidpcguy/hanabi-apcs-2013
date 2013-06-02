package junit_tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.Test;

import final_project.GameState;
import final_project.Player;
import final_project.Server;

public class JUnitNetworkTest {

	private int testPort = 6666;

	private String testIP;

	@Test
	public void test_network() throws Exception {
		testIP = Inet4Address.getLocalHost().getHostAddress();
		Server testServer = new Server(new GameState(2, false), testPort, false);
		testServer.start();
		Thread.sleep(50);
		assertEquals(testServer.getClients().size(), 0);
		assertNotNull(testServer.getOutput());
		assertNotNull(testServer.getInput());

		Player testPlayerA = new Player(testPort, testIP, null);
		testPlayerA.start();
		Thread.sleep(50);
		Player testPlayerB = new Player(testPort, testIP, null);
		testPlayerB.start();
		Thread.sleep(50);

		assertTrue(testPlayerA.connected());
		assertTrue(testPlayerA.getPlayerNum() == 0);

		assertTrue(testPlayerB.connected());
		assertTrue(testPlayerB.getPlayerNum() == 1);

		assertTrue(testServer.allAreConnected());
		testServer.getServer().close();
		
		//FIXME
	}

	@Test
	public void test_connect() throws IOException {
		testIP = Inet4Address.getLocalHost().getHostAddress();
		
		Server server = new Server(new GameState(1, false), testPort, true);
		System.out.println("made server");
		Socket test_socket = new Socket(testIP, testPort);
		System.out.println("made socket");
		ObjectInputStream ois = new ObjectInputStream(test_socket.getInputStream());
		server.connect(1);
		assertEquals(ois.readInt(),0);
		assertTrue(test_socket.isConnected());
		
		
	}
}
