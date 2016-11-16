import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.List;
import java.util.ArrayList;

class Server extends Thread
{
	int lastConnectionId = 0;
	private List<Connection> connections;
	private BlockingQueue<Packet> packetQueue;

	private class Connection extends Thread
	{
		Socket clientConnection;
		int connectionNumber;

		public Connection(Socket connection, int conNumber)
		{
			clientConnection = connection;
			connectionNumber = conNumber;	
		}

		@Override
		public void run()
		{
			InputStream input;
			try
			{
				input = clientConnection.getInputStream();
			} catch (IOException e)
			{
				e.printStackTrace();
				return;
			}


			List<Byte> data = new ArrayList<Byte>();

			try
			{
				byte in = (byte)input.read();
				while(in != '\0')
				{
					data.add(in);
					in = (byte)input.read();
				}
			} catch (IOException e)
			{
				e.printStackTrace();
				return;
			}

			try
			{
				System.out.println("Putting packet into queue");
				packetQueue.put(Packet.parsePacket(data.toArray(new Byte[data.size()]), connectionNumber));
				System.out.println("Packet in queue!");
			} catch (Exception e)
			{
				System.out.println("hi");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}

	private class ConnectionListenThread extends Thread
	{
		ServerSocket listenerSocket;

		public ConnectionListenThread()
		{
			try
			{
				listenerSocket = new ServerSocket(53737);
			}
			catch (IOException e)
			{
				e.printStackTrace();
				//TODO: real error handling
			}
		}

		@Override
		public void run()
		{
			while (true)
			{
				try
				{
					Socket connectionSocket = listenerSocket.accept();

					Connection cThread = new Connection(connectionSocket, lastConnectionId++);
					cThread.start();

					connections.add(cThread);
				}
				catch (IOException e)
				{
					e.printStackTrace();
					//TODO: real error handling
				}
			}
		}
	}




	public static abstract class Packet
	{
		public enum PacketType
		{
			NULL, JOIN_REQUEST, UPDATE_PLAYER_ACTION, END_OF_ROUND_ACTIONS, START_GAME,
			JOIN_RESPONSE;

			public static final PacketType values[] = values();
		}

		public static Packet parsePacket(Byte[] data, int connectionNumber) throws Exception
		{
			if (data.length < 1)
				throw new Exception("Packet has no length");
			if (data[0] < 0 || data[0] > PacketType.values.length)
				throw new Exception("Unknown packet: " + data[0]);

			byte[] primitiveData = new byte[data.length];
			for (int i = 0; i < data.length; i++)
				primitiveData[i] = data[i];

			PacketType type = PacketType.values[data[0]];

			switch(type)
			{
				case JOIN_REQUEST: return new JoinRequestPacket(primitiveData, connectionNumber);
				case UPDATE_PLAYER_ACTION: return new UpdatePlayerActionPacket(primitiveData, connectionNumber);
				case END_OF_ROUND_ACTIONS: return new EndOfRoundActionsPacket(primitiveData, connectionNumber);
				// case START_GAME: return new StartGamePacket(primitiveData, connectionNumber);
				default: throw new Exception("Unkown packet: " + data[0]);
			}
		}

		public static void sendPacket(Packet p, OutputStream out)
		{
			byte[] data = p.getBytes();

			try
			{
				out.write(data);
			} catch (IOException e)
			{
				e.printStackTrace();
				return;
			}
		}

		public PacketType type;
		public int connectionNumber;

		public Packet(PacketType t, int conNumber)
		{
			type = t;
			connectionNumber = conNumber;
		}

		public abstract byte[] getBytes();
	}

	public static class JoinRequestPacket extends Packet
	{
		public JoinRequestPacket()
		{
			super(PacketType.JOIN_REQUEST, -1);
		}

		public JoinRequestPacket(byte[] data, int connectionNumber)
		{
			super(PacketType.JOIN_REQUEST, connectionNumber);
		}

		public byte[] getBytes()
		{
			return new byte[]{};
		}
	}

	//Update packet sent to the server whenever the user changes an action.  Allows all other clients to display current action choices.
	public static class UpdatePlayerActionPacket extends Packet
	{
		public byte[] actions;

		public UpdatePlayerActionPacket(byte[] data, int connectionNumber) throws Exception
		{
			super(PacketType.UPDATE_PLAYER_ACTION, connectionNumber);

			if (data.length != 9)
				throw new Exception("Recieved malformed UPDATE_PLAYER_ACTION packet");

			actions = new byte[8];

			System.arraycopy(data, 1, actions, 0, 8);
		}

		public byte[] getBytes()
		{
			return new byte[]{};
		}
	}

	//Packet sent by client at end of round with finalized actions.  Ensures synchronization between clients and server
	public static class EndOfRoundActionsPacket extends Packet
	{
		public Engine.Action[] actions;

		public EndOfRoundActionsPacket(byte[] data, int connectionNumber) throws Exception
		{
			super(PacketType.END_OF_ROUND_ACTIONS, connectionNumber);
			//TODO: process data
			//NOTE: Need way to determine player number of packets
		}

		public byte[] getBytes()
		{
			return new byte[]{};
		}
	}







	public static class JoinResponsePacket extends Packet
	{
		public boolean response;

		public JoinResponsePacket(boolean r)
		{
			super(PacketType.JOIN_RESPONSE, -1);

			response = r;
		}

		public JoinResponsePacket(byte[] data, int connectionNumber) throws Exception
		{
			super(PacketType.JOIN_RESPONSE, connectionNumber);

			if (data.length != 2)
				throw new Exception("Recieved malformed JOIN_RESPONSE packet");

			type = PacketType.JOIN_RESPONSE;
			response = (data[0] == 0 ? false : true);
		}

		public byte[] getBytes()
		{
			return new byte[]{(byte)type.ordinal(), (byte)(response?1:0)};
		}
	}




	int playerCount;

	public Server()
	{
		packetQueue = new ArrayBlockingQueue<Packet>(1024);
		connections = new ArrayList<Connection>();
	}

	@Override
	public void run()
	{
		ConnectionListenThread listenThread = new ConnectionListenThread();
		listenThread.start();

		System.out.println("Starting server...");
		while(true)
		{
			try
			{
				System.out.println(packetQueue.take());
			} catch (InterruptedException e)
			{
				e.printStackTrace();
				return;
			}
		}
	}







	public static void main(String[] args)
	{
		Server test = new Server();
		test.run();
	}
}