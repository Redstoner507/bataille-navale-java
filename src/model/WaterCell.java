package model;

public class WaterCell extends Cell {
    private boolean _hasShip = false;
    private boolean _isSunk = false;
    private Ship _ship = null;

    public WaterCell() {
        super(CellType.WATER);
    }

    @Override
    public void hit() {

    }

    @Override
    public boolean hasShip() {
        return _hasShip;
    }

    @Override
    public Ship getShip() {
        return _ship;
    }

    public void placeShip(Ship newShip) {
        this._ship = newShip;
        this._hasShip = true;
    }

    @Override
    public boolean isSunk() {
        return _isSunk;
    }

    @Override
    public boolean hasWeapon() throws Exception {
        throw new Exception();
    }
}
