package fnl;

import java.util.*;

import robocode.control.RobotSetup;
import searchpractice.Constants;
import searchpractice.Coordinates;

public class AstarFunction {
	private Node FinishNode = new Node();
	private Node InitialNode = new Node();
	private int step = 64;
	private Node ActualNode = new Node();
	private List<Coordinates> moves = new ArrayList<>();
	private Set<Coordinates> obstacles = new HashSet<Coordinates>();
	private Set<Node> nodes = new HashSet<>();
	private Set<Node> openset = new HashSet<>();
	private Set<Node> closedSet = new HashSet<>();

	public AstarFunction() {
		Random r = new Random(Constants.seed);
		for (int NdxObstacle = 0; NdxObstacle < Constants.NUMBER_OBSTACLES; NdxObstacle++) {
			Double InitialObstacleRow = (double) (r.nextInt(Constants.NUMBER_ROWS + 1)) * Constants.SIZE_OF_TILES + 32;
			Double InitialObstacleCol = (double) (r.nextInt(Constants.NUMBER_COLUMNS + 1)) * Constants.SIZE_OF_TILES
					+ 32;
			Coordinates newCoordinates = new Coordinates(InitialObstacleRow, InitialObstacleCol);
			if (!obstacles.contains(newCoordinates)) {
				obstacles.add(newCoordinates);
				Node node = new Node();
				node.setCoordinates(newCoordinates);
				node.setObsatcle(true);
				nodes.add(node);

			} else {
				NdxObstacle--;
				continue;
			}

		}
		for (Coordinates ob : obstacles) {
			System.out.println(ob.toString());
		}
		double xCoordinateOfStart = (double) (r.nextInt(Constants.NUMBER_ROWS + 1)) * Constants.SIZE_OF_TILES + 32;
		double yCoordinateOfStart = (double) (r.nextInt(Constants.NUMBER_COLUMNS + 1)) * Constants.SIZE_OF_TILES + 32;
		InitialNode.setInitial(true);
		InitialNode.setCoordinates(new Coordinates(xCoordinateOfStart, yCoordinateOfStart));
		nodes.add(InitialNode);
		System.out.println(InitialNode.getCoordinates().toString());
		ActualNode = InitialNode;
		double xCoordinateOfFinish = (double) (r.nextInt(Constants.NUMBER_ROWS + 1)) * Constants.SIZE_OF_TILES + 32;
		double yCoordinateOfFinish = (double) (r.nextInt(Constants.NUMBER_COLUMNS + 1)) * Constants.SIZE_OF_TILES + 32;
		FinishNode.setFinish(true);
		FinishNode.setCoordinates(new Coordinates(xCoordinateOfFinish, yCoordinateOfFinish));
		nodes.add(FinishNode);
		System.out.println(FinishNode.getCoordinates().toString());
	}

	public Node getNode(Coordinates coordninates) {
		for (Node n : nodes) {
			if ((n.getCoordinates().getxCoordinate() == coordninates.getxCoordinate())
					&& (n.getCoordinates().getyCoordinate() == coordninates.getyCoordinate())) {
				return n;

			}
		}
		return null;
	}

	public void generatedHeuristicFunction() {
		for (int i = 0; i <= Constants.NUMBER_ROWS; i++) {
			for (int j = 0; j <= Constants.NUMBER_COLUMNS; j++) {
				double h = Math.sqrt((Math.pow((i * step + 32 - FinishNode.getCoordinates().getxCoordinate()), 2))
						+ (Math.pow((j * step + 32 - FinishNode.getCoordinates().getyCoordinate()), 2)));
				Coordinates coorinates = new Coordinates((double) i * step + 32, (double) j * step + 32);
				Node node = getNode(coorinates);
				if (node != null) {
					node.setH(h);
					if (node.isInitial()) {
						node.setF(node.getG() + h);
					}
				} else {
					node = new Node();
					node.setH(h);
					node.setCoordinates(coorinates);
					nodes.add(node);
				}

			}
		}

		System.out.println(nodes.size());
	}

	public Node getNodeWithLowestF() {
		double minF = Double.MAX_VALUE;
		Node minNode = new Node();
		for (Node n : openset) {
			if (n.getF() < minF) {
				minNode = n;
				minF = n.getF();
			}
		}
		return minNode;
	}

	public List<Coordinates> astarAlgorithm() {
		generatedHeuristicFunction();
		openset.add(InitialNode);
		while (!openset.isEmpty()) {
			ActualNode = getNodeWithLowestF();
			if (ActualNode.isFinish()) {
				reconstructPath();
				System.out.println(moves.size());
				return moves;

			}
			openset.remove(ActualNode);
			closedSet.add(ActualNode);
			Set<Node> neighbours = new HashSet<>();
			neighbours.add(getNode(new Coordinates(ActualNode.getCoordinates().getxCoordinate() - 64,
					ActualNode.getCoordinates().getyCoordinate())));
			neighbours.add(getNode(new Coordinates(ActualNode.getCoordinates().getxCoordinate() + 64,
					ActualNode.getCoordinates().getyCoordinate())));
			neighbours.add(getNode(new Coordinates(ActualNode.getCoordinates().getxCoordinate(),
					ActualNode.getCoordinates().getyCoordinate() - 64)));
			neighbours.add(getNode(new Coordinates(ActualNode.getCoordinates().getxCoordinate(),
					ActualNode.getCoordinates().getyCoordinate() + 64)));

			for (Node neighbour : neighbours) {
				if ((neighbour == null) || (closedSet.contains(neighbour)) || (neighbour.isObsatcle())) {
					continue;
				}

				double tentiveG = ActualNode.getG() + Constants.DISTANCE_NEIGHBOUR_CURRENT;
				if ((!openset.contains(neighbour)) || (tentiveG < neighbour.getG())) {
					neighbour.setParent(ActualNode);
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

	private List<Coordinates> reconstructPath() {
		List<Coordinates> help = new ArrayList<>();
		Node cursor = ActualNode;
		while (cursor.getParent() != null) {
			help.add(cursor.getCoordinates());
			cursor = cursor.getParent();
		}
		help.add(cursor.getCoordinates());

		for (int i = help.size() - 1; i >= 0; i--)
			moves.add(help.get(i));

		return null;
	}

	public static void main(String[] args) {
		AstarFunction a = new AstarFunction();
		List<Coordinates> list = a.astarAlgorithm();
		System.out.println("Cesta");
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).toString());
		}
	}
}
