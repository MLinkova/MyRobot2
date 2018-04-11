package fnl;

import java.util.*;

import robocode.control.RobotSetup;
import searchpractice.Constants;
import searchpractice.Coordinates;

public class Astar {
	private Tile finishTile = new Tile();
	private Tile initialTile = new Tile();
	private Tile actualTile = new Tile();
	private List<Coordinates> moves = new ArrayList<>();
	private Set<Coordinates> obstacles = new HashSet<Coordinates>();
	private Set<Tile> tiles = new HashSet<>();
	private Set<Tile> openset = new HashSet<>();
	private Set<Tile> closedSet = new HashSet<>();

	public Astar() {
		// generator of position of obstacles
		Random r = new Random(Constants.seed);
		for (int NdxObstacle = 0; NdxObstacle < Constants.NUMBER_OBSTACLES; NdxObstacle++) {
			Double InitialObstacleRow = (double) (r.nextInt(Constants.NUMBER_ROWS + 1)) * Constants.SIZE_OF_TILES
					+ Constants.HALF_TILE;
			Double InitialObstacleCol = (double) (r.nextInt(Constants.NUMBER_COLUMNS + 1)) * Constants.SIZE_OF_TILES
					+ Constants.HALF_TILE;
			Coordinates newCoordinates = new Coordinates(InitialObstacleRow, InitialObstacleCol);
			if (!obstacles.contains(newCoordinates)) {
				obstacles.add(newCoordinates);
				Tile node = new Tile();
				node.setCoordinates(newCoordinates);
				node.setObsatcle(true);
				tiles.add(node);

			} else {
				NdxObstacle--;
				continue;
			}

		}
		// generate position of initial tile
		boolean ok = false;
		while (!ok) {
			double xCoordinateOfStart = (double) (r.nextInt(Constants.NUMBER_ROWS + 1)) * Constants.SIZE_OF_TILES
					+ Constants.HALF_TILE;
			double yCoordinateOfStart = (double) (r.nextInt(Constants.NUMBER_COLUMNS + 1)) * Constants.SIZE_OF_TILES
					+ Constants.HALF_TILE;
			Coordinates newAgentCoordinates = new Coordinates(xCoordinateOfStart, yCoordinateOfStart);
			if (!obstacles.contains(newAgentCoordinates)) {
				ok = true; // if generated location is free for use
				initialTile.setInitial(true);
				initialTile.setCoordinates(new Coordinates(xCoordinateOfStart, yCoordinateOfStart));
				tiles.add(initialTile);
				actualTile = initialTile;
			} else {
				continue; // else find another position
			}
		}

		// generate position of finished tile
		ok = false;
		while (!ok) {
			double xCoordinateOfFinish = (double) (r.nextInt(Constants.NUMBER_ROWS + 1)) * Constants.SIZE_OF_TILES
					+ Constants.HALF_TILE;
			double yCoordinateOfFinish = (double) (r.nextInt(Constants.NUMBER_COLUMNS + 1)) * Constants.SIZE_OF_TILES
					+ Constants.HALF_TILE;
			Coordinates newAgentCoordinates = new Coordinates(xCoordinateOfFinish, yCoordinateOfFinish);
			if (!obstacles.contains(newAgentCoordinates)) {
				ok = true; // if generated location is free for use
				finishTile.setFinish(true);
				finishTile.setCoordinates(new Coordinates(xCoordinateOfFinish, yCoordinateOfFinish));
				tiles.add(finishTile);
			} else {
				continue; // else find another position
			}
		}
	}

	public Tile getNode(Coordinates coordninates) {
		// method for geting tile according to coordinates
		for (Tile n : tiles) {
			if ((n.getCoordinates().getxCoordinate() == coordninates.getxCoordinate())
					&& (n.getCoordinates().getyCoordinate() == coordninates.getyCoordinate())) {
				return n;

			}
		}
		return null;
	}

