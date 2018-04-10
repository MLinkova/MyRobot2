package fnl;

import java.util.*;

import robocode.control.RobotSetup;
import searchpractice.Constants;
import searchpractice.Coordinates;

public class AstarFunction {
	private double[][] heuristicFunction = new double[Constants.NUMBER_ROWS + 1][Constants.NUMBER_COLUMNS + 1];
	// private double[] xCoordinates = new double[Constants.NUMBER_OBSTACLES +
	// 1];
	/*
	 * private double[] yCoordinates = new double[Constants.NUMBER_OBSTACLES +
	 * 1];
	 */
	private double xCoordinateOfFinish;
	private double yCoordinateOfFinish;
	private double xCoordinateOfStart;
	private double yCoordinateOfStart;
	private int step = 64;
	private double xActual;
	private double yActual;
	private List<String> moves;
	private Set<Coordinates> obstacles = new HashSet<Coordinates>();
	private Set<Node> nodes= new HashSet<>();
	private Set<Node> openset= new HashSet<>();
	private Set<Node> closedSet= new HashSet<>();

	public AstarFunction() {
		Random r = new Random(Constants.seed);
		for (int NdxObstacle = 0; NdxObstacle < Constants.NUMBER_OBSTACLES; NdxObstacle++) {
			Double InitialObstacleRow = (double) (r.nextInt(Constants.NUMBER_ROWS + 1)) * Constants.SIZE_OF_TILES + 32;
			Double InitialObstacleCol = (double) (r.nextInt(Constants.NUMBER_COLUMNS + 1)) * Constants.SIZE_OF_TILES
					+ 32;
			Coordinates newCoordinates = new Coordinates(InitialObstacleRow, InitialObstacleCol);
			if (!obstacles.contains(newCoordinates)) {
				obstacles.add(newCoordinates);
			} else {
				NdxObstacle--;
				continue;
			}

		}
		xCoordinateOfStart = (double) (r.nextInt(Constants.NUMBER_ROWS + 1)) * Constants.SIZE_OF_TILES + 32;
		yCoordinateOfStart = (double) (r.nextInt(Constants.NUMBER_COLUMNS + 1)) * Constants.SIZE_OF_TILES + 32;
		xActual = xCoordinateOfStart;
		yActual = yCoordinateOfStart;
		xCoordinateOfFinish = (double) 10 * Constants.SIZE_OF_TILES + 32;
		yCoordinateOfFinish = (double) 5 * Constants.SIZE_OF_TILES + 32;

	}

	public void generatedHeuristicFunction() {
		for (int i = 0; i <= Constants.NUMBER_ROWS; i++) {
			for (int j = 0; j <= Constants.NUMBER_COLUMNS; j++) {
				double h = Math.sqrt((Math.pow((i * 64 + 32 - xCoordinateOfFinish), 2))
						+ (Math.pow((j * 64 + 32 - yCoordinateOfFinish), 2)));
				Node node= new Node();
				node.setH(h);
				node.setCoordinates(new Coordinates((double) i*step+32, (double)j*step+32));

			}
		}
	}

	public void multiplyF(double h) {
		double g = ((xActual - 32) / step) + 1;
		double f = g + h;

	}

	public List<String> astarAlgorithm() {
		Map<Coordinates, Map<Coordinates, Double>> table = new HashMap<>();
		Coordinates up;
		Coordinates down;
		Coordinates left;
		Coordinates right;
		while ((xActual != xCoordinateOfFinish) && (yActual != yCoordinateOfFinish)) {
			if (!table.containsKey(new Coordinates(xActual, yActual))) {
				Map<Coordinates, Double> sucessor = new HashMap<>();
				up = new Coordinates(xActual, yActual + 64);
				down = new Coordinates(xActual, yActual - 64);
				right = new Coordinates(xActual + 64, yActual);
				left = new Coordinates(xActual - 64, yActual);
				if (!obstacles.contains(up)) {
					if (!table.containsKey(up)) {
						if (up.getyCoordinate() < Constants.BATTLEFIELD_HEIGH) {
							double  h=heuristicFunction[(int)((up.getxCoordinate()-32)/step)][(int)((up.getyCoordinate()-32)/step)];
							

						}
					}
				}
			}
		}
		return null;
	}
}
