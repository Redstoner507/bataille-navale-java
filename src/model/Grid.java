package model;

public class Grid {

    private static final int MIN_SIZE = 6;
    private static final int MAX_SIZE = 10;
    private int _size;

    private Case[] grid;
    
    public Grid(int size) {
        if (size >= MIN_SIZE && size <= MAX_SIZE) {
            this._size = size;
            this.grid = new Case[size * size];
        }
        else {
            throw new IllegalArgumentException("Size must be between 6 and 10");
        }
    }

    public int getSize() {
        return this._size;
    }

}