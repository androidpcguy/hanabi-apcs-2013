import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.Socket;


public class simpleclient
{
	public static void main( String... args ) throws Exception
	{
		ObjectInputStream ois;
		for ( ;; )
		{
			Socket s = new Socket( "192.168.1.106", simpleserver.port );
			ois = new ObjectInputStream( s.getInputStream() );
			System.out.println( ois.readObject() );
			s.close();
			 Thread.sleep( 100 );
		}
	}
}
