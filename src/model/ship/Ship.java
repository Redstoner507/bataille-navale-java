package model.ship;

import model.Orientation;
import model.Placeable;
import model.cell.CellType;

public abstract class Ship extends Placeable {

    public Ship(int size, String name) {
        super(size, Orientation.HORIZONTAL, name, CellType.WATER);
    }
}
