package model;

public class IslandCell extends Cell {
    private boolean _hasWeapon = false;

    public IslandCell() {
        super(CellType.ISLAND);
    }

    @Override
    public void hit() {

    }

    @Override
    public boolean hasShip() throws Exception {
        throw new Exception();
    }

    @Override
    public Ship getShip() throws Exception {
        throw new Exception();
    }

    @Override
    public void placeShip(Ship newShip) throws Exception {
        throw new Exception();
    }

    @Override
    public boolean isSunk() throws Exception {
        throw new Exception();
    }

    @Override
    public boolean hasWeapon() throws Exception {
        return _hasWeapon;
    }
}
