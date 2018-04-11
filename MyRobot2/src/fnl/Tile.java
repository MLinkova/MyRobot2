package fnl;

import searchpractice.Coordinates;

public class Tile {
	// class contains information of one tile, This information are important
	// for A * algorithm
	private Coordinates coordinates;
	private Tile parent = null;
	private double g = 1;
	private double h = 1;
	private double f = 1;
	private boolean isInitial = false;
	private boolean isObsatcle = false;
	private boolean isFinish = false;

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}

	public Tile getParent() {
		return parent;
	}

	public void setParent(Tile parent) {
		this.parent = parent;
	}

	public double getG() {
		return g;
	}

	public void setG(double g) {
		this.g = g;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		this.h = h;
	}

	public double getF() {
		return f;
	}

	public void setF(double f) {
		this.f = f;
	}

	public boolean isInitial() {
		return isInitial;
	}

	public void setInitial(boolean isInitial) {
		this.isInitial = isInitial;
	}

	public boolean isObsatcle() {
		return isObsatcle;
	}

	public void setObsatcle(boolean isObsatcle) {
		this.isObsatcle = isObsatcle;
	}

	public boolean isFinish() {
		return isFinish;
	}

	public void setFinish(boolean isFinish) {
		this.isFinish = isFinish;
	}

	@Override
	public String toString() {
		return coordinates.getxCoordinate() + " " + coordinates.getyCoordinate() + " |\n";
	}

}
