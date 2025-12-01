package model.cell;

import model.Placeable;

public abstract class Cell {
    private final CellType _type;
    private CellState _state;
    private Placeable _object;
    
    public Cell(CellType type) {
        this._type = type;
        _state = (type == CellType.ISLAND) ? CellState.ISLAND : CellState.WATER;
    }

    public Placeable getObject() {
        return _object;
    }

    public void removeObject() {
        _object = null;
    }

    public void setObject(Placeable object) {
        _object = object;
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
