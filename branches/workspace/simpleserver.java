import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;


public class simpleserver
{
	public static final int port = 100;


	public static void main( String... args ) throws Exception
	{
		ServerSocket server = new ServerSocket( port );
		boolean done = false;
		int i = 0;
		while ( true )
		{
			Socket s = server.accept();
			System.out.println( s );
			//ObjectOutputStream o = new ObjectOutputStream( s.getOutputStream() );
			//String g = "This is the " + ( i++ ) + "-th visit to this server";
			//o.writeObject( g );
			//o.flush();
			//o.close();
		}
	}
}