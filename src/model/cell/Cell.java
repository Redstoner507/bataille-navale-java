package model.cell;

import model.AttackResult;
import model.AttackStatus;
import model.Placeable;

public abstract class Cell {
    private final CellType _type;
    private CellState _state;
    private Placeable _object;
    private boolean _isHit = false;
    
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

    public boolean isHit() {
        return _isHit;
    }

    public AttackResult hit() {
        if (_isHit) {
            return new AttackResult(AttackStatus.ALREADY_VISITED, "This cell is already visited");
        }
        _isHit = true;
        if (_object == null) {
            return new AttackResult(AttackStatus.MISS, "You missed !");
        }
        return _object.hit();
    }
}
