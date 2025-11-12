package model;

public class Grid {

    private static final int MIN_SIZE = 6;
    private static final int MAX_SIZE = 10;
    private int _size;

    private int[] grid;
    public Grid(int size) {
        if (size >= MIN_SIZE && size <= MAX_SIZE) {
            this._size = size;
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