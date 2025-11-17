package model;

public class CellWater extends Cell {
    
    private boolean hasShip = false;

    public CellWater() {
        super(CellType.WATER);
    }

    public boolean hasShip() {
        return hasShip;
    }

    public void placeShip() {
        this.hasShip = true;
    }
}
