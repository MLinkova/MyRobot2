package fnl;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import robocode.AdvancedRobot;
import robocode.Robot;
import robocode.RobotStatus;
import robocode.ScannedRobotEvent;
import robocode.StatusEvent;
import robocode.control.RobotSetup;
import robocode.control.RobotSpecification;
import robocode.util.Utils;
import searchpractice.Constants;
import searchpractice.Coordinates;

public class FnlBot extends Robot {
	private List<Coordinates> moves = new ArrayList<>();
	private int distance = 64;

	public FnlBot() {
		AstarFunction af = new AstarFunction();
		moves = af.astarAlgorithm();
	}

	public void run() {

		for (int i = 0; i < moves.size(); i++) {
			if (getX() < moves.get(i).getxCoordinate()) {
				turnRight(90);
				ahead(distance);
				turnLeft(90);
			} else if (getX() > moves.get(i).getxCoordinate()) {
				turnLeft(90);
				ahead(distance);
				turnRight(90);
			}
			if (getY() > moves.get(i).getyCoordinate()) {
				back(distance);

			} else if ((getY() < moves.get(i).getyCoordinate())) {
				ahead(distance);

			}
		}

	}

}
