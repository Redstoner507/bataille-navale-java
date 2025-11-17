package model;

import java.awt.*;
import java.util.ArrayList;

public class Grid {
    private static final int MIN_SIZE = 6;
    private static final int MAX_SIZE = 10;

    private final int _size;
    private final CellState[][] _cells;
    private final ArrayList<Ship> _ships = new ArrayList<>();

    public Grid(int size) {
        if (size < MIN_SIZE || size > MAX_SIZE)
            throw new IllegalArgumentException("Size must be between " + MIN_SIZE + " and " + MAX_SIZE);
        this._size = size;
        this._cells = new CellState[size][size];
        for (int r = 0; r < size; r++)
            for (int c = 0; c < size; c++)
                _cells[r][c] = CellState.EMPTY;
    }

    /**
     * Tests if the boat's placement coordinates are valid.
     * @return true if the coordinates are valid, false otherwise.
     */
    public boolean canPlaceShip(Ship ship, int startRow, int startCol) {
        for (int i = 0; i < ship.getSize(); i++) {
            int r = startRow;
            int c = startCol;

            if (ship.getOrientation() == Orientation.HORIZONTAL) c += i;
            else r += i;

            if (r < 0 || r >= _size || c < 0 || c >= _size) return false;
            if (_cells[r][c] != CellState.EMPTY) return false;
        }
        return true;
    }

    /**
     * Places the boat in the grid if the coordinates are valid.
     * @return true if the boat was placed, false otherwise.
     */
    public boolean placeShip(Ship ship, int startRow, int startCol) {
        if (!canPlaceShip(ship, startRow, startCol)) return false;

        ship.setStart(startRow, startCol);
        _ships.add(ship);

        for (Point p : ship.getCoordinates())
            _cells[p.x][p.y] = CellState.SHIP_SEGMENT;

        return true;
    }

    public int getSize() {
        return this._size;
    }

    public CellState getCellState(int row, int col) {
        return _cells[row][col];
    }

    public ArrayList<Ship> getShips() {
        return _ships;
    }
}