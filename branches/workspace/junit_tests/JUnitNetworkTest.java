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
		testPlayerA.startGUI();
		testPlayerB.startGUI();
		testPlayerA.getGameComp().setVisible(false);
		testPlayerB.getGameComp().setVisible(false);
		testPlayerB.start();
		Thread.sleep(50);

		assertTrue(testPlayerA.connected());
		assertTrue(testPlayerA.getPlayerNum() == 0);

		assertTrue(testPlayerB.connected());
		assertTrue(testPlayerB.getPlayerNum() == 1);

		assertTrue(testServer.allAreConnected());
		testServer.getServer().close();

	}

	@Test
	public void test_connect() throws IOException, InterruptedException {
		testIP = Inet4Address.getLocalHost().getHostAddress();

		final Server server = new Server(new GameState(1, false), testPort,
				true);
		System.out.println("made server");
		final Socket test_socket = new Socket(testIP, testPort);
		System.out.println("made socket");
		Thread.sleep(50);
		new Thread() {

			public void run() {

				try {
					ObjectInputStream ois = new ObjectInputStream(
							test_socket.getInputStream());
					System.out.println("connecting");
					System.out.println("connected");
					server.connect(1);
					assertEquals(ois.readInt(), 0);
					System.out.println("assertion passed");
					assertTrue(test_socket.isConnected());
					test_socket.close();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}.start();

	}
}
