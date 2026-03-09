package ch3_complexBV.StatePattern.map;

public class Map {
    private char[][] map;
    public Map() {
        this.map = new char[10][10];
    }

    public void printMap() {
        for(int i = 0; i < map.length; i++) {
            for(int j = 0; j < map[i].length; j++) {
                System.out.print(map[i][j]);
            }
        }
    }
}
