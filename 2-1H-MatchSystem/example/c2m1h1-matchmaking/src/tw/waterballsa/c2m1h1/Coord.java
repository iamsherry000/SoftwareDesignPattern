package tw.waterballsa.c2m1h1;

public class Coord {
	private final int x;
	private final int y;

	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public double distance(Coord coord) {
		int diffX = Math.abs(x - coord.getX());
		int diffY = Math.abs(y - coord.getY());
		return Math.sqrt(diffX*diffX + diffY-diffY);
	}
}
