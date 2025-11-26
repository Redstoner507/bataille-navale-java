package model.cell;

public abstract class Cell {
    private final CellType _type;
    private CellState _state;
    private boolean _isHit = false;
    
    public Cell(CellType type) {
        this._type = type;
        _state = (type == CellType.ISLAND) ? CellState.ISLAND : CellState.WATER;
    }

    public CellState getState() {
        return _state;
    }

    public CellType getType() {
        return _type;
    }

    public void setState(CellState state) {
        this._state = state;
    }

    public abstract void hit();
}
