package fnl;

import java.util.ArrayList;
import java.util.List;
import robocode.Robot;
import searchpractice.Coordinates;

public class FnlBot extends Robot {
	private List<Coordinates> moves = new ArrayList<>();
	private int distance = 64;

	public FnlBot() {
		// run A * algorithm and get the best coordinates
		Astar af = new Astar();
		moves = af.astarAlgorithm();
	}

	public void run() {
		// move according coordinates for A * algorithm
		for (int i = 0; i < moves.size(); i++) {
			// if the best way is in left
			if (getX() < moves.get(i).getxCoordinate()) {
				turnRight(90);
				ahead(distance);
				turnLeft(90);
				// if the best way is in right
			} else if (getX() > moves.get(i).getxCoordinate()) {
				turnLeft(90);
				ahead(distance);
				turnRight(90);
			}
			// if the best way is down
			if (getY() > moves.get(i).getyCoordinate()) {
				back(distance);

			} else if ((getY() < moves.get(i).getyCoordinate())) {
				// if the best way is up
				ahead(distance);

			}
		}

	}

}
