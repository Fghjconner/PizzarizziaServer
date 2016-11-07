import java.util.ArrayList;
import java.util.List;

public class Engine
{
	public enum ActionType
	{
		USE, MOVE, THROW, STAND
	}

	public enum Direction
	{
		RIGHT, DOWN, LEFT, UP, NONE;

		public Direction reverse()
		{
			switch (this)
			{
				case RIGHT: return LEFT;
				case DOWN: return UP;
				case LEFT: return RIGHT;
				case UP: return DOWN;
				default: return NONE;
			}
		}
	}

	public class Action
	{
		public ActionType type;
		public Direction direction;

		public Action(ActionType actionType, Direction actionDirection)
		{
			type = actionType;
			direction = actionDirection;
		}
	}


	private class Location
	{
		public int x;
		public int y;

		public Location(int xCoord, int yCoord)
		{
			x = xCoord;
			y = yCoord;
		}

		public Location(Location oldLoc)
		{
			x = oldLoc.x;
			y = oldLoc.y;
		}

		public Location getAdjacent(Direction direction)
		{
			Location adjacentLoc = new Location(this);

			switch(direction)
			{
				case RIGHT: adjacentLoc.x++; break;
				case LEFT: adjacentLoc.x--; break;
				case UP: adjacentLoc.y++; break;
				case DOWN: adjacentLoc.y--; break;
			}

			return adjacentLoc;
		}

		@Override
		public boolean equals(Object other)
		{
			return other instanceof Location && x == ((Location) other).x && y == ((Location) other).y;
		}
	}

	private class Player
	{
		public Item held = null;
		public Location loc;
	}

	private enum Tile
	{
		EMPTY, WALL, OVEN, COUNTER, MEAT, VEGETABLES, CHEESE, SAUCE, CHEESAUCE, TRASH, DOUGH, PICKUP
	}

	public abstract class Item
	{
		abstract GraphicsCommunicationObject.Item makeGraphicsItem();
	}

	public class Pizza extends Item
	{
		public Direction flightDirection = Direction.NONE;

		public boolean sauce = false;
		public boolean cheese = false;
		public boolean meat = false;
		public boolean vegetables = false;

		public int cookedness = 0;

		public boolean isRaw()
		{
			return cookedness < 4;
		}

		public boolean isCooked()
		{
			return cookedness >= 4 && cookedness < 8;
		}

		public boolean isBurnt()
		{
			return cookedness >= 8;
		}

		public GraphicsCommunicationObject.Item makeGraphicsItem()
		{
			return new GraphicsCommunicationObject.Pizza(this);
		}
	}

	public class Meat extends Item
	{
		public GraphicsCommunicationObject.Item makeGraphicsItem()
		{
			return new GraphicsCommunicationObject.Meat();
		}
	}

	public class Vegetables extends Item
	{
		public GraphicsCommunicationObject.Item makeGraphicsItem()
		{
			return new GraphicsCommunicationObject.Vegetables();
		}
	}



	private static Tile[][] map =
	{  //1			2			3			4			5			6			7
		{Tile.CHEESAUCE,Tile.CHEESAUCE,	Tile.EMPTY,		Tile.OVEN,		Tile.OVEN,		Tile.EMPTY,		Tile.EMPTY},		//1
		{Tile.CHEESE,	Tile.EMPTY,		Tile.EMPTY,		Tile.EMPTY,		Tile.EMPTY,		Tile.EMPTY,		Tile.MEAT},			//2
		{Tile.SAUCE,	Tile.EMPTY,		Tile.EMPTY,		Tile.EMPTY,		Tile.EMPTY,		Tile.EMPTY,		Tile.MEAT},			//3
		{Tile.EMPTY,	Tile.EMPTY,		Tile.EMPTY,		Tile.COUNTER,	Tile.COUNTER,	Tile.EMPTY,		Tile.EMPTY},		//4
		{Tile.TRASH,	Tile.EMPTY,		Tile.EMPTY,		Tile.COUNTER,	Tile.COUNTER,	Tile.EMPTY,		Tile.EMPTY},		//5
		{Tile.EMPTY,	Tile.EMPTY,		Tile.EMPTY,		Tile.COUNTER,	Tile.COUNTER,	Tile.EMPTY,		Tile.VEGETABLES},	//6
		{Tile.DOUGH,	Tile.DOUGH,		Tile.EMPTY,		Tile.EMPTY,		Tile.EMPTY,		Tile.EMPTY,		Tile.VEGETABLES},	//7
		{Tile.EMPTY,	Tile.EMPTY,		Tile.EMPTY,		Tile.EMPTY,		Tile.EMPTY,		Tile.EMPTY,		Tile.EMPTY},		//8
		{Tile.WALL,		Tile.WALL,		Tile.WALL,		Tile.PICKUP,	Tile.PICKUP,	Tile.PICKUP,	Tile.PICKUP},		//9
	};

