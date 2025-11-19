package model;

import model.ship.Ship;

public abstract class Cell {
    private final CellType _type;
    private boolean _isHit = false;
    
    public Cell(CellType type) {
        this._type = type;
    }

    public abstract void hit();

    public abstract boolean hasShip() throws Exception;

    public abstract Ship getShip() throws Exception;

    public abstract void placeShip(Ship newShip) throws Exception;

    public abstract boolean isSunk() throws Exception;

    public abstract boolean hasWeapon() throws Exception;

    public boolean isHit() {
        return _isHit;
    }

    public CellType getType() {
        return _type;
    }
}
