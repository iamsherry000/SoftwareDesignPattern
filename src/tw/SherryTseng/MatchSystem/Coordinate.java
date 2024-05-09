package src.tw.SherryTseng.MatchSystem;

import src.tw.waterballsa.c2m1h1.Coord;

public class Coordinate {
    private int x;
    private int y;
    
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Getter
    public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

    public double distance(Coord coord) {
		int distX = Math.abs(x - coord.getX());
		int distY = Math.abs(y - coord.getY());
		return Math.sqrt(distX*distX + distY*distY);
	}
    
}
