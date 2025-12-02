package model.ship;

import model.Orientation;
import model.Placeable;
import model.PlaceableType;
import model.cell.CellType;

public abstract class Ship extends Placeable {
    private int _cellsRemaining;

    public Ship(int size, String name) {
        super(size, Orientation.HORIZONTAL, name, CellType.WATER, PlaceableType.SHIP);
        _cellsRemaining = size;
    }

    /*
     * Constructor for copy
     */
    public Ship(Ship model) {
        this(model.getSize(), model.getName());
    }

    /**
     * Checks if a ship is sunk.
     * @return true if the number of cells remaining is inferior or equal to 0.
     */
    public boolean isSunk() {
        return _cellsRemaining <= 0;
    }

    public void hit() {
        if (_cellsRemaining > 0) {
            _cellsRemaining--;
        }
    }
}
