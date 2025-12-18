package model.ship;

import model.*;
import model.cell.CellState;
import model.cell.CellType;

public abstract class Ship extends Placeable {
    private int _cellsRemaining;

    public Ship(int size, String name) {
        super(size, Orientation.HORIZONTAL, name, CellType.WATER, PlaceableType.SHIP, CellState.SHIP);
        _cellsRemaining = size;
    }

    /**
     * Checks if a ship is sunk.
     * @return true if the number of cells remaining is inferior or equal to 0.
     */
    public boolean isSunk() {
        return _cellsRemaining <= 0;
    }

    @Override
    public AttackResult hit() {
        _cellsRemaining--;
        if (_cellsRemaining > 0) {
            return new AttackResult(AttackStatus.HIT, "You hit a ship !");
        }
        else {
            return new AttackResult(AttackStatus.SUNK, "You sunk a " + getName() + " !");
        }
    }
}