	public void heuristicFunction() {
		// computing heuristic function using manhattan distance
		for (int i = 0; i <= Constants.NUMBER_ROWS; i++) {
			for (int j = 0; j <= Constants.NUMBER_COLUMNS; j++) {
				double h = Math.sqrt((Math.pow((i * Constants.SIZE_OF_TILES + Constants.HALF_TILE
						- finishTile.getCoordinates().getxCoordinate()), 2))
						+ (Math.pow((j * Constants.SIZE_OF_TILES + Constants.HALF_TILE
								- finishTile.getCoordinates().getyCoordinate()), 2)));
				Coordinates coorinates = new Coordinates((double) i * Constants.SIZE_OF_TILES + Constants.HALF_TILE,
						(double) j * Constants.SIZE_OF_TILES + Constants.HALF_TILE);
				Tile node = getNode(coorinates);
				if (node != null) {
					node.setH(h);
					if (node.isInitial()) {
						node.setF(node.getG() + h);
					}
				} else {
					node = new Tile();
					node.setH(h);
					node.setCoordinates(coorinates);
					tiles.add(node);
				}

			}
		}
	}

	public Tile getNodeWithLowestF() {
		// method for choosing tile with lowest f from set of not evalu
		double minF = Double.MAX_VALUE;
		Tile minNode = new Tile();
		for (Tile n : openset) {
			if (n.getF() < minF) {
				minNode = n;
				minF = n.getF();
			}
		}
		return minNode;
	}

	public List<Coordinates> astarAlgorithm() {
		// main method for running A* algorithm
		heuristicFunction();
		openset.add(initialTile);
		while (!openset.isEmpty()) {
			actualTile = getNodeWithLowestF();
			if (actualTile.isFinish()) {
				reconstructPath();
				return moves;

			}
			openset.remove(actualTile);
			closedSet.add(actualTile);
			// create set of neighbour of actual tile
			Set<Tile> neighbours = new HashSet<>();
			neighbours
					.add(getNode(new Coordinates(actualTile.getCoordinates().getxCoordinate() - Constants.SIZE_OF_TILES,
							actualTile.getCoordinates().getyCoordinate())));
			neighbours
					.add(getNode(new Coordinates(actualTile.getCoordinates().getxCoordinate() + Constants.SIZE_OF_TILES,
							actualTile.getCoordinates().getyCoordinate())));
			neighbours.add(getNode(new Coordinates(actualTile.getCoordinates().getxCoordinate(),
					actualTile.getCoordinates().getyCoordinate() - Constants.SIZE_OF_TILES)));
			neighbours.add(getNode(new Coordinates(actualTile.getCoordinates().getxCoordinate(),
					actualTile.getCoordinates().getyCoordinate() + Constants.SIZE_OF_TILES)));

			for (Tile neighbour : neighbours) {
				if ((neighbour == null) || (closedSet.contains(neighbour)) || (neighbour.isObsatcle())) {
					continue;
				}

				double tentiveG = actualTile.getG() + Constants.DISTANCE_NEIGHBOUR_CURRENT;
				if ((!openset.contains(neighbour)) || (tentiveG < neighbour.getG())) {
					neighbour.setParent(actualTile);
					neighbour.setG(tentiveG);
					neighbour.setF(neighbour.getG() + neighbour.getH());
					if (!openset.contains(neighbour)) {
						openset.add(neighbour);
					}

				}
			}
		}

		return null;
	}

	private void reconstructPath() {
		// method for reconstructing path of correct coordinates for Robot
		List<Coordinates> help = new ArrayList<>();
		Tile cursor = actualTile;
		// move to parent until you can
		while (cursor.getParent() != null) {
			help.add(cursor.getCoordinates());
			cursor = cursor.getParent();
		}
		// add tile on the end of path
		help.add(cursor.getCoordinates());

		for (int i = help.size() - 1; i >= 0; i--)
			moves.add(help.get(i));
	}
}