	private Item[][] items = new Item[7][9];

	private Player[] players;

	public Engine(int playerCount)
	{
		players = new Player[playerCount];
		for (int i = 0; i < playerCount; i++)
			players[i] = new Player();
	}

	public GraphicsCommunicationObject performTurn(Action[][] actions)
	{
		GraphicsCommunicationObject graphics = new GraphicsCommunicationObject();

		for (int currentStep = 0; currentStep < 4; currentStep++)
		{
			for (int playerNumber = 0; playerNumber < players.length; playerNumber++)
			{
				graphics.add(currentStep, performAction(currentStep, playerNumber, actions));
			}

			updateWorld(currentStep, actions, graphics);
		}

		return null;
	}

	private GraphicsCommunicationObject.GraphicsElement performAction(int currentStep, int playerNumber, Action[][] actions)
	{
		Action currentAction = actions[playerNumber][currentStep];
		Player currentPlayer = players[playerNumber];
		Location targetLoc = players[playerNumber].loc.getAdjacent(currentAction.direction);

		switch (currentAction.type)
		{
			case USE:
				if (currentPlayer.held == null)
				{
					if (getItem(targetLoc) != null)
					{
						currentPlayer.held = getItem(targetLoc);
						setItem(targetLoc, null);

						return new GraphicsCommunicationObject.PickUpElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentAction.direction, currentPlayer.held.makeGraphicsItem());
					}
					else
					{
						switch(getTile(targetLoc))
						{
							case DOUGH: currentPlayer.held = new Pizza(); break;
							case MEAT: currentPlayer.held = new Meat(); break;
							case VEGETABLES: currentPlayer.held = new Vegetables(); break;
							default: return new GraphicsCommunicationObject.StandElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentPlayer.held.makeGraphicsItem());
						}
						
						return new GraphicsCommunicationObject.UseElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentAction.direction, currentPlayer.held.makeGraphicsItem());
					}
				}
				else if (currentPlayer.held instanceof Pizza)
				{
					Pizza heldPizza = (Pizza) currentPlayer.held;

					if (getItem(targetLoc) != null)
					{
						Item targetItem = getItem(targetLoc);

						if (targetItem instanceof Meat && !heldPizza.meat && heldPizza.isRaw())
							heldPizza.meat = true;
						else if (targetItem instanceof Vegetables && !heldPizza.vegetables && heldPizza.isRaw())
							heldPizza.vegetables = true;
						else
							return new GraphicsCommunicationObject.StandElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentPlayer.held.makeGraphicsItem());

						setItem(targetLoc, null);
						return new GraphicsCommunicationObject.PickUpElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentAction.direction, currentPlayer.held.makeGraphicsItem());
					}
					else
					{
						Tile targetTile = getTile(targetLoc);

						if (targetTile == Tile.TRASH)
							currentPlayer.held = null;
						else if (targetTile == Tile.SAUCE && !heldPizza.cheese && !heldPizza.meat && !heldPizza.vegetables && heldPizza.isRaw())
							heldPizza.sauce = true;
						else if (targetTile == Tile.CHEESAUCE && !heldPizza.cheese && !heldPizza.meat && !heldPizza.vegetables && heldPizza.isRaw())
							heldPizza.sauce = heldPizza.cheese = true;
						else if (targetTile == Tile.CHEESE && !heldPizza.meat && !heldPizza.vegetables && heldPizza.isRaw())
							heldPizza.cheese = true;
						else if (targetTile == Tile.COUNTER || targetTile == Tile.PICKUP || targetTile == Tile.OVEN || targetTile == Tile.EMPTY)
						{
							setItem(targetLoc, heldPizza);
							currentPlayer.held = null;

							return new GraphicsCommunicationObject.DropElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentAction.direction);
						}
						else
							return new GraphicsCommunicationObject.StandElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentPlayer.held.makeGraphicsItem());

						return new GraphicsCommunicationObject.UseElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentAction.direction, currentPlayer.held.makeGraphicsItem());
					}
				}
				else if (currentPlayer.held instanceof Meat)
				{
					Meat heldMeat = (Meat) currentPlayer.held;

					if (getItem(targetLoc) != null)
					{
						Item targetItem = getItem(targetLoc);

						if (targetItem instanceof Pizza && !((Pizza) targetItem).meat && ((Pizza) targetItem).isRaw())
						{
							((Pizza) targetItem).meat = true;
							currentPlayer.held = null;

							return new GraphicsCommunicationObject.DropElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentAction.direction);
						}
						else
							return new GraphicsCommunicationObject.StandElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentPlayer.held.makeGraphicsItem());
					}
					else
					{
						Tile targetTile = getTile(targetLoc);

						if (targetTile == Tile.TRASH)
							currentPlayer.held = null;
						else if (targetTile == Tile.COUNTER || targetTile == Tile.PICKUP || targetTile == Tile.EMPTY)
						{
							setItem(targetLoc, heldMeat);
							currentPlayer.held = null;

							return new GraphicsCommunicationObject.DropElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentAction.direction);
						}
						else
							return new GraphicsCommunicationObject.StandElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentPlayer.held.makeGraphicsItem());
					}
				}
				else if (currentPlayer.held instanceof Vegetables)
				{
					Vegetables heldVegetables = (Vegetables) currentPlayer.held;

					if (getItem(targetLoc) != null)
					{
						Item targetItem = getItem(targetLoc);

						if (targetItem instanceof Pizza && !((Pizza) targetItem).vegetables && ((Pizza) targetItem).isRaw())
						{
							((Pizza) targetItem).vegetables = true;
							currentPlayer.held = null;

							return new GraphicsCommunicationObject.DropElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentAction.direction);
						}
						else
							return new GraphicsCommunicationObject.StandElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentPlayer.held.makeGraphicsItem());
					}
					else
					{
						Tile targetTile = getTile(targetLoc);

						if (targetTile == Tile.TRASH)
							currentPlayer.held = null;
						else if (targetTile == Tile.COUNTER || targetTile == Tile.PICKUP || targetTile == Tile.EMPTY)
						{
							setItem(targetLoc, heldVegetables);
							currentPlayer.held = null;

							return new GraphicsCommunicationObject.DropElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentAction.direction);
						}
						else
							return new GraphicsCommunicationObject.StandElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentPlayer.held.makeGraphicsItem());
					}
				}
				else
					return null;
			case MOVE:
				if (getTile(targetLoc) == Tile.EMPTY)
				{
					GraphicsCommunicationObject.MoveElement graphicsElement = new GraphicsCommunicationObject.MoveElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentAction.direction, currentPlayer.held.makeGraphicsItem());

					if (getItem(targetLoc) != null)
					{
						setItem(targetLoc, null);
						stunPlayer(playerNumber, currentStep, actions);
						graphicsElement.stunnedAfter = true;
					}

					currentPlayer.loc = targetLoc;
					return graphicsElement;
				}
				else
				{
					stunPlayer(playerNumber, currentStep, actions);

					return new GraphicsCommunicationObject.CollideElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentAction.direction, currentPlayer.held.makeGraphicsItem());
				}
			case THROW:
				if (currentPlayer.held instanceof Pizza && isValidLocation(targetLoc) && (getTile(targetLoc) == Tile.EMPTY || getTile(targetLoc) == Tile.COUNTER))
				{
					setItem(currentPlayer.loc, currentPlayer.held);
					((Pizza) getItem(currentPlayer.loc)).flightDirection = currentAction.direction;
					currentPlayer.held = null;

					return new GraphicsCommunicationObject.ThrowElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentAction.direction);
				}
				else
					return new GraphicsCommunicationObject.StandElement(playerNumber, currentPlayer.loc.x, currentPlayer.loc.y, currentPlayer.held.makeGraphicsItem());
			default:
				return null; //TODO: Throw error?
		}

	}

	//Preforms action independent updates to the world (eg. removing delivered pizzas, pizzas cooking, pizzas flying, player collisions)
	private void updateWorld(int currentStep, Action[][] actions, GraphicsCommunicationObject graphics)
	{
		while (handleCollision(currentStep, actions[currentStep], graphics));

		for (int x = 0; x < map.length; x++)
			for (int y = 0; y < map[0].length; y++)
			{
				Location currentLoc = new Location(x, y);

				if (getItem(currentLoc) != null)
				{
					Item currentItem = getItem(currentLoc);

					int player = getPlayerAt(currentLoc);
					if (player != -1)
					{
						stunPlayer(player, currentStep, actions);
						graphics.addCollision(currentStep, player, false);
						setItem(currentLoc, null);
					}
					else if (currentItem instanceof Pizza)
					{
						Pizza currentPizza = ((Pizza) currentItem);

						if (currentPizza.flightDirection != Direction.NONE)
						{
							setItem(currentLoc, null);

							int distance = 0;
							Location flightLoc = new Location(currentLoc);
							boolean impacted = false;
							while(impacted)
							{
								flightLoc = flightLoc.getAdjacent(currentPizza.flightDirection);
								distance++;

								player = getPlayerAt(flightLoc);

								if (player != -1)
								{
									stunPlayer(player, currentStep, actions);
									graphics.addCollision(currentStep, player, false);
									impacted = true;
								}
								else if ((getTile(flightLoc.getAdjacent(currentPizza.flightDirection)) != Tile.EMPTY && getTile(flightLoc.getAdjacent(currentPizza.flightDirection)) != Tile.COUNTER && getTile(flightLoc.getAdjacent(currentPizza.flightDirection)) != Tile.OVEN) || getTile(flightLoc) == Tile.OVEN)
								{
									setItem(flightLoc, currentPizza);
									graphics.add(currentStep, new GraphicsCommunicationObject.FlyElement(currentLoc.x, currentLoc.y, currentPizza.flightDirection, distance, currentPizza.makeGraphicsItem()));
									impacted = true;
								}
							}
						}
						else
						{
							if (getTile(currentLoc) == Tile.OVEN)
								currentPizza.cookedness++;

							graphics.add(currentStep, new GraphicsCommunicationObject.SitElement(currentLoc.x, currentLoc.x, currentItem.makeGraphicsItem()));
						}
					}
					else
						graphics.add(currentStep, new GraphicsCommunicationObject.SitElement(currentLoc.x, currentLoc.x, currentItem.makeGraphicsItem()));
				}
			}
	}

	private void stunPlayer(int playerNumber, int currentStep, Action[][] actions)
	{
		if (currentStep < 3)
		{
			if (actions[currentStep][playerNumber].type != ActionType.STAND)
			{
				for (int step = 3; step > currentStep + 1; step--)
					actions[step][playerNumber] = actions[step-1][playerNumber];

				actions[currentStep+1][playerNumber] = new Action(ActionType.STAND, Direction.NONE);
			}
		}
	}

	//Loops through players until it finds a collision, then resolves it. Returns true if a collision is found, false otherwise
	//Should be called repeatedly until all collisions are resolved
	private boolean handleCollision(int currentStep, Action[] actions, GraphicsCommunicationObject graphics)
	{
		for (int playerNumber = 0; playerNumber < players.length; playerNumber++)
			for (int otherPlayer = playerNumber + 1; otherPlayer < players.length; otherPlayer++)
				if (players[playerNumber].loc.equals(players[otherPlayer].loc))
				{
					if (actions[playerNumber].type == ActionType.MOVE)
					{
						players[playerNumber].loc = players[playerNumber].loc.getAdjacent(actions[playerNumber].direction.reverse());

						graphics.addCollision(currentStep, playerNumber, true);
					}

					if (actions[otherPlayer].type == ActionType.MOVE)
					{
						players[otherPlayer].loc = players[otherPlayer].loc.getAdjacent(actions[otherPlayer].direction.reverse());

						graphics.addCollision(currentStep, otherPlayer, true);
					}

					return true;
				}

		return false;
	}

	private Tile getTile(Location loc)
	{
		return map[loc.x][loc.y];
	}

	private Item getItem(Location loc)
	{
		return items[loc.x][loc.y];
	}

	private void setItem(Location loc, Item item)
	{
		items[loc.x][loc.y] = item;
	}

	private int getPlayerAt(Location loc)
	{
		List<Player> out = new ArrayList<Player>();

		for (int i = 0; i < players.length; i++)
			if (players[i].loc.equals(loc))
				return i;
		return -1;
	}

	private boolean isValidLocation(Location loc)
	{
		return loc.x < 0 || loc.y < 0 || loc.x >= map.length || loc.y >= map[0].length;
	}
}