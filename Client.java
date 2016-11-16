import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;

class Client
{
	public static void main(String[] args) throws Throwable
	{
		Socket test = new Socket("localhost", 53737);
		test.getOutputStream().write(new byte[]{(byte)Server.Packet.PacketType.JOIN_REQUEST.ordinal(), (byte)0});

		while(true);
	}
}