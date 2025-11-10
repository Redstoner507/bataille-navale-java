package model;

public class Grid {

    private int[] grid;
    public Grid(int size) {
        if (size >= 6 && size <= 10) {
            this.grid = new int[size * size];
        }
        else {
            throw new IllegalArgumentException("Size must be between 6 and 10");
        }
        for (int i : grid) {
            grid[i] = 0;
        }
    }

}