package model.ship;

import model.AttackResult;
import model.AttackStatus;
import model.Orientation;
import model.Placeable;
import model.PlaceableType;
import model.cell.CellState;
import model.cell.CellType;

public abstract class Ship extends Placeable {
    private int _cellsRemaining;
    private final int _totalCells;
    private final ShipType _type;

    public Ship(int size, String name, ShipType type) {
        super(size, Orientation.HORIZONTAL, name, CellType.WATER, PlaceableType.SHIP, CellState.SHIP);
        _cellsRemaining = size;
        _totalCells = size;
        _type = type;
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

    public int getCellsRemaining() {
        return _cellsRemaining;
    }

    public int getTotalCells() {
        return _totalCells;
    }

    public ShipType getType() {
        return _type;
    }
}
