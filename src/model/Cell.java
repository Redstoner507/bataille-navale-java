package model;

public abstract class Cell {
    private CellType type;
    private boolean isHit = false;
    
    public Cell(CellType type) {
        this.type = type;
    }

    public void hit() {
        isHit = true;
    }
    public boolean isHit() {
        return isHit;
    }

    public CellType getType() {
        return type;
    }
}
