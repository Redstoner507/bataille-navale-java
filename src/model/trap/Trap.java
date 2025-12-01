package model.trap;

import model.Grid;
import model.Orientation;
import model.Placeable;
import model.PlaceableType;
import model.cell.CellType;

public abstract class Trap extends Placeable {
    private final TrapType _trapType;

    public Trap(TrapType type) {
        super(1, Orientation.HORIZONTAL, type.name(), CellType.WATER, PlaceableType.TRAP);
        _trapType = type;
    }

    public abstract void activate(Grid grid, int row, int col);

    public TrapType getTrapType() {
        return _trapType;
    }
}
