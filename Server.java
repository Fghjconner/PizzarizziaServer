import java.io.IOException;
import java.net.ServerSocket;

class Server extends Thread
{
	private class ConnectionThread extends Thread
	{
		public ConnectionThread()
		{

		}

		@Override
		public void run()
		{

		}
	}

	private class ConnectionListenThread extends Thread
	{
		ServerSocket listenerSocket;

		public ConnectionListenThread()
		{
			
		}

		@Override
		public void run()
		{
			ServerSocket listenerSocket = new ServerSocket(53737);

			while (true)
			{
				try
				{

				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}


	public Server()
	{
		super();
	}

	@Override
	public void run()
	{
		
	}
}